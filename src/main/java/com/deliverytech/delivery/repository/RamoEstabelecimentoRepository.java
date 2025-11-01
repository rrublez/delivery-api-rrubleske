package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.RamoEstabelecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para operações de persistência de RamoEstabelecimento.
 * Estende JpaRepository para operações CRUD padrão.
 */
@Repository
public interface RamoEstabelecimentoRepository extends JpaRepository<RamoEstabelecimento, String> {

    /**
     * Busca ramos por nome (case-insensitive).
     *
     * @param nome nome do ramo
     * @return lista de ramos encontrados
     */
    List<RamoEstabelecimento> findByNomeIgnoreCase(String nome);

    /**
     * Busca ramos que contenham a palavra-chave no nome.
     *
     * @param nome palavra-chave
     * @return lista de ramos encontrados
     */
    List<RamoEstabelecimento> findByNomeContainingIgnoreCase(String nome);
}
