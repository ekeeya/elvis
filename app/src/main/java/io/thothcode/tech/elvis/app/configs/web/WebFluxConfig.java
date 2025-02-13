package io.thothcode.tech.elvis.app.configs.web;

import io.thothcode.tech.elvis.app.common.configs.PageableHandlerMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {

    @Override
    public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
        configurer.addCustomResolver(new PageableHandlerMethodArgumentResolver());
    }

    @Bean
    public RouterFunction<ServerResponse> htmlRouter(@Value("classpath:/static/index.html") Resource html) {
        return RouterFunctions.route(
                GET("/*"),
                request -> ServerResponse.ok().contentType(MediaType.TEXT_HTML).bodyValue(html)
        );
    }

    @Bean
    public RouterFunction<ServerResponse> assetsRouter() {
        return RouterFunctions
                .resources("/assets/**", new ClassPathResource("static/assets/"));
    }
}
