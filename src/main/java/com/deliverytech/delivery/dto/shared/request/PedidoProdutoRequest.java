package com.deliverytech.delivery.dto.shared.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "PedidoProdutoRequest", description = "Request com item do pedido (produto)")
public class PedidoProdutoRequest {

  @NotNull(message = "ID do produto é obrigatório")
  @Schema(description = "ID do produto a adicionar ao pedido", example = "1")
  private Long produtoId;

  @NotNull(message = "Quantidade é obrigatória")
  @Min(value = 1, message = "Quantidade deve ser maior que 0")
  @Schema(description = "Quantidade de unidades do produto", example = "2")
  private Integer quantidade;

  @NotNull(message = "Preço unitário é obrigatório")
  @DecimalMin(value = "0.01", message = "Preço unitário deve ser maior que 0")
  @Schema(description = "Preço unitário do produto em reais", example = "45.90")
  private BigDecimal precoUnitario;

  @Size(max = 255, message = "Observações não podem exceder 255 caracteres")
  @Schema(description = "Observações/notas sobre o item do pedido (ex: sem cebola)", example = "Sem cebola, com extra de queijo", maxLength = 255)
  private String observacoes;

}
