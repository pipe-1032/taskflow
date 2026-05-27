package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dto.NotificationRequest;
import com.taskflow.taskflow.entity.Notification;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.repository.NotificationRepository;
import com.taskflow.taskflow.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationService notificationService;

    private User user;
    private Notification notification;
    private NotificationRequest request;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Juan");
        user.setEmail("juan@gmail.com");

        notification = new Notification();
        notification.setId(1L);
        notification.setMessage("Test");
        notification.setType("TEST");
        notification.setUser(user);

        request = new NotificationRequest();
        request.setMessage("Test");
        request.setType("TEST");
        request.setUserId(1L);
    }

    @Test
    void shouldCreateNotificationSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.createNotification(request);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnCreate() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> notificationService.createNotification(request));

        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void shouldCreateAutomaticNotification() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.createAutomaticNotification(
                user,
                "Mensaje",
                "TYPE"
        );

        assertNotNull(result);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void shouldGetAllNotifications() {
        when(notificationRepository.findAll()).thenReturn(List.of(notification));

        List<Notification> result = notificationService.getAllNotifications();

        assertEquals(1, result.size());
        verify(notificationRepository).findAll();
    }

    @Test
    void shouldGetNotificationsByUserIdSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(notificationRepository.findByUser(user)).thenReturn(List.of(notification));

        List<Notification> result = notificationService.getNotificationsByUserId(1L);

        assertEquals(1, result.size());
        verify(notificationRepository).findByUser(user);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnGetByUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> notificationService.getNotificationsByUserId(1L));

        assertEquals("Usuario no encontrado", ex.getMessage());
    }
}