package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.CategoriaProdutoRequestDTO;
import com.deliverytech.delivery.dto.CategoriaProdutoResponseDTO;
import java.util.List;

/**
 * Interface de serviço para operações de Categoria de Produto.
 * Define contrato para operações CRUD e regras de negócio.
 */
public interface CategoriaProdutoService {

    /**
     * Cria uma nova categoria de produto.
     *
     * @param requestDTO dados da categoria a ser criada
     * @return categoria criada
     */
    CategoriaProdutoResponseDTO create(CategoriaProdutoRequestDTO requestDTO);

    /**
     * Busca uma categoria de produto por ID.
     *
     * @param id identificador único da categoria
     * @return categoria encontrada
     */
    CategoriaProdutoResponseDTO findById(String id);

    /**
     * Busca todas as categorias de produto.
     *
     * @return lista de todas as categorias
     */
    List<CategoriaProdutoResponseDTO> findAll();

    /**
     * Busca categorias de produto por nome.
     *
     * @param categoria nome da categoria
     * @return lista de categorias encontradas
     */
    List<CategoriaProdutoResponseDTO> findByCategoria(String categoria);

    /**
     * Atualiza uma categoria de produto existente.
     *
     * @param id identificador da categoria
     * @param requestDTO novos dados da categoria
     * @return categoria atualizada
     */
    CategoriaProdutoResponseDTO update(String id, CategoriaProdutoRequestDTO requestDTO);

    /**
     * Deleta uma categoria de produto.
     *
     * @param id identificador da categoria
     */
    void delete(String id);

    /**
     * Valida se uma categoria já existe pelo nome.
     *
     * @param categoria nome da categoria
     * @return true se existe, false caso contrário
     */
    boolean categoriaExists(String categoria);
}
