package com.deliverytech.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RamoEstabelecimentoResponseDTO {

    private String id;

    private String nome;

    private String descricao;

    private Integer totalEstabelecimentos;

}
