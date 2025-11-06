package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.request.PedidoRequest;
import com.deliverytech.delivery.dto.response.PedidoRelatorioResponse;
import com.deliverytech.delivery.dto.response.PedidoResponse;
import com.deliverytech.delivery.dto.response.VendasPorRestauranteResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PedidoService {

  PedidoResponse criarPedido(PedidoRequest request);

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
