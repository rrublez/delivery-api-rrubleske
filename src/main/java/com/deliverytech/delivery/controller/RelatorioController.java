package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.pedido.response.PedidoRelatorioResponse;
import com.deliverytech.delivery.dto.produto.response.ProdutoMaisVendidoResponse;
import com.deliverytech.delivery.dto.shared.response.RankingClienteResponse;
import com.deliverytech.delivery.dto.shared.response.VendasPorRestauranteResponse;
import com.deliverytech.delivery.service.RelatorioService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

  private final RelatorioService relatorioService;

  public RelatorioController(RelatorioService relatorioService) {
    this.relatorioService = relatorioService;
  }

  /**
   * GET /api/relatorios/vendas-por-restaurante
   * Retorna vendas totais agrupadas por restaurante
   * 
   * @return Lista de VendasPorRestauranteResponse com totalizações
   */
  @GetMapping("/vendas-por-restaurante")
  public ResponseEntity<List<VendasPorRestauranteResponse>> obterVendasPorRestaurante() {
    try {
      var response = relatorioService.obterVendasPorRestaurante();
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  /**
   * GET /api/relatorios/produtos-mais-vendidos
   * Retorna top produtos mais vendidos
   * 
   * @return Lista de ProdutoMaisVendidoResponse ordenada por quantidade vendida
   */
  @GetMapping("/produtos-mais-vendidos")
  public ResponseEntity<List<ProdutoMaisVendidoResponse>> obterProdutosMaisVendidos() {
    try {
      var response = relatorioService.obterProdutosMaisVendidos();
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  /**
   * GET /api/relatorios/clientes-ativos
   * Retorna ranking de clientes mais ativos (por número de pedidos)
   * 
   * @return Lista de RankingClienteResponse ordenada por total de pedidos
   */
  @GetMapping("/clientes-ativos")
  public ResponseEntity<List<RankingClienteResponse>> obterClientesAtivos() {
    try {
      var response = relatorioService.obterClientesAtivos();
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  /**
   * GET /api/relatorios/pedidos-por-periodo
   * Retorna pedidos dentro de um período, opcionalmente filtrado por status
   * 
   * @param dataInicial Data inicial do período (formato: ISO 8601, ex: 2025-01-01T00:00:00)
   * @param dataFinal Data final do período (formato: ISO 8601, ex: 2025-12-31T23:59:59)
   * @param status Status opcional do pedido (ex: PENDENTE, CONFIRMADO, ENTREGUE, CANCELADO)
   * @return Lista de PedidoRelatorioResponse ordenada por data decrescente
   */
  @GetMapping("/pedidos-por-periodo")
  public ResponseEntity<List<PedidoRelatorioResponse>> obterPedidosPorPeriodo(
      @RequestParam(name = "dataInicial")
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
      @RequestParam(name = "dataFinal")
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal,
      @RequestParam(name = "status", required = false) String status) {
    try {
      if (dataInicial.isAfter(dataFinal)) {
        return ResponseEntity.badRequest().build();
      }
      var response = relatorioService.obterPedidosPorPeriodo(dataInicial, dataFinal, status);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

}
