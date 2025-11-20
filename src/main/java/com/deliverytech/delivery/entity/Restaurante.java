package com.deliverytech.delivery.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "restaurantes")
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(length = 100)
    private String nome;

    @Column(length = 255)
    private String endereco;

    @Column(length = 15, unique = true)
    private String telefone;

    @Column(length = 14, unique = true)
    private String cnpj;

    @Column(length = 20)
    private String ramoAtividade;

    private Boolean ativo;

    private BigDecimal taxaEntrega;

    @ManyToMany
    @JoinTable(name = "restaurante_produto", joinColumns = @JoinColumn(name = "restaurante_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id"))
    private Set<Produto> produtos = new HashSet<>();

    @OneToMany(mappedBy = "restaurante")
    private List<Pedido> pedidos = new ArrayList<>();

}
