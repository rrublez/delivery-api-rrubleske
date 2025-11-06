package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.request.ClienteRequest;
import com.deliverytech.delivery.dto.response.ClienteResponse;
import com.deliverytech.delivery.service.ClienteService;
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
public class ClienteController {

  private final ClienteService clienteService;

  public ClienteController(ClienteService clienteService) {
    this.clienteService = clienteService;
  }

  @PostMapping
  public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody ClienteRequest request) {
    var response = clienteService.criarCliente(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<List<ClienteResponse>> findByEmail(@PathVariable String email) {
    var response = clienteService.findByEmail(email);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/ativo")
  public ResponseEntity<ClienteResponse> findByAtivoTrue() {
    var response = clienteService.findByAtivoTrue();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/nome")
  public ResponseEntity<List<ClienteResponse>> findByNome(@RequestParam String nome) {
    var response = clienteService.findByNomeContainingIgnoreCase(nome);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/existe-email/{email}")
  public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
    var response = clienteService.existsByEmail(email);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/relatorio/ranking-por-pedidos")
  public ResponseEntity<List<Object>> obterRankingClientesPorNumeroPedidos() {
    var response = clienteService.obterRankingClientesPorNumeroPedidos();
    return ResponseEntity.ok(new java.util.ArrayList<>(response));
  }

}
