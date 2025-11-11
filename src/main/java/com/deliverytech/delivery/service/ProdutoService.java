package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.request.AtualizarDisponibilidadeProdutoRequest;
import com.deliverytech.delivery.dto.request.ProdutoRequest;
import com.deliverytech.delivery.dto.response.FaturamentoPorCategoriaResponse;
import com.deliverytech.delivery.dto.response.ProdutoMaisVendidoResponse;
import com.deliverytech.delivery.dto.response.ProdutoResponse;
import java.math.BigDecimal;
import java.util.List;

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

}
