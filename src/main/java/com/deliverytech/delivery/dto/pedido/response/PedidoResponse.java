package com.deliverytech.delivery.dto.pedido.response;

import com.deliverytech.delivery.dto.cliente.response.ClienteResponse;
import com.deliverytech.delivery.dto.restaurante.response.RestauranteResponse;
import com.deliverytech.delivery.dto.shared.response.PedidoProdutoResponse;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "PedidoResponse", description = "Response com os dados completos do pedido")
public class PedidoResponse {

  @Schema(description = "Identificador único do pedido", example = "1")
  private Long id;

  @Schema(description = "Número único do pedido", example = "PED-2025-001")
  private String numeroPedido;

  @Schema(description = "Status do pedido", example = "PENDENTE")
  private String status;

  @Schema(description = "Dados do cliente que realizou o pedido")
  private ClienteResponse cliente;

  @Schema(description = "Dados do restaurante que preparou o pedido")
  private RestauranteResponse restaurante;

  @Schema(description = "Valor total do pedido em reais", example = "89.90")
  private BigDecimal valorTotal;

  @Schema(description = "Data e hora de criação do pedido", example = "2025-01-15T14:30:00")
  private LocalDateTime dataPedido;

  @Schema(description = "Lista de produtos do pedido")
  private List<PedidoProdutoResponse> itens = new ArrayList<>();

}
