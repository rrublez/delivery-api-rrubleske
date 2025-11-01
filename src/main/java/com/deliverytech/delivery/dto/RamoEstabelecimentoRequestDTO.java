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
public class RamoEstabelecimentoRequestDTO {

    @Size(max = 50)
    @NotBlank(message = "Nome do ramo não pode ser vazio")
    private String nome;

    @Size(max = 150)
    private String descricao;

}
