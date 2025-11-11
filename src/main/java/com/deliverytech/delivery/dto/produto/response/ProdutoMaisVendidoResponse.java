package com.deliverytech.delivery.dto.produto.response;

import java.math.BigDecimal;

public class ProdutoMaisVendidoResponse {

  private Long produtoId;
  private String produtoNome;
  private String categoria;
  private Long quantidadeVendida;
  private BigDecimal faturamento;

  public ProdutoMaisVendidoResponse() {
  }

  public ProdutoMaisVendidoResponse(Long produtoId, String produtoNome, String categoria,
      Long quantidadeVendida, BigDecimal faturamento) {
    this.produtoId = produtoId;
    this.produtoNome = produtoNome;
    this.categoria = categoria;
    this.quantidadeVendida = quantidadeVendida;
    this.faturamento = faturamento;
  }

  // Getters e Setters
  public Long getProdutoId() {
    return produtoId;
  }

  public void setProdutoId(Long produtoId) {
    this.produtoId = produtoId;
  }

  public String getProdutoNome() {
    return produtoNome;
  }

  public void setProdutoNome(String produtoNome) {
    this.produtoNome = produtoNome;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public Long getQuantidadeVendida() {
    return quantidadeVendida;
  }

  public void setQuantidadeVendida(Long quantidadeVendida) {
    this.quantidadeVendida = quantidadeVendida;
  }

  public BigDecimal getFaturamento() {
    return faturamento;
  }

  public void setFaturamento(BigDecimal faturamento) {
    this.faturamento = faturamento;
  }

}
