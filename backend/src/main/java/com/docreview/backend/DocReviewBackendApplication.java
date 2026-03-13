package com.docreview.backend;

import com.docreview.backend.assistant.config.AssistantPromptProperties;
import com.docreview.backend.config.FrameworkSecurityProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@MapperScan("com.docreview.backend.user.mapper")
@EnableConfigurationProperties({
    FrameworkSecurityProperties.class,
    AssistantPromptProperties.class
})
public class DocReviewBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocReviewBackendApplication.class, args);
    }
}
