package com.deliverytech.delivery.dto.pedido.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(name = "PedidoRelatorioResponse", description = "Response com dados resumidos de pedido para relatórios")
public class PedidoRelatorioResponse {

  @Schema(description = "ID do pedido", example = "1")
  private Long pedidoId;
  
  @Schema(description = "Número do pedido", example = "PED-2025-001")
  private String numeroPedido;
  
  @Schema(description = "Status do pedido", example = "ENTREGUE")
  private String status;
  
  @Schema(description = "Nome do cliente", example = "João Silva")
  private String clienteNome;
  
  @Schema(description = "Nome do restaurante", example = "Pizzaria Dom Pedro")
  private String restauranteNome;
  
  @Schema(description = "Valor total do pedido", example = "89.90")
  private BigDecimal valorTotal;
  
  @Schema(description = "Data e hora do pedido", example = "2025-01-15T14:30:00")
  private LocalDateTime dataPedido;

  public PedidoRelatorioResponse() {
  }

  public PedidoRelatorioResponse(Long pedidoId, String numeroPedido, String status,
      String clienteNome, String restauranteNome, BigDecimal valorTotal,
      LocalDateTime dataPedido) {
    this.pedidoId = pedidoId;
    this.numeroPedido = numeroPedido;
    this.status = status;
    this.clienteNome = clienteNome;
    this.restauranteNome = restauranteNome;
    this.valorTotal = valorTotal;
    this.dataPedido = dataPedido;
  }

  // Getters e Setters
  public Long getPedidoId() {
    return pedidoId;
  }

  public void setPedidoId(Long pedidoId) {
    this.pedidoId = pedidoId;
  }

  public String getNumeroPedido() {
    return numeroPedido;
  }

  public void setNumeroPedido(String numeroPedido) {
    this.numeroPedido = numeroPedido;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getClienteNome() {
    return clienteNome;
  }

  public void setClienteNome(String clienteNome) {
    this.clienteNome = clienteNome;
  }

  public String getRestauranteNome() {
    return restauranteNome;
  }

  public void setRestauranteNome(String restauranteNome) {
    this.restauranteNome = restauranteNome;
  }

  public BigDecimal getValorTotal() {
    return valorTotal;
  }

  public void setValorTotal(BigDecimal valorTotal) {
    this.valorTotal = valorTotal;
  }

  public LocalDateTime getDataPedido() {
    return dataPedido;
  }

  public void setDataPedido(LocalDateTime dataPedido) {
    this.dataPedido = dataPedido;
  }

}
