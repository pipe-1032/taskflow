package com.taskflow.taskflow.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void shouldCreateApiResponseWithEmptyConstructorAndSetters() {
        ApiResponse<String> response = new ApiResponse<>();

        response.setSuccess(true);
        response.setMessage("Operación exitosa");
        response.setData("dato");

        assertTrue(response.isSuccess());
        assertEquals("Operación exitosa", response.getMessage());
        assertEquals("dato", response.getData());
    }

    @Test
    void shouldCreateApiResponseWithAllArgsConstructor() {
        ApiResponse<String> response = new ApiResponse<>(true, "Mensaje", "contenido");

        assertTrue(response.isSuccess());
        assertEquals("Mensaje", response.getMessage());
        assertEquals("contenido", response.getData());
    }
}