package com.docreview.backend.auth.api;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.List;

public record SessionUserResponse(
    @JsonSerialize(using = ToStringSerializer.class)
    Long userId,
    String username,
    String displayName,
    String organizationName,
    List<String> roleCodes,
    List<String> roleNames,
    List<String> permissionCodes
) {
}
