package com.docreview.backend.user.api;

import com.docreview.backend.common.api.ApiResponse;
import com.docreview.backend.common.api.PageResult;
import com.docreview.backend.common.i18n.MessageResolver;
import com.docreview.backend.user.service.SystemUserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/internal/users")
public class UserController {

    private final SystemUserService systemUserService;
    private final MessageResolver messageResolver;

    public UserController(SystemUserService systemUserService, MessageResolver messageResolver) {
        this.systemUserService = systemUserService;
        this.messageResolver = messageResolver;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PageResult<UserSummary>> listUsers(@Valid @ModelAttribute UserPageQuery query) {
        return ApiResponse.success(
            messageResolver.getMessage("user.list.success"),
            systemUserService.listUsers(query)
        );
    }
}
