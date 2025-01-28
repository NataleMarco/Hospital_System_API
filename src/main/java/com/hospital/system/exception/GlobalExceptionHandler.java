package com.hospital.system.exception;

import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String expectedTypeName = Objects.requireNonNull(ex.getRequiredType()).getSimpleName();

        String error = String.format("Invalid value for parameter '%s': '%s'. Expected type: %s",
                ex.getName(), ex.getValue(), expectedTypeName);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
        String message = "Se produjo un error de integridad de datos.";

        Throwable cause = ex.getRootCause();
        if (cause instanceof ConstraintViolationException consEx) {
            String constraintName = consEx.getConstraintName();

            if (constraintName != null) {
                message = switch (constraintName) {
                    case "unique_constraint_name" -> "El valor proporcionado ya existe. Debe ser único.";
                    case "not_null_constraint_name" -> "Faltan campos obligatorios.";
                    // Agrega más casos según tus restricciones
                    default -> "Violación de restricción de base de datos: " + constraintName;
                };
            }
        }

        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Utiliza ex.getBindingResult() para obtener los errores de validación
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        LoggerFactory.getLogger(GlobalExceptionHandler.class).error("Unhandled exception", ex);

        String message = "An unexpected error occurred.";
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
