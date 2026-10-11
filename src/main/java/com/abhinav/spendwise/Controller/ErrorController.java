package com.abhinav.spendwise.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.abhinav.spendwise.Domain.Dtos.ApiErrorResponse;

import jakarta.persistence.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Global Exception Handler for the ExpenseTracker Application.
 * Uses @RestControllerAdvice to catch and handle exceptions across all controllers.
 * Provides standardized error responses using ApiErrorResponse DTO.
 */
@RestControllerAdvice
@Slf4j
public class ErrorController {

    /**
     * Handles all uncaught exceptions (fallback handler).
     * Logs the error with stack trace for debugging.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {
        log.error("Unexpected error occurred", ex);

        ApiErrorResponse error = ApiErrorResponse.builder()
                                                 .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                                 .message("An unexpected error occurred")
                                                 .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles cases where a requested entity is not found in the database.
     * Example: Category or Expense with a given ID does not exist.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.warn("Entity not found: {}", ex.getMessage());

        ApiErrorResponse error = ApiErrorResponse.builder()
                                                 .status(HttpStatus.NOT_FOUND.value())
                                                 .message(ex.getMessage())
                                                 .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles invalid login attempts.
     * Example: Wrong email/password combination.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());

        ApiErrorResponse error = ApiErrorResponse.builder()
                                                 .status(HttpStatus.UNAUTHORIZED.value())
                                                 .message("Invalid credentials, please check your email and password.")
                                                 .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles invalid arguments passed to APIs.
     * Example: Creating a category with a duplicate name.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Invalid argument provided: {}", ex.getMessage());

        ApiErrorResponse error = ApiErrorResponse.builder()
                                                 .status(HttpStatus.BAD_REQUEST.value())
                                                 .message(ex.getMessage())
                                                 .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
