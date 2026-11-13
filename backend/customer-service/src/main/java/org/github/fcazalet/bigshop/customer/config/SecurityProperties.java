package org.github.fcazalet.bigshop.customer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public record SecurityProperties(Realm shop, Keycloak keycloak) {

    public record Realm(String issuer, String jwkSetUri, String audience) {
    }

    public record Keycloak(String baseUrl, String realm, String clientId, String clientSecret,
                           String verificationClientId, String verificationRedirectUri) {
    }
}