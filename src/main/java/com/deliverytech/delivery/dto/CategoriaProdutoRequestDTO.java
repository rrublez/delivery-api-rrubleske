package com.deliverytech.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaProdutoRequestDTO {

    @Size(max = 50)
    @NotBlank(message = "Categoria não pode ser vazia")
    private String categoria;

    @Size(max = 150)
    private String descricao;

}
