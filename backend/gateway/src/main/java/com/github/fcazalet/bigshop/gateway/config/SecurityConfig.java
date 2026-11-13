package com.github.fcazalet.bigshop.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Each path prefix is bound to exactly one realm. A token issued by "shop" is
 * rejected on /admin-api/** (wrong issuer and wrong audience), and vice versa.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * Admin API: only tokens from the "admin" realm, with a team role.
     */
    @Bean
    @Order(1)
    SecurityWebFilterChain adminChain(ServerHttpSecurity http,
                                      SecurityProperties props,
                                      CorsConfigurationSource cors) {
        return http
                .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/admin-api/**"))
                .cors(c -> c.configurationSource(cors))
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // stateless bearer-token API
                .authorizeExchange(ex -> ex
                        .pathMatchers("/admin-api/me").authenticated()
                        .pathMatchers("/admin-api/catalog/**").hasAnyRole("admin", "catalog-manager")
                        .pathMatchers("/admin-api/orders/**").hasAnyRole("admin", "order-manager", "support")
                        // Least privilege by default: anything else requires "admin"
                        .anyExchange().hasRole("admin"))
                .oauth2ResourceServer(o -> o.jwt(j -> j
                        .jwtDecoder(decoder(props.admin()))
                        .jwtAuthenticationConverter(rolesConverter())))
                .build();
    }

    /**
     * Public/customer API: only tokens from the "shop" realm.
     */
    @Bean
    @Order(2)
    SecurityWebFilterChain shopChain(ServerHttpSecurity http,
                                     SecurityProperties props,
                                     CorsConfigurationSource cors) {
        return http
                .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/api/**"))
                .cors(c -> c.configurationSource(cors))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ex -> ex
                        .pathMatchers(HttpMethod.GET, "/api/catalog/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/customers/register").permitAll()
                        .anyExchange().hasRole("customer"))
                .oauth2ResourceServer(o -> o.jwt(j -> j
                        .jwtDecoder(decoder(props.shop()))
                        .jwtAuthenticationConverter(rolesConverter())))
                .build();
    }

    /**
     * Everything else is closed, except the health endpoint.
     */
    @Bean
    @Order(3)
    SecurityWebFilterChain fallbackChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ex -> ex
                        .pathMatchers("/actuator/health/**").permitAll()
                        .anyExchange().denyAll())
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(SecurityProperties props) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(props.corsAllowedOrigins());
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Validates signature (JWKS), expiry, issuer and audience.
     */
    private static ReactiveJwtDecoder decoder(SecurityProperties.Realm realm) {
        NimbusReactiveJwtDecoder decoder =
                NimbusReactiveJwtDecoder.withJwkSetUri(realm.jwkSetUri()).build();
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefaultWithIssuer(realm.issuer()),
                new JwtClaimValidator<List<String>>("aud",
                        aud -> aud != null && aud.contains(realm.audience())));
        decoder.setJwtValidator(validator);
        return decoder;
    }

    /**
     * Maps the flat "roles" claim (Keycloak mapper) to ROLE_xxx authorities.
     */
    private static Converter<Jwt, Mono<AbstractAuthenticationToken>> rolesConverter() {
        JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
        authorities.setAuthoritiesClaimName("roles");
        authorities.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorities);
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }
}
