package com.hospital.system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            DoctorAssociatedAppointmentsException.class,
            PatientAssociatedAppointmentsException.class
    })
    public ResponseEntity<ErrorResponse> handleAssociatedAppointmentsExceptions(BusinessException ex) {
        ErrorResponse response = new ErrorResponse(
                ex.getErrorCode(),
                ex.getMessage(),
                new HashMap<>()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT); // 409 Conflict
    }

    @ExceptionHandler({
            DoctorNotFoundException.class,
            PatientNotFoundException.class,
            AppointmentNotFoundException.class,
    })
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(BusinessException ex){
        ErrorResponse response = new ErrorResponse(
                ex.getErrorCode(),
                ex.getMessage(),
                new HashMap<>()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("errors", errors);

        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

}
