package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.request.AtualizarStatusPedidoRequest;
import com.deliverytech.delivery.dto.request.CalcularPedidoRequest;
import com.deliverytech.delivery.dto.request.PedidoRequest;
import com.deliverytech.delivery.dto.response.CalcularPedidoResponse;
import com.deliverytech.delivery.dto.response.PedidoResponse;
import com.deliverytech.delivery.service.PedidoService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

  private final PedidoService pedidoService;

  public PedidoController(PedidoService pedidoService) {
    this.pedidoService = pedidoService;
  }

  @PostMapping
  public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody PedidoRequest request) {
    var response = pedidoService.criarPedido(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * GET /api/pedidos/{id} - Buscar pedido completo
   * @param id ID do pedido
   * @return Pedido encontrado
   */
  @GetMapping("/{id}")
  public ResponseEntity<PedidoResponse> obterPorId(@PathVariable Long id) {
    var response = pedidoService.obterPorId(id);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/pedidos - Listar com filtros (status, data)
   * @param status Status do pedido (opcional)
   * @param dataInicial Data inicial (opcional)
   * @param dataFinal Data final (opcional)
   * @return Lista de pedidos filtrados
   */
  @GetMapping
  public ResponseEntity<List<PedidoResponse>> listarComFiltros(
      @RequestParam(required = false) String status,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal) {
    var response = pedidoService.listarComFiltros(status, dataInicial, dataFinal);
    return ResponseEntity.ok(response);
  }

  /**
   * PATCH /api/pedidos/{id}/status - Atualizar status
   * @param id ID do pedido
   * @param request Novo status
   * @return Pedido atualizado
   */
  @PatchMapping("/{id}/status")
  public ResponseEntity<PedidoResponse> atualizarStatus(
      @PathVariable Long id,
      @Valid @RequestBody AtualizarStatusPedidoRequest request) {
    var response = pedidoService.atualizarStatus(id, request);
    return ResponseEntity.ok(response);
  }

  /**
   * DELETE /api/pedidos/{id} - Cancelar pedido
   * @param id ID do pedido
   * @return Sem conteúdo
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> cancelarPedido(@PathVariable Long id) {
    pedidoService.cancelarPedido(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * GET /api/clientes/{clienteId}/pedidos - Histórico do cliente
   * @param clienteId ID do cliente
   * @return Lista de pedidos do cliente
   */
  @GetMapping("/clientes/{clienteId}")
  public ResponseEntity<List<PedidoResponse>> pedidosPorCliente(@PathVariable Long clienteId) {
    var response = pedidoService.pedidosPorCliente(clienteId);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/restaurantes/{restauranteId}/pedidos - Pedidos do restaurante
   * @param restauranteId ID do restaurante
   * @return Lista de pedidos do restaurante
   */
  @GetMapping("/restaurantes/{restauranteId}")
  public ResponseEntity<List<PedidoResponse>> pedidosPorRestaurante(@PathVariable Long restauranteId) {
    var response = pedidoService.pedidosPorRestaurante(restauranteId);
    return ResponseEntity.ok(response);
  }

  /**
   * POST /api/pedidos/calcular - Calcular total sem salvar
   * @param request Dados para cálculo
   * @return Cálculo com subtotal, taxa e total
   */
  @PostMapping("/calcular")
  public ResponseEntity<CalcularPedidoResponse> calcularTotal(
      @Valid @RequestBody CalcularPedidoRequest request) {
    var response = pedidoService.calcularTotal(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/cliente/{clienteId}")
  public ResponseEntity<List<PedidoResponse>> findByClienteId(@PathVariable Long clienteId) {
    var response = pedidoService.findByClienteId(clienteId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/status/{status}")
  public ResponseEntity<List<PedidoResponse>> findByStatus(@PathVariable String status) {
    var response = pedidoService.findByStatus(status);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/top-10-maiores")
  public ResponseEntity<List<PedidoResponse>> findTop10ByOrderByValorTotalDesc() {
    var response = pedidoService.findTop10ByOrderByValorTotalDesc();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/data-range")
  public ResponseEntity<List<PedidoResponse>> findByDataPedidoBetween(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal) {
    var response = pedidoService.findByDataPedidoBetween(dataInicial, dataFinal);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/restaurante/{restauranteId}/top-5")
  public ResponseEntity<List<PedidoResponse>> findTop5MaioresPedidosPorRestaurante(
      @PathVariable Long restauranteId) {
    var response = pedidoService.findTop5MaioresPedidosPorRestaurante(restauranteId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/relatorio/vendas-por-restaurante")
  public ResponseEntity<List<Object>> obterVendasPorRestaurante() {
    var response = pedidoService.obterVendasPorRestaurante();
    return ResponseEntity.ok(new java.util.ArrayList<>(response));
  }

  @GetMapping("/relatorio/valor-acima")
  public ResponseEntity<List<PedidoResponse>> findPedidosComValorAcimaDe(
      @RequestParam BigDecimal valor) {
    var response = pedidoService.findPedidosComValorAcimaDe(valor);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/relatorio/periodo-status")
  public ResponseEntity<List<Object>> obterRelatorioByPeriodoAndStatus(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal,
      @RequestParam String status) {
    var response = pedidoService.obterRelatorioByPeriodoAndStatus(dataInicial, dataFinal, status);
    return ResponseEntity.ok(new java.util.ArrayList<>(response));
  }

}