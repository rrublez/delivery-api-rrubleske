package com.deliverytech.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para representar um item no histórico de consumo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemHistoricoDTO {

    /**
     * ID do item do pedido
     */
    private String id;

    /**
     * Nome do produto
     */
    private String nomeProduto;

    /**
     * Descrição do produto
     */
    private String descricaoProduto;

    /**
     * Quantidade solicitada
     */
    private Integer quantidade;

    /**
     * Valor unitário do produto no momento do pedido
     */
    private BigDecimal valorUnitario;

    /**
     * Valor total do item (quantidade x valorUnitario)
     */
    private BigDecimal valorTotal;

    /**
     * Indica se o item estava em promoção no momento do consumo
     */
    private Boolean emPromocao;

}
