package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para operações de persistência de Endereço.
 * Estende JpaRepository para operações CRUD padrão.
 */
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, String> {

    /**
     * Busca endereços por cidade.
     *
     * @param cidade nome da cidade
     * @return lista de endereços encontrados
     */
    List<Endereco> findByCidade(String cidade);

    /**
     * Busca endereços por CEP.
     *
     * @param cep CEP do endereço
     * @return lista de endereços encontrados
     */
    List<Endereco> findByCep(String cep);

    /**
     * Verifica se existe um endereço com o CEP informado.
     *
     * @param cep CEP a verificar
     * @return true se existe, false caso contrário
     */
    boolean existsByCep(String cep);
}
