package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.cliente.request.ClienteRequest;
import com.deliverytech.delivery.dto.cliente.response.ClienteResponse;
import com.deliverytech.delivery.dto.shared.response.RankingClienteResponse;
import java.util.List;

public interface ClienteService {

  ClienteResponse criarCliente(ClienteRequest request);

  List<ClienteResponse> findByEmail(String email);

  ClienteResponse findByAtivoTrue();

  List<ClienteResponse> findByNomeContainingIgnoreCase(String nome);

  boolean existsByEmail(String email);

  List<RankingClienteResponse> obterRankingClientesPorNumeroPedidos();

}
