package com.taskflow.taskflow.controller;

import com.taskflow.taskflow.dto.ApiResponse;
import com.taskflow.taskflow.dto.AuthResponse;
import com.taskflow.taskflow.dto.LoginRequest;
import com.taskflow.taskflow.dto.UserResponse;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.security.JwtService;
import com.taskflow.taskflow.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@RequestBody User user) {
        User savedUser = userService.registerUser(user);

        UserResponse userResponse = new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );

        ApiResponse<UserResponse> response = new ApiResponse<>(
                true,
                "Cuenta creada correctamente",
                userResponse
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userService.loginUser(loginRequest);

        String token = jwtService.generateToken(user.getEmail());

        AuthResponse authResponse = new AuthResponse(token);

        ApiResponse<AuthResponse> response = new ApiResponse<>(
                true,
                "Inicio de sesión exitoso",
                authResponse
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        ApiResponse<Void> response = new ApiResponse<>(
                true,
                "Usuario eliminado correctamente",
                null
        );

        return ResponseEntity.ok(response);
    }
}