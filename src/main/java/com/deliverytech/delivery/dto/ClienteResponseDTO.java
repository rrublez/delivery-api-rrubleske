package com.deliverytech.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    /**
     * Lista de endereços do cliente.
     * Um cliente pode ter cadastrado até 3 endereços diferentes.
     */
    private List<EnderecoResponseDTO> enderecos;

}
