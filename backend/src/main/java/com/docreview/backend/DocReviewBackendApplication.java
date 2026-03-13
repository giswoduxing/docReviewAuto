package com.docreview.backend;

import com.docreview.backend.assistant.config.AssistantPromptProperties;
import com.docreview.backend.config.FrameworkSecurityProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(
    exclude = UserDetailsServiceAutoConfiguration.class,
    excludeName = {
        "com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeAgentAutoConfiguration",
        "com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeChatAutoConfiguration",
        "com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeEmbeddingAutoConfiguration",
        "com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeAudioSpeechAutoConfiguration",
        "com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeAudioTranscriptionAutoConfiguration",
        "com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeImageAutoConfiguration",
        "com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeVideoAutoConfiguration",
        "com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeRerankAutoConfiguration"
    }
)
@MapperScan({
    "com.docreview.backend.user.mapper",
    "com.docreview.backend.organization.mapper",
    "com.docreview.backend.role.mapper"
})
@EnableConfigurationProperties({
    FrameworkSecurityProperties.class,
    AssistantPromptProperties.class
})
public class DocReviewBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocReviewBackendApplication.class, args);
    }
}
