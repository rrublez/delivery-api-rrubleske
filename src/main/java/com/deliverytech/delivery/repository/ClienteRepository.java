package com.deliverytech.delivery.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.deliverytech.delivery.dto.response.RankingClienteResponse;
import com.deliverytech.delivery.entity.Cliente;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByEmail(String email);

    Cliente findByAtivoTrue();

    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByCpfAndIdNot(String cpf, Long id);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    // Query: Ranking de clientes por número de pedidos
    @Query("SELECT NEW com.deliverytech.delivery.dto.response.RankingClienteResponse("
        + "c.id, c.nome, c.email, COUNT(p.id)) "
        + "FROM Cliente c "
        + "LEFT JOIN c.pedidos p "
        + "GROUP BY c.id, c.nome, c.email "
        + "ORDER BY COUNT(p.id) DESC")
    List<RankingClienteResponse> obterRankingClientesPorNumeroPedidos();

}
