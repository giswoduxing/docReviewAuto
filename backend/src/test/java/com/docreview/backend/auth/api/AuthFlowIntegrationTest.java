package com.docreview.backend.auth.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.docreview.backend.user.domain.SystemUserEntity;
import com.docreview.backend.user.mapper.SystemUserMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthFlowIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private SystemUserMapper systemUserMapper;

    @Test
    void seededAdminShouldExposeManagementPermissions() {
        ResponseEntity<JsonNode> loginResponse = login("admin", "A123456!");

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();
        assertThat(loginResponse.getBody().path("data").path("username").asText()).isEqualTo("admin");
        assertThat(loginResponse.getBody().path("data").path("permissionCodes")).isNotEmpty();
        assertThat(loginResponse.getBody().path("data").path("permissionCodes").toString())
            .contains("organization:manage", "role:manage", "user:manage");
    }

    @Test
    void createdUserShouldOnlyReceiveAssignedMenuPermissions() {
        String suffix = uniqueSuffix();
        String adminCookie = sessionCookie(login("admin", "A123456!"));

        long organizationId = createOrganization(adminCookie, "Sales " + suffix, "SALES_" + suffix).path("data").path("id").asLong();
        long roleId = createRole(
            adminCookie,
            "ORG_" + suffix,
            "Org Manager " + suffix,
            List.of("organization:manage")
        ).path("data").path("id").asLong();

        createUser(
            adminCookie,
            "user" + suffix.toLowerCase(),
            "B123456!",
            "Org User " + suffix,
            "user" + suffix.toLowerCase() + "@example.com",
            organizationId,
            List.of(roleId)
        );

        String userCookie = sessionCookie(login("user" + suffix.toLowerCase(), "B123456!"));

        ResponseEntity<JsonNode> sessionResponse = authenticatedExchange(
            userCookie,
            HttpMethod.GET,
            "/api/internal/auth/session",
            null
        );
        assertThat(sessionResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sessionResponse.getBody().path("data").path("permissionCodes").toString())
            .contains("organization:manage")
            .doesNotContain("role:manage", "user:manage");

        ResponseEntity<JsonNode> organizationResponse = authenticatedExchange(
            userCookie,
            HttpMethod.GET,
            "/api/internal/organizations",
            null
        );
        assertThat(organizationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<JsonNode> userListResponse = authenticatedExchange(
            userCookie,
            HttpMethod.GET,
            "/api/internal/users?pageNumber=1&pageSize=10",
            null
        );
        assertThat(userListResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void userShouldBeLockedAfterThreeFailedPasswordsAndRecoverAfterExpiry() {
        String suffix = uniqueSuffix();
        String adminCookie = sessionCookie(login("admin", "A123456!"));

        long organizationId = createOrganization(adminCookie, "Risk " + suffix, "RISK_" + suffix).path("data").path("id").asLong();
        long roleId = createRole(
            adminCookie,
            "USER_" + suffix,
            "User Manager " + suffix,
            List.of("user:manage")
        ).path("data").path("id").asLong();

        String username = "lock" + suffix.toLowerCase();
        createUser(
            adminCookie,
            username,
            "C123456!",
            "Locked User " + suffix,
            username + "@example.com",
            organizationId,
            List.of(roleId)
        );

        assertThat(login(username, "wrong-password").getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(login(username, "wrong-password").getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        ResponseEntity<JsonNode> lockedResponse = login(username, "wrong-password");
        assertThat(lockedResponse.getStatusCode()).isEqualTo(HttpStatus.LOCKED);
        assertThat(lockedResponse.getBody().path("code").asText()).isEqualTo("ACCOUNT_LOCKED");

        ResponseEntity<JsonNode> blockedCorrectPassword = login(username, "C123456!");
        assertThat(blockedCorrectPassword.getStatusCode()).isEqualTo(HttpStatus.LOCKED);

        SystemUserEntity lockedUser = systemUserMapper.selectOne(
            Wrappers.<SystemUserEntity>lambdaQuery()
                .eq(SystemUserEntity::getUsername, username)
                .last("LIMIT 1")
        );
        assertThat(lockedUser.getLockedUntil()).isAfter(LocalDateTime.now().plusMinutes(9));

        lockedUser.setLockedUntil(LocalDateTime.now().minusMinutes(1));
        lockedUser.setFailedLoginAttempts(0);
        systemUserMapper.updateById(lockedUser);

        ResponseEntity<JsonNode> recoveredLogin = login(username, "C123456!");
        assertThat(recoveredLogin.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private JsonNode createOrganization(String cookie, String name, String code) {
        ResponseEntity<JsonNode> response = authenticatedExchange(
            cookie,
            HttpMethod.POST,
            "/api/internal/organizations",
            Map.of("name", name, "code", code, "leaderName", "Owner")
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }

    private JsonNode createRole(String cookie, String code, String name, List<String> permissionCodes) {
        ResponseEntity<JsonNode> response = authenticatedExchange(
            cookie,
            HttpMethod.POST,
            "/api/internal/roles",
            Map.of("code", code, "name", name, "description", "Generated in test", "permissionCodes", permissionCodes)
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }

    private JsonNode createUser(
        String cookie,
        String username,
        String password,
        String displayName,
        String email,
        long organizationId,
        List<Long> roleIds
    ) {
        ResponseEntity<JsonNode> response = authenticatedExchange(
            cookie,
            HttpMethod.POST,
            "/api/internal/users",
            Map.of(
                "username", username,
                "password", password,
                "displayName", displayName,
                "email", email,
                "organizationId", organizationId,
                "roleIds", roleIds
            )
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }

    private ResponseEntity<JsonNode> authenticatedExchange(
        String cookie,
        HttpMethod method,
        String path,
        Object body
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, cookie);

        return testRestTemplate.exchange(
            "http://localhost:" + port + path,
            method,
            new HttpEntity<>(body, headers),
            JsonNode.class
        );
    }

    private ResponseEntity<JsonNode> login(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return testRestTemplate.exchange(
            "http://localhost:" + port + "/api/public/auth/login",
            HttpMethod.POST,
            new HttpEntity<>(Map.of("username", username, "password", password), headers),
            JsonNode.class
        );
    }

    private String sessionCookie(ResponseEntity<JsonNode> response) {
        assertThat(response.getHeaders().getFirst(HttpHeaders.SET_COOKIE)).isNotBlank();
        return response.getHeaders().getFirst(HttpHeaders.SET_COOKIE).split(";", 2)[0];
    }

    private String uniqueSuffix() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
    }
}
