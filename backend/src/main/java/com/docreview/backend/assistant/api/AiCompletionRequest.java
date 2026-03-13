package com.docreview.backend.assistant.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AiCompletionRequest(
    @NotBlank(message = "{assistant.prompt.required}")
    @Size(max = 2000, message = "{assistant.prompt.size.max}")
    String prompt
) {
}
