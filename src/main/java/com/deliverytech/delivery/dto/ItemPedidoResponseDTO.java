package com.deliverytech.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoResponseDTO {

    private String id;

    private ProdutoEstabelecimentoResponseDTO produtoEstabelecimento;

    private Integer quantidade;

    private BigDecimal valorUnitario;

    private BigDecimal valorTotal;

    /**
     * Indica se o item estava em promoção no momento da compra
     */
    private Boolean emPromocao;

}
