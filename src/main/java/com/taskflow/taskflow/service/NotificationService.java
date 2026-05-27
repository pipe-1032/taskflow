package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dto.NotificationRequest;
import com.taskflow.taskflow.entity.Notification;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.repository.NotificationRepository;
import com.taskflow.taskflow.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public Notification createNotification(NotificationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Notification notification = new Notification();
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setUser(user);

        return notificationRepository.save(notification);
    }

    public Notification createAutomaticNotification(User user, String message, String type) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setType(type);
        notification.setUser(user);

        return notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return notificationRepository.findByUser(user);
    }
}