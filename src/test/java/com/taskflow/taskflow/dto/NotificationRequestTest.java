package com.taskflow.taskflow.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificationRequestTest {

    @Test
    void shouldCreateNotificationRequestWithEmptyConstructorAndSetters() {
        NotificationRequest request = new NotificationRequest();

        request.setMessage("Cuenta creada correctamente");
        request.setType("ACCOUNT_CREATED");
        request.setUserId(1L);

        assertEquals("Cuenta creada correctamente", request.getMessage());
        assertEquals("ACCOUNT_CREATED", request.getType());
        assertEquals(1L, request.getUserId());
    }

    @Test
    void shouldCreateNotificationRequestWithAllArgsConstructor() {
        NotificationRequest request = new NotificationRequest(
                "Cuenta creada correctamente",
                "ACCOUNT_CREATED",
                1L
        );

        assertEquals("Cuenta creada correctamente", request.getMessage());
        assertEquals("ACCOUNT_CREATED", request.getType());
        assertEquals(1L, request.getUserId());
    }
}
