package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Estabelecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para operações de persistência de Estabelecimento.
 * Estende JpaRepository para operações CRUD padrão.
 */
@Repository
public interface EstabelecimentoRepository extends JpaRepository<Estabelecimento, String> {

    /**
     * Busca estabelecimentos por nome (case-insensitive).
     *
     * @param nome nome do estabelecimento
     * @return lista de estabelecimentos encontrados
     */
    List<Estabelecimento> findByNomeIgnoreCase(String nome);

    /**
     * Busca estabelecimentos por ramo.
     *
     * @param ramoId ID do ramo
     * @return lista de estabelecimentos do ramo
     */
    List<Estabelecimento> findByRamoId(String ramoId);

    /**
     * Busca estabelecimentos que contenham a palavra-chave no nome.
     *
     * @param nome palavra-chave
     * @return lista de estabelecimentos encontrados
     */
    List<Estabelecimento> findByNomeContainingIgnoreCase(String nome);

    /**
     * Verifica se um estabelecimento com o CNPJ já existe.
     *
     * @param cnpj CNPJ do estabelecimento
     * @return true se existe, false caso contrário
     */
    boolean existsByCnpj(String cnpj);
}
