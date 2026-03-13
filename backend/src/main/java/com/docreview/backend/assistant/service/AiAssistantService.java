package com.docreview.backend.assistant.service;

import com.docreview.backend.assistant.api.AiCompletionRequest;
import com.docreview.backend.assistant.api.AiCompletionResponse;
import java.util.Locale;

public interface AiAssistantService {

    boolean ready();

    AiCompletionResponse complete(AiCompletionRequest request, Locale locale);
}
