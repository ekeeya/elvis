package io.thothcode.tech.elvis.app.configs.security;


import io.thothcode.tech.elvis.app.repositories.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .authorizeExchange(exchanges -> {
                    exchanges
                            .pathMatchers("/login", "/assets/**",
                                    "/css/**", "/js/**", "/img/**", "/fonts/**", "/favicon**")
                            .permitAll()
                            //.pathMatchers( "/api/**").hasRole("ADMIN")
                            .anyExchange().authenticated();
                })
                .httpBasic(httpBasicSpec -> httpBasicSpec.authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)))
                .formLogin(formLoginSpec -> formLoginSpec.loginPage("/login")
                        .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/"))
                        .authenticationFailureHandler((exchange, exception) -> Mono.error(exception)))
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(Customizer.withDefaults())
                )
                .logout(Customizer.withDefaults())
                .build();
    }

    @Bean
    public ReactiveUserDetailsService apiUserDetailsService() {
        return (username) -> userRepository.findUserEntityByUsername(username)
                .map(user -> User.withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRoles())
                        .build());
    }


}
