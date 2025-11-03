package com.deliverytech.delivery.exception;

import com.deliverytech.delivery.dto.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manipulador global de exceções para a API.
 * 
 * Define como diferentes tipos de erros são tratados e retornados:
 * - Erros de validação (4xx): Retornam status 400 SEM campo 'trace'
 * - Erros de servidor (5xx): Retornam status 500 COM campo 'trace'
 * - Erros customizados: Mapeados para status HTTP apropriados
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Manipula exceções de validação (MethodArgumentNotValidException).
     * Retorna 400 Bad Request com detalhes dos campos inválidos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        log.warn("Erro de validação na requisição: {}", request.getDescription(false));

        // Mapear erros de validação por campo
        Map<String, List<String>> validationErrors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        error -> ((FieldError) error).getField(),
                        Collectors.mapping(
                                error -> error.getDefaultMessage(),
                                Collectors.toList()
                        )
                ));

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Erro de validação nos dados fornecidos")
                .path(request.getDescription(false).replace("uri=", ""))
                .validationErrors(validationErrors)
                .trace(null) // Não incluir trace para erros 4xx
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Manipula ResourceNotFoundException (não encontrado).
     * Retorna 404 Not Found.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request) {

        log.warn("Recurso não encontrado: {}", ex.getMessage());

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .trace(null) // Não incluir trace para 4xx
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Manipula IllegalArgumentException (argumento inválido).
     * Retorna 400 Bad Request.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request) {

        log.warn("Argumento inválido: {}", ex.getMessage());

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .trace(null) // Não incluir trace para 4xx
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Manipula BusinessException (exceção de negócio).
     * Retorna 409 Conflict ou status definido na exceção.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(
            BusinessException ex,
            WebRequest request) {

        log.warn("Erro de negócio: {}", ex.getMessage());

        HttpStatus status = ex.getHttpStatus() != null ? 
                ex.getHttpStatus() : HttpStatus.CONFLICT;

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .trace(null) // Não incluir trace para 4xx
                .build();

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Manipula exceções genéricas não tratadas (erro 500).
     * Retorna 500 Internal Server Error COM stack trace.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(
            Exception ex,
            WebRequest request) {

        log.error("Erro não tratado na aplicação", ex);

        String trace = getStackTrace(ex);

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Erro interno do servidor")
                .path(request.getDescription(false).replace("uri=", ""))
                .trace(trace) // Incluir trace apenas para erros 5xx
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Converte stack trace da exceção para string.
     */
    private String getStackTrace(Exception ex) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.getClass().getName()).append(": ").append(ex.getMessage()).append("\n");
        
        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append("\tat ").append(element).append("\n");
        }
        
        if (ex.getCause() != null) {
            sb.append("Caused by: ").append(getStackTrace((Exception) ex.getCause()));
        }
        
        return sb.toString();
    }
}
