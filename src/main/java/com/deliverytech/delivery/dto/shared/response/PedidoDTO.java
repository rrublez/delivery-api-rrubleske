package com.deliverytech.delivery.dto.shared.response;

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
public class PedidoDTO {

  private Long id;

  private String numeroPedido;

  private String status;

  private ClienteDTO cliente;

  private RestauranteDTO restaurante;

  private BigDecimal valorTotal;

  private LocalDateTime dataPedido;

  private List<PedidoProdutoDTO> itens = new ArrayList<>();

}
