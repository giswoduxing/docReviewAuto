package com.docreview.backend.assistant.api;

import com.docreview.backend.assistant.service.AiAssistantService;
import com.docreview.backend.common.api.ApiResponse;
import com.docreview.backend.common.i18n.MessageResolver;
import jakarta.validation.Valid;
import java.util.Locale;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/internal/assistant")
public class AssistantController {

    private final AiAssistantService aiAssistantService;
    private final MessageResolver messageResolver;

    public AssistantController(
        AiAssistantService aiAssistantService,
        MessageResolver messageResolver
    ) {
        this.aiAssistantService = aiAssistantService;
        this.messageResolver = messageResolver;
    }

    @PostMapping("/completions")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<AiCompletionResponse> complete(
        @Valid @RequestBody AiCompletionRequest request,
        Locale locale
    ) {
        return ApiResponse.success(
            messageResolver.getMessage("assistant.completion.success"),
            aiAssistantService.complete(request, locale)
        );
    }
}
