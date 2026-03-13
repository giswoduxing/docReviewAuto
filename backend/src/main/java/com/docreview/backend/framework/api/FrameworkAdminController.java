package com.docreview.backend.framework.api;

import com.docreview.backend.common.api.ApiResponse;
import com.docreview.backend.common.i18n.MessageResolver;
import com.docreview.backend.framework.service.FrameworkRuntimeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/framework")
public class FrameworkAdminController {

    private final FrameworkRuntimeService frameworkRuntimeService;
    private final MessageResolver messageResolver;

    public FrameworkAdminController(
        FrameworkRuntimeService frameworkRuntimeService,
        MessageResolver messageResolver
    ) {
        this.frameworkRuntimeService = frameworkRuntimeService;
        this.messageResolver = messageResolver;
    }

    @GetMapping("/runtime")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<FrameworkRuntimeResponse> runtime() {
        return ApiResponse.success(
            messageResolver.getMessage("framework.internal.runtime.success"),
            frameworkRuntimeService.runtime()
        );
    }
}
