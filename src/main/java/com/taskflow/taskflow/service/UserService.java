package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dto.LoginRequest;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.repository.NotificationRepository;
import com.taskflow.taskflow.repository.TaskRepository;
import com.taskflow.taskflow.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final TaskRepository taskRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       NotificationService notificationService,
                       TaskRepository taskRepository,
                       NotificationRepository notificationRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.taskRepository = taskRepository;
        this.notificationRepository = notificationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        notificationService.createAutomaticNotification(
                savedUser,
                "Cuenta creada correctamente",
                "ACCOUNT_CREATED"
        );

        return savedUser;
    }

    public User loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return user;
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean hasTasks = taskRepository.existsByUser(user);

        if (hasTasks) {
            throw new RuntimeException("No se puede eliminar el usuario porque tiene tareas asociadas");
        }

        notificationRepository.deleteAll(notificationRepository.findByUser(user));
        userRepository.delete(user);
    }
}