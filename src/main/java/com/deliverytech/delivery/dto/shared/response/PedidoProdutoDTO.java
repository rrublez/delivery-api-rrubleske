package com.deliverytech.delivery.dto.shared.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoProdutoDTO {

  private Long id;

  private ProdutoDTO produto;

  private Integer quantidade;

  private BigDecimal precoUnitario;

  private BigDecimal subtotal;

  private String observacoes;

}
