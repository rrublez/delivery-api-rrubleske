package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.CategoriaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para operações de persistência de CategoriaProduto.
 * Estende JpaRepository para operações CRUD padrão.
 */
@Repository
public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, String> {

    /**
     * Busca categorias por nome (case-insensitive).
     *
     * @param categoria nome da categoria
     * @return lista de categorias encontradas
     */
    List<CategoriaProduto> findByCategoriIgnoreCase(String categoria);

    /**
     * Verifica se uma categoria já existe pelo nome.
     *
     * @param categoria nome da categoria
     * @return true se existe, false caso contrário
     */
    boolean existsByCategoriIgnoreCase(String categoria);
}
