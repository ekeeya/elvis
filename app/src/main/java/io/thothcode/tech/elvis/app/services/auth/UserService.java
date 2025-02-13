package io.thothcode.tech.elvis.app.services.auth;
import io.thothcode.tech.gluon.types.requests.UserRequestDTO;
import io.thothcode.tech.gluon.types.responses.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface UserService  {
    Mono<UserResponseDTO> registerUser(UserRequestDTO userRequest);

    Mono<Page<UserResponseDTO>>findAll(Pageable pageable);
}
