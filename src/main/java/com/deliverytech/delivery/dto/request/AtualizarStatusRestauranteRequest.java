package com.deliverytech.delivery.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para atualizar o status (ativo/inativo) de um restaurante
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarStatusRestauranteRequest {

  @NotNull(message = "Status ativo/inativo é obrigatório")
  private Boolean ativo;

}
