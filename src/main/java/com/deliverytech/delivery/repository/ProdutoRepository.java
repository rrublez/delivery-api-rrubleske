package com.deliverytech.delivery.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.deliverytech.delivery.dto.produto.response.FaturamentoPorCategoriaResponse;
import com.deliverytech.delivery.dto.produto.response.ProdutoMaisVendidoResponse;
import com.deliverytech.delivery.entity.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

  @Query("SELECT p FROM Produto p JOIN p.restaurantes r WHERE r.id = :restauranteId")
  List<Produto> findByRestauranteId(@Param("restauranteId") Long restauranteId);

  List<Produto> findByDisponivelTrue();

  List<Produto> findByCategoria(String categoria);

  List<Produto> findByPrecoLessThanEqual(BigDecimal preco);

  // Buscar produtos por nome (LIKE case-insensitive)
  @Query("SELECT p FROM Produto p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
  List<Produto> findByNome(@Param("nome") String nome);

  // Query: Produtos mais vendidos
  @Query("SELECT NEW com.deliverytech.delivery.dto.produto.response.ProdutoMaisVendidoResponse("
      + "p.id, p.nome, p.categoria, COALESCE(SUM(pp.quantidade), 0L), COALESCE(SUM(pp.subtotal), 0.0)) "
      + "FROM Produto p "
      + "LEFT JOIN PedidoProduto pp ON pp.produto.id = p.id "
      + "GROUP BY p.id, p.nome, p.categoria "
      + "ORDER BY SUM(pp.quantidade) DESC, SUM(pp.subtotal) DESC")
  List<ProdutoMaisVendidoResponse> obterProdutosMaisVendidos();

  // Query: Faturamento por categoria
  @Query("SELECT NEW com.deliverytech.delivery.dto.produto.response.FaturamentoPorCategoriaResponse("
      + "p.categoria, COUNT(DISTINCT p.id), COALESCE(SUM(pp.quantidade), 0L), COALESCE(SUM(pp.subtotal), 0.0)) "
      + "FROM Produto p "
      + "LEFT JOIN PedidoProduto pp ON pp.produto.id = p.id "
      + "GROUP BY p.categoria "
      + "ORDER BY SUM(pp.subtotal) DESC")
  List<FaturamentoPorCategoriaResponse> obterFaturamentoPorCategoria();

}
