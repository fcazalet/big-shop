package org.github.fcazalet.bigshop.customer.service;

import org.github.fcazalet.bigshop.customer.model.request.RegistrationRequest;
import org.github.fcazalet.bigshop.customer.service.keycloak.KeycloakAdminClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.Locale;
import java.util.Optional;

@Service
public class RegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    private final KeycloakAdminClient keycloak;

    RegistrationService(KeycloakAdminClient keycloak) {
        this.keycloak = keycloak;
    }

    /**
     * Never reveals whether the email is already registered: an existing account
     * is silently ignored so the response is identical (avoids email enumeration).
     */
    public void register(RegistrationRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        Optional<String> userId = keycloak.createUser(
                email, request.firstName().trim(), request.lastName().trim(), request.password());

        if (userId.isEmpty()) {
            logger.info("Registration ignored: account already exists");
            return;
        }
        try {
            keycloak.sendVerificationEmail(userId.get());
        } catch (RestClientException e) {
            // The account exists; Keycloak will propose the verification again at first login.
            logger.warn("Could not send the verification email for user {}", userId.get(), e);
        }
    }
}
