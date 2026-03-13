package com.docreview.backend.user.api;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.time.LocalDateTime;
import java.util.List;

public record UserSummary(
    @JsonSerialize(using = ToStringSerializer.class)
    Long id,
    String username,
    String displayName,
    String email,
    @JsonSerialize(using = ToStringSerializer.class)
    Long organizationId,
    String organizationName,
    List<String> roleNames,
    String status,
    LocalDateTime updatedAt
) {
}
