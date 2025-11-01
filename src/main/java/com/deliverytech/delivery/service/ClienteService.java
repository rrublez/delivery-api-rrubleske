package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ClienteRequestDTO;
import com.deliverytech.delivery.dto.ClienteResponseDTO;
import java.util.List;

/**
 * Interface de serviço para operações de Cliente.
 * Define contrato para operações CRUD e regras de negócio.
 */
public interface ClienteService {

    /**
     * Cria um novo cliente.
     *
     * @param requestDTO dados do cliente a ser criado
     * @return cliente criado
     */
    ClienteResponseDTO create(ClienteRequestDTO requestDTO);

    /**
     * Busca um cliente por ID.
     *
     * @param id identificador único do cliente
     * @return cliente encontrado
     */
    ClienteResponseDTO findById(String id);

    /**
     * Busca todos os clientes.
     *
     * @return lista de todos os clientes
     */
    List<ClienteResponseDTO> findAll();

    /**
     * Busca clientes por email.
     *
     * @param email email do cliente
     * @return cliente encontrado
     */
    ClienteResponseDTO findByEmail(String email);

    /**
     * Atualiza um cliente existente.
     *
     * @param id identificador do cliente
     * @param requestDTO novos dados do cliente
     * @return cliente atualizado
     */
    ClienteResponseDTO update(String id, ClienteRequestDTO requestDTO);

    /**
     * Deleta um cliente.
     *
     * @param id identificador do cliente
     */
    void delete(String id);

    /**
     * Valida se um email já existe no sistema.
     *
     * @param email email a validar
     * @return true se existe, false caso contrário
     */
    boolean emailExists(String email);

    /**
     * Valida se um documento de identidade já existe.
     *
     * @param documento documento a validar
     * @return true se existe, false caso contrário
     */
    boolean documentoExists(String documento);
}
