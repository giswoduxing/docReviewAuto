package com.docreview.backend.common.api;

import java.time.Instant;

public record ApiResponse<T>(
    String code,
    String message,
    T data,
    Instant timestamp
) {

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("COMMON_SUCCESS", message, data, Instant.now());
    }

    public static ApiResponse<Void> failure(String code, String message) {
        return new ApiResponse<>(code, message, null, Instant.now());
    }
}
