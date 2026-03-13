package com.docreview.backend.user.api;

import java.time.LocalDateTime;

public record UserSummary(
    Long id,
    String username,
    String displayName,
    String email,
    String status,
    LocalDateTime updatedAt
) {
}
