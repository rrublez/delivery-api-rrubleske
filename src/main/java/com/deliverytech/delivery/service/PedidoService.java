package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ClienteHistoricoResponseDTO;
import com.deliverytech.delivery.dto.PedidoRequestDTO;
import com.deliverytech.delivery.dto.PedidoResponseDTO;

/**
 * Service para operações relacionadas a Pedidos.
 * Inclui consultas de histórico de consumo e operações CRUD.
 */
public interface PedidoService {

    /**
     * Busca o histórico de consumo de um cliente pelo CPF.
     * Retorna todos os pedidos do cliente, ordenados do mais recente ao mais antigo,
     * junto com os 3 produtos mais pedidos nos últimos 10 pedidos.
     *
     * @param documento CPF/Documento de identificação do cliente
     * @return ClienteHistoricoResponseDTO com histórico completo
     * @throws IllegalArgumentException se o cliente não for encontrado
     */
    ClienteHistoricoResponseDTO getHistoricoByDocumento(String documento);

    /**
     * Busca o histórico de consumo de um cliente pelo número do pedido.
     * Retorna o cliente associado ao pedido com seu histórico completo.
     *
     * @param numeroPedido número do pedido no formato YYYYMM-xxxxx
     * @return ClienteHistoricoResponseDTO com histórico completo
     * @throws IllegalArgumentException se o pedido não for encontrado
     */
    ClienteHistoricoResponseDTO getHistoricoByNumeroPedido(String numeroPedido);

    /**
     * Cria um novo pedido.
     *
     * @param pedidoRequestDTO dados do pedido a criar
     * @return PedidoResponseDTO com os dados do pedido criado
     * @throws IllegalArgumentException se algum dado for inválido
     */
    PedidoResponseDTO create(PedidoRequestDTO pedidoRequestDTO);

    /**
     * Busca um pedido pelo número do pedido.
     *
     * @param numeroPedido número do pedido no formato YYYYMM-xxxxx
     * @return PedidoResponseDTO com os dados do pedido
     * @throws IllegalArgumentException se o pedido não for encontrado
     */
    PedidoResponseDTO getByNumeroPedido(String numeroPedido);

}
