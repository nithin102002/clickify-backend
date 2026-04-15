package com.example.product_service.exception;

import com.example.product_service.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse("BAD_REQUEST", ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse("NOT_FOUND", ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        String error = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(field -> field.getField() + " : " + field.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        return ResponseEntity.badRequest().body(
                new ErrorResponse("VALIDATION_ERROR", error, LocalDateTime.now())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse("INTERNAL_ERROR", ex.getMessage(), LocalDateTime.now())
        );
    }
}

