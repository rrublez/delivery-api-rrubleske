package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.dto.request.ClienteRequest;
import com.deliverytech.delivery.dto.response.ClienteResponse;
import com.deliverytech.delivery.dto.response.RankingClienteResponse;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.service.ClienteService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

  private final ClienteRepository clienteRepository;

  public ClienteServiceImpl(ClienteRepository clienteRepository) {
    this.clienteRepository = clienteRepository;
  }

  @Override
  public ClienteResponse criarCliente(ClienteRequest request) {
    var cliente = new Cliente();
    cliente.setNome(request.getNome());
    cliente.setEmail(request.getEmail());
    cliente.setTelefone(request.getTelefone());
    cliente.setCpf(request.getCpf());
    cliente.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);

    var clienteSalvo = clienteRepository.save(cliente);
    return mapearParaResponse(clienteSalvo);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ClienteResponse> findByEmail(String email) {
    return clienteRepository.findByEmail(email)
        .stream()
        .map(this::mapearParaResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public ClienteResponse findByAtivoTrue() {
    var cliente = clienteRepository.findByAtivoTrue();
    return cliente != null ? mapearParaResponse(cliente) : null;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ClienteResponse> findByNomeContainingIgnoreCase(String nome) {
    return clienteRepository.findByNomeContainingIgnoreCase(nome)
        .stream()
        .map(this::mapearParaResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByEmail(String email) {
    return clienteRepository.existsByEmail(email);
  }

  @Override
  @Transactional(readOnly = true)
  public List<RankingClienteResponse> obterRankingClientesPorNumeroPedidos() {
    return clienteRepository.obterRankingClientesPorNumeroPedidos();
  }

  private ClienteResponse mapearParaResponse(Cliente cliente) {
    return new ClienteResponse(
        cliente.getId(),
        cliente.getNome(),
        cliente.getEmail(),
        cliente.getTelefone(),
        cliente.getCpf(),
        cliente.getAtivo()
    );
  }

}
