package com.deliverytech.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para representar um pedido no histórico de consumo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoHistoricoDTO {

    /**
     * ID do pedido
     */
    private String id;

    /**
     * Número do pedido no formato YYYYMM-xxxxx
     */
    private String numeroPedido;

    /**
     * Data e hora do pedido
     */
    private LocalDateTime dataPedido;

    /**
     * Status do pedido
     */
    private String status;

    /**
     * Valor total do pedido
     */
    private BigDecimal valorTotal;

    /**
     * Lista de itens do pedido
     */
    private List<ItemHistoricoDTO> itens;

}
