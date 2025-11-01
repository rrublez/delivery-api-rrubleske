package com.deliverytech.delivery.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categoria_estabelecimentos")
public class CategoriaEstabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 25)
    @NotBlank(message = "Categoria não pode ser vazia")
    private String categoria;

    @Size(max = 100)
    private String descricao;

    @OneToMany(mappedBy = "categoria")
    private List<Estabelecimento> estabelecimentos;

}
