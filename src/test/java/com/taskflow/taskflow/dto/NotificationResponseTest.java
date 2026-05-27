package com.taskflow.taskflow.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NotificationResponseTest {

    @Test
    void shouldCreateNotificationResponseWithEmptyConstructorAndSetters() {
        UserResponse userResponse = new UserResponse(1L, "Juan Felipe", "juan@gmail.com", "USER");

        NotificationResponse response = new NotificationResponse();
        response.setId(1L);
        response.setMessage("Cuenta creada correctamente");
        response.setRead(false);
        response.setType("ACCOUNT_CREATED");
        response.setCreatedAt("2026-05-25T18:00:00");
        response.setUser(userResponse);

        assertEquals(1L, response.getId());
        assertEquals("Cuenta creada correctamente", response.getMessage());
        assertFalse(response.isRead());
        assertEquals("ACCOUNT_CREATED", response.getType());
        assertEquals("2026-05-25T18:00:00", response.getCreatedAt());
        assertNotNull(response.getUser());
        assertEquals("Juan Felipe", response.getUser().getName());
    }

    @Test
    void shouldCreateNotificationResponseWithAllArgsConstructor() {
        UserResponse userResponse = new UserResponse(1L, "Juan Felipe", "juan@gmail.com", "USER");

        NotificationResponse response = new NotificationResponse(
                1L,
                "Cuenta creada correctamente",
                false,
                "ACCOUNT_CREATED",
                "2026-05-25T18:00:00",
                userResponse
        );

        assertEquals(1L, response.getId());
        assertEquals("Cuenta creada correctamente", response.getMessage());
        assertFalse(response.isRead());
        assertEquals("ACCOUNT_CREATED", response.getType());
        assertEquals("2026-05-25T18:00:00", response.getCreatedAt());
        assertNotNull(response.getUser());
        assertEquals("Juan Felipe", response.getUser().getName());
    }
}