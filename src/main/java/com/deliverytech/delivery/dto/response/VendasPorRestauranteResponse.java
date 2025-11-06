package com.deliverytech.delivery.dto.response;

import java.math.BigDecimal;

public class VendasPorRestauranteResponse {

  private Long restauranteId;
  private String restauranteNome;
  private Long totalPedidos;
  private BigDecimal totalVendas;
  private BigDecimal ticketMedio;

  public VendasPorRestauranteResponse() {
  }

  // Construtor que recebe Double do AVG() e converte para BigDecimal
  public VendasPorRestauranteResponse(Long restauranteId, String restauranteNome,
      Long totalPedidos, BigDecimal totalVendas, Double ticketMedio) {
    this.restauranteId = restauranteId;
    this.restauranteNome = restauranteNome;
    this.totalPedidos = totalPedidos;
    this.totalVendas = totalVendas;
    this.ticketMedio = ticketMedio != null ? BigDecimal.valueOf(ticketMedio) : BigDecimal.ZERO;
  }

  // Getters e Setters
  public Long getRestauranteId() {
    return restauranteId;
  }

  public void setRestauranteId(Long restauranteId) {
    this.restauranteId = restauranteId;
  }

  public String getRestauranteNome() {
    return restauranteNome;
  }

  public void setRestauranteNome(String restauranteNome) {
    this.restauranteNome = restauranteNome;
  }

  public Long getTotalPedidos() {
    return totalPedidos;
  }

  public void setTotalPedidos(Long totalPedidos) {
    this.totalPedidos = totalPedidos;
  }

  public BigDecimal getTotalVendas() {
    return totalVendas;
  }

  public void setTotalVendas(BigDecimal totalVendas) {
    this.totalVendas = totalVendas;
  }

  public BigDecimal getTicketMedio() {
    return ticketMedio;
  }

  public void setTicketMedio(BigDecimal ticketMedio) {
    this.ticketMedio = ticketMedio;
  }

}
