package com.example.orn.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Catches any exception that escapes a controller and returns a clean,
 * consistent JSON error instead of the default Spring Boot 500 page.
 * Also logs the full stack trace server-side so it shows up in Render logs.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Added for Phase 2 (Role-Based Authorization): without this explicit
    // handler, AccessDeniedException thrown by @PreAuthorize checks would be
    // swallowed by the generic Exception handler below and returned as a
    // 500 instead of the correct 403 Forbidden.
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException e) {
        log.warn("Access denied: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("You do not have permission to access this resource");
    }

    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    public ResponseEntity<String> handleDuplicateResult(IncorrectResultSizeDataAccessException e) {
        log.error("Query returned more than one result where a single result was expected", e);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Account data conflict. Please contact support.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        log.error("Illegal argument (often a malformed/legacy password hash)", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unable to process request due to invalid stored data.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception e) {
        log.error("Unhandled exception reached the global handler", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong. Please try again later.");
    }
}
