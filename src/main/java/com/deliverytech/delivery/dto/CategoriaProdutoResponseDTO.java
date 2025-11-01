package com.deliverytech.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaProdutoResponseDTO {

    private String id;

    private String categoria;

    private String descricao;

    private Integer totalProdutos;

}
