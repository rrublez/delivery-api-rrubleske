package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.cliente.request.ClienteRequest;
import com.deliverytech.delivery.dto.cliente.request.ClienteUpdateRequest;
import com.deliverytech.delivery.dto.cliente.response.ClienteResponse;
import com.deliverytech.delivery.dto.shared.response.RankingClienteResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {

  ClienteResponse criarCliente(ClienteRequest request);

  ClienteResponse findById(Long id);

  Page<ClienteResponse> findAll(Pageable pageable);

  List<ClienteResponse> findByEmail(String email);

  ClienteResponse findByAtivoTrue();

  List<ClienteResponse> findByNomeContainingIgnoreCase(String nome);

  boolean existsByEmail(String email);

  List<RankingClienteResponse> obterRankingClientesPorNumeroPedidos();

  ClienteResponse atualizarCliente(Long id, ClienteUpdateRequest request);

}
