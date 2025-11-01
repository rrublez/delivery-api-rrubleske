package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.EnderecoRequestDTO;
import com.deliverytech.delivery.dto.EnderecoResponseDTO;
import com.deliverytech.delivery.service.EnderecoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gerenciar operações relacionadas a Endereços.
 * Expõe endpoints REST para operações CRUD.
 *
 * Base URL: /api/v1/enderecos
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/enderecos")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService enderecoService;

    /**
     * Cria um novo endereço.
     *
     * @param requestDTO dados do endereço a ser criado
     * @return ResponseEntity com o endereço criado e status 201
     */
    @PostMapping
    public ResponseEntity<EnderecoResponseDTO> create(@Valid @RequestBody EnderecoRequestDTO requestDTO) {
        log.info("POST /api/v1/enderecos - Criando novo endereço");
        try {
            EnderecoResponseDTO response = enderecoService.create(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao criar endereço: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Busca um endereço por ID.
     *
     * @param id identificador único do endereço
     * @return ResponseEntity com o endereço encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> findById(@PathVariable String id) {
        log.info("GET /api/v1/enderecos/{} - Buscando endereço por ID", id);
        try {
            EnderecoResponseDTO response = enderecoService.findById(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao buscar endereço: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Busca todos os endereços.
     *
     * @return ResponseEntity com lista de todos os endereços
     */
    @GetMapping
    public ResponseEntity<List<EnderecoResponseDTO>> findAll() {
        log.info("GET /api/v1/enderecos - Buscando todos os endereços");
        List<EnderecoResponseDTO> enderecos = enderecoService.findAll();
        return ResponseEntity.ok(enderecos);
    }

    /**
     * Busca endereços por cidade.
     *
     * @param cidade nome da cidade
     * @return ResponseEntity com lista de endereços da cidade
     */
    @GetMapping("/cidade/{cidade}")
    public ResponseEntity<List<EnderecoResponseDTO>> findByCidade(@PathVariable String cidade) {
        log.info("GET /api/v1/enderecos/cidade/{} - Buscando endereços por cidade", cidade);
        List<EnderecoResponseDTO> enderecos = enderecoService.findByCidade(cidade);
        return ResponseEntity.ok(enderecos);
    }

    /**
     * Busca endereços por CEP.
     *
     * @param cep CEP do endereço
     * @return ResponseEntity com lista de endereços com o CEP
     */
    @GetMapping("/cep/{cep}")
    public ResponseEntity<List<EnderecoResponseDTO>> findByCep(@PathVariable String cep) {
        log.info("GET /api/v1/enderecos/cep/{} - Buscando endereços por CEP", cep);
        List<EnderecoResponseDTO> enderecos = enderecoService.findByCep(cep);
        return ResponseEntity.ok(enderecos);
    }

    /**
     * Atualiza um endereço existente.
     *
     * @param id identificador do endereço
     * @param requestDTO novos dados do endereço
     * @return ResponseEntity com o endereço atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> update(@PathVariable String id, 
                                                      @Valid @RequestBody EnderecoRequestDTO requestDTO) {
        log.info("PUT /api/v1/enderecos/{} - Atualizando endereço", id);
        try {
            EnderecoResponseDTO response = enderecoService.update(id, requestDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao atualizar endereço: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Deleta um endereço.
     *
     * @param id identificador do endereço
     * @return ResponseEntity sem conteúdo (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("DELETE /api/v1/enderecos/{} - Deletando endereço", id);
        try {
            enderecoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Erro ao deletar endereço: {}", e.getMessage());
            throw e;
        }
    }
}
