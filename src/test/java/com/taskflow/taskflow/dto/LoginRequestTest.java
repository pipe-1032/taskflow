package com.taskflow.taskflow.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void shouldCreateLoginRequestWithEmptyConstructorAndSetters() {
        LoginRequest request = new LoginRequest();

        request.setEmail("juan@gmail.com");
        request.setPassword("123456");

        assertEquals("juan@gmail.com", request.getEmail());
        assertEquals("123456", request.getPassword());
    }

    @Test
    void shouldCreateLoginRequestWithAllArgsConstructor() {
        LoginRequest request = new LoginRequest("juan@gmail.com", "123456");

        assertEquals("juan@gmail.com", request.getEmail());
        assertEquals("123456", request.getPassword());
    }
}