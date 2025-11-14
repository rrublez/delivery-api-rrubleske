package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.pedido.response.PedidoRelatorioResponse;
import com.deliverytech.delivery.dto.produto.response.ProdutoMaisVendidoResponse;
import com.deliverytech.delivery.dto.shared.response.RankingClienteResponse;
import com.deliverytech.delivery.dto.shared.response.VendasPorRestauranteResponse;
import com.deliverytech.delivery.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST para gerar Relatórios
 * Padronização: Utiliza códigos HTTP corretos e anotações OpenAPI
 */
@RestController
@RequestMapping("/api/relatorios")
@Tag(name = "Relatórios", description = "APIs para gerar relatórios de vendas e análises")
public class RelatorioController {

  private final RelatorioService relatorioService;

  public RelatorioController(RelatorioService relatorioService) {
    this.relatorioService = relatorioService;
  }

  /**
   * GET /api/relatorios/vendas-por-restaurante - Relatório: Vendas por restaurante
   * Código: 200 OK
   */
  @GetMapping("/vendas-por-restaurante")
  @Operation(summary = "Vendas por restaurante", description = "Retorna total de vendas agrupado por restaurante")
  @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
  public ResponseEntity<List<VendasPorRestauranteResponse>> obterVendasPorRestaurante() {
    var response = relatorioService.obterVendasPorRestaurante();
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/relatorios/produtos-mais-vendidos - Relatório: Produtos mais vendidos
   * Código: 200 OK
   */
  @GetMapping("/produtos-mais-vendidos")
  @Operation(summary = "Produtos mais vendidos", description = "Retorna ranking dos produtos mais vendidos")
  @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
  public ResponseEntity<List<ProdutoMaisVendidoResponse>> obterProdutosMaisVendidos() {
    var response = relatorioService.obterProdutosMaisVendidos();
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/relatorios/clientes-ativos - Relatório: Clientes mais ativos
   * Código: 200 OK
   */
  @GetMapping("/clientes-ativos")
  @Operation(summary = "Clientes mais ativos", description = "Retorna ranking de clientes ordenados por número de pedidos realizados")
  @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
  public ResponseEntity<List<RankingClienteResponse>> obterClientesAtivos() {
    var response = relatorioService.obterClientesAtivos();
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/relatorios/pedidos-por-periodo - Relatório: Pedidos por período
   * Código: 200 OK ou 400 Bad Request
   */
  @GetMapping("/pedidos-por-periodo")
  @Operation(summary = "Pedidos por período", description = "Retorna pedidos dentro de um período, opcionalmente filtrados por status")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Datas inválidas (dataInicial posterior a dataFinal)")
  })
  public ResponseEntity<List<PedidoRelatorioResponse>> obterPedidosPorPeriodo(
      @Parameter(description = "Data inicial (ISO 8601, ex: 2025-01-01T00:00:00)") 
      @RequestParam(name = "dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
      @Parameter(description = "Data final (ISO 8601, ex: 2025-12-31T23:59:59)") 
      @RequestParam(name = "dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal,
      @Parameter(description = "Status do pedido (ex: PENDENTE, ENTREGUE, CANCELADO)") 
      @RequestParam(name = "status", required = false) String status) {
    
    if (dataInicial.isAfter(dataFinal)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    
    var response = relatorioService.obterPedidosPorPeriodo(dataInicial, dataFinal, status);
    return ResponseEntity.ok(response);
  }
}
