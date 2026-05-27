package com.taskflow.taskflow.service;

import com.taskflow.taskflow.entity.Task;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.repository.TaskRepository;
import com.taskflow.taskflow.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public Task createTask(Task task) {
        Long userId = task.getUser().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        task.setUser(user);
        Task savedTask = taskRepository.save(task);

        notificationService.createAutomaticNotification(
                user,
                "Tarea creada correctamente",
                "TASK_CREATED"
        );

        return savedTask;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setDueDate(updatedTask.getDueDate());

        Task savedTask = taskRepository.save(existingTask);

        notificationService.createAutomaticNotification(
                existingTask.getUser(),
                "Tarea actualizada correctamente",
                "TASK_UPDATED"
        );

        return savedTask;
    }

    public void deleteTask(Long id) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        taskRepository.delete(existingTask);
    }
}