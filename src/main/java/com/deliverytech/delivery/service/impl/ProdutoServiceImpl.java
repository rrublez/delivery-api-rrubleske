package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.dto.CategoriaProdutoResponseDTO;
import com.deliverytech.delivery.dto.ProdutoRequestDTO;
import com.deliverytech.delivery.dto.ProdutoResponseDTO;
import com.deliverytech.delivery.entity.CategoriaProduto;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.repository.CategoriaProdutoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Produto.
 * Responsável pela lógica de negócio e operações CRUD.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaProdutoRepository categoriaProdutoRepository;

    @Override
    public ProdutoResponseDTO create(ProdutoRequestDTO requestDTO) {
        log.info("Criando novo produto: {}", requestDTO.getNome());

        CategoriaProduto categoria = categoriaProdutoRepository.findById(requestDTO.getCategoriaId())
                .orElseThrow(() -> {
                    log.error("Categoria não encontrada: {}", requestDTO.getCategoriaId());
                    return new IllegalArgumentException("Categoria não encontrada");
                });

        Produto produto = Produto.builder()
                .id(UUID.randomUUID().toString())
                .nome(requestDTO.getNome())
                .descricao(requestDTO.getDescricao())
                .unidade(requestDTO.getUnidade())
                .categoria(categoria)
                .build();

        Produto produtoSalvo = produtoRepository.save(produto);
        log.info("Produto criado com sucesso: {}", produtoSalvo.getId());

        return toResponseDTO(produtoSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoResponseDTO findById(String id) {
        log.info("Buscando produto com ID: {}", id);

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Produto não encontrado: {}", id);
                    return new IllegalArgumentException("Produto não encontrado");
                });

        return toResponseDTO(produto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> findAll() {
        log.info("Buscando todos os produtos");

        return produtoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> findByNome(String nome) {
        log.info("Buscando produtos com nome: {}", nome);

        return produtoRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> findByCategoria(String categoriaId) {
        log.info("Buscando produtos da categoria: {}", categoriaId);

        categoriaProdutoRepository.findById(categoriaId)
                .orElseThrow(() -> {
                    log.error("Categoria não encontrada: {}", categoriaId);
                    return new IllegalArgumentException("Categoria não encontrada");
                });

        return produtoRepository.findByCategoriaId(categoriaId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProdutoResponseDTO update(String id, ProdutoRequestDTO requestDTO) {
        log.info("Atualizando produto com ID: {}", id);

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Produto não encontrado: {}", id);
                    return new IllegalArgumentException("Produto não encontrado");
                });

        CategoriaProduto categoria = categoriaProdutoRepository.findById(requestDTO.getCategoriaId())
                .orElseThrow(() -> {
                    log.error("Categoria não encontrada: {}", requestDTO.getCategoriaId());
                    return new IllegalArgumentException("Categoria não encontrada");
                });

        produto.setNome(requestDTO.getNome());
        produto.setDescricao(requestDTO.getDescricao());
        produto.setUnidade(requestDTO.getUnidade());
        produto.setCategoria(categoria);

        Produto produtoAtualizado = produtoRepository.save(produto);
        log.info("Produto atualizado com sucesso: {}", id);

        return toResponseDTO(produtoAtualizado);
    }

    @Override
    public void delete(String id) {
        log.info("Deletando produto com ID: {}", id);

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Produto não encontrado: {}", id);
                    return new IllegalArgumentException("Produto não encontrado");
                });

        produtoRepository.delete(produto);
        log.info("Produto deletado com sucesso: {}", id);
    }

    /**
     * Converte uma entidade Produto para ProdutoResponseDTO.
     *
     * @param produto entidade a converter
     * @return DTO de resposta
     */
    private ProdutoResponseDTO toResponseDTO(Produto produto) {
        CategoriaProdutoResponseDTO categoriaDTO = CategoriaProdutoResponseDTO.builder()
                .id(produto.getCategoria().getId())
                .categoria(produto.getCategoria().getCategoria())
                .descricao(produto.getCategoria().getDescricao())
                .totalProdutos(0)
                .build();

        return ProdutoResponseDTO.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .descricao(produto.getDescricao())
                .unidade(produto.getUnidade())
                .categoria(categoriaDTO)
                .totalEstabelecimentos(0)
                .build();
    }
}
