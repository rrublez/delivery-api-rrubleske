package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.dto.request.ProdutoRequest;
import com.deliverytech.delivery.dto.response.FaturamentoPorCategoriaResponse;
import com.deliverytech.delivery.dto.response.ProdutoMaisVendidoResponse;
import com.deliverytech.delivery.dto.response.ProdutoResponse;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.service.ProdutoService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProdutoServiceImpl implements ProdutoService {

  private final ProdutoRepository produtoRepository;

  public ProdutoServiceImpl(ProdutoRepository produtoRepository) {
    this.produtoRepository = produtoRepository;
  }

  @Override
  public ProdutoResponse criarProduto(ProdutoRequest request) {
    var produto = new Produto();
    produto.setNome(request.getNome());
    produto.setDescricao(request.getDescricao());
    produto.setPreco(request.getPreco());
    produto.setDisponivel(request.getDisponivel() != null ? request.getDisponivel() : true);
    produto.setCategoria(request.getCategoria());

    var produtoSalvo = produtoRepository.save(produto);
    return mapearParaResponse(produtoSalvo);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProdutoResponse> findByRestauranteId(Long restauranteId) {
    return produtoRepository.findByRestauranteId(restauranteId)
        .stream()
        .map(this::mapearParaResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProdutoResponse> findByDisponivelTrue() {
    return produtoRepository.findByDisponivelTrue()
        .stream()
        .map(this::mapearParaResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProdutoResponse> findByCategoria(String categoria) {
    return produtoRepository.findByCategoria(categoria)
        .stream()
        .map(this::mapearParaResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProdutoResponse> findByPrecoLessThanEqual(BigDecimal preco) {
    return produtoRepository.findByPrecoLessThanEqual(preco)
        .stream()
        .map(this::mapearParaResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProdutoMaisVendidoResponse> obterProdutosMaisVendidos() {
    return produtoRepository.obterProdutosMaisVendidos();
  }

  @Override
  @Transactional(readOnly = true)
  public List<FaturamentoPorCategoriaResponse> obterFaturamentoPorCategoria() {
    return produtoRepository.obterFaturamentoPorCategoria();
  }

  private ProdutoResponse mapearParaResponse(Produto produto) {
    return new ProdutoResponse(
        produto.getId(),
        produto.getNome(),
        produto.getDescricao(),
        produto.getPreco(),
        produto.getDisponivel(),
        produto.getCategoria()
    );
  }

}
