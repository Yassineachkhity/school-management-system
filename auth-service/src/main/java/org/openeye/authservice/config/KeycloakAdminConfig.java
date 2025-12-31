package org.openeye.authservice.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakAdminConfig {

    @Bean
    public Keycloak keycloak(KeycloakProperties properties) {
        KeycloakBuilder builder = KeycloakBuilder.builder()
                .serverUrl(properties.getServerUrl())
                .realm(properties.getAdminRealm())
                .grantType(OAuth2Constants.PASSWORD)
                .username(properties.getAdminUsername())
                .password(properties.getAdminPassword());

        String clientId = resolveAdminClientId(properties);
        builder.clientId(clientId);

        String clientSecret = resolveAdminClientSecret(properties);
        if (clientSecret != null && !clientSecret.isBlank()) {
            builder.clientSecret(clientSecret);
        }

        return builder.build();
    }

    private String resolveAdminClientId(KeycloakProperties properties) {
        String adminClientId = properties.getAdminClientId();
        if (adminClientId != null && !adminClientId.isBlank()) {
            return adminClientId;
        }
        return properties.getClientId();
    }

    private String resolveAdminClientSecret(KeycloakProperties properties) {
        String adminClientSecret = properties.getAdminClientSecret();
        if (adminClientSecret != null && !adminClientSecret.isBlank()) {
            return adminClientSecret;
        }
        return properties.getClientSecret();
    }
}
