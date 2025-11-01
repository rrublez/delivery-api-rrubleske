package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ProdutoRequestDTO;
import com.deliverytech.delivery.dto.ProdutoResponseDTO;
import java.util.List;

/**
 * Interface de serviço para operações de Produto.
 * Define contrato para operações CRUD e regras de negócio.
 */
public interface ProdutoService {

    /**
     * Cria um novo produto.
     *
     * @param requestDTO dados do produto a ser criado
     * @return produto criado
     */
    ProdutoResponseDTO create(ProdutoRequestDTO requestDTO);

    /**
     * Busca um produto por ID.
     *
     * @param id identificador único do produto
     * @return produto encontrado
     */
    ProdutoResponseDTO findById(String id);

    /**
     * Busca todos os produtos.
     *
     * @return lista de todos os produtos
     */
    List<ProdutoResponseDTO> findAll();

    /**
     * Busca produtos por nome.
     *
     * @param nome nome do produto
     * @return lista de produtos encontrados
     */
    List<ProdutoResponseDTO> findByNome(String nome);

    /**
     * Busca produtos por categoria.
     *
     * @param categoriaId ID da categoria
     * @return lista de produtos da categoria
     */
    List<ProdutoResponseDTO> findByCategoria(String categoriaId);

    /**
     * Atualiza um produto existente.
     *
     * @param id identificador do produto
     * @param requestDTO novos dados do produto
     * @return produto atualizado
     */
    ProdutoResponseDTO update(String id, ProdutoRequestDTO requestDTO);

    /**
     * Deleta um produto.
     *
     * @param id identificador do produto
     */
    void delete(String id);
}
