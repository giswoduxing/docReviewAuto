package com.docreview.backend.framework.api;

import java.util.List;

public record FrameworkRuntimeResponse(
    String applicationName,
    int javaVersion,
    List<String> activeProfiles,
    boolean aiReady,
    String datasourceUrl,
    String redisHost,
    Integer redisPort,
    String aiModel
) {
}
