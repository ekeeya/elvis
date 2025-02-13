package io.thothcode.tech.gluon.oauth2.mongo;

import io.thothcode.tech.gluon.oauth2.mongo.constants.MongoDbSignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OAuth2TokenSettings {
    private Duration accessTokenTimeToLive;
    private boolean reuseRefreshTokens = true;
    private Duration refreshTokenTimeToLive;
    private MongoDbSignatureAlgorithm idTokenSignatureAlgorithm;
}
