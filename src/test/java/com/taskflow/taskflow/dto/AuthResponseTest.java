package com.taskflow.taskflow.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void shouldCreateAuthResponseWithEmptyConstructorAndSetter() {
        AuthResponse response = new AuthResponse();
        response.setToken("mi-token");

        assertEquals("mi-token", response.getToken());
    }

    @Test
    void shouldCreateAuthResponseWithAllArgsConstructor() {
        AuthResponse response = new AuthResponse("jwt-token");

        assertEquals("jwt-token", response.getToken());
    }
}