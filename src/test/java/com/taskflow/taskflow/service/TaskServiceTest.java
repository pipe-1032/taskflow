package com.taskflow.taskflow.service;

import com.taskflow.taskflow.entity.Task;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.repository.TaskRepository;
import com.taskflow.taskflow.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Juan");
        user.setEmail("juan@gmail.com");

        task = new Task();
        task.setId(1L);
        task.setTitle("Test");
        task.setDescription("Test desc");
        task.setStatus("PENDIENTE");
        task.setPriority("ALTA");
        task.setDueDate(LocalDate.now());
        task.setUser(user);
    }

    @Test
    void shouldCreateTaskSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(task);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(taskRepository).save(task);
        verify(notificationService).createAutomaticNotification(
                user,
                "Tarea creada correctamente",
                "TASK_CREATED"
        );
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnCreate() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> taskService.createTask(task));

        assertEquals("Usuario no encontrado", ex.getMessage());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void shouldGetTaskByIdSuccessfully() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Test", result.getTitle());
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> taskService.getTaskById(1L));

        assertEquals("Tarea no encontrada", ex.getMessage());
    }

    @Test
    void shouldUpdateTaskSuccessfully() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        Task updated = new Task();
        updated.setTitle("Nuevo");
        updated.setDescription("Nueva desc");
        updated.setStatus("EN_PROCESO");
        updated.setPriority("MEDIA");
        updated.setDueDate(LocalDate.now());

        Task result = taskService.updateTask(1L, updated);

        assertEquals("Nuevo", result.getTitle());
        verify(notificationService).createAutomaticNotification(
                user,
                "Tarea actualizada correctamente",
                "TASK_UPDATED"
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdateTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> taskService.updateTask(1L, task));

        assertEquals("Tarea no encontrada", ex.getMessage());
    }

    @Test
    void shouldDeleteTaskSuccessfully() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository).delete(task);
    }

    @Test
    void shouldThrowExceptionWhenDeleteTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> taskService.deleteTask(1L));

        assertEquals("Tarea no encontrada", ex.getMessage());
    }
    @Test
    void shouldGetAllTasksSuccessfully() {
        when(taskRepository.findAll()).thenReturn(java.util.List.of(task));

        var result = taskService.getAllTasks();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(taskRepository).findAll();
    }
}