package com.avoris.app.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Global exception handler that catches runtime exceptions and provides a custom error response.
 * This ensures that unhandled exceptions result in appropriate HTTP responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles RuntimeExceptions globally, returning a custom error message with HTTP 500 status.
     *
     * @param ex the exception that occurred
     * @return a custom error message
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeException(RuntimeException ex) {
        return "An error occurred: " + ex.getMessage();
    }
}
