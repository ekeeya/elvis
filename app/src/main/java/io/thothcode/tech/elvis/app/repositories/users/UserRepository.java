package io.thothcode.tech.elvis.app.repositories.users;

import io.thothcode.tech.gluon.entities.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;


@Repository
public interface UserRepository extends ReactiveMongoRepository<UserEntity, String> {
    Mono<UserEntity> findUserEntityByUsername(String username);
    Flux<UserEntity> findUserEntitiesByRoleIn(Collection<UserEntity.ROLES> role, Pageable pageable);
}

