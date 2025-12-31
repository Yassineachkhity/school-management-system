package org.openeye.authservice.service;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.openeye.authservice.config.KeycloakProperties;
import org.openeye.authservice.exceptions.KeycloakOperationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakAdminService {

    private final Keycloak keycloak;
    private final KeycloakProperties properties;

    public String createUser(String email, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(email);
        user.setEmail(email);
        user.setEnabled(true);

        UsersResource usersResource = realm().users();
        try (Response response = usersResource.create(user)) {
            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                throw new KeycloakOperationException("Failed to create Keycloak user: " + response.getStatus());
            }
            String userId = CreatedResponseUtil.getCreatedId(response);
            if (password != null && !password.isBlank()) {
                resetPassword(userId, password);
            }
            return userId;
        } catch (Exception ex) {
            throw new KeycloakOperationException("Keycloak user creation failed", ex);
        }
    }

    public void deleteUser(String userId) {
        try {
            realm().users().delete(userId);
        } catch (Exception ex) {
            throw new KeycloakOperationException("Keycloak user deletion failed", ex);
        }
    }

    public void updateUserEmail(String userId, String email) {
        try {
            UserRepresentation representation = realm().users().get(userId).toRepresentation();
            representation.setEmail(email);
            representation.setUsername(email);
            realm().users().get(userId).update(representation);
        } catch (Exception ex) {
            throw new KeycloakOperationException("Keycloak user update failed", ex);
        }
    }

    public void resetPassword(String userId, String password) {
        try {
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);
            realm().users().get(userId).resetPassword(credential);
        } catch (Exception ex) {
            throw new KeycloakOperationException("Keycloak password reset failed", ex);
        }
    }

    public void ensureRealmRoleExists(String roleName) {
        RolesResource rolesResource = realm().roles();
        try {
            rolesResource.get(roleName).toRepresentation();
        } catch (NotFoundException ex) {
            RoleRepresentation representation = new RoleRepresentation();
            representation.setName(roleName);
            rolesResource.create(representation);
        } catch (Exception ex) {
            throw new KeycloakOperationException("Keycloak role lookup failed", ex);
        }
    }

    public void assignRealmRole(String userId, String roleName) {
        try {
            RolesResource rolesResource = realm().roles();
            RoleRepresentation role = rolesResource.get(roleName).toRepresentation();
            realm().users().get(userId).roles().realmLevel().add(List.of(role));
        } catch (Exception ex) {
            throw new KeycloakOperationException("Keycloak role assignment failed", ex);
        }
    }

    public void removeRealmRole(String userId, String roleName) {
        try {
            RolesResource rolesResource = realm().roles();
            RoleRepresentation role = rolesResource.get(roleName).toRepresentation();
            realm().users().get(userId).roles().realmLevel().remove(List.of(role));
        } catch (Exception ex) {
            throw new KeycloakOperationException("Keycloak role removal failed", ex);
        }
    }

    public void deleteRealmRole(String roleName) {
        try {
            realm().roles().deleteRole(roleName);
        } catch (Exception ex) {
            throw new KeycloakOperationException("Keycloak role deletion failed", ex);
        }
    }

    public List<UserRepresentation> listUsers() {
        try {
            List<UserRepresentation> results = new ArrayList<>();
            int first = 0;
            int max = 100;
            List<UserRepresentation> batch;
            do {
                batch = realm().users().list(first, max);
                results.addAll(batch);
                first += max;
            } while (!batch.isEmpty());
            return results;
        } catch (Exception ex) {
            throw new KeycloakOperationException("Keycloak user list failed", ex);
        }
    }

    public List<String> getUserRealmRoles(String userId) {
        try {
            return realm().users().get(userId).roles().realmLevel().listAll().stream()
                    .map(RoleRepresentation::getName)
                    .toList();
        } catch (Exception ex) {
            throw new KeycloakOperationException("Keycloak user role fetch failed", ex);
        }
    }

    private RealmResource realm() {
        return keycloak.realm(properties.getRealm());
    }
}
