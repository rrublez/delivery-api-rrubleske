package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de persistência de Pedido.
 * Estende JpaRepository para operações CRUD padrão.
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, String> {

    /**
     * Busca um pedido por número do pedido.
     *
     * @param numeroPedido número do pedido
     * @return Optional contendo o pedido se encontrado
     */
    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    /**
     * Busca todos os pedidos de um cliente pelo CPF/documento.
     *
     * @param documentoIdentificacao CPF/documento do cliente
     * @return Lista de pedidos ordenada por data descendente
     */
    @Query("SELECT p FROM Pedido p WHERE p.cliente.documentoIdentificacao = :documento ORDER BY p.dataPedido DESC")
    List<Pedido> findByClienteDocumento(@Param("documento") String documentoIdentificacao);

    /**
     * Busca todos os pedidos de um cliente pelo ID.
     *
     * @param clienteId ID do cliente
     * @return Lista de pedidos ordenada por data descendente
     */
    List<Pedido> findByClienteIdOrderByDataPedidoDesc(String clienteId);

}
