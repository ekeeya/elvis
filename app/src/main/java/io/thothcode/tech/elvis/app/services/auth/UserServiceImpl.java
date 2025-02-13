package io.thothcode.tech.elvis.app.services.auth;
import io.thothcode.tech.elvis.app.repositories.users.UserRepository;
import io.thothcode.tech.elvis.app.services.BaseService;
import io.thothcode.tech.gluon.entities.UserEntity;
import io.thothcode.tech.gluon.types.requests.UserRequestDTO;
import io.thothcode.tech.gluon.types.responses.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl  extends BaseService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserResponseDTO> registerUser(UserRequestDTO request) {
        return Mono.just(request)
                .map(req -> new UserEntity(
                        req.getUsername(),
                        req.getFirstname(),
                        req.getLastname(),
                        req.getEmail(),
                        passwordEncoder.encode(req.getPassword()),
                        req.getTelephone(),
                        req.getRole(),
                        req.getEnabled()
                ))
                .flatMap(user -> userRepository.save(user)
                        .map(UserResponseDTO::new)
                        .onErrorResume(e -> {
                            log.error(e.getMessage(), e);
                            return Mono.error(new RuntimeException("An error occurred during User registration"));
                        })
                );
    }

    @Override
    public Mono<Page<UserResponseDTO>> findAll(Pageable pageable) {
        List<UserEntity.ROLES> roles = List.of(UserEntity.ROLES.USER, UserEntity.ROLES.ADMIN);
        Flux<UserEntity> users = userRepository.findUserEntitiesByRoleIn(roles, pageable);
        return users.collectList()
                .zipWith(userRepository.count())
                .map(p -> new PageImpl<>(p.getT1().stream().map(UserResponseDTO::new).collect(Collectors.toList()), pageable, p.getT2()));
    }
}
