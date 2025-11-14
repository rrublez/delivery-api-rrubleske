package com.deliverytech.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.deliverytech.delivery.entity.Restaurante;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    List<Restaurante> findByRamoAtividade(String ramoAtividade);

    List<Restaurante> findByAtivoTrue();

    List<Restaurante> findByTaxaEntregaLessThanEqual(BigDecimal taxa);

    List<Restaurante> findByRamoAtividadeAndAtivoTrue(String ramoAtividade);

    List<Restaurante> findByAtivoTrueAndTaxaEntregaLessThanEqual(BigDecimal taxa);

    Optional<Restaurante> findByIdAndAtivoTrue(Long id);

    // Métodos para verificar duplicidades
    boolean existsByCnpj(String cnpj);

    boolean existsByTelefone(String telefone);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByCnpjAndIdNot(String cnpj, Long id);

    boolean existsByTelefoneAndIdNot(String telefone, Long id);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

}

