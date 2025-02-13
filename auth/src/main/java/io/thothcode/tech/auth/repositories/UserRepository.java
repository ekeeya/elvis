package io.thothcode.tech.auth.repositories;

import io.thothcode.tech.gluon.entities.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
    UserEntity findUserByUsername(String username);
}
