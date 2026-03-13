package com.docreview.backend.assistant.service;

import com.docreview.backend.assistant.api.AiCompletionRequest;
import com.docreview.backend.assistant.api.AiCompletionResponse;
import com.docreview.backend.common.exception.BusinessException;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class DisabledAiAssistantService implements AiAssistantService {

    @Override
    public boolean ready() {
        return false;
    }

    @Override
    public AiCompletionResponse complete(AiCompletionRequest request, Locale locale) {
        throw new BusinessException("AI_NOT_CONFIGURED", "assistant.integration.disabled");
    }
}
