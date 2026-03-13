package com.docreview.backend.user.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateUserRequest(
    @NotBlank(message = "{user.create.username.required}")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{3,31}$", message = "{user.create.username.pattern}")
    String username,
    @NotBlank(message = "{user.create.password.required}")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,32}$",
        message = "{user.create.password.pattern}"
    )
    String password,
    @NotBlank(message = "{user.create.displayName.required}")
    @Size(max = 128, message = "{user.create.displayName.size.max}")
    String displayName,
    @NotBlank(message = "{user.create.email.required}")
    @Email(message = "{user.create.email.invalid}")
    @Size(max = 128, message = "{user.create.email.size.max}")
    String email,
    @NotNull(message = "{user.create.organization.required}")
    Long organizationId,
    @NotEmpty(message = "{user.create.roles.required}")
    List<@NotNull(message = "{user.create.roles.required}") Long> roleIds
) {
}
