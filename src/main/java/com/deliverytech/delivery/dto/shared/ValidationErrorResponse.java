package com.deliverytech.delivery.dto.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Resposta padronizada para erros de validação
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Resposta padronizada para erros de validação")
public class ValidationErrorResponse {

  @Schema(description = "Timestamp da resposta", example = "2025-11-13T10:30:00")
  private LocalDateTime timestamp;

  @Schema(description = "Código HTTP da resposta", example = "400")
  private int statusCode;

  @Schema(description = "Tipo de erro", example = "Validation Error")
  private String errorType;

  @Schema(description = "Mensagem geral do erro", example = "Um ou mais campos contêm valores inválidos")
  private String message;

  @Schema(description = "Caminho da requisição que gerou o erro")
  private String path;

  @Schema(description = "Lista de erros por campo")
  private List<FieldError> errors;

  @Schema(description = "Sucesso da operação", example = "false")
  private boolean success;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class FieldError {
    @Schema(description = "Nome do campo que contém erro", example = "email")
    private String field;

    @Schema(description = "Valor rejeitado", example = "invalid-email")
    private Object rejectedValue;

    @Schema(description = "Mensagem de erro específica", example = "Email deve ser válido")
    private String message;
  }
}
