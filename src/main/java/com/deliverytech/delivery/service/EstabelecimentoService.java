package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.EstabelecimentoRequestDTO;
import com.deliverytech.delivery.dto.EstabelecimentoResponseDTO;
import java.util.List;

/**
 * Interface de serviço para operações de Estabelecimento.
 * Define contrato para operações CRUD e regras de negócio.
 */
public interface EstabelecimentoService {

    /**
     * Cria um novo estabelecimento.
     *
     * @param requestDTO dados do estabelecimento a ser criado
     * @return estabelecimento criado
     */
    EstabelecimentoResponseDTO create(EstabelecimentoRequestDTO requestDTO);

    /**
     * Busca um estabelecimento por ID.
     *
     * @param id identificador único do estabelecimento
     * @return estabelecimento encontrado
     */
    EstabelecimentoResponseDTO findById(String id);

    /**
     * Busca todos os estabelecimentos.
     *
     * @return lista de todos os estabelecimentos
     */
    List<EstabelecimentoResponseDTO> findAll();

    /**
     * Busca estabelecimentos por nome.
     *
     * @param nome nome do estabelecimento
     * @return lista de estabelecimentos encontrados
     */
    List<EstabelecimentoResponseDTO> findByNome(String nome);

    /**
     * Busca estabelecimentos por ramo.
     *
     * @param ramoId ID do ramo
     * @return lista de estabelecimentos do ramo
     */
    List<EstabelecimentoResponseDTO> findByRamo(String ramoId);

    /**
     * Atualiza um estabelecimento existente.
     *
     * @param id identificador do estabelecimento
     * @param requestDTO novos dados do estabelecimento
     * @return estabelecimento atualizado
     */
    EstabelecimentoResponseDTO update(String id, EstabelecimentoRequestDTO requestDTO);

    /**
     * Deleta um estabelecimento.
     *
     * @param id identificador do estabelecimento
     */
    void delete(String id);

    /**
     * Valida se um CNPJ já existe no sistema.
     *
     * @param cnpj CNPJ a validar
     * @return true se existe, false caso contrário
     */
    boolean cnpjExists(String cnpj);
}
