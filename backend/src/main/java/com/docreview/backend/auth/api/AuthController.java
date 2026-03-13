package com.docreview.backend.auth.api;

import com.docreview.backend.auth.service.AuthenticationService;
import com.docreview.backend.common.api.ApiResponse;
import com.docreview.backend.common.i18n.MessageResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping
public class AuthController {

    private final AuthenticationService authenticationService;
    private final MessageResolver messageResolver;

    public AuthController(AuthenticationService authenticationService, MessageResolver messageResolver) {
        this.authenticationService = authenticationService;
        this.messageResolver = messageResolver;
    }

    @PostMapping("/api/public/auth/login")
    public ApiResponse<SessionUserResponse> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse
    ) {
        return ApiResponse.success(
            messageResolver.getMessage("auth.login.success"),
            authenticationService.login(request, httpServletRequest, httpServletResponse)
        );
    }

    @PostMapping("/api/internal/auth/logout")
    public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authenticationService.logout(request, response);
        return ApiResponse.success(messageResolver.getMessage("auth.logout.success"), null);
    }

    @GetMapping("/api/internal/auth/session")
    public ApiResponse<SessionUserResponse> currentSession() {
        return ApiResponse.success(
            messageResolver.getMessage("auth.session.success"),
            authenticationService.currentSession()
        );
    }
}
