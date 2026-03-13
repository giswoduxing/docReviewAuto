package com.docreview.backend.role.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateRoleRequest(
    @NotBlank(message = "{role.create.code.required}")
    @Pattern(regexp = "^[A-Z][A-Z0-9_]{1,63}$", message = "{role.create.code.pattern}")
    String code,
    @NotBlank(message = "{role.create.name.required}")
    @Size(max = 128, message = "{role.create.name.size.max}")
    String name,
    @Size(max = 255, message = "{role.create.description.size.max}")
    String description,
    @NotEmpty(message = "{role.create.permissions.required}")
    List<@NotBlank(message = "{role.create.permissions.required}") String> permissionCodes
) {
}
