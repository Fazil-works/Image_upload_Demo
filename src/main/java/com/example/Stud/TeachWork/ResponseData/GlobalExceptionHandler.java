package com.example.Stud.TeachWork.ResponseData;

import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice  // Ensures that this class handles exceptions globally across all controllers
public class GlobalExceptionHandler {
    
    // A helper method to construct the ResponseData in a consistent format
    private ResponseData buildApiResponse(
            int code,
            Status status,
            String message,
            List<String> data,
            String path,
            LocalDateTime timestamp) {

        return new ResponseData(
                status.toString(),  // Status (e.g., SUCCESS, FAIL, ERROR)
                message,  // Message describing the result
                data  // List of error messages or relevant data
        );
    }

    // Handles validation errors (e.g., when @Valid validation fails)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseData> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        // Extracting error messages from validation exceptions
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(err -> err.getDefaultMessage())  // Get default message for each error
                .collect(Collectors.toList());  // Collect all error messages into a list

        // Return the structured response
        return new ResponseEntity<>( 
                buildApiResponse(
                    HttpStatus.BAD_REQUEST.value(),  // HTTP Status Code 400
                    Status.FAIL,  // Status FAIL as validation failed
                    "Validation failed",  // Message to inform validation failed
                    errors,  // List of error messages
                    request.getRequestURI(),  // Request URI
                    LocalDateTime.now()  // Timestamp of the error
                ),
                HttpStatus.BAD_REQUEST  // HTTP status BAD_REQUEST
        );
    }

    // Handles business logic errors (e.g., duplicate email or phone number)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseData> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        // Return the business logic validation error response
        return new ResponseEntity<>( 
                buildApiResponse(
                    HttpStatus.BAD_REQUEST.value(),  // HTTP Status Code 400
                    Status.FAIL,  // Status FAIL for business logic errors
                    "Business validation failed",  // Message explaining the failure
                    List.of(ex.getMessage()),  // Specific error message as a list
                    request.getRequestURI(),  // Request URI
                    LocalDateTime.now()  // Timestamp of when the error occurred
                ),
                HttpStatus.BAD_REQUEST  // HTTP status BAD_REQUEST
        );
    }

    // Catch-all handler for unexpected exceptions (e.g., server errors)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData> handleUnexpectedException(
            Exception ex,
            HttpServletRequest request) {

        // Print the stack trace for debugging purposes
        ex.printStackTrace();

        // Return the response for unexpected errors
        return new ResponseEntity<>( 
                buildApiResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),  // HTTP Status Code 500
                    Status.ERROR,  // Status ERROR for unexpected errors
                    "Unexpected error occurred",  // Generic error message
                    List.of("Something went wrong"),  // General error message
                    request.getRequestURI(),  // Request URI
                    LocalDateTime.now()  // Timestamp of the error
                ),
                HttpStatus.INTERNAL_SERVER_ERROR  // HTTP status INTERNAL_SERVER_ERROR
        );
    }

    // Handles runtime exceptions like "User not found"
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseData> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request) {

        // Return response for runtime errors like user not found
        return new ResponseEntity<>( 
                buildApiResponse(
                    HttpStatus.NOT_FOUND.value(),  // HTTP Status Code 404
                    Status.FAIL,  // Status FAIL
                    ex.getMessage(),  // e.g., "User not found"
                    List.of(ex.getMessage()),  // Error message as a list
                    request.getRequestURI(),  // Request URI
                    LocalDateTime.now()  // Timestamp of when the error occurred
                ),
                HttpStatus.NOT_FOUND  // HTTP status NOT_FOUND
        );
    }

    // Custom handler for file already exists error
    @ExceptionHandler(FileAlreadyExistsException.class)
    public ResponseEntity<ResponseData> handleFileAlreadyExistsException(
            FileAlreadyExistsException ex,
            HttpServletRequest request) {

        // Return response for file already exists error
        return new ResponseEntity<>( 
                buildApiResponse(
                    HttpStatus.BAD_REQUEST.value(),  // HTTP Status Code 400
                    Status.FAIL,  // Status FAIL
                    "File already exists",  // Custom error message
                    List.of(ex.getMessage()),  // Error message as a list
                    request.getRequestURI(),  // Request URI
                    LocalDateTime.now()  // Timestamp of when the error occurred
                ),
                HttpStatus.BAD_REQUEST  // HTTP status BAD_REQUEST
        );
    }
}
