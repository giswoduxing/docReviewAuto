package com.docreview.backend.auth.service;

import java.util.List;
import java.util.Set;

public final class PermissionCatalog {

    private static final List<PermissionDefinition> PERMISSIONS = List.of(
        new PermissionDefinition(
            "organization:manage",
            "auth.permission.organization.title",
            "auth.permission.organization.description"
        ),
        new PermissionDefinition(
            "role:manage",
            "auth.permission.role.title",
            "auth.permission.role.description"
        ),
        new PermissionDefinition(
            "user:manage",
            "auth.permission.user.title",
            "auth.permission.user.description"
        )
    );

    private PermissionCatalog() {
    }

    public static List<PermissionDefinition> all() {
        return PERMISSIONS;
    }

    public static boolean isSupported(String permissionCode) {
        return permissionCodes().contains(permissionCode);
    }

    public static Set<String> permissionCodes() {
        return PERMISSIONS.stream()
            .map(PermissionDefinition::code)
            .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    public record PermissionDefinition(
        String code,
        String titleKey,
        String descriptionKey
    ) {
    }
}
