package com.deliverytech.delivery.dto.shared.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(name = "VendasPorRestauranteResponse", description = "Response com relatório de vendas por restaurante")
public class VendasPorRestauranteResponse {

  @Schema(description = "ID do restaurante", example = "1")
  private Long restauranteId;
  
  @Schema(description = "Nome do restaurante", example = "Pizzaria Dom Pedro")
  private String restauranteNome;
  
  @Schema(description = "Total de pedidos realizados", example = "45")
  private Long totalPedidos;
  
  @Schema(description = "Total de vendas em reais", example = "2250.00")
  private BigDecimal totalVendas;
  
  @Schema(description = "Ticket médio em reais", example = "50.00")
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
