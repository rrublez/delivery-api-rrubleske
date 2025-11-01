package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.RamoEstabelecimentoRequestDTO;
import com.deliverytech.delivery.dto.RamoEstabelecimentoResponseDTO;
import java.util.List;

/**
 * Interface de serviço para operações de Ramo (Ramo do Estabelecimento).
 * Define contrato para operações CRUD e regras de negócio.
 */
public interface RamoEstabelecimentoService {

    /**
     * Cria um novo ramo.
     *
     * @param requestDTO dados do ramo a ser criado
     * @return ramo criado
     */
    RamoEstabelecimentoResponseDTO create(RamoEstabelecimentoRequestDTO requestDTO);

    /**
     * Busca um ramo por ID.
     *
     * @param id identificador único do ramo
     * @return ramo encontrado
     */
    RamoEstabelecimentoResponseDTO findById(String id);

    /**
     * Busca todos os ramos.
     *
     * @return lista de todos os ramos
     */
    List<RamoEstabelecimentoResponseDTO> findAll();

    /**
     * Busca ramos por nome.
     *
     * @param nome nome do ramo
     * @return lista de ramos encontrados
     */
    List<RamoEstabelecimentoResponseDTO> findByNome(String nome);

    /**
     * Atualiza um ramo existente.
     *
     * @param id identificador do ramo
     * @param requestDTO novos dados do ramo
     * @return ramo atualizado
     */
    RamoEstabelecimentoResponseDTO update(String id, RamoEstabelecimentoRequestDTO requestDTO);

    /**
     * Deleta um ramo.
     *
     * @param id identificador do ramo
     */
    void delete(String id);
}
