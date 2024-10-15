package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle UserNotFoundException globally
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        // Return a 404 status code along with the exception message
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Handle other custom or general exceptions if needed
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex, WebRequest request) {
        // Return a 500 status code for other unhandled exceptions
        return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
