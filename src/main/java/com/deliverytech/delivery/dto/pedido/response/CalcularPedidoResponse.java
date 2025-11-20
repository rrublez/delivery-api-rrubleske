package com.deliverytech.delivery.dto.pedido.response;

import com.deliverytech.delivery.dto.shared.response.PedidoProdutoResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CalcularPedidoResponse", description = "Response com o cálculo de valores do pedido")
public class CalcularPedidoResponse {

  @Schema(description = "Lista de produtos do pedido")
  private List<PedidoProdutoResponse> itens = new ArrayList<>();

  @Schema(description = "Subtotal dos produtos em reais", example = "85.00")
  private BigDecimal subtotal;

  @Schema(description = "Taxa de entrega em reais", example = "7.90")
  private BigDecimal taxaEntrega;

  @Schema(description = "Valor total do pedido (subtotal + taxa)", example = "92.90")
  private BigDecimal valorTotal;

  // Compat: alguns testes esperam campo "total" em vez de "valorTotal"
  @JsonProperty("total")
  public BigDecimal getTotal() {
    return valorTotal;
  }

}
