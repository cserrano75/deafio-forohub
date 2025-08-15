package com.alura.desafio_forohub.exception;

public class UnauthorizedAccessException extends RuntimeException{
    public UnauthorizedAccessException(String message) {
        super(message);
    }
    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
