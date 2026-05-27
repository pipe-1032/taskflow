package com.taskflow.taskflow.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SecurityConfigTest {

    @Test
    void shouldCreatePasswordEncoderSuccessfully() {
        JwtAuthenticationFilter mockFilter = mock(JwtAuthenticationFilter.class);
        SecurityConfig securityConfig = new SecurityConfig(mockFilter);

        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder.matches("123456", passwordEncoder.encode("123456")));
    }

    @Test
    void shouldCreateSecurityConfigInstance() {
        JwtAuthenticationFilter mockFilter = mock(JwtAuthenticationFilter.class);
        SecurityConfig securityConfig = new SecurityConfig(mockFilter);

        assertNotNull(securityConfig);
    }
}