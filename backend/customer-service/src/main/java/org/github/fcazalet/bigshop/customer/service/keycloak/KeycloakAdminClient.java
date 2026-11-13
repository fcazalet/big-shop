package org.github.fcazalet.bigshop.customer.service.keycloak;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.github.fcazalet.bigshop.customer.config.SecurityProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;


/**
 * Minimal client for the Keycloak Admin REST API, authenticated with client_credentials.
 */
@Component
public class KeycloakAdminClient {

    private final SecurityProperties.Keycloak props;
    private final RestClient http;

    private String cachedToken;
    private Instant tokenExpiresAt = Instant.EPOCH;

    KeycloakAdminClient(SecurityProperties props) {
        this.props = props.keycloak();
        this.http = RestClient.builder().baseUrl(this.props.baseUrl()).build();
    }

    /**
     * Creates the user with a password and the VERIFY_EMAIL required action.
     *
     * @return the new user id, or empty if an account with this email already exists
     */
    public Optional<String> createUser(String email, String firstName, String lastName, String password) {
        Map<String, Object> body = Map.of(
                "username", email,
                "email", email,
                "firstName", firstName,
                "lastName", lastName,
                "enabled", true,
                "emailVerified", false,
                "requiredActions", List.of("VERIFY_EMAIL"),
                "credentials", List.of(Map.of(
                        "type", "password",
                        "value", password,
                        "temporary", false)));
        try {
            ResponseEntity<Void> response = http.post()
                    .uri("/admin/realms/{realm}/users", props.realm())
                    .headers(h -> h.setBearerAuth(accessToken()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
            URI location = response.getHeaders().getLocation();
            if (location == null) {
                throw new IllegalStateException("Keycloak did not return the user location");
            }
            String path = location.getPath();
            return Optional.of(path.substring(path.lastIndexOf('/') + 1));
        } catch (HttpClientErrorException.Conflict e) {
            return Optional.empty();
        } catch (HttpClientErrorException.BadRequest e) {
            // Typically a password policy violation
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid registration data (check the password policy).");
        }
    }

    /**
     * Asks Keycloak to send the "verify your email" message.
     */
    public void sendVerificationEmail(String userId) {
        http.put()
                .uri(uri -> uri
                        .path("/admin/realms/{realm}/users/{id}/execute-actions-email")
                        .queryParam("client_id", props.verificationClientId())
                        .queryParam("redirect_uri", props.verificationRedirectUri())
                        .build(props.realm(), userId))
                .headers(h -> h.setBearerAuth(accessToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(List.of("VERIFY_EMAIL"))
                .retrieve()
                .toBodilessEntity();
    }

    private synchronized String accessToken() {
        if (cachedToken != null && Instant.now().isBefore(tokenExpiresAt.minusSeconds(30))) {
            return cachedToken;
        }
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", props.clientId());
        form.add("client_secret", props.clientSecret());

        Map<String, Object> response = http.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", props.realm())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {
                });

        cachedToken = (String) response.get("access_token");
        tokenExpiresAt = Instant.now().plusSeconds(((Number) response.get("expires_in")).longValue());
        return cachedToken;
    }
}