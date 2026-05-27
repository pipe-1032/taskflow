package com.taskflow.taskflow.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @Test
    void shouldCreateUserResponseWithEmptyConstructorAndSetters() {
        UserResponse response = new UserResponse();

        response.setId(1L);
        response.setName("Juan Felipe");
        response.setEmail("juan@gmail.com");
        response.setRole("USER");

        assertEquals(1L, response.getId());
        assertEquals("Juan Felipe", response.getName());
        assertEquals("juan@gmail.com", response.getEmail());
        assertEquals("USER", response.getRole());
    }

    @Test
    void shouldCreateUserResponseWithAllArgsConstructor() {
        UserResponse response = new UserResponse(1L, "Juan Felipe", "juan@gmail.com", "USER");

        assertEquals(1L, response.getId());
        assertEquals("Juan Felipe", response.getName());
        assertEquals("juan@gmail.com", response.getEmail());
        assertEquals("USER", response.getRole());
    }
}