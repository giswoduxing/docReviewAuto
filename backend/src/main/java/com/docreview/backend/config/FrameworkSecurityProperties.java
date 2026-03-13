package com.docreview.backend.config;

import jakarta.validation.constraints.NotBlank;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "doc-review.security")
public record FrameworkSecurityProperties(
    @NotBlank(message = "{security.bootstrap.username.required}")
    String adminUsername,
    @NotBlank(message = "{security.bootstrap.password.required}")
    String adminPassword,
    int maxFailedAttempts,
    Duration lockDuration
) {
}
