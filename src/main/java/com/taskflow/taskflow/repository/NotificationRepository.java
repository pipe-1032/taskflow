package com.taskflow.taskflow.repository;

import com.taskflow.taskflow.entity.Notification;
import com.taskflow.taskflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUser(User user);

    boolean existsByUser(User user);
}