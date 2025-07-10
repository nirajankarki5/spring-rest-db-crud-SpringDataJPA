package com.example.restdb.exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Add a handler for 404 errors (proper status codes)
    // for ..employees/9999
    // this does not show 404 for ../employeessssss
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(Exception ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Resource not found",
                DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // for ../employeessss, show 404 as well
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Endpoint not found",
                DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // for generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllException(Exception ex) {

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
