package io.thothcode.tech.gluon.oauth2.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OAuth2ClientSettings {
    private boolean requireProofKey = false;
    private boolean requireAuthorizationConsent = false;
}
