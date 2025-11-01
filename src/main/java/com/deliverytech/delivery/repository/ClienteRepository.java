package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para operações de persistência de Cliente.
 * Estende JpaRepository para operações CRUD padrão.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {

    /**
     * Busca um cliente por email.
     *
     * @param email email do cliente
     * @return Optional contendo o cliente se encontrado
     */
    Optional<Cliente> findByEmail(String email);

    /**
     * Verifica se existe um cliente com o email informado.
     *
     * @param email email a verificar
     * @return true se existe, false caso contrário
     */
    boolean existsByEmail(String email);

    /**
     * Verifica se existe um cliente com o documento informado.
     *
     * @param documentoIdentificacao documento a verificar
     * @return true se existe, false caso contrário
     */
    boolean existsByDocumentoIdentificacao(String documentoIdentificacao);
}
