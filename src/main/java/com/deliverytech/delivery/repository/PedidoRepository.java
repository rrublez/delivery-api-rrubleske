package com.deliverytech.delivery.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.deliverytech.delivery.dto.response.PedidoRelatorioResponse;
import com.deliverytech.delivery.dto.response.VendasPorRestauranteResponse;
import com.deliverytech.delivery.entity.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
  List<Pedido> findByClienteId(Long clienteId);

  List<Pedido> findByStatus(String status);

  List<Pedido> findByRestauranteId(Long restauranteId);

  List<Pedido> findByStatusAndDataPedidoBetween(String status, LocalDateTime dataInicial, LocalDateTime dataFinal);

  List<Pedido> findTop10ByOrderByValorTotalDesc();

  List<Pedido> findByDataPedidoBetween(LocalDateTime dataInicial, LocalDateTime dataFinal);

  @Query("SELECT p FROM Pedido p JOIN p.cliente c WHERE p.restaurante.id = :restauranteId ORDER BY p.valorTotal DESC, c.nome ASC")
  List<Pedido> findTop5MaioresPedidosPorRestaurante(@Param("restauranteId") Long restauranteId);

  // Query: Total de vendas por restaurante
  @Query("SELECT NEW com.deliverytech.delivery.dto.response.VendasPorRestauranteResponse("
      + "r.id, r.nome, COUNT(p.id), COALESCE(SUM(p.valorTotal), 0), AVG(p.valorTotal)) "
      + "FROM Pedido p JOIN p.restaurante r "
      + "GROUP BY r.id, r.nome "
      + "ORDER BY SUM(p.valorTotal) DESC")
  List<VendasPorRestauranteResponse> obterVendasPorRestaurante();

  // Query: Pedidos com valor acima de X
  @Query("SELECT p FROM Pedido p WHERE p.valorTotal > :valor ORDER BY p.valorTotal DESC")
  List<Pedido> findPedidosComValorAcimaDe(@Param("valor") BigDecimal valor);

  // Query: Relatório por período e status
  @Query("SELECT NEW com.deliverytech.delivery.dto.response.PedidoRelatorioResponse("
      + "p.id, p.numeroPedido, p.status, c.nome, r.nome, p.valorTotal, p.dataPedido) "
      + "FROM Pedido p "
      + "JOIN p.cliente c "
      + "JOIN p.restaurante r "
      + "WHERE p.dataPedido BETWEEN :dataInicial AND :dataFinal "
      + "AND p.status = :status "
      + "ORDER BY p.dataPedido DESC")
  List<PedidoRelatorioResponse> obterRelatorioByPeriodoAndStatus(
      @Param("dataInicial") LocalDateTime dataInicial,
      @Param("dataFinal") LocalDateTime dataFinal,
      @Param("status") String status);

}