package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.CategoriaProdutoRequestDTO;
import com.deliverytech.delivery.dto.CategoriaProdutoResponseDTO;
import com.deliverytech.delivery.service.CategoriaProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gerenciar operações relacionadas a Categorias de Produto.
 * Expõe endpoints REST para operações CRUD.
 *
 * Base URL: /api/v1/categorias-produto
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/categorias-produto")
@RequiredArgsConstructor
public class CategoriaProdutoController {

    private final CategoriaProdutoService categoriaProdutoService;

    /**
     * Cria uma nova categoria de produto.
     *
     * @param requestDTO dados da categoria a ser criada
     * @return ResponseEntity com a categoria criada e status 201
     */
    @PostMapping
    public ResponseEntity<CategoriaProdutoResponseDTO> create(@Valid @RequestBody CategoriaProdutoRequestDTO requestDTO) {
        log.info("POST /api/v1/categorias-produto - Criando nova categoria");
        try {
            CategoriaProdutoResponseDTO response = categoriaProdutoService.create(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao criar categoria: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Busca uma categoria de produto por ID.
     *
     * @param id identificador único da categoria
     * @return ResponseEntity com a categoria encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaProdutoResponseDTO> findById(@PathVariable String id) {
        log.info("GET /api/v1/categorias-produto/{} - Buscando categoria por ID", id);
        try {
            CategoriaProdutoResponseDTO response = categoriaProdutoService.findById(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao buscar categoria: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Busca todas as categorias de produto.
     *
     * @return ResponseEntity com lista de todas as categorias
     */
    @GetMapping
    public ResponseEntity<List<CategoriaProdutoResponseDTO>> findAll() {
        log.info("GET /api/v1/categorias-produto - Buscando todas as categorias");
        List<CategoriaProdutoResponseDTO> categorias = categoriaProdutoService.findAll();
        return ResponseEntity.ok(categorias);
    }

    /**
     * Busca categorias por nome.
     *
     * @param categoria nome da categoria
     * @return ResponseEntity com lista de categorias encontradas
     */
    @GetMapping("/nome/{categoria}")
    public ResponseEntity<List<CategoriaProdutoResponseDTO>> findByCategoria(@PathVariable String categoria) {
        log.info("GET /api/v1/categorias-produto/nome/{} - Buscando categorias por nome", categoria);
        List<CategoriaProdutoResponseDTO> categorias = categoriaProdutoService.findByCategoria(categoria);
        return ResponseEntity.ok(categorias);
    }

    /**
     * Atualiza uma categoria de produto existente.
     *
     * @param id identificador da categoria
     * @param requestDTO novos dados da categoria
     * @return ResponseEntity com a categoria atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaProdutoResponseDTO> update(@PathVariable String id,
                                                              @Valid @RequestBody CategoriaProdutoRequestDTO requestDTO) {
        log.info("PUT /api/v1/categorias-produto/{} - Atualizando categoria", id);
        try {
            CategoriaProdutoResponseDTO response = categoriaProdutoService.update(id, requestDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao atualizar categoria: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Deleta uma categoria de produto.
     *
     * @param id identificador da categoria
     * @return ResponseEntity sem conteúdo (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("DELETE /api/v1/categorias-produto/{} - Deletando categoria", id);
        try {
            categoriaProdutoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Erro ao deletar categoria: {}", e.getMessage());
            throw e;
        }
    }
}
