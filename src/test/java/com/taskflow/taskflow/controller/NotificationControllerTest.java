package com.taskflow.taskflow.controller;

import com.taskflow.taskflow.entity.Notification;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.exception.GlobalExceptionHandler;
import com.taskflow.taskflow.security.JwtAuthenticationFilter;
import com.taskflow.taskflow.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private User user;
    private Notification notification;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Juan Felipe");
        user.setEmail("juan@gmail.com");
        user.setPassword("123456");
        user.setRole("USER");

        notification = new Notification();
        notification.setId(1L);
        notification.setMessage("Cuenta creada correctamente");
        notification.setType("ACCOUNT_CREATED");
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.of(2026, 5, 25, 18, 0));
        notification.setUser(user);
    }

    @Test
    void shouldCreateNotificationSuccessfully() throws Exception {
        when(notificationService.createNotification(any())).thenReturn(notification);

        String requestBody = """
                {
                  "message": "Cuenta creada correctamente",
                  "type": "ACCOUNT_CREATED",
                  "userId": 1
                }
                """;

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Notificación creada correctamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.message").value("Cuenta creada correctamente"))
                .andExpect(jsonPath("$.data.type").value("ACCOUNT_CREATED"))
                .andExpect(jsonPath("$.data.read").value(false))
                .andExpect(jsonPath("$.data.user.id").value(1))
                .andExpect(jsonPath("$.data.user.name").value("Juan Felipe"))
                .andExpect(jsonPath("$.data.user.email").value("juan@gmail.com"))
                .andExpect(jsonPath("$.data.user.role").value("USER"));
    }

    @Test
    void shouldReturnErrorWhenCreateNotificationFails() throws Exception {
        when(notificationService.createNotification(any()))
                .thenThrow(new RuntimeException("Usuario no encontrado"));

        String requestBody = """
                {
                  "message": "Cuenta creada correctamente",
                  "type": "ACCOUNT_CREATED",
                  "userId": 1
                }
                """;

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Usuario no encontrado"));
    }

    @Test
    void shouldGetAllNotificationsSuccessfully() throws Exception {
        when(notificationService.getAllNotifications()).thenReturn(List.of(notification));

        mockMvc.perform(get("/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lista de notificaciones obtenida correctamente"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].message").value("Cuenta creada correctamente"))
                .andExpect(jsonPath("$.data[0].type").value("ACCOUNT_CREATED"));
    }

    @Test
    void shouldHandleNotificationWithNullCreatedAt() throws Exception {
        notification.setCreatedAt(null);
        when(notificationService.getAllNotifications()).thenReturn(List.of(notification));

        mockMvc.perform(get("/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lista de notificaciones obtenida correctamente"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].message").value("Cuenta creada correctamente"))
                .andExpect(jsonPath("$.data[0].createdAt").value((Object) null));
    }

    @Test
    void shouldGetNotificationsByUserIdSuccessfully() throws Exception {
        when(notificationService.getNotificationsByUserId(1L)).thenReturn(List.of(notification));

        mockMvc.perform(get("/notifications/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Notificaciones del usuario obtenidas correctamente"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].message").value("Cuenta creada correctamente"))
                .andExpect(jsonPath("$.data[0].user.id").value(1));
    }

    @Test
    void shouldReturnErrorWhenUserNotificationsNotFound() throws Exception {
        when(notificationService.getNotificationsByUserId(1L))
                .thenThrow(new RuntimeException("Usuario no encontrado"));

        mockMvc.perform(get("/notifications/user/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Usuario no encontrado"));
    }
}