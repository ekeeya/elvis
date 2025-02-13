package io.thothcode.tech.auth.oauth2.repositories;


import io.thothcode.tech.gluon.oauth2.mongo.entities.MongoDbOAuth2Authorization;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface AuthorizationRepository extends MongoRepository<MongoDbOAuth2Authorization, String> {
    Optional<MongoDbOAuth2Authorization> findByState(String state);

    Optional<MongoDbOAuth2Authorization> findByAuthorizationCode(String authorizationCode);

    Optional<MongoDbOAuth2Authorization> findByAccessToken(String accessToken);

    Optional<MongoDbOAuth2Authorization> findByRefreshToken(String refreshToken);

    Optional<MongoDbOAuth2Authorization> findByStateOrAuthorizationCodeOrAccessTokenOrRefreshToken(String token);
}
