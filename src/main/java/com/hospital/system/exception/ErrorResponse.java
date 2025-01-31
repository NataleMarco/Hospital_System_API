package com.hospital.system.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ErrorResponse {
    // Getters (necesarios para la serialización JSON)
    private final String code;
    private final String message;
    private final Map<String, Object> details; // Opcional: metadata adicional

    // Constructor
    public ErrorResponse(String code, String message, Map<String, Object> details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }

}