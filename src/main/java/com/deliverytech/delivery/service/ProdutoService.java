package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.request.ProdutoRequest;
import com.deliverytech.delivery.dto.response.FaturamentoPorCategoriaResponse;
import com.deliverytech.delivery.dto.response.ProdutoMaisVendidoResponse;
import com.deliverytech.delivery.dto.response.ProdutoResponse;
import java.math.BigDecimal;
import java.util.List;

public interface ProdutoService {

  ProdutoResponse criarProduto(ProdutoRequest request);

  List<ProdutoResponse> findByRestauranteId(Long restauranteId);

  List<ProdutoResponse> findByDisponivelTrue();

  List<ProdutoResponse> findByCategoria(String categoria);

  List<ProdutoResponse> findByPrecoLessThanEqual(BigDecimal preco);

  List<ProdutoMaisVendidoResponse> obterProdutosMaisVendidos();

  List<FaturamentoPorCategoriaResponse> obterFaturamentoPorCategoria();

}
