package com.docreview.backend.auth.api;

public record PermissionOptionResponse(
    String code,
    String titleKey,
    String descriptionKey
) {
}
