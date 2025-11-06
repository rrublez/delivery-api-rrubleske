package com.deliverytech.delivery.dto.response;

public class RankingClienteResponse {

  private Long clienteId;
  private String clienteNome;
  private String email;
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
