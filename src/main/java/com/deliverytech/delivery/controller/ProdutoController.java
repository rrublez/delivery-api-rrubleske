package com.deliverytech.delivery.controller;

import java.math.BigDecimal;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import com.deliverytech.delivery.dto.produto.request.AtualizarDisponibilidadeProdutoRequest;
import com.deliverytech.delivery.dto.produto.request.ProdutoRequest;
import com.deliverytech.delivery.dto.produto.response.ProdutoResponse;
import com.deliverytech.delivery.service.ProdutoService;
import jakarta.validation.Valid;

/**
 * Controller REST para gerenciar Produtos
 * Endpoints: CRUD completo + filtros e relatórios
 */
@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "APIs para gerenciar produtos e categorias")
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
  @Operation(summary = "Criar novo produto", description = "Cria um novo produto com as informações fornecidas")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos")
  })
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
  @Operation(summary = "Obter produto por ID", description = "Retorna os detalhes de um produto específico")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produto encontrado"),
      @ApiResponse(responseCode = "404", description = "Produto não encontrado")
  })
  public ResponseEntity<ProdutoResponse> obterPorId(
      @Parameter(description = "ID do produto") @PathVariable Long id) {
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
  @Operation(summary = "Atualizar produto completo", description = "Atualiza todos os campos de um produto existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "404", description = "Produto não encontrado")
  })
  public ResponseEntity<ProdutoResponse> atualizar(
      @Parameter(description = "ID do produto") @PathVariable Long id,
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
  @Operation(summary = "Deletar produto", description = "Remove um produto do sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso"),
      @ApiResponse(responseCode = "404", description = "Produto não encontrado")
  })
  public ResponseEntity<Void> deletar(@Parameter(description = "ID do produto") @PathVariable Long id) {
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
  @Operation(summary = "Atualizar disponibilidade do produto", description = "Altera o status de disponibilidade de um produto")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Disponibilidade atualizada com sucesso"),
      @ApiResponse(responseCode = "404", description = "Produto não encontrado")
  })
  public ResponseEntity<ProdutoResponse> atualizarDisponibilidade(
      @Parameter(description = "ID do produto") @PathVariable Long id,
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
  @Operation(summary = "Listar produtos por restaurante", description = "Retorna todos os produtos de um restaurante específico")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produtos listados com sucesso"),
      @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
  })
  public ResponseEntity<List<ProdutoResponse>> findByRestauranteId(
      @Parameter(description = "ID do restaurante") @PathVariable Long restauranteId) {
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
  @Operation(summary = "Listar produtos por categoria", description = "Retorna todos os produtos de uma categoria específica")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produtos listados com sucesso"),
      @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
  })
  public ResponseEntity<List<ProdutoResponse>> findByCategoria(
      @Parameter(description = "Nome da categoria") @PathVariable String categoria) {
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
  @Operation(summary = "Buscar produtos por nome", description = "Retorna produtos que contenham o nome informado (case-insensitive)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produtos encontrados")
  })
  public ResponseEntity<List<ProdutoResponse>> buscarPorNome(
      @Parameter(description = "Nome ou parte do nome do produto") @RequestParam String nome) {
    var response = produtoService.findByNome(nome);
    return ResponseEntity.ok(response);
  }

  // ==================== MÉTODOS MANTIDOS PARA COMPATIBILIDADE ====================

  @GetMapping("/disponivel")
  @Operation(summary = "Listar produtos disponíveis", description = "Retorna todos os produtos marcados como disponíveis")
  @ApiResponse(responseCode = "200", description = "Produtos disponíveis listados")
  public ResponseEntity<List<ProdutoResponse>> findByDisponivelTrue() {
    var response = produtoService.findByDisponivelTrue();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/preco-maximo")
  @Operation(summary = "Listar produtos por preço máximo", description = "Retorna produtos com preço menor ou igual ao informado")
  @ApiResponse(responseCode = "200", description = "Produtos filtrados por preço")
  public ResponseEntity<List<ProdutoResponse>> findByPrecoLessThanEqual(
      @Parameter(description = "Preço máximo") @RequestParam BigDecimal preco) {
    var response = produtoService.findByPrecoLessThanEqual(preco);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/relatorio/mais-vendidos")
  @Operation(summary = "Relatório de produtos mais vendidos", description = "Retorna ranking dos produtos mais vendidos")
  @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
  public ResponseEntity<List<Object>> obterProdutosMaisVendidos() {
    var response = produtoService.obterProdutosMaisVendidos();
    return ResponseEntity.ok(new java.util.ArrayList<>(response));
  }

  @GetMapping("/relatorio/faturamento-por-categoria")
  @Operation(summary = "Faturamento por categoria", description = "Retorna faturamento total agrupado por categoria de produtos")
  @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
  public ResponseEntity<List<Object>> obterFaturamentoPorCategoria() {
    var response = produtoService.obterFaturamentoPorCategoria();
    return ResponseEntity.ok(new java.util.ArrayList<>(response));
  }

}
