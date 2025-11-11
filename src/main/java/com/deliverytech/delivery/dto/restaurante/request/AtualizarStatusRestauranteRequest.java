package com.deliverytech.delivery.dto.restaurante.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "AtualizarStatusRestauranteRequest", description = "Request para alterar status de um restaurante")
public class AtualizarStatusRestauranteRequest {

  @NotNull(message = "Status ativo/inativo é obrigatório")
  @Schema(description = "Status do restaurante (true: ativo, false: inativo)", example = "true")
  private Boolean ativo;

}
