package com.deliverytech.delivery.dto.shared.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RankingClienteResponse", description = "Response com ranking de clientes mais ativos")
public class RankingClienteResponse {

  @Schema(description = "ID do cliente", example = "1")
  private Long clienteId;
  
  @Schema(description = "Nome do cliente", example = "João Silva")
  private String clienteNome;
  
  @Schema(description = "Email do cliente", example = "joao@example.com")
  private String email;
  
  @Schema(description = "Total de pedidos realizados", example = "15")
  private Long totalPedidos;

  public RankingClienteResponse() {
  }

  public RankingClienteResponse(Long clienteId, String clienteNome, String email,
      Long totalPedidos) {
    this.clienteId = clienteId;
    this.clienteNome = clienteNome;
    this.email = email;
    this.totalPedidos = totalPedidos;
  }

  // Getters e Setters
  public Long getClienteId() {
    return clienteId;
  }

  public void setClienteId(Long clienteId) {
    this.clienteId = clienteId;
  }

  public String getClienteNome() {
    return clienteNome;
  }

  public void setClienteNome(String clienteNome) {
    this.clienteNome = clienteNome;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Long getTotalPedidos() {
    return totalPedidos;
  }

  public void setTotalPedidos(Long totalPedidos) {
    this.totalPedidos = totalPedidos;
  }

}
