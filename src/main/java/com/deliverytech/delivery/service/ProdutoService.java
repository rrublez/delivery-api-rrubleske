package com.deliverytech.delivery.service;

import java.math.BigDecimal;
import java.util.List;
import com.deliverytech.delivery.dto.produto.request.AtualizarDisponibilidadeProdutoRequest;
import com.deliverytech.delivery.dto.produto.request.ProdutoRequest;
import com.deliverytech.delivery.dto.produto.response.FaturamentoPorCategoriaResponse;
import com.deliverytech.delivery.dto.produto.response.ProdutoMaisVendidoResponse;
import com.deliverytech.delivery.dto.produto.response.ProdutoResponse;

public interface ProdutoService {

  // CRUD Básico
  ProdutoResponse criarProduto(ProdutoRequest request);

  ProdutoResponse obterPorId(Long id);

  ProdutoResponse atualizarProduto(Long id, ProdutoRequest request);

  void deletarProduto(Long id);

  // Atualizações parciais
  ProdutoResponse atualizarDisponibilidade(Long id, AtualizarDisponibilidadeProdutoRequest request);

  // Buscas e filtros
  List<ProdutoResponse> findByRestauranteId(Long restauranteId);

  List<ProdutoResponse> findByDisponivelTrue();

  List<ProdutoResponse> findByCategoria(String categoria);

  List<ProdutoResponse> findByPrecoLessThanEqual(BigDecimal preco);

  List<ProdutoResponse> findByNome(String nome);

  // Relatórios
  List<ProdutoMaisVendidoResponse> obterProdutosMaisVendidos();

  List<FaturamentoPorCategoriaResponse> obterFaturamentoPorCategoria();

  boolean isOwner(Long produtoId);

}
