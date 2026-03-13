package com.docreview.backend.assistant.api;

public record AiCompletionResponse(
    String content,
    String provider,
    String locale
) {
}
