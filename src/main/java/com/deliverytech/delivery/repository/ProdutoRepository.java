package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para operações de persistência de Produto.
 * Estende JpaRepository para operações CRUD padrão.
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, String> {

    /**
     * Busca produtos por nome (case-insensitive).
     *
     * @param nome nome do produto
     * @return lista de produtos encontrados
     */
    List<Produto> findByNomeIgnoreCase(String nome);

    /**
     * Busca produtos por categoria.
     *
     * @param categoriaId ID da categoria
     * @return lista de produtos da categoria
     */
    List<Produto> findByCategoriaId(String categoriaId);

    /**
     * Busca produtos que contenham a palavra-chave no nome.
     *
     * @param nome palavra-chave
     * @return lista de produtos encontrados
     */
    List<Produto> findByNomeContainingIgnoreCase(String nome);
}
