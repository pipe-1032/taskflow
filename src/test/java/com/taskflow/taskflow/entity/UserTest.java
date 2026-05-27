package com.taskflow.taskflow.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUserWithEmptyConstructorAndSetters() {
        User user = new User();

        user.setId(1L);
        user.setName("Juan Felipe");
        user.setEmail("juan@gmail.com");
        user.setPassword("123456");
        user.setRole("USER");

        assertEquals(1L, user.getId());
        assertEquals("Juan Felipe", user.getName());
        assertEquals("juan@gmail.com", user.getEmail());
        assertEquals("123456", user.getPassword());
        assertEquals("USER", user.getRole());
    }

    @Test
    void shouldCreateUserWithAllArgsConstructor() {
        User user = new User(1L, "Juan Felipe", "juan@gmail.com", "123456", "USER");

        assertEquals(1L, user.getId());
        assertEquals("Juan Felipe", user.getName());
        assertEquals("juan@gmail.com", user.getEmail());
        assertEquals("123456", user.getPassword());
        assertEquals("USER", user.getRole());
    }
}