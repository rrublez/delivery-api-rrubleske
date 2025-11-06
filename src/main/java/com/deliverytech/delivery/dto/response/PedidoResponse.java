package com.deliverytech.delivery.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {

  private Long id;

  private String numeroPedido;

  private String status;

  private ClienteResponse cliente;

  private RestauranteResponse restaurante;

  private BigDecimal valorTotal;

  private LocalDateTime dataPedido;

  private List<PedidoProdutoResponse> itens = new ArrayList<>();

}
