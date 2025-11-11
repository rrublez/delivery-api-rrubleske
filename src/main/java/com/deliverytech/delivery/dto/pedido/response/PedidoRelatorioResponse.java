package com.deliverytech.delivery.dto.pedido.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoRelatorioResponse {

  private Long pedidoId;
  private String numeroPedido;
  private String status;
  private String clienteNome;
  private String restauranteNome;
  private BigDecimal valorTotal;
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
