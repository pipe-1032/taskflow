package com.taskflow.taskflow.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    @Test
    void shouldCreateNotificationWithEmptyConstructorAndSetters() {
        User user = new User(1L, "Juan Felipe", "juan@gmail.com", "123456", "USER");

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setMessage("Cuenta creada correctamente");
        notification.setRead(false);
        notification.setType("ACCOUNT_CREATED");
        notification.setCreatedAt(LocalDateTime.of(2026, 5, 25, 18, 0));
        notification.setUser(user);

        assertEquals(1L, notification.getId());
        assertEquals("Cuenta creada correctamente", notification.getMessage());
        assertFalse(notification.isRead());
        assertEquals("ACCOUNT_CREATED", notification.getType());
        assertEquals(LocalDateTime.of(2026, 5, 25, 18, 0), notification.getCreatedAt());
        assertNotNull(notification.getUser());
        assertEquals("Juan Felipe", notification.getUser().getName());
    }

    @Test
    void shouldCreateNotificationWithAllArgsConstructor() {
        User user = new User(1L, "Juan Felipe", "juan@gmail.com", "123456", "USER");

        Notification notification = new Notification(
                1L,
                "Cuenta creada correctamente",
                false,
                "ACCOUNT_CREATED",
                LocalDateTime.of(2026, 5, 25, 18, 0),
                user
        );

        assertEquals(1L, notification.getId());
        assertEquals("Cuenta creada correctamente", notification.getMessage());
        assertFalse(notification.isRead());
        assertEquals("ACCOUNT_CREATED", notification.getType());
        assertEquals(LocalDateTime.of(2026, 5, 25, 18, 0), notification.getCreatedAt());
        assertNotNull(notification.getUser());
        assertEquals("Juan Felipe", notification.getUser().getName());
    }

    @Test
    void shouldSetValuesWhenPrePersistIsCalled() {
        Notification notification = new Notification();

        assertNull(notification.getCreatedAt());

        notification.prePersist();

        assertNotNull(notification.getCreatedAt());
        assertFalse(notification.isRead());
    }
}