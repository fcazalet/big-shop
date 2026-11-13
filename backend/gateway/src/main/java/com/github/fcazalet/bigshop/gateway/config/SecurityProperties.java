package com.github.fcazalet.bigshop.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.lang.Record;
import java.util.List;

@ConfigurationProperties(prefix = "app.security")
public record SecurityProperties(Realm shop, Realm admin, List<String> corsAllowedOrigins) {

    public record Realm(String issuer, String jwkSetUri, String audience) {
    }
}