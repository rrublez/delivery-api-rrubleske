package com.deliverytech.delivery.dto.shared.response;

import com.deliverytech.delivery.dto.produto.response.ProdutoResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PedidoProdutoResponse", description = "Response com item do pedido (produto)")
public class PedidoProdutoResponse {

  @Schema(description = "ID do item do pedido", example = "1")
  private Long id;

  @Schema(description = "Detalhes do produto")
  private ProdutoResponse produto;

  @Schema(description = "Quantidade de unidades", example = "2")
  private Integer quantidade;

  @Schema(description = "Preço unitário em reais", example = "45.90")
  private BigDecimal precoUnitario;

  @Schema(description = "Subtotal do item (quantidade × preço unitário)", example = "91.80")
  private BigDecimal subtotal;

  @Schema(description = "Observações sobre o item", example = "Sem cebola, com extra de queijo")
  private String observacoes;

}
