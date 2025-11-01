package com.deliverytech.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar um produto nos top 3 mais pedidos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoMaisPedidoDTO {

    /**
     * ID do produto
     */
    private String produtoId;

    /**
     * Nome do produto
     */
    private String nomeProduto;

    /**
     * Quantidade total de vezes que este produto foi pedido nos últimos 10 pedidos
     */
    private Integer totalVezesPedido;

    /**
     * Quantidade total de unidades pedidas
     */
    private Integer totalUnidadesPedidas;

}
