package com.taskflow.taskflow.controller;

import com.taskflow.taskflow.dto.LoginRequest;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.exception.GlobalExceptionHandler;
import com.taskflow.taskflow.security.JwtAuthenticationFilter;
import com.taskflow.taskflow.security.JwtService;
import com.taskflow.taskflow.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

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
    void shouldRegisterUserSuccessfully() throws Exception {
        when(userService.registerUser(any(User.class))).thenReturn(user);

        String requestBody = """
                {
                  "name": "Juan Felipe",
                  "email": "juan@gmail.com",
                  "password": "123456",
                  "role": "USER"
                }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cuenta creada correctamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Juan Felipe"))
                .andExpect(jsonPath("$.data.email").value("juan@gmail.com"))
                .andExpect(jsonPath("$.data.role").value("USER"));
    }

    @Test
    void shouldReturnErrorWhenRegisterFails() throws Exception {
        when(userService.registerUser(any(User.class)))
                .thenThrow(new RuntimeException("El correo ya está registrado"));

        String requestBody = """
                {
                  "name": "Juan Felipe",
                  "email": "juan@gmail.com",
                  "password": "123456",
                  "role": "USER"
                }
                """;

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("El correo ya está registrado"));
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        when(userService.loginUser(any(LoginRequest.class))).thenReturn(user);
        when(jwtService.generateToken("juan@gmail.com")).thenReturn("fake-jwt-token");

        String requestBody = """
                {
                  "email": "juan@gmail.com",
                  "password": "123456"
                }
                """;

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Inicio de sesión exitoso"))
                .andExpect(jsonPath("$.data.token").value("fake-jwt-token"));
    }

    @Test
    void shouldReturnErrorWhenLoginFails() throws Exception {
        when(userService.loginUser(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Contraseña incorrecta"));

        String requestBody = """
                {
                  "email": "juan@gmail.com",
                  "password": "123456"
                }
                """;

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Contraseña incorrecta"));
    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Usuario eliminado correctamente"));
    }

    @Test
    void shouldReturnErrorWhenDeleteFails() throws Exception {
        doThrow(new RuntimeException("No se puede eliminar el usuario porque tiene tareas asociadas"))
                .when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("No se puede eliminar el usuario porque tiene tareas asociadas"));
    }
}