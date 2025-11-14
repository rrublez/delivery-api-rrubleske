package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.pedido.response.PedidoRelatorioResponse;
import com.deliverytech.delivery.dto.produto.response.ProdutoMaisVendidoResponse;
import com.deliverytech.delivery.dto.shared.response.RankingClienteResponse;
import com.deliverytech.delivery.dto.shared.response.VendasPorRestauranteResponse;
import java.time.LocalDateTime;
import java.util.List;

public interface RelatorioService {

  /**
   * Obtém vendas totais agrupadas por restaurante
   * @return Lista de VendasPorRestauranteResponse com ID, Nome, Total de Pedidos, Total de Vendas e Ticket Médio
   */
  List<VendasPorRestauranteResponse> obterVendasPorRestaurante();

  /**
   * Obtém top 10 produtos mais vendidos
   * @return Lista de ProdutoMaisVendidoResponse com ID, Nome, Categoria, Quantidade Vendida e Faturamento
   */
  List<ProdutoMaisVendidoResponse> obterProdutosMaisVendidos();

  /**
   * Obtém ranking de clientes mais ativos (por número de pedidos)
   * @return Lista de RankingClienteResponse com ID, Nome, Email e Total de Pedidos
   */
  List<RankingClienteResponse> obterClientesAtivos();

  /**
   * Obtém relatório de pedidos em um período específico, opcionalmente filtrado por status
   * @param dataInicial Data inicial do período
   * @param dataFinal Data final do período
   * @param status Status do pedido (opcional - pode ser nulo para trazer todos)
   * @return Lista de PedidoRelatorioResponse com detalhes dos pedidos
   */
  List<PedidoRelatorioResponse> obterPedidosPorPeriodo(LocalDateTime dataInicial,
      LocalDateTime dataFinal, String status);

}
