package com.deliverytech.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {

    private String id;

    private String nome;

    private String email;

    private String telefone;

    private String documentoIdentificacao;

    private String observacoes;

    private EnderecoResponseDTO endereco;

}
