package com.deliverytech.delivery.dto.produto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para atualizar disponibilidade do produto
 * PATCH /api/produtos/{id}/disponibilidade
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AtualizarDisponibilidadeProdutoRequest", description = "Request para alterar disponibilidade de um produto")
public class AtualizarDisponibilidadeProdutoRequest {

  @NotNull(message = "Disponibilidade é obrigatória")
  @Schema(description = "Status de disponibilidade (true: disponível, false: indisponível)", example = "false")
  private Boolean disponivel;

}
