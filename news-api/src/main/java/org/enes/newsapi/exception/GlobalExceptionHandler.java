package org.enes.newsapi.exception;

import java.util.List;

import org.enes.newsapi.dto.ErrorResponse;
import org.enes.newsapi.util.ErrorResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final ErrorResponseBuilder errorResponseBuilder;

    public GlobalExceptionHandler(ErrorResponseBuilder errorResponseBuilder) {
        this.errorResponseBuilder = errorResponseBuilder;
    }

    @ExceptionHandler(NewsNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNewsNotFoundException(
        NewsNotFoundException ex,
        HttpServletRequest request
    ) {
        log.warn("News not found with id: {} for path: {}", ex.getMessage(), request.getRequestURI());

        ErrorResponse errorResponse = errorResponseBuilder.buildFromException(ex, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestParamException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestParamException(
        InvalidRequestParamException ex,
        HttpServletRequest request
    ) {
        log.warn("Invalid request parameters: {} for path: {}", ex.getMessage(), request.getRequestURI());

        ErrorResponse errorResponse = errorResponseBuilder.buildFromException(ex, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidSortFieldException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSortFieldException(
        InvalidSortFieldException ex,
        HttpServletRequest request
    ) {
        log.warn("Invalid sort field: {} for path: {}", ex.getMessage(), request.getRequestURI());

        ErrorResponse errorResponse = errorResponseBuilder.buildFromException(ex, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(InvalidSortDirectionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSortDirectionException(
        InvalidSortDirectionException ex,
        HttpServletRequest request
    ) {
        log.warn("Invalid sort direction: {} for path: {}", ex.getMessage(), request.getRequestURI());

        ErrorResponse errorResponse = errorResponseBuilder.buildFromException(ex, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPageSizeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPageSizeException(
        InvalidPageSizeException ex,
        HttpServletRequest request
    ) {
        log.warn("Invalid page size: {} for path: {}", ex.getMessage(), request.getRequestURI());

        ErrorResponse errorResponse = errorResponseBuilder.buildFromException(ex, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
        Exception ex,
        HttpServletRequest request
    ) {
        log.error("An unexpected error occurred: {} for path: {}", ex.getMessage(), request.getRequestURI());
        
        ErrorResponse errorResponse = errorResponseBuilder.buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal Server Error",
            "An unexpected error occurred",
            request.getRequestURI(),
            "INTERNAL_SERVER_ERROR",
            List.of("Please try again later", "If the problem persists, please contact support")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
