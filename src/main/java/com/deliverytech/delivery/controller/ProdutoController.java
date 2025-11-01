package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.ProdutoRequestDTO;
import com.deliverytech.delivery.dto.ProdutoResponseDTO;
import com.deliverytech.delivery.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gerenciar operações relacionadas a Produtos.
 * Expõe endpoints REST para operações CRUD.
 *
 * Base URL: /api/v1/produtos
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    /**
     * Cria um novo produto.
     *
     * @param requestDTO dados do produto a ser criado
     * @return ResponseEntity com o produto criado e status 201
     */
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> create(@Valid @RequestBody ProdutoRequestDTO requestDTO) {
        log.info("POST /api/v1/produtos - Criando novo produto");
        try {
            ProdutoResponseDTO response = produtoService.create(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao criar produto: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Busca um produto por ID.
     *
     * @param id identificador único do produto
     * @return ResponseEntity com o produto encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> findById(@PathVariable String id) {
        log.info("GET /api/v1/produtos/{} - Buscando produto por ID", id);
        try {
            ProdutoResponseDTO response = produtoService.findById(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao buscar produto: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Busca todos os produtos.
     *
     * @return ResponseEntity com lista de todos os produtos
     */
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> findAll() {
        log.info("GET /api/v1/produtos - Buscando todos os produtos");
        List<ProdutoResponseDTO> produtos = produtoService.findAll();
        return ResponseEntity.ok(produtos);
    }

    /**
     * Busca produtos por nome (búsca parcial).
     *
     * @param nome nome ou parte do nome do produto
     * @return ResponseEntity com lista de produtos encontrados
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<ProdutoResponseDTO>> findByNome(@PathVariable String nome) {
        log.info("GET /api/v1/produtos/nome/{} - Buscando produtos por nome", nome);
        List<ProdutoResponseDTO> produtos = produtoService.findByNome(nome);
        return ResponseEntity.ok(produtos);
    }

    /**
     * Busca produtos de uma categoria específica.
     *
     * @param categoriaId ID da categoria
     * @return ResponseEntity com lista de produtos da categoria
     */
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProdutoResponseDTO>> findByCategoria(@PathVariable String categoriaId) {
        log.info("GET /api/v1/produtos/categoria/{} - Buscando produtos por categoria", categoriaId);
        try {
            List<ProdutoResponseDTO> produtos = produtoService.findByCategoria(categoriaId);
            return ResponseEntity.ok(produtos);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao buscar produtos por categoria: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Atualiza um produto existente.
     *
     * @param id identificador do produto
     * @param requestDTO novos dados do produto
     * @return ResponseEntity com o produto atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> update(@PathVariable String id,
                                                     @Valid @RequestBody ProdutoRequestDTO requestDTO) {
        log.info("PUT /api/v1/produtos/{} - Atualizando produto", id);
        try {
            ProdutoResponseDTO response = produtoService.update(id, requestDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao atualizar produto: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Deleta um produto.
     *
     * @param id identificador do produto
     * @return ResponseEntity sem conteúdo (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("DELETE /api/v1/produtos/{} - Deletando produto", id);
        try {
            produtoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Erro ao deletar produto: {}", e.getMessage());
            throw e;
        }
    }
}
