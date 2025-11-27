package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.cliente.request.ClienteRequest;
import com.deliverytech.delivery.dto.cliente.request.ClienteUpdateRequest;
import com.deliverytech.delivery.dto.cliente.response.ClienteResponse;
import com.deliverytech.delivery.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST para gerenciar Clientes
 * Padronização: Utiliza códigos HTTP corretos e anotações OpenAPI
 */
@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "APIs para gerenciar clientes")
public class ClienteController {

  private final ClienteService clienteService;

  public ClienteController(ClienteService clienteService) {
    this.clienteService = clienteService;
  }

  /**
   * POST /api/clientes - Criar novo cliente
   * Código: 201 Created ou 400 Bad Request
   */
  @PostMapping
  @Operation(summary = "Criar novo cliente", description = "Cria um novo cliente com validação de email e CPF únicos")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos ou email/CPF já registrados")
  })
  public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody ClienteRequest request) {
    var response = clienteService.criarCliente(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar cliente por ID", description = "Retorna os dados de um cliente pelo identificador")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
      @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
  })
  public ResponseEntity<ClienteResponse> findById(@Parameter(description = "ID do cliente") @PathVariable Long id) {
    var response = clienteService.findById(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  @Operation(summary = "Listar clientes", description = "Lista todos os clientes com paginação")
  @ApiResponse(responseCode = "200", description = "Clientes listados com sucesso")
  public ResponseEntity<Page<ClienteResponse>> listar(@Parameter(description = "Parâmetros de paginação") Pageable pageable) {
    var response = clienteService.findAll(pageable);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cliente atualizado"),
      @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
  })
  public ResponseEntity<ClienteResponse> atualizar(
      @Parameter(description = "ID do cliente") @PathVariable Long id,
      @Valid @RequestBody ClienteUpdateRequest request) {
    var response = clienteService.atualizarCliente(id, request);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/clientes/email/{email} - Buscar cliente por email
   * Código: 200 OK ou 404 Not Found
   */
  @GetMapping("/email/{email}")
  @Operation(summary = "Buscar cliente por email", description = "Retorna lista de clientes com o email informado")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cliente(s) encontrado(s)"),
      @ApiResponse(responseCode = "404", description = "Nenhum cliente com esse email")
  })
  public ResponseEntity<List<ClienteResponse>> findByEmail(
      @Parameter(description = "Email do cliente") @PathVariable String email) {
    var response = clienteService.findByEmail(email);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/clientes/ativo - Buscar cliente ativo
   * Código: 200 OK ou 404 Not Found
   */
  @GetMapping("/ativo")
  @Operation(summary = "Buscar cliente ativo", description = "Retorna o primeiro cliente cadastrado como ativo")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cliente ativo encontrado"),
      @ApiResponse(responseCode = "404", description = "Nenhum cliente ativo")
  })
  public ResponseEntity<ClienteResponse> findByAtivoTrue() {
    var response = clienteService.findByAtivoTrue();
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/clientes/nome?nome={nome} - Buscar clientes por nome
   * Código: 200 OK ou 404 Not Found
   */
  @GetMapping("/nome")
  @Operation(summary = "Buscar clientes por nome", description = "Retorna lista de clientes que contenham o nome informado (case-insensitive)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cliente(s) encontrado(s)"),
      @ApiResponse(responseCode = "404", description = "Nenhum cliente com esse nome")
  })
  public ResponseEntity<List<ClienteResponse>> findByNome(
      @Parameter(description = "Nome ou parte do nome do cliente") @RequestParam String nome) {
    var response = clienteService.findByNomeContainingIgnoreCase(nome);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/clientes/existe-email/{email} - Verificar existência de email
   * Código: 200 OK
   */
  @GetMapping("/existe-email/{email}")
  @Operation(summary = "Verificar existência de email", description = "Verifica se um email já está registrado no sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Retorna true se existe, false caso contrário")
  })
  public ResponseEntity<Boolean> existsByEmail(
      @Parameter(description = "Email para verificar") @PathVariable String email) {
    var response = clienteService.existsByEmail(email);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/clientes/relatorio/ranking-por-pedidos - Relatório: Ranking de clientes
   * Código: 200 OK
   */
  @GetMapping("/relatorio/ranking-por-pedidos")
  @Operation(summary = "Ranking de clientes por número de pedidos", description = "Retorna ranking dos clientes ordenados por quantidade de pedidos realizados")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Ranking gerado com sucesso")
  })
  public ResponseEntity<List<Object>> obterRankingClientesPorNumeroPedidos() {
    var response = clienteService.obterRankingClientesPorNumeroPedidos();
    return ResponseEntity.ok(new java.util.ArrayList<>(response));
  }
}
