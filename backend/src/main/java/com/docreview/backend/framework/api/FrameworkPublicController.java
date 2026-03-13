package com.docreview.backend.framework.api;

import com.docreview.backend.common.api.ApiResponse;
import com.docreview.backend.common.i18n.MessageResolver;
import com.docreview.backend.framework.service.FrameworkRuntimeService;
import java.util.Locale;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/framework")
public class FrameworkPublicController {

    private final FrameworkRuntimeService frameworkRuntimeService;
    private final MessageResolver messageResolver;

    public FrameworkPublicController(
        FrameworkRuntimeService frameworkRuntimeService,
        MessageResolver messageResolver
    ) {
        this.frameworkRuntimeService = frameworkRuntimeService;
        this.messageResolver = messageResolver;
    }

    @GetMapping("/ping")
    public ApiResponse<FrameworkPingResponse> ping(Locale locale) {
        return ApiResponse.success(
            messageResolver.getMessage("framework.public.ping.success"),
            frameworkRuntimeService.ping(locale)
        );
    }
}
