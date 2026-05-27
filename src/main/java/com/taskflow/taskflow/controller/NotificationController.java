package com.taskflow.taskflow.controller;

import com.taskflow.taskflow.dto.ApiResponse;
import com.taskflow.taskflow.dto.NotificationRequest;
import com.taskflow.taskflow.dto.NotificationResponse;
import com.taskflow.taskflow.dto.UserResponse;
import com.taskflow.taskflow.entity.Notification;
import com.taskflow.taskflow.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponse>> createNotification(@RequestBody NotificationRequest request) {
        Notification savedNotification = notificationService.createNotification(request);
        NotificationResponse notificationResponse = mapToNotificationResponse(savedNotification);

        ApiResponse<NotificationResponse> response = new ApiResponse<>(
                true,
                "Notificación creada correctamente",
                notificationResponse
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getAllNotifications() {
        List<NotificationResponse> notificationResponses = notificationService.getAllNotifications()
                .stream()
                .map(this::mapToNotificationResponse)
                .collect(Collectors.toList());

        ApiResponse<List<NotificationResponse>> response = new ApiResponse<>(
                true,
                "Lista de notificaciones obtenida correctamente",
                notificationResponses
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotificationsByUserId(@PathVariable Long userId) {
        List<NotificationResponse> notificationResponses = notificationService.getNotificationsByUserId(userId)
                .stream()
                .map(this::mapToNotificationResponse)
                .collect(Collectors.toList());

        ApiResponse<List<NotificationResponse>> response = new ApiResponse<>(
                true,
                "Notificaciones del usuario obtenidas correctamente",
                notificationResponses
        );

        return ResponseEntity.ok(response);
    }

    private NotificationResponse mapToNotificationResponse(Notification notification) {
        UserResponse userResponse = new UserResponse(
                notification.getUser().getId(),
                notification.getUser().getName(),
                notification.getUser().getEmail(),
                notification.getUser().getRole()
        );

        return new NotificationResponse(
                notification.getId(),
                notification.getMessage(),
                notification.isRead(),
                notification.getType(),
                notification.getCreatedAt() != null ? notification.getCreatedAt().toString() : null,
                userResponse
        );
    }
}