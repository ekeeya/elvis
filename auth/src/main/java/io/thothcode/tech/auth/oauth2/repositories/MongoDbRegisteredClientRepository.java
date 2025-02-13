package io.thothcode.tech.auth.oauth2.repositories;

import io.thothcode.tech.gluon.oauth2.mongo.entities.MongoDbOAuth2RegisteredClient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MongoDbRegisteredClientRepository extends MongoRepository<MongoDbOAuth2RegisteredClient, String> {
    /**
     * Find by client id optional.
     *
     * @param clientId the client id
     * @return the optional
     */
    Optional<MongoDbOAuth2RegisteredClient> findByClientId(String clientId);

}
