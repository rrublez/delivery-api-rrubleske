package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.request.PedidoRequest;
import com.deliverytech.delivery.dto.response.PedidoResponse;
import com.deliverytech.delivery.service.PedidoService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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