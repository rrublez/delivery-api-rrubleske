package com.deliverytech.delivery.dto;

import com.deliverytech.delivery.commons.TipoEndereco;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoResponseDTO {

    private String id;

    private String rua;

    private String numero;

    private String complemento;

    private String cidade;

    private String estado;

    private String cep;

    private String bairro;

    private String pontoReferencia;

    private TipoEndereco tipoEndereco;

}
