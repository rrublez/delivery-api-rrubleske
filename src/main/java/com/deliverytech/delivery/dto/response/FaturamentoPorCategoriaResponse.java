package com.deliverytech.delivery.dto.response;

import java.math.BigDecimal;

public class FaturamentoPorCategoriaResponse {

  private String categoria;
  private Long totalProdutos;
  private Long quantidadeVendida;
  private BigDecimal totalFaturamento;

  public FaturamentoPorCategoriaResponse() {
  }

  public FaturamentoPorCategoriaResponse(String categoria, Long totalProdutos,
      Long quantidadeVendida, BigDecimal totalFaturamento) {
    this.categoria = categoria;
    this.totalProdutos = totalProdutos;
    this.quantidadeVendida = quantidadeVendida;
    this.totalFaturamento = totalFaturamento;
  }

  // Getters e Setters
  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public Long getTotalProdutos() {
    return totalProdutos;
  }

  public void setTotalProdutos(Long totalProdutos) {
    this.totalProdutos = totalProdutos;
  }

  public Long getQuantidadeVendida() {
    return quantidadeVendida;
  }

  public void setQuantidadeVendida(Long quantidadeVendida) {
    this.quantidadeVendida = quantidadeVendida;
  }

  public BigDecimal getTotalFaturamento() {
    return totalFaturamento;
  }

  public void setTotalFaturamento(BigDecimal totalFaturamento) {
    this.totalFaturamento = totalFaturamento;
  }

}
