package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.EstabelecimentoRequestDTO;
import com.deliverytech.delivery.dto.EstabelecimentoResponseDTO;
import com.deliverytech.delivery.service.EstabelecimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gerenciar operações relacionadas a Estabelecimentos.
 * Expõe endpoints REST para operações CRUD.
 *
 * Base URL: /api/v1/estabelecimentos
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/estabelecimentos")
@RequiredArgsConstructor
public class EstabelecimentoController {

    private final EstabelecimentoService estabelecimentoService;

    /**
     * Cria um novo estabelecimento.
     *
     * @param requestDTO dados do estabelecimento a ser criado
     * @return ResponseEntity com o estabelecimento criado e status 201
     */
    @PostMapping
    public ResponseEntity<EstabelecimentoResponseDTO> create(@Valid @RequestBody EstabelecimentoRequestDTO requestDTO) {
        log.info("POST /api/v1/estabelecimentos - Criando novo estabelecimento");
        try {
            EstabelecimentoResponseDTO response = estabelecimentoService.create(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao criar estabelecimento: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Busca um estabelecimento por ID.
     *
     * @param id identificador único do estabelecimento
     * @return ResponseEntity com o estabelecimento encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponseDTO> findById(@PathVariable String id) {
        log.info("GET /api/v1/estabelecimentos/{} - Buscando estabelecimento por ID", id);
        try {
            EstabelecimentoResponseDTO response = estabelecimentoService.findById(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao buscar estabelecimento: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Busca todos os estabelecimentos.
     *
     * @return ResponseEntity com lista de todos os estabelecimentos
     */
    @GetMapping
    public ResponseEntity<List<EstabelecimentoResponseDTO>> findAll() {
        log.info("GET /api/v1/estabelecimentos - Buscando todos os estabelecimentos");
        List<EstabelecimentoResponseDTO> estabelecimentos = estabelecimentoService.findAll();
        return ResponseEntity.ok(estabelecimentos);
    }

    /**
     * Busca estabelecimentos por nome (busca parcial).
     *
     * @param nome nome ou parte do nome do estabelecimento
     * @return ResponseEntity com lista de estabelecimentos encontrados
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<EstabelecimentoResponseDTO>> findByNome(@PathVariable String nome) {
        log.info("GET /api/v1/estabelecimentos/nome/{} - Buscando estabelecimentos por nome", nome);
        List<EstabelecimentoResponseDTO> estabelecimentos = estabelecimentoService.findByNome(nome);
        return ResponseEntity.ok(estabelecimentos);
    }

    /**
     * Busca estabelecimentos de um ramo específico.
     *
     * @param ramoId ID do ramo
     * @return ResponseEntity com lista de estabelecimentos do ramo
     */
    @GetMapping("/ramo/{ramoId}")
    public ResponseEntity<List<EstabelecimentoResponseDTO>> findByRamo(@PathVariable String ramoId) {
        log.info("GET /api/v1/estabelecimentos/ramo/{} - Buscando estabelecimentos por ramo", ramoId);
        try {
            List<EstabelecimentoResponseDTO> estabelecimentos = estabelecimentoService.findByRamo(ramoId);
            return ResponseEntity.ok(estabelecimentos);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao buscar estabelecimentos por ramo: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Atualiza um estabelecimento existente.
     *
     * @param id identificador do estabelecimento
     * @param requestDTO novos dados do estabelecimento
     * @return ResponseEntity com o estabelecimento atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponseDTO> update(@PathVariable String id,
                                                             @Valid @RequestBody EstabelecimentoRequestDTO requestDTO) {
        log.info("PUT /api/v1/estabelecimentos/{} - Atualizando estabelecimento", id);
        try {
            EstabelecimentoResponseDTO response = estabelecimentoService.update(id, requestDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao atualizar estabelecimento: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Deleta um estabelecimento.
     *
     * @param id identificador do estabelecimento
     * @return ResponseEntity sem conteúdo (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("DELETE /api/v1/estabelecimentos/{} - Deletando estabelecimento", id);
        try {
            estabelecimentoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Erro ao deletar estabelecimento: {}", e.getMessage());
            throw e;
        }
    }
}
