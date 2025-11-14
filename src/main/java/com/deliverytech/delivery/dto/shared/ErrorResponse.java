package com.deliverytech.delivery.dto.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Resposta padronizada para erros gerais da API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Resposta padronizada para erros da API")
public class ErrorResponse {

  @Schema(description = "Timestamp da resposta", example = "2025-11-13T10:30:00")
  private LocalDateTime timestamp;

  @Schema(description = "Código HTTP da resposta", example = "404")
  private int statusCode;

  @Schema(description = "Tipo de erro", example = "Not Found")
  private String errorType;

  @Schema(description = "Mensagem de erro", example = "Recurso não encontrado")
  private String message;

  @Schema(description = "Caminho da requisição que gerou o erro")
  private String path;

  @Schema(description = "Detalhes adicionais do erro (quando disponível)")
  private String details;

  @Schema(description = "Sucesso da operação", example = "false")
  private boolean success;

  public static ErrorResponse notFound(String message, String path) {
    return ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .statusCode(404)
        .errorType("Not Found")
        .message(message)
        .path(path)
        .success(false)
        .build();
  }

  public static ErrorResponse badRequest(String message, String path) {
    return ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .statusCode(400)
        .errorType("Bad Request")
        .message(message)
        .path(path)
        .success(false)
        .build();
  }

  public static ErrorResponse conflict(String message, String path) {
    return ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .statusCode(409)
        .errorType("Conflict")
        .message(message)
        .path(path)
        .success(false)
        .build();
  }

  public static ErrorResponse internalServerError(String message, String path) {
    return ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .statusCode(500)
        .errorType("Internal Server Error")
        .message(message)
        .path(path)
        .success(false)
        .build();
  }
}
