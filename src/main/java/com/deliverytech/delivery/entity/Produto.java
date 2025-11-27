package com.deliverytech.delivery.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "produtos")
public class Produto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(length = 100)
  private String nome;

  @Column(length = 255) 
  private String descricao;

  private BigDecimal preco;

  private Boolean disponivel;
  
  @Column(nullable = false)
  private Integer estoque = 0;

  @Column(length = 20)
  private String categoria;

  // Um mesmo produto pode estar cadastrado em vários restaurantes
  @ManyToMany(mappedBy = "produtos")
  private Set<Restaurante> restaurantes = new HashSet<>();

}
