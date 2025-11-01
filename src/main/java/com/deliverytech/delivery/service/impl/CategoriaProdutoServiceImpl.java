package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.dto.CategoriaProdutoRequestDTO;
import com.deliverytech.delivery.dto.CategoriaProdutoResponseDTO;
import com.deliverytech.delivery.entity.CategoriaProduto;
import com.deliverytech.delivery.repository.CategoriaProdutoRepository;
import com.deliverytech.delivery.service.CategoriaProdutoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de CategoriaProduto.
 * Responsável pela lógica de negócio e operações CRUD.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaProdutoServiceImpl implements CategoriaProdutoService {

    private final CategoriaProdutoRepository categoriaProdutoRepository;

    @Override
    public CategoriaProdutoResponseDTO create(CategoriaProdutoRequestDTO requestDTO) {
        log.info("Criando nova categoria: {}", requestDTO.getCategoria());

        if (categoriaProdutoRepository.existsByCategoriIgnoreCase(requestDTO.getCategoria())) {
            log.error("Categoria já existe: {}", requestDTO.getCategoria());
            throw new IllegalArgumentException("Categoria já existe no sistema");
        }

        CategoriaProduto categoria = CategoriaProduto.builder()
                .id(UUID.randomUUID().toString())
                .categoria(requestDTO.getCategoria())
                .descricao(requestDTO.getDescricao())
                .build();

        CategoriaProduto categoriaSalva = categoriaProdutoRepository.save(categoria);
        log.info("Categoria criada com sucesso: {}", categoriaSalva.getId());

        return toResponseDTO(categoriaSalva);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaProdutoResponseDTO findById(String id) {
        log.info("Buscando categoria com ID: {}", id);

        CategoriaProduto categoria = categoriaProdutoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Categoria não encontrada: {}", id);
                    return new IllegalArgumentException("Categoria não encontrada");
                });

        return toResponseDTO(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaProdutoResponseDTO> findAll() {
        log.info("Buscando todas as categorias");

        return categoriaProdutoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaProdutoResponseDTO> findByCategoria(String categoria) {
        log.info("Buscando categorias com nome: {}", categoria);

        return categoriaProdutoRepository.findByCategoriIgnoreCase(categoria)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoriaProdutoResponseDTO update(String id, CategoriaProdutoRequestDTO requestDTO) {
        log.info("Atualizando categoria com ID: {}", id);

        CategoriaProduto categoria = categoriaProdutoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Categoria não encontrada: {}", id);
                    return new IllegalArgumentException("Categoria não encontrada");
                });

        // Validar se novo nome já existe (e não é ele mesmo)
        if (!categoria.getCategoria().equals(requestDTO.getCategoria()) &&
                categoriaProdutoRepository.existsByCategoriIgnoreCase(requestDTO.getCategoria())) {
            log.error("Categoria já existe: {}", requestDTO.getCategoria());
            throw new IllegalArgumentException("Categoria já existe no sistema");
        }

        categoria.setCategoria(requestDTO.getCategoria());
        categoria.setDescricao(requestDTO.getDescricao());

        CategoriaProduto categoriaAtualizada = categoriaProdutoRepository.save(categoria);
        log.info("Categoria atualizada com sucesso: {}", id);

        return toResponseDTO(categoriaAtualizada);
    }

    @Override
    public void delete(String id) {
        log.info("Deletando categoria com ID: {}", id);

        CategoriaProduto categoria = categoriaProdutoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Categoria não encontrada: {}", id);
                    return new IllegalArgumentException("Categoria não encontrada");
                });

        categoriaProdutoRepository.delete(categoria);
        log.info("Categoria deletada com sucesso: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean categoriaExists(String categoria) {
        return categoriaProdutoRepository.existsByCategoriIgnoreCase(categoria);
    }

    /**
     * Converte uma entidade CategoriaProduto para CategoriaProdutoResponseDTO.
     *
     * @param categoria entidade a converter
     * @return DTO de resposta
     */
    private CategoriaProdutoResponseDTO toResponseDTO(CategoriaProduto categoria) {
        return CategoriaProdutoResponseDTO.builder()
                .id(categoria.getId())
                .categoria(categoria.getCategoria())
                .descricao(categoria.getDescricao())
                .totalProdutos(0)
                .build();
    }
}
