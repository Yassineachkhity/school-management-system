package org.openeye.inscriptionservice.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null || realmAccess.get("roles") == null) {
            return Collections.emptyList();
        }
        Object roles = realmAccess.get("roles");
        if (!(roles instanceof List<?> roleList)) {
            return Collections.emptyList();
        }
        return roleList.stream()
                .filter(role -> role instanceof String)
                .map(role -> "ROLE_" + ((String) role).toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
