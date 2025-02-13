package io.thothcode.tech.elvis.app.api;

import io.thothcode.tech.elvis.app.services.auth.UserService;
import io.thothcode.tech.gluon.types.requests.UserRequestDTO;
import io.thothcode.tech.gluon.types.responses.ListResponseDTO;
import io.thothcode.tech.gluon.types.responses.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;




    @PostMapping
    public Mono<UserResponseDTO> saveUser(@RequestBody UserRequestDTO request) {
        return userService.registerUser(request);
    }


    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ListResponseDTO<UserResponseDTO>>> fetchUsers(
            Pageable pageable
    ) {
        return userService.findAll(pageable)
                .flatMap(pageData -> Mono.just(ResponseEntity.ok(new ListResponseDTO<>(pageData))));
    }
}
