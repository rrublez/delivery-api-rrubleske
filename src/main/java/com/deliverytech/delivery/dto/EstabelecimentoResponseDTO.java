package com.deliverytech.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstabelecimentoResponseDTO {

    private String id;

    private String nome;

    private String cnpj;

    private String telefone;

    private String email;

    private RamoEstabelecimentoResponseDTO ramo;

}
