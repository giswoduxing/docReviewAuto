package com.docreview.backend.assistant.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "doc-review.ai")
public record AssistantPromptProperties(
    @NotBlank(message = "{assistant.system.prompt.required}")
    String systemPromptTemplate
) {
}
