package com.deliverytech.delivery.dto.produto.request;

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
public class AtualizarDisponibilidadeProdutoRequest {

  @NotNull(message = "Disponibilidade é obrigatória")
  private Boolean disponivel;

}
