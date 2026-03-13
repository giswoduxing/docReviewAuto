package com.docreview.backend.auth.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank(message = "{auth.login.username.required}")
    @Size(max = 64, message = "{auth.login.username.size.max}")
    String username,
    @NotBlank(message = "{auth.login.password.required}")
    @Size(max = 64, message = "{auth.login.password.size.max}")
    String password
) {
}
