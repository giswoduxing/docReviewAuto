package com.docreview.backend.auth.service;

import com.docreview.backend.auth.api.LoginRequest;
import com.docreview.backend.auth.api.SessionUserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {

    SessionUserResponse login(
        LoginRequest request,
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse
    );

    void logout(HttpServletRequest request, HttpServletResponse response);

    SessionUserResponse currentSession();
}
