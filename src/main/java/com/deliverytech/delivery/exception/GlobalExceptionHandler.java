package com.deliverytech.delivery.exception;

import com.deliverytech.delivery.dto.shared.ErrorResponse;
import com.deliverytech.delivery.dto.shared.ValidationErrorResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Handler global de exceções da API
 * Padroniza as respostas de erro com codes HTTP corretos
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handler para erros de validação (400 Bad Request)
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
      MethodArgumentNotValidException ex,
      WebRequest request) {

    List<ValidationErrorResponse.FieldError> fieldErrors = new ArrayList<>();

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error ->
                fieldErrors.add(
                    ValidationErrorResponse.FieldError.builder()
                        .field(error.getField())
                        .rejectedValue(error.getRejectedValue())
                        .message(error.getDefaultMessage())
                        .build()));

    ValidationErrorResponse response =
        ValidationErrorResponse.builder()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .errorType("Validation Error")
            .message("Um ou mais campos contêm valores inválidos")
            .path(request.getDescription(false).replace("uri=", ""))
            .errors(fieldErrors)
            .success(false)
            .build();

    return ResponseEntity.badRequest().body(response);
  }

  /**
   * Handler para exceções de recurso não encontrado (404 Not Found)
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException ex,
      WebRequest request) {

    ErrorResponse response = ErrorResponse.notFound(
        ex.getMessage(),
        request.getDescription(false).replace("uri=", "")
    );

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  /**
   * Handler para exceções de conflito (409 Conflict)
   */
  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorResponse> handleConflictException(
      ConflictException ex,
      WebRequest request) {

    ErrorResponse response = ErrorResponse.conflict(
        ex.getMessage(),
        request.getDescription(false).replace("uri=", "")
    );

    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  /**
   * Handler para exceções de requisição inválida (400 Bad Request)
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
      IllegalArgumentException ex,
      WebRequest request) {

    ErrorResponse response = ErrorResponse.badRequest(
        ex.getMessage(),
        request.getDescription(false).replace("uri=", "")
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * Handler genérico para todas as outras exceções (500 Internal Server Error)
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(
      Exception ex,
      WebRequest request) {

    ErrorResponse response = ErrorResponse.internalServerError(
        ex.getMessage() != null ? ex.getMessage() : "Erro interno do servidor",
        request.getDescription(false).replace("uri=", "")
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
