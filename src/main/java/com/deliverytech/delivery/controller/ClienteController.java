package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.ClienteRequestDTO;
import com.deliverytech.delivery.dto.ClienteResponseDTO;
import com.deliverytech.delivery.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gerenciar operações relacionadas a Clientes.
 * Expõe endpoints REST para operações CRUD.
 *
 * Base URL: /api/v1/clientes
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * Cria um novo cliente.
     *
     * @param requestDTO dados do cliente a ser criado
     * @return ResponseEntity com o cliente criado e status 201
     */
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> create(@Valid @RequestBody ClienteRequestDTO requestDTO) {
        log.info("POST /api/v1/clientes - Criando novo cliente");
        try {
            ClienteResponseDTO response = clienteService.create(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao criar cliente: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Busca um cliente por ID.
     *
     * @param id identificador único do cliente
     * @return ResponseEntity com o cliente encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> findById(@PathVariable String id) {
        log.info("GET /api/v1/clientes/{} - Buscando cliente por ID", id);
        try {
            ClienteResponseDTO response = clienteService.findById(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao buscar cliente: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Busca todos os clientes.
     *
     * @return ResponseEntity com lista de todos os clientes
     */
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> findAll() {
        log.info("GET /api/v1/clientes - Buscando todos os clientes");
        List<ClienteResponseDTO> clientes = clienteService.findAll();
        return ResponseEntity.ok(clientes);
    }

    /**
     * Busca um cliente por email.
     *
     * @param email email do cliente
     * @return ResponseEntity com o cliente encontrado
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteResponseDTO> findByEmail(@PathVariable String email) {
        log.info("GET /api/v1/clientes/email/{} - Buscando cliente por email", email);
        try {
            ClienteResponseDTO response = clienteService.findByEmail(email);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao buscar cliente por email: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Atualiza um cliente existente.
     *
     * @param id identificador do cliente
     * @param requestDTO novos dados do cliente
     * @return ResponseEntity com o cliente atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> update(@PathVariable String id, 
                                                     @Valid @RequestBody ClienteRequestDTO requestDTO) {
        log.info("PUT /api/v1/clientes/{} - Atualizando cliente", id);
        try {
            ClienteResponseDTO response = clienteService.update(id, requestDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao atualizar cliente: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Deleta um cliente.
     *
     * @param id identificador do cliente
     * @return ResponseEntity sem conteúdo (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("DELETE /api/v1/clientes/{} - Deletando cliente", id);
        try {
            clienteService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Erro ao deletar cliente: {}", e.getMessage());
            throw e;
        }
    }
}
