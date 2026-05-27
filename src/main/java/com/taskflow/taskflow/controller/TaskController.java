package com.taskflow.taskflow.controller;

import com.taskflow.taskflow.dto.ApiResponse;
import com.taskflow.taskflow.dto.TaskResponse;
import com.taskflow.taskflow.dto.UserResponse;
import com.taskflow.taskflow.entity.Task;
import com.taskflow.taskflow.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@RequestBody Task task) {
        Task savedTask = taskService.createTask(task);
        TaskResponse taskResponse = mapToTaskResponse(savedTask);

        ApiResponse<TaskResponse> response = new ApiResponse<>(
                true,
                "Tarea creada correctamente",
                taskResponse
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTasks() {
        List<TaskResponse> taskResponses = taskService.getAllTasks()
                .stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());

        ApiResponse<List<TaskResponse>> response = new ApiResponse<>(
                true,
                "Lista de tareas obtenida correctamente",
                taskResponses
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        TaskResponse taskResponse = mapToTaskResponse(task);

        ApiResponse<TaskResponse> response = new ApiResponse<>(
                true,
                "Tarea encontrada correctamente",
                taskResponse
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(id, task);
        TaskResponse taskResponse = mapToTaskResponse(updatedTask);

        ApiResponse<TaskResponse> response = new ApiResponse<>(
                true,
                "Tarea actualizada correctamente",
                taskResponse
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);

        ApiResponse<Void> response = new ApiResponse<>(
                true,
                "Tarea eliminada correctamente",
                null
        );

        return ResponseEntity.ok(response);
    }

    private TaskResponse mapToTaskResponse(Task task) {
        UserResponse userResponse = new UserResponse(
                task.getUser().getId(),
                task.getUser().getName(),
                task.getUser().getEmail(),
                task.getUser().getRole()
        );

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate() != null ? task.getDueDate().toString() : null,
                userResponse
        );
    }
}