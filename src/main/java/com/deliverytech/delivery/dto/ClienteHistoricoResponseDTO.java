package com.deliverytech.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para representar o histórico de consumo completo de um cliente.
 * Inclui os 3 itens mais pedidos nos últimos 10 pedidos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteHistoricoResponseDTO {

    /**
     * ID do cliente
     */
    private String clienteId;

    /**
     * Nome do cliente
     */
    private String nomeCliente;

    /**
     * CPF/Documento de identificação do cliente
     */
    private String documentoIdentificacao;

    /**
     * Email do cliente
     */
    private String email;

    /**
     * Telefone do cliente
     */
    private String telefone;

    /**
     * Array com os 3 produtos mais pedidos nos últimos 10 pedidos
     */
    private List<ProdutoMaisPedidoDTO> topProdutos;

    /**
     * Histórico de pedidos do cliente, do mais recente ao mais antigo
     */
    private List<PedidoHistoricoDTO> historicoPedidos;

}
