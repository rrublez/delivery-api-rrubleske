package com.deliverytech.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequestDTO {

    @Size(max = 50)
    @NotBlank(message = "Nome do produto não pode ser vazio")
    private String nome;

    @Size(max = 150)
    @NotBlank(message = "Descrição do produto não pode ser vazia")
    private String descricao;

    @Size(max = 20)
    @NotBlank(message = "Unidade do produto não pode ser vazia")
    private String unidade;

    @NotNull(message = "Categoria do produto não pode ser vazia")
    private String categoriaId;

}
