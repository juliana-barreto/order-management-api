package com.juliana_barreto.ecommerce.shared;

import com.juliana_barreto.ecommerce.shared.exceptions.BusinessException;
import com.juliana_barreto.ecommerce.shared.exceptions.DatabaseException;
import com.juliana_barreto.ecommerce.shared.exceptions.InvalidDataException;
import com.juliana_barreto.ecommerce.shared.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // Handling for ERROR 404
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> resourceNotFound(
      ResourceNotFoundException ex, HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.NOT_FOUND.value(),
        "Resource not found",
        ex.getMessage(),
        request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  // Handling for ERROR 400
  @ExceptionHandler(InvalidDataException.class)
  public ResponseEntity<ErrorResponse> invalidData(
      InvalidDataException ex, HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.value(),
        "Invalid data",
        ex.getMessage(),
        request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  // Handling for ERROR 400/409
  @ExceptionHandler(DatabaseException.class)
  public ResponseEntity<ErrorResponse> databaseError(
      DatabaseException ex, HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.value(), // ou HttpStatus.CONFLICT
        "Database error",
        ex.getMessage(),
        request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  // Handling for ERROR 422
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(
      MethodArgumentNotValidException ex, HttpServletRequest request) {

    // Turn error messages into a single string
    String errorMessage = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining("; "));

    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.UNPROCESSABLE_ENTITY.value(),
        "Validation Error",
        errorMessage,
        request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
  }

  // Handling for Business Rules (HTTP 422)
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> businessRule(BusinessException ex, HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.UNPROCESSABLE_ENTITY.value(), // 422
        "Business rule violation",
        ex.getMessage(),
        request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
  }

  // Handling for ERROR 500
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> exception(
      Exception ex, HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Internal server error",
        ex.getMessage(),
        request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
