package com.alura.desafio_forohub.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
            ApiError body = new ApiError(
                    Instant.now(),
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ex.getMessage(),
                    req.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }

        @ExceptionHandler(UnauthorizedAccessException.class)
        public ResponseEntity<ApiError> handleForbidden(UnauthorizedAccessException ex, HttpServletRequest req) {
            ApiError body = new ApiError(
                    Instant.now(),
                    HttpStatus.FORBIDDEN.value(),
                    HttpStatus.FORBIDDEN.getReasonPhrase(),
                    ex.getMessage(),
                    req.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
        }

        // Fallback genérico (opcional pero recomendable)
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleUnexpected(Exception ex, HttpServletRequest req) {
            ApiError body = new ApiError(
                    Instant.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    "Unexpected error",
                    req.getRequestURI());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
}
