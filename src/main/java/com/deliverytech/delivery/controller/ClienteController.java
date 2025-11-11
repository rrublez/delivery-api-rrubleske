package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.cliente.request.ClienteRequest;
import com.deliverytech.delivery.dto.cliente.response.ClienteResponse;
import com.deliverytech.delivery.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "APIs para gerenciar clientes")
public class ClienteController {

  private final ClienteService clienteService;

  public ClienteController(ClienteService clienteService) {
    this.clienteService = clienteService;
  }

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
