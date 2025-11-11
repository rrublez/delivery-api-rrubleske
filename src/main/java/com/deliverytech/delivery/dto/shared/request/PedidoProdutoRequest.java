package com.deliverytech.delivery.dto.shared.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoProdutoRequest {

  @NotNull(message = "ID do produto é obrigatório")
  private Long produtoId;

  @NotNull(message = "Quantidade é obrigatória")
  @Min(value = 1, message = "Quantidade deve ser maior que 0")
  private Integer quantidade;

  @NotNull(message = "Preço unitário é obrigatório")
  @DecimalMin(value = "0.01", message = "Preço unitário deve ser maior que 0")
  private BigDecimal precoUnitario;

  @Size(max = 255, message = "Observações não podem exceder 255 caracteres")
  private String observacoes;

}
