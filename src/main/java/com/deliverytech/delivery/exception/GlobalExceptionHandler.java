package com.deliverytech.delivery.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles validation errors from @Valid annotation
   * Returns HTTP 400 with detailed field validation errors
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, WebRequest request) {

    List<ApiErrorResponse.FieldError> fieldErrors = new ArrayList<>();

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error ->
                fieldErrors.add(
                    ApiErrorResponse.FieldError.builder()
                        .field(error.getField())
                        .rejectedValue(error.getRejectedValue())
                        .message(error.getDefaultMessage())
                        .build()));

    ApiErrorResponse response =
        ApiErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Erro de Validação")
            .message("Um ou mais campos contêm valores inválidos")
            .path(request.getDescription(false).replace("uri=", ""))
            .errors(fieldErrors)
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  /**
   * Handles generic exceptions
   * Returns HTTP 500 with error details
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleGlobalException(
      Exception ex, WebRequest request) {

    ApiErrorResponse response =
        ApiErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error("Erro Interno do Servidor")
            .message(ex.getMessage() != null ? ex.getMessage() : "Erro inesperado ocorreu")
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
