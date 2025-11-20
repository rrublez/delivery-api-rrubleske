package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.pedido.request.AtualizarStatusPedidoRequest;
import com.deliverytech.delivery.dto.pedido.request.CalcularPedidoRequest;
import com.deliverytech.delivery.dto.pedido.request.PedidoRequest;
import com.deliverytech.delivery.dto.pedido.response.CalcularPedidoResponse;
import com.deliverytech.delivery.dto.pedido.response.PedidoResponse;
import com.deliverytech.delivery.security.SecurityUtils;
import com.deliverytech.delivery.service.ClienteService;
import com.deliverytech.delivery.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller REST para gerenciar Pedidos
 * Padronização: Utiliza códigos HTTP corretos e anotações OpenAPI
 */
@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "APIs para gerenciar pedidos de delivery")
public class PedidoController {

  private final PedidoService pedidoService;
  private final ClienteService clienteService;

  public PedidoController(PedidoService pedidoService, ClienteService clienteService) {
    this.pedidoService = pedidoService;
    this.clienteService = clienteService;
  }

  /**
   * POST /api/pedidos - Criar novo pedido
   * Código: 201 Created ou 400 Bad Request
   */
  @PostMapping
  @Operation(summary = "Criar novo pedido", description = "Cria um novo pedido com validação de produtos e cliente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos ou produto não disponível")
  })
  @PreAuthorize("hasRole('CLIENTE')")
  public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody PedidoRequest request) {
    var response = pedidoService.criarPedido(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/meus")
  @Operation(summary = "Listar meus pedidos", description = "Retorna os pedidos do cliente autenticado")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Pedidos do cliente listados com sucesso"),
      @ApiResponse(responseCode = "401", description = "Cliente não autenticado"),
      @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
  })
  @PreAuthorize("hasRole('CLIENTE')")
  public ResponseEntity<List<PedidoResponse>> meusPedidos() {
    String email = SecurityUtils.getCurrentUserEmail()
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado"));
    var cliente = clienteService.findByEmail(email)
        .stream()
        .findFirst()
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado para o usuário autenticado"));
    var response = pedidoService.findByClienteId(cliente.getId());
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/pedidos/{id} - Buscar pedido por ID
   * Código: 200 OK ou 404 Not Found
   */
  @GetMapping("/{id}")
  @Operation(summary = "Obter pedido por ID", description = "Retorna os detalhes completos de um pedido, incluindo itens e cliente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
      @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
  })
  @PreAuthorize("@pedidoService.canAccess(#id)")
  public ResponseEntity<PedidoResponse> obterPorId(@Parameter(description = "ID do pedido") @PathVariable Long id) {
    var response = pedidoService.obterPorId(id);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/pedidos - Listar pedidos com filtros opcionais
   * Código: 200 OK
   */
  @GetMapping
  @Operation(summary = "Listar pedidos com filtros", description = "Retorna lista de pedidos com filtros opcionais por status e período")
  @ApiResponse(responseCode = "200", description = "Pedidos listados com sucesso")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<PedidoResponse>> listarComFiltros(
      @Parameter(description = "Status do pedido (PENDENTE, ENTREGUE, CANCELADO)") @RequestParam(required = false) String status,
      @Parameter(description = "Data inicial (formato ISO 8601)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
      @Parameter(description = "Data final (formato ISO 8601)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal) {
    var response = pedidoService.listarComFiltros(status, dataInicial, dataFinal);
    return ResponseEntity.ok(response);
  }

  /**
   * PATCH /api/pedidos/{id}/status - Atualizar status do pedido
   * Código: 200 OK, 400 Bad Request ou 404 Not Found
   */
  @PatchMapping("/{id}/status")
  @Operation(summary = "Atualizar status do pedido", description = "Altera o status de um pedido existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Status inválido"),
      @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
  })
  public ResponseEntity<PedidoResponse> atualizarStatus(
      @Parameter(description = "ID do pedido") @PathVariable Long id,
      @Valid @RequestBody AtualizarStatusPedidoRequest request) {
    var response = pedidoService.atualizarStatus(id, request);
    return ResponseEntity.ok(response);
  }

  /**
   * DELETE /api/pedidos/{id} - Cancelar pedido
   * Código: 204 No Content ou 404 Not Found
   */
  @DeleteMapping("/{id}")
  @Operation(summary = "Cancelar pedido", description = "Cancela um pedido existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Pedido cancelado com sucesso"),
      @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
  })
  @PreAuthorize("@pedidoService.canAccess(#id)")
  public ResponseEntity<Void> cancelarPedido(@Parameter(description = "ID do pedido") @PathVariable Long id) {
    pedidoService.cancelarPedido(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * GET /api/pedidos/clientes/{clienteId} - Histórico do cliente
   * Código: 200 OK
   */
  @GetMapping("/clientes/{clienteId}")
  @Operation(summary = "Histórico de pedidos do cliente", description = "Retorna todos os pedidos realizado por um cliente específico")
  @ApiResponse(responseCode = "200", description = "Pedidos do cliente listados")
  public ResponseEntity<List<PedidoResponse>> pedidosPorCliente(
      @Parameter(description = "ID do cliente") @PathVariable Long clienteId) {
    var response = pedidoService.pedidosPorCliente(clienteId);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/pedidos/restaurantes/{restauranteId} - Pedidos do restaurante
   * Código: 200 OK
   */
  @GetMapping("/restaurantes/{restauranteId}")
  @Operation(summary = "Pedidos do restaurante", description = "Retorna todos os pedidos recebidos por um restaurante")
  @ApiResponse(responseCode = "200", description = "Pedidos do restaurante listados")
  public ResponseEntity<List<PedidoResponse>> pedidosPorRestaurante(
      @Parameter(description = "ID do restaurante") @PathVariable Long restauranteId) {
    var response = pedidoService.pedidosPorRestaurante(restauranteId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/restaurante")
  @Operation(summary = "Pedidos do restaurante logado", description = "Retorna todos os pedidos do restaurante vinculado ao usuário autenticado")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Pedidos listados com sucesso"),
      @ApiResponse(responseCode = "403", description = "Usuário não está vinculado a um restaurante")
  })
  @PreAuthorize("hasRole('RESTAURANTE')")
  public ResponseEntity<List<PedidoResponse>> pedidosDoRestauranteLogado() {
    Long restauranteId = SecurityUtils.getCurrentUser()
        .map(usuario -> usuario.getRestauranteId())
        .filter(Objects::nonNull)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não vinculado a restaurante"));
    var response = pedidoService.pedidosPorRestaurante(restauranteId);
    return ResponseEntity.ok(response);
  }

  /**
   * POST /api/pedidos/calcular - Calcular total do pedido
   * Código: 200 OK ou 400 Bad Request
   */
  @PostMapping("/calcular")
  @Operation(summary = "Calcular total do pedido", description = "Calcula o total do pedido incluindo subtotal, taxa de entrega e impostos, sem salvar")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cálculo realizado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos para cálculo")
  })
  public ResponseEntity<CalcularPedidoResponse> calcularTotal(
      @Valid @RequestBody CalcularPedidoRequest request) {
    var response = pedidoService.calcularTotal(request);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/pedidos/cliente/{clienteId} - Buscar pedidos por cliente
   * Código: 200 OK
   */
  @GetMapping("/cliente/{clienteId}")
  @Operation(summary = "Buscar pedidos por cliente", description = "Retorna todos os pedidos de um cliente específico")
  @ApiResponse(responseCode = "200", description = "Pedidos encontrados")
  public ResponseEntity<List<PedidoResponse>> findByClienteId(
      @Parameter(description = "ID do cliente") @PathVariable Long clienteId) {
    var response = pedidoService.findByClienteId(clienteId);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/pedidos/status/{status} - Buscar pedidos por status
   * Código: 200 OK
   */
  @GetMapping("/status/{status}")
  @Operation(summary = "Buscar pedidos por status", description = "Retorna todos os pedidos com um status específico")
  @ApiResponse(responseCode = "200", description = "Pedidos encontrados")
  public ResponseEntity<List<PedidoResponse>> findByStatus(
      @Parameter(description = "Status do pedido") @PathVariable String status) {
    var response = pedidoService.findByStatus(status);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/pedidos/top-10-maiores - Top 10 maiores pedidos
   * Código: 200 OK
   */
  @GetMapping("/top-10-maiores")
  @Operation(summary = "Top 10 maiores pedidos", description = "Retorna os 10 pedidos com maior valor total")
  @ApiResponse(responseCode = "200", description = "Top 10 listado")
  public ResponseEntity<List<PedidoResponse>> findTop10ByOrderByValorTotalDesc() {
    var response = pedidoService.findTop10ByOrderByValorTotalDesc();
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/pedidos/data-range - Buscar pedidos por período
   * Código: 200 OK
   */
  @GetMapping("/data-range")
  @Operation(summary = "Buscar pedidos por período", description = "Retorna pedidos dentro de um período específico")
  @ApiResponse(responseCode = "200", description = "Pedidos encontrados")
  public ResponseEntity<List<PedidoResponse>> findByDataPedidoBetween(
      @Parameter(description = "Data inicial (ISO 8601)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
      @Parameter(description = "Data final (ISO 8601)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal) {
    var response = pedidoService.findByDataPedidoBetween(dataInicial, dataFinal);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/pedidos/restaurante/{restauranteId}/top-5 - Top 5 maiores pedidos por restaurante
   * Código: 200 OK
   */
  @GetMapping("/restaurante/{restauranteId}/top-5")
  @Operation(summary = "Top 5 maiores pedidos por restaurante", description = "Retorna os 5 maiores pedidos de um restaurante")
  @ApiResponse(responseCode = "200", description = "Top 5 listado")
  public ResponseEntity<List<PedidoResponse>> findTop5MaioresPedidosPorRestaurante(
      @Parameter(description = "ID do restaurante") @PathVariable Long restauranteId) {
    var response = pedidoService.findTop5MaioresPedidosPorRestaurante(restauranteId);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/pedidos/relatorio/vendas-por-restaurante - Relatório: Vendas por restaurante
   * Código: 200 OK
   */
  @GetMapping("/relatorio/vendas-por-restaurante")
  @Operation(summary = "Vendas por restaurante", description = "Retorna relatório de vendas totais agrupadas por restaurante")
  @ApiResponse(responseCode = "200", description = "Relatório gerado")
  public ResponseEntity<List<Object>> obterVendasPorRestaurante() {
    var response = pedidoService.obterVendasPorRestaurante();
    return ResponseEntity.ok(new java.util.ArrayList<>(response));
  }

  /**
   * GET /api/pedidos/relatorio/valor-acima - Pedidos com valor acima de
   * Código: 200 OK
   */
  @GetMapping("/relatorio/valor-acima")
  @Operation(summary = "Pedidos com valor acima de", description = "Retorna pedidos com valor total superior ao informado")
  @ApiResponse(responseCode = "200", description = "Pedidos encontrados")
  public ResponseEntity<List<PedidoResponse>> findPedidosComValorAcimaDe(
      @Parameter(description = "Valor mínimo") @RequestParam BigDecimal valor) {
    var response = pedidoService.findPedidosComValorAcimaDe(valor);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/pedidos/relatorio/periodo-status - Relatório por período e status
   * Código: 200 OK
   */
  @GetMapping("/relatorio/periodo-status")
  @Operation(summary = "Relatório por período e status", description = "Retorna relatório de pedidos filtrados por período e status")
  @ApiResponse(responseCode = "200", description = "Relatório gerado")
  public ResponseEntity<List<Object>> obterRelatorioByPeriodoAndStatus(
      @Parameter(description = "Data inicial (ISO 8601)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
      @Parameter(description = "Data final (ISO 8601)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal,
      @Parameter(description = "Status do pedido") @RequestParam String status) {
    var response = pedidoService.obterRelatorioByPeriodoAndStatus(dataInicial, dataFinal, status);
    return ResponseEntity.ok(new java.util.ArrayList<>(response));
  }
}