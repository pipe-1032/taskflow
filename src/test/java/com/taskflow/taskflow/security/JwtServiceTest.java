package com.taskflow.taskflow.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void shouldGenerateTokenSuccessfully() {
        String token = jwtService.generateToken("juan@gmail.com");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldExtractUsernameSuccessfully() {
        String token = jwtService.generateToken("juan@gmail.com");

        String username = jwtService.extractUsername(token);

        assertEquals("juan@gmail.com", username);
    }

    @Test
    void shouldValidateTokenSuccessfully() {
        String token = jwtService.generateToken("juan@gmail.com");

        boolean valid = jwtService.isTokenValid(token, "juan@gmail.com");

        assertTrue(valid);
    }

    @Test
    void shouldReturnFalseWhenTokenDoesNotMatchUser() {
        String token = jwtService.generateToken("juan@gmail.com");

        boolean valid = jwtService.isTokenValid(token, "otro@gmail.com");

        assertFalse(valid);
    }
}