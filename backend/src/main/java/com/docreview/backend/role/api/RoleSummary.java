package com.docreview.backend.role.api;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.List;

public record RoleSummary(
    @JsonSerialize(using = ToStringSerializer.class)
    Long id,
    String code,
    String name,
    String description,
    String status,
    boolean builtIn,
    List<String> permissionCodes
) {
}
