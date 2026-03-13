package com.docreview.backend.framework.api;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
class FrameworkEndpointTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void publicPingShouldReturnLocalizedPayload() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAcceptLanguageAsLocales(List.of(Locale.SIMPLIFIED_CHINESE));

        ResponseEntity<JsonNode> response = testRestTemplate.exchange(
            "http://localhost:" + port + "/api/public/framework/ping",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            JsonNode.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().path("code").asText()).isEqualTo("COMMON_SUCCESS");
        assertThat(response.getBody().path("data").path("locale").asText()).isEqualTo("zh-CN");
    }

    @Test
    void runtimeShouldRequireAuthentication() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(
            "http://localhost:" + port + "/api/internal/framework/runtime",
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void runtimeShouldAllowSeededAdministratorSession() {
        String sessionCookie = login("admin", "A123456!");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, sessionCookie);

        ResponseEntity<JsonNode> response = testRestTemplate.exchange(
            "http://localhost:" + port + "/api/internal/framework/runtime",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            JsonNode.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().path("data").path("applicationName").asText())
            .isEqualTo("doc-review-backend");
    }

    private String login(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<JsonNode> response = testRestTemplate.exchange(
            "http://localhost:" + port + "/api/public/auth/login",
            HttpMethod.POST,
            new HttpEntity<>(Map.of("username", username, "password", password), headers),
            JsonNode.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getFirst(HttpHeaders.SET_COOKIE)).isNotBlank();
        return response.getHeaders().getFirst(HttpHeaders.SET_COOKIE).split(";", 2)[0];
    }
}
