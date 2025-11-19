package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.dto.produto.request.AtualizarDisponibilidadeProdutoRequest;
import com.deliverytech.delivery.dto.produto.request.ProdutoRequest;
import com.deliverytech.delivery.dto.produto.response.FaturamentoPorCategoriaResponse;
import com.deliverytech.delivery.dto.produto.response.ProdutoMaisVendidoResponse;
import com.deliverytech.delivery.dto.produto.response.ProdutoResponse;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.security.SecurityUtils;
import com.deliverytech.delivery.service.ProdutoService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("produtoService")
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
  public ProdutoResponse obterPorId(Long id) {
    var produto = produtoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
    return mapearParaResponse(produto);
  }

  @Override
  public ProdutoResponse atualizarProduto(Long id, ProdutoRequest request) {
    var produto = produtoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));

    produto.setNome(request.getNome());
    produto.setDescricao(request.getDescricao());
    produto.setPreco(request.getPreco());
    produto.setDisponivel(request.getDisponivel());
    produto.setCategoria(request.getCategoria());

    var produtoAtualizado = produtoRepository.save(produto);
    return mapearParaResponse(produtoAtualizado);
  }

  @Override
  public void deletarProduto(Long id) {
    var produto = produtoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
    produtoRepository.delete(produto);
  }

  @Override
  public ProdutoResponse atualizarDisponibilidade(Long id, AtualizarDisponibilidadeProdutoRequest request) {
    var produto = produtoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));

    produto.setDisponivel(request.getDisponivel());
    var produtoAtualizado = produtoRepository.save(produto);
    return mapearParaResponse(produtoAtualizado);
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
  public List<ProdutoResponse> findByNome(String nome) {
    return produtoRepository.findByNome(nome)
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

  @Override
  @Transactional(readOnly = true)
  public boolean isOwner(Long produtoId) {
    if (produtoId == null) {
      return false;
    }
    Long restauranteId = SecurityUtils.getCurrentUser()
        .map(usuario -> usuario.getRestauranteId())
        .orElse(null);
    if (restauranteId == null) {
      return false;
    }
    return produtoRepository.findById(produtoId)
        .map(produto -> produto.getRestaurantes()
            .stream()
            .anyMatch(restaurante -> restauranteId.equals(restaurante.getId())))
        .orElse(false);
  }

}
