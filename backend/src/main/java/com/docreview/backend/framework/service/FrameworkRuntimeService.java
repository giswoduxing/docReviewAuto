package com.docreview.backend.framework.service;

import com.docreview.backend.assistant.service.AiAssistantService;
import com.docreview.backend.framework.api.FrameworkPingResponse;
import com.docreview.backend.framework.api.FrameworkRuntimeResponse;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class FrameworkRuntimeService {

    private final Environment environment;
    private final AiAssistantService aiAssistantService;

    public FrameworkRuntimeService(Environment environment, AiAssistantService aiAssistantService) {
        this.environment = environment;
        this.aiAssistantService = aiAssistantService;
    }

    public FrameworkPingResponse ping(Locale locale) {
        return new FrameworkPingResponse(
            environment.getProperty("spring.application.name", "doc-review-backend"),
            locale.toLanguageTag(),
            Instant.now()
        );
    }

    public FrameworkRuntimeResponse runtime() {
        return new FrameworkRuntimeResponse(
            environment.getProperty("spring.application.name", "doc-review-backend"),
            Runtime.version().feature(),
            resolveProfiles(),
            aiAssistantService.ready(),
            environment.getProperty("spring.datasource.url"),
            environment.getProperty("spring.data.redis.host"),
            environment.getProperty("spring.data.redis.port", Integer.class),
            environment.getProperty("spring.ai.dashscope.chat.options.model")
        );
    }

    private List<String> resolveProfiles() {
        String[] profiles = environment.getActiveProfiles();
        if (profiles.length == 0) {
            profiles = environment.getDefaultProfiles();
        }
        return Arrays.stream(profiles).toList();
    }
}
