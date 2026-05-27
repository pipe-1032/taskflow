package com.taskflow.taskflow.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void shouldCreateTaskWithEmptyConstructorAndSetters() {
        User user = new User();
        user.setId(1L);
        user.setName("Juan Felipe");
        user.setEmail("juan@gmail.com");
        user.setPassword("123456");
        user.setRole("USER");

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Tarea prueba");
        task.setDescription("Descripción prueba");
        task.setStatus("PENDIENTE");
        task.setPriority("ALTA");
        task.setDueDate(LocalDate.of(2026, 6, 10));
        task.setUser(user);

        assertEquals(1L, task.getId());
        assertEquals("Tarea prueba", task.getTitle());
        assertEquals("Descripción prueba", task.getDescription());
        assertEquals("PENDIENTE", task.getStatus());
        assertEquals("ALTA", task.getPriority());
        assertEquals(LocalDate.of(2026, 6, 10), task.getDueDate());
        assertNotNull(task.getUser());
        assertEquals("Juan Felipe", task.getUser().getName());
    }

    @Test
    void shouldCreateTaskWithAllArgsConstructor() {
        User user = new User(1L, "Juan Felipe", "juan@gmail.com", "123456", "USER");

        Task task = new Task(
                1L,
                "Tarea prueba",
                "Descripción prueba",
                "PENDIENTE",
                "ALTA",
                LocalDate.of(2026, 6, 10),
                user
        );

        assertEquals(1L, task.getId());
        assertEquals("Tarea prueba", task.getTitle());
        assertEquals("Descripción prueba", task.getDescription());
        assertEquals("PENDIENTE", task.getStatus());
        assertEquals("ALTA", task.getPriority());
        assertEquals(LocalDate.of(2026, 6, 10), task.getDueDate());
        assertNotNull(task.getUser());
        assertEquals("Juan Felipe", task.getUser().getName());
    }
}