package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.EnderecoRequestDTO;
import com.deliverytech.delivery.dto.EnderecoResponseDTO;
import java.util.List;

/**
 * Interface de serviço para operações de Endereço.
 * Define contrato para operações CRUD e regras de negócio.
 */
public interface EnderecoService {

    /**
     * Cria um novo endereço.
     *
     * @param requestDTO dados do endereço a ser criado
     * @return endereço criado
     */
    EnderecoResponseDTO create(EnderecoRequestDTO requestDTO);

    /**
     * Busca um endereço por ID.
     *
     * @param id identificador único do endereço
     * @return endereço encontrado
     */
    EnderecoResponseDTO findById(String id);

    /**
     * Busca todos os endereços.
     *
     * @return lista de todos os endereços
     */
    List<EnderecoResponseDTO> findAll();

    /**
     * Busca endereços por cidade.
     *
     * @param cidade cidade do endereço
     * @return lista de endereços da cidade
     */
    List<EnderecoResponseDTO> findByCidade(String cidade);

    /**
     * Busca endereços por CEP.
     *
     * @param cep CEP do endereço
     * @return lista de endereços com o CEP
     */
    List<EnderecoResponseDTO> findByCep(String cep);

    /**
     * Atualiza um endereço existente.
     *
     * @param id identificador do endereço
     * @param requestDTO novos dados do endereço
     * @return endereço atualizado
     */
    EnderecoResponseDTO update(String id, EnderecoRequestDTO requestDTO);

    /**
     * Deleta um endereço.
     *
     * @param id identificador do endereço
     */
    void delete(String id);

    /**
     * Valida se um CEP existe no sistema.
     *
     * @param cep CEP a validar
     * @return true se existe, false caso contrário
     */
    boolean cepExists(String cep);
}
