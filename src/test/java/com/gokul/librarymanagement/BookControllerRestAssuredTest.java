package com.gokul.librarymanagement;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
classes = LibraryManagementApplication.class)
@AutoConfigureTestRestTemplate
public class BookControllerRestAssuredTest {

    @org.springframework.boot.test.context.TestConfiguration
    public static class TestConfiguration {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(req -> req.anyRequest().permitAll());
            return http.build();
        }
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getAllBooksTest() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/book", String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }
}
