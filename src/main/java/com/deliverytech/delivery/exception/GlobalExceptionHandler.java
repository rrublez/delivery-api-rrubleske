package com.deliverytech.delivery.exception;

import com.deliverytech.delivery.dto.shared.ErrorResponse;
import com.deliverytech.delivery.dto.shared.ValidationErrorResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

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

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ErrorResponse> handleResponseStatusException(
      ResponseStatusException ex,
      WebRequest request) {
    HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
    if (status == null) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    ErrorResponse response = buildErrorResponse(
        status,
        Optional.ofNullable(ex.getReason()).orElse(status.getReasonPhrase()),
        extractPath(request));
    return ResponseEntity.status(status).body(response);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      WebRequest request) {
    ErrorResponse response = buildErrorResponse(
        HttpStatus.METHOD_NOT_ALLOWED,
        "Método não suportado para esta rota",
        extractPath(request));
    var supportedMethods = ex.getSupportedHttpMethods();
    if (supportedMethods != null && !supportedMethods.isEmpty()) {
      response.setDetails("Métodos suportados: " + supportedMethods);
    }
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
      DataIntegrityViolationException ex,
      WebRequest request) {
    ErrorResponse response = buildErrorResponse(
        HttpStatus.CONFLICT,
        "Violação de integridade dos dados",
        extractPath(request));
    response.setDetails(Optional.ofNullable(ex.getMostSpecificCause())
        .map(Throwable::getMessage)
        .orElse(ex.getMessage()));
    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  /**
   * Handler genérico para todas as outras exceções (500 Internal Server Error)
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(
      Exception ex,
      WebRequest request) {

    ErrorResponse response = buildErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ex.getMessage() != null ? ex.getMessage() : "Erro interno do servidor",
        extractPath(request));
    response.setDetails(ex.getClass().getSimpleName());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  private ErrorResponse buildErrorResponse(HttpStatus status, String message, String path) {
    return ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .statusCode(status.value())
        .errorType(status.getReasonPhrase())
        .message(message)
        .path(path)
        .success(false)
        .build();
  }

  private String extractPath(WebRequest request) {
    return request.getDescription(false).replace("uri=", "");
  }
}
