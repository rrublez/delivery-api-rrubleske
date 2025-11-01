package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.RamoEstabelecimentoRequestDTO;
import com.deliverytech.delivery.dto.RamoEstabelecimentoResponseDTO;
import com.deliverytech.delivery.service.RamoEstabelecimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gerenciar operações relacionadas a Ramos de Estabelecimento.
 * Expõe endpoints REST para operações CRUD.
 *
 * Base URL: /api/v1/ramos
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/ramos")
@RequiredArgsConstructor
public class RamoEstabelecimentoController {

    private final RamoEstabelecimentoService ramoService;

    /**
     * Cria um novo ramo.
     *
     * @param requestDTO dados do ramo a ser criado
     * @return ResponseEntity com o ramo criado e status 201
     */
    @PostMapping
    public ResponseEntity<RamoEstabelecimentoResponseDTO> create(@Valid @RequestBody RamoEstabelecimentoRequestDTO requestDTO) {
        log.info("POST /api/v1/ramos - Criando novo ramo");
        try {
            RamoEstabelecimentoResponseDTO response = ramoService.create(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao criar ramo: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Busca um ramo por ID.
     *
     * @param id identificador único do ramo
     * @return ResponseEntity com o ramo encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<RamoEstabelecimentoResponseDTO> findById(@PathVariable String id) {
        log.info("GET /api/v1/ramos/{} - Buscando ramo por ID", id);
        try {
            RamoEstabelecimentoResponseDTO response = ramoService.findById(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao buscar ramo: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Busca todos os ramos.
     *
     * @return ResponseEntity com lista de todos os ramos
     */
    @GetMapping
    public ResponseEntity<List<RamoEstabelecimentoResponseDTO>> findAll() {
        log.info("GET /api/v1/ramos - Buscando todos os ramos");
        List<RamoEstabelecimentoResponseDTO> ramos = ramoService.findAll();
        return ResponseEntity.ok(ramos);
    }

    /**
     * Busca ramos por nome (busca parcial).
     *
     * @param nome nome ou parte do nome do ramo
     * @return ResponseEntity com lista de ramos encontrados
     */
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<RamoEstabelecimentoResponseDTO>> findByNome(@PathVariable String nome) {
        log.info("GET /api/v1/ramos/nome/{} - Buscando ramos por nome", nome);
        List<RamoEstabelecimentoResponseDTO> ramos = ramoService.findByNome(nome);
        return ResponseEntity.ok(ramos);
    }

    /**
     * Atualiza um ramo existente.
     *
     * @param id identificador do ramo
     * @param requestDTO novos dados do ramo
     * @return ResponseEntity com o ramo atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<RamoEstabelecimentoResponseDTO> update(@PathVariable String id,
                                                                 @Valid @RequestBody RamoEstabelecimentoRequestDTO requestDTO) {
        log.info("PUT /api/v1/ramos/{} - Atualizando ramo", id);
        try {
            RamoEstabelecimentoResponseDTO response = ramoService.update(id, requestDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao atualizar ramo: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Deleta um ramo.
     *
     * @param id identificador do ramo
     * @return ResponseEntity sem conteúdo (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("DELETE /api/v1/ramos/{} - Deletando ramo", id);
        try {
            ramoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Erro ao deletar ramo: {}", e.getMessage());
            throw e;
        }
    }
}
