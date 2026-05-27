package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dto.LoginRequest;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.repository.NotificationRepository;
import com.taskflow.taskflow.repository.TaskRepository;
import com.taskflow.taskflow.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Juan Felipe");
        user.setEmail("juan@gmail.com");
        user.setPassword("123456");
        user.setRole("USER");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("juan@gmail.com");
        loginRequest.setPassword("123456");
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encoded-password");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Juan Felipe");
        savedUser.setEmail("juan@gmail.com");
        savedUser.setPassword("encoded-password");
        savedUser.setRole("USER");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.registerUser(user);

        assertNotNull(result);
        assertEquals("juan@gmail.com", result.getEmail());
        assertEquals("encoded-password", result.getPassword());

        verify(userRepository).existsByEmail(user.getEmail());
        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(any(User.class));
        verify(notificationService).createAutomaticNotification(
                savedUser,
                "Cuenta creada correctamente",
                "ACCOUNT_CREATED"
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUser(user));

        assertEquals("El correo ya está registrado", exception.getMessage());
        verify(userRepository).existsByEmail(user.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(notificationService, never()).createAutomaticNotification(any(), anyString(), anyString());
    }

    @Test
    void shouldLoginUserSuccessfully() {
        User storedUser = new User();
        storedUser.setId(1L);
        storedUser.setName("Juan Felipe");
        storedUser.setEmail("juan@gmail.com");
        storedUser.setPassword("encoded-password");
        storedUser.setRole("USER");

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(storedUser));
        when(passwordEncoder.matches("123456", "encoded-password")).thenReturn(true);

        User result = userService.loginUser(loginRequest);

        assertNotNull(result);
        assertEquals("juan@gmail.com", result.getEmail());
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches("123456", "encoded-password");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnLogin() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.loginUser(loginRequest));

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsIncorrect() {
        User storedUser = new User();
        storedUser.setId(1L);
        storedUser.setName("Juan Felipe");
        storedUser.setEmail("juan@gmail.com");
        storedUser.setPassword("encoded-password");
        storedUser.setRole("USER");

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(storedUser));
        when(passwordEncoder.matches("123456", "encoded-password")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.loginUser(loginRequest));

        assertEquals("Contraseña incorrecta", exception.getMessage());
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches("123456", "encoded-password");
    }

    @Test
    void shouldDeleteUserSuccessfullyWhenNoTasks() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.existsByUser(user)).thenReturn(false);
        when(notificationRepository.findByUser(user)).thenReturn(Collections.emptyList());

        userService.deleteUser(1L);

        verify(userRepository).findById(1L);
        verify(taskRepository).existsByUser(user);
        verify(notificationRepository).findByUser(user);
        verify(notificationRepository).deleteAll(Collections.emptyList());
        verify(userRepository).delete(user);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnDelete() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(taskRepository, never()).existsByUser(any(User.class));
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserHasTasks() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.existsByUser(user)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));

        assertEquals("No se puede eliminar el usuario porque tiene tareas asociadas", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(taskRepository).existsByUser(user);
        verify(notificationRepository, never()).findByUser(any(User.class));
        verify(notificationRepository, never()).deleteAll(any());
        verify(userRepository, never()).delete(any(User.class));
    }
}