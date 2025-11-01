package com.deliverytech.delivery.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 50)
    @NotBlank(message = "Nome do produto não pode ser vazio")
    private String nome;

    @Size(max = 150)
    @NotBlank(message = "Descrição do produto não pode ser vazia")
    private String descricao;

    // unidade venda: dúzia / kg / unidade, etc
    @NotBlank(message = "Unidade do produto não pode ser vazia")
    private String unidade;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaProduto categoria;

    @OneToMany(mappedBy = "produto")
    private List<ProdutoEstabelecimento> estabelecimentoPrecos;

}
