package com.deliverytech.delivery.dto.shared.response;

import com.deliverytech.delivery.dto.produto.response.ProdutoResponse;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoProdutoResponse {

  private Long id;

  private ProdutoResponse produto;

  private Integer quantidade;

  private BigDecimal precoUnitario;

  private BigDecimal subtotal;

  private String observacoes;

}
