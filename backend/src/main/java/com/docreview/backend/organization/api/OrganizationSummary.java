package com.docreview.backend.organization.api;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public record OrganizationSummary(
    @JsonSerialize(using = ToStringSerializer.class)
    Long id,
    String name,
    String code,
    @JsonSerialize(using = ToStringSerializer.class)
    Long parentId,
    String parentName,
    String leaderName,
    String status
) {
}
