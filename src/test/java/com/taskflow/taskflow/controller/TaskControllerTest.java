package com.taskflow.taskflow.controller;

import com.taskflow.taskflow.entity.Task;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.exception.GlobalExceptionHandler;
import com.taskflow.taskflow.security.JwtAuthenticationFilter;
import com.taskflow.taskflow.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Juan Felipe");
        user.setEmail("juan@gmail.com");
        user.setPassword("123456");
        user.setRole("USER");

        task = new Task();
        task.setId(1L);
        task.setTitle("Tarea prueba");
        task.setDescription("Descripción prueba");
        task.setStatus("PENDIENTE");
        task.setPriority("ALTA");
        task.setDueDate(LocalDate.of(2026, 6, 10));
        task.setUser(user);
    }

    @Test
    void shouldCreateTaskSuccessfully() throws Exception {
        when(taskService.createTask(any(Task.class))).thenReturn(task);

        String requestBody = """
                {
                  "title": "Tarea prueba",
                  "description": "Descripción prueba",
                  "status": "PENDIENTE",
                  "priority": "ALTA",
                  "dueDate": "2026-06-10",
                  "user": {
                    "id": 1
                  }
                }
                """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Tarea creada correctamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Tarea prueba"))
                .andExpect(jsonPath("$.data.description").value("Descripción prueba"))
                .andExpect(jsonPath("$.data.status").value("PENDIENTE"))
                .andExpect(jsonPath("$.data.priority").value("ALTA"))
                .andExpect(jsonPath("$.data.user.id").value(1))
                .andExpect(jsonPath("$.data.user.name").value("Juan Felipe"))
                .andExpect(jsonPath("$.data.user.email").value("juan@gmail.com"))
                .andExpect(jsonPath("$.data.user.role").value("USER"));
    }

    @Test
    void shouldReturnErrorWhenCreateTaskFails() throws Exception {
        when(taskService.createTask(any(Task.class)))
                .thenThrow(new RuntimeException("Usuario no encontrado"));

        String requestBody = """
                {
                  "title": "Tarea prueba",
                  "description": "Descripción prueba",
                  "status": "PENDIENTE",
                  "priority": "ALTA",
                  "dueDate": "2026-06-10",
                  "user": {
                    "id": 1
                  }
                }
                """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Usuario no encontrado"));
    }

    @Test
    void shouldGetAllTasksSuccessfully() throws Exception {
        when(taskService.getAllTasks()).thenReturn(List.of(task));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lista de tareas obtenida correctamente"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Tarea prueba"));
    }

    @Test
    void shouldHandleTaskWithNullDueDate() throws Exception {
        task.setDueDate(null);
        when(taskService.getAllTasks()).thenReturn(List.of(task));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Lista de tareas obtenida correctamente"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Tarea prueba"))
                .andExpect(jsonPath("$.data[0].dueDate").value((Object) null));
    }

    @Test
    void shouldGetTaskByIdSuccessfully() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(task);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Tarea encontrada correctamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Tarea prueba"));
    }

    @Test
    void shouldReturnErrorWhenTaskNotFound() throws Exception {
        when(taskService.getTaskById(1L))
                .thenThrow(new RuntimeException("Tarea no encontrada"));

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Tarea no encontrada"));
    }

    @Test
    void shouldUpdateTaskSuccessfully() throws Exception {
        when(taskService.updateTask(anyLong(), any(Task.class))).thenReturn(task);

        String requestBody = """
                {
                  "title": "Tarea prueba",
                  "description": "Descripción prueba",
                  "status": "PENDIENTE",
                  "priority": "ALTA",
                  "dueDate": "2026-06-10"
                }
                """;

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Tarea actualizada correctamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Tarea prueba"));
    }

    @Test
    void shouldReturnErrorWhenUpdateFails() throws Exception {
        when(taskService.updateTask(anyLong(), any(Task.class)))
                .thenThrow(new RuntimeException("Tarea no encontrada"));

        String requestBody = """
                {
                  "title": "Tarea prueba",
                  "description": "Descripción prueba",
                  "status": "PENDIENTE",
                  "priority": "ALTA",
                  "dueDate": "2026-06-10"
                }
                """;

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Tarea no encontrada"));
    }

    @Test
    void shouldDeleteTaskSuccessfully() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Tarea eliminada correctamente"));
    }

    @Test
    void shouldReturnErrorWhenDeleteFails() throws Exception {
        doThrow(new RuntimeException("Tarea no encontrada"))
                .when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Tarea no encontrada"));
    }
}