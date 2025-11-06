package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.request.ProdutoRequest;
import com.deliverytech.delivery.dto.response.ProdutoResponse;
import com.deliverytech.delivery.service.ProdutoService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
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
@RequestMapping("/api/produtos")
public class ProdutoController {

  private final ProdutoService produtoService;

  public ProdutoController(ProdutoService produtoService) {
    this.produtoService = produtoService;
  }

  @PostMapping
  public ResponseEntity<ProdutoResponse> criar(@Valid @RequestBody ProdutoRequest request) {
    var response = produtoService.criarProduto(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/restaurante/{restauranteId}")
  public ResponseEntity<List<ProdutoResponse>> findByRestauranteId(
      @PathVariable Long restauranteId) {
    var response = produtoService.findByRestauranteId(restauranteId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/disponivel")
  public ResponseEntity<List<ProdutoResponse>> findByDisponivelTrue() {
    var response = produtoService.findByDisponivelTrue();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/categoria/{categoria}")
  public ResponseEntity<List<ProdutoResponse>> findByCategoria(@PathVariable String categoria) {
    var response = produtoService.findByCategoria(categoria);
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
