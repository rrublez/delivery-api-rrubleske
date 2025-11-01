package com.deliverytech.delivery.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoRequestDTO {

    @NotNull(message = "ID do produto/estabelecimento não pode ser vazio")
    private String produtoEstabelecimentoId;

    @NotNull(message = "Quantidade não pode ser vazia")
    @Positive(message = "Quantidade deve ser maior que zero")
    private Integer quantidade;

}
