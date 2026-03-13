package com.docreview.backend.assistant.service;

import com.docreview.backend.assistant.api.AiCompletionRequest;
import com.docreview.backend.assistant.api.AiCompletionResponse;
import com.docreview.backend.assistant.config.AssistantPromptProperties;
import java.util.Locale;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@ConditionalOnBean(ChatClient.Builder.class)
public class DashScopeAiAssistantService implements AiAssistantService {

    private final ChatClient chatClient;
    private final AssistantPromptProperties assistantPromptProperties;

    public DashScopeAiAssistantService(
        ChatClient.Builder chatClientBuilder,
        AssistantPromptProperties assistantPromptProperties
    ) {
        this.chatClient = chatClientBuilder.build();
        this.assistantPromptProperties = assistantPromptProperties;
    }

    @Override
    public boolean ready() {
        return true;
    }

    @Override
    public AiCompletionResponse complete(AiCompletionRequest request, Locale locale) {
        String systemPrompt = assistantPromptProperties.systemPromptTemplate()
            .replace("{locale}", locale.toLanguageTag());

        String content = chatClient.prompt()
            .system(systemPrompt)
            .user(request.prompt())
            .call()
            .content();

        return new AiCompletionResponse(content, "dashscope", locale.toLanguageTag());
    }
}
