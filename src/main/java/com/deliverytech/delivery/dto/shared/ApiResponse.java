package com.deliverytech.delivery.dto.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper genérico para respostas de sucesso da API
 * @param <T> Tipo do objeto de dados retornado
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Resposta padronizada de sucesso da API")
public class ApiResponse<T> {

  @Schema(description = "Timestamp da resposta", example = "2025-11-13T10:30:00")
  private LocalDateTime timestamp;

  @Schema(description = "Código HTTP da resposta", example = "200")
  private int statusCode;

  @Schema(description = "Mensagem de sucesso", example = "Operação realizada com sucesso")
  private String message;

  @Schema(description = "Dados retornados pela API")
  private T data;

  @Schema(description = "Sucesso da operação", example = "true")
  private boolean success;

  public static <T> ApiResponse<T> ok(T data, String message) {
    return ApiResponse.<T>builder()
        .timestamp(LocalDateTime.now())
        .statusCode(200)
        .message(message)
        .data(data)
        .success(true)
        .build();
  }

  public static <T> ApiResponse<T> created(T data, String message) {
    return ApiResponse.<T>builder()
        .timestamp(LocalDateTime.now())
        .statusCode(201)
        .message(message)
        .data(data)
        .success(true)
        .build();
  }

  public static <T> ApiResponse<T> noContent() {
    return ApiResponse.<T>builder()
        .timestamp(LocalDateTime.now())
        .statusCode(204)
        .message("Operação realizada com sucesso")
        .success(true)
        .build();
  }
}
