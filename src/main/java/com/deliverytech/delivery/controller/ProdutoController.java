package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.request.AtualizarDisponibilidadeProdutoRequest;
import com.deliverytech.delivery.dto.request.ProdutoRequest;
import com.deliverytech.delivery.dto.response.ProdutoResponse;
import com.deliverytech.delivery.service.ProdutoService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST para gerenciar Produtos
 * Endpoints: CRUD completo + filtros e relatórios
 */
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

  private final ProdutoService produtoService;

  public ProdutoController(ProdutoService produtoService) {
    this.produtoService = produtoService;
  }

  /**
   * POST /api/produtos - Criar novo produto
   * Body: ProdutoRequest (nome, descricao, preco, disponivel, categoria)
   * 
   * Response: 201 Created com ProdutoResponse
   */
  @PostMapping
  public ResponseEntity<ProdutoResponse> criar(@Valid @RequestBody ProdutoRequest request) {
    var response = produtoService.criarProduto(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * GET /api/produtos/{id} - Buscar produto por ID
   * Path param: id
   * 
   * Response: 200 OK com ProdutoResponse
   * Error: 404 Not Found se não existir
   */
  @GetMapping("/{id}")
  public ResponseEntity<ProdutoResponse> obterPorId(@PathVariable Long id) {
    var response = produtoService.obterPorId(id);
    return ResponseEntity.ok(response);
  }

  /**
   * PUT /api/produtos/{id} - Atualizar produto completo
   * Path param: id
   * Body: ProdutoRequest (todos os campos)
   * 
   * Response: 200 OK com ProdutoResponse atualizado
   * Error: 404 Not Found
   */
  @PutMapping("/{id}")
  public ResponseEntity<ProdutoResponse> atualizar(
      @PathVariable Long id,
      @Valid @RequestBody ProdutoRequest request) {
    var response = produtoService.atualizarProduto(id, request);
    return ResponseEntity.ok(response);
  }

  /**
   * DELETE /api/produtos/{id} - Remover produto
   * Path param: id
   * 
   * Response: 204 No Content
   * Error: 404 Not Found se não existir
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable Long id) {
    produtoService.deletarProduto(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * PATCH /api/produtos/{id}/disponibilidade - Ativar/desativar produto
   * Path param: id
   * Body: AtualizarDisponibilidadeProdutoRequest { disponivel: true/false }
   * 
   * Response: 200 OK com ProdutoResponse atualizado
   * Error: 404 Not Found
   * 
   * Exemplo:
   * PATCH /api/produtos/1/disponibilidade
   * { "disponivel": false }
   */
  @PatchMapping("/{id}/disponibilidade")
  public ResponseEntity<ProdutoResponse> atualizarDisponibilidade(
      @PathVariable Long id,
      @Valid @RequestBody AtualizarDisponibilidadeProdutoRequest request) {
    var response = produtoService.atualizarDisponibilidade(id, request);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/restaurantes/{restauranteId}/produtos - Produtos de um restaurante
   * Path param: restauranteId
   * 
   * Response: 200 OK com lista de ProdutoResponse
   */
  @GetMapping("/restaurante/{restauranteId}")
  public ResponseEntity<List<ProdutoResponse>> findByRestauranteId(
      @PathVariable Long restauranteId) {
    var response = produtoService.findByRestauranteId(restauranteId);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/produtos/categoria/{categoria} - Produtos por categoria
   * Path param: categoria
   * 
   * Response: 200 OK com lista de ProdutoResponse
   */
  @GetMapping("/categoria/{categoria}")
  public ResponseEntity<List<ProdutoResponse>> findByCategoria(@PathVariable String categoria) {
    var response = produtoService.findByCategoria(categoria);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/produtos/buscar?nome={nome} - Buscar produtos por nome
   * Query param: nome (busca case-insensitive com LIKE)
   * 
   * Response: 200 OK com lista de ProdutoResponse
   * 
   * Exemplos:
   * GET /api/produtos/buscar?nome=Pizza
   * GET /api/produtos/buscar?nome=Margarita
   */
  @GetMapping("/buscar")
  public ResponseEntity<List<ProdutoResponse>> buscarPorNome(@RequestParam String nome) {
    var response = produtoService.findByNome(nome);
    return ResponseEntity.ok(response);
  }

  // ==================== MÉTODOS MANTIDOS PARA COMPATIBILIDADE ====================

  @GetMapping("/disponivel")
  public ResponseEntity<List<ProdutoResponse>> findByDisponivelTrue() {
    var response = produtoService.findByDisponivelTrue();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/preco-maximo")
  public ResponseEntity<List<ProdutoResponse>> findByPrecoLessThanEqual(
      @RequestParam BigDecimal preco) {
    var response = produtoService.findByPrecoLessThanEqual(preco);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/relatorio/mais-vendidos")
  public ResponseEntity<List<Object>> obterProdutosMaisVendidos() {
    var response = produtoService.obterProdutosMaisVendidos();
    return ResponseEntity.ok(new java.util.ArrayList<>(response));
  }

  @GetMapping("/relatorio/faturamento-por-categoria")
  public ResponseEntity<List<Object>> obterFaturamentoPorCategoria() {
    var response = produtoService.obterFaturamentoPorCategoria();
    return ResponseEntity.ok(new java.util.ArrayList<>(response));
  }

}
