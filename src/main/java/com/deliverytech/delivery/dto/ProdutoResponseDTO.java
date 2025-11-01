package com.deliverytech.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponseDTO {

    private String id;

    private String nome;

    private String descricao;

    private String unidade;

    private CategoriaProdutoResponseDTO categoria;

    private Integer totalEstabelecimentos;

}
