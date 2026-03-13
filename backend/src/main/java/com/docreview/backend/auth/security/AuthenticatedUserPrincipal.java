package com.docreview.backend.auth.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public record AuthenticatedUserPrincipal(
    Long userId,
    String username,
    String displayName,
    String organizationName,
    List<String> roleCodes,
    List<String> roleNames,
    List<String> permissionCodes
) implements Serializable {

    public Collection<? extends GrantedAuthority> authorities() {
        return Stream.concat(
            roleCodes.stream().map(roleCode -> new SimpleGrantedAuthority("ROLE_" + roleCode)),
            permissionCodes.stream().map(SimpleGrantedAuthority::new)
        ).toList();
    }
}
