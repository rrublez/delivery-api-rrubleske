package com.deliverytech.delivery.dto.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalcularPedidoResponse {

  private List<PedidoProdutoResponse> itens = new ArrayList<>();

  private BigDecimal subtotal;

  private BigDecimal taxaEntrega;

  private BigDecimal valorTotal;

}
