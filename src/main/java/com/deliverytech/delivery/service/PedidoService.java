package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.request.AtualizarStatusPedidoRequest;
import com.deliverytech.delivery.dto.request.CalcularPedidoRequest;
import com.deliverytech.delivery.dto.request.PedidoRequest;
import com.deliverytech.delivery.dto.response.CalcularPedidoResponse;
import com.deliverytech.delivery.dto.response.PedidoRelatorioResponse;
import com.deliverytech.delivery.dto.response.PedidoResponse;
import com.deliverytech.delivery.dto.response.VendasPorRestauranteResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PedidoService {

  PedidoResponse criarPedido(PedidoRequest request);

  PedidoResponse obterPorId(Long id);

  List<PedidoResponse> listarComFiltros(String status, LocalDateTime dataInicial, LocalDateTime dataFinal);

  PedidoResponse atualizarStatus(Long id, AtualizarStatusPedidoRequest request);

  void cancelarPedido(Long id);

  List<PedidoResponse> pedidosPorCliente(Long clienteId);

  List<PedidoResponse> pedidosPorRestaurante(Long restauranteId);

  CalcularPedidoResponse calcularTotal(CalcularPedidoRequest request);

  List<PedidoResponse> findByClienteId(Long clienteId);

  List<PedidoResponse> findByStatus(String status);

  List<PedidoResponse> findTop10ByOrderByValorTotalDesc();

  List<PedidoResponse> findByDataPedidoBetween(LocalDateTime dataInicial, LocalDateTime dataFinal);

  List<PedidoResponse> findTop5MaioresPedidosPorRestaurante(Long restauranteId);

  List<VendasPorRestauranteResponse> obterVendasPorRestaurante();

  List<PedidoResponse> findPedidosComValorAcimaDe(BigDecimal valor);

  List<PedidoRelatorioResponse> obterRelatorioByPeriodoAndStatus(LocalDateTime dataInicial,
      LocalDateTime dataFinal, String status);

}
