package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.dto.pedido.response.PedidoRelatorioResponse;
import com.deliverytech.delivery.dto.produto.response.ProdutoMaisVendidoResponse;
import com.deliverytech.delivery.dto.shared.response.RankingClienteResponse;
import com.deliverytech.delivery.dto.shared.response.VendasPorRestauranteResponse;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.service.RelatorioService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RelatorioServiceImpl implements RelatorioService {

  private final PedidoRepository pedidoRepository;
  private final ProdutoRepository produtoRepository;
  private final ClienteRepository clienteRepository;

  public RelatorioServiceImpl(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository,
      ClienteRepository clienteRepository) {
    this.pedidoRepository = pedidoRepository;
    this.produtoRepository = produtoRepository;
    this.clienteRepository = clienteRepository;
  }

  @Override
  public List<VendasPorRestauranteResponse> obterVendasPorRestaurante() {
    return pedidoRepository.obterVendasPorRestaurante();
  }

  @Override
  public List<ProdutoMaisVendidoResponse> obterProdutosMaisVendidos() {
    return produtoRepository.obterProdutosMaisVendidos();
  }

  @Override
  public List<RankingClienteResponse> obterClientesAtivos() {
    return clienteRepository.obterRankingClientesPorNumeroPedidos();
  }

  @Override
  public List<PedidoRelatorioResponse> obterPedidosPorPeriodo(LocalDateTime dataInicial,
      LocalDateTime dataFinal, String status) {
    if (status != null && !status.isBlank()) {
      return pedidoRepository.obterRelatorioByPeriodoAndStatus(dataInicial, dataFinal, status);
    } else {
      // Se status não foi fornecido, buscar todos os pedidos do período
      return pedidoRepository.findByDataPedidoBetween(dataInicial, dataFinal).stream()
          .map(this::convertToRelatorioResponse)
          .toList();
    }
  }

  /**
   * Converte um Pedido para PedidoRelatorioResponse
   */
  private PedidoRelatorioResponse convertToRelatorioResponse(
      com.deliverytech.delivery.entity.Pedido pedido) {
    return new PedidoRelatorioResponse(
        pedido.getId(),
        pedido.getNumeroPedido(),
        pedido.getStatus(),
        pedido.getCliente().getNome(),
        pedido.getRestaurante().getNome(),
        pedido.getValorTotal(),
        pedido.getDataPedido()
    );
  }

}
