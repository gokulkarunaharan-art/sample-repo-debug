package com.gokul.librarymanagement;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(BookControllerRestAssuredTest.TestConfiguration.class)
@ComponentScan(basePackages = "com.gokul.librarymanagement")
public class BookControllerRestAssuredTest {


    @Configuration
    public static class TestConfiguration{
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(req->req.anyRequest().permitAll());
            return http.build();
        }
    }


    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.reset();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void getAllBooksTest() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/book")
                .then()
                .assertThat().statusCode(200);
    }

}
