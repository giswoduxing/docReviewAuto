package com.docreview.backend.framework.api;

import java.time.Instant;

public record FrameworkPingResponse(
    String applicationName,
    String locale,
    Instant timestamp
) {
}
