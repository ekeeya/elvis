package io.thothcode.tech.auth.oauth2.repositories;


import io.thothcode.tech.gluon.oauth2.mongo.entities.MongoDbOAuth2AuthorizationConsent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizationConsentRepository extends MongoRepository<MongoDbOAuth2AuthorizationConsent, String> {
    /**
     * Find by registered client id and principal name optional.
     *
     * @param registeredClientId the registered client id
     * @param principalName      the principal name
     * @return the optional
     */
    Optional<MongoDbOAuth2AuthorizationConsent> findByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);

    /**
     * Delete by registered client id and principal name.
     *
     * @param registeredClientId the registered client id
     * @param principalName      the principal name
     */
    void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);
}
