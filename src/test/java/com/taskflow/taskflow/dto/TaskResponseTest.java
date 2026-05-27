package com.taskflow.taskflow.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskResponseTest {

    @Test
    void shouldCreateTaskResponseWithEmptyConstructorAndSetters() {
        UserResponse userResponse = new UserResponse(1L, "Juan Felipe", "juan@gmail.com", "USER");

        TaskResponse response = new TaskResponse();
        response.setId(1L);
        response.setTitle("Tarea prueba");
        response.setDescription("Descripción prueba");
        response.setStatus("PENDIENTE");
        response.setPriority("ALTA");
        response.setDueDate("2026-06-10");
        response.setUser(userResponse);

        assertEquals(1L, response.getId());
        assertEquals("Tarea prueba", response.getTitle());
        assertEquals("Descripción prueba", response.getDescription());
        assertEquals("PENDIENTE", response.getStatus());
        assertEquals("ALTA", response.getPriority());
        assertEquals("2026-06-10", response.getDueDate());
        assertNotNull(response.getUser());
        assertEquals("Juan Felipe", response.getUser().getName());
    }

    @Test
    void shouldCreateTaskResponseWithAllArgsConstructor() {
        UserResponse userResponse = new UserResponse(1L, "Juan Felipe", "juan@gmail.com", "USER");

        TaskResponse response = new TaskResponse(
                1L,
                "Tarea prueba",
                "Descripción prueba",
                "PENDIENTE",
                "ALTA",
                "2026-06-10",
                userResponse
        );

        assertEquals(1L, response.getId());
        assertEquals("Tarea prueba", response.getTitle());
        assertEquals("Descripción prueba", response.getDescription());
        assertEquals("PENDIENTE", response.getStatus());
        assertEquals("ALTA", response.getPriority());
        assertEquals("2026-06-10", response.getDueDate());
        assertNotNull(response.getUser());
        assertEquals("Juan Felipe", response.getUser().getName());
    }
}