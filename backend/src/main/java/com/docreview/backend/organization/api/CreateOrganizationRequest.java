package com.docreview.backend.organization.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateOrganizationRequest(
    @NotBlank(message = "{organization.create.name.required}")
    @Size(max = 128, message = "{organization.create.name.size.max}")
    String name,
    @NotBlank(message = "{organization.create.code.required}")
    @Pattern(regexp = "^[A-Z][A-Z0-9_-]{1,63}$", message = "{organization.create.code.pattern}")
    String code,
    Long parentId,
    @Size(max = 64, message = "{organization.create.leader.size.max}")
    String leaderName
) {
}
