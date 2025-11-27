package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.dto.cliente.request.ClienteRequest;
import com.deliverytech.delivery.dto.cliente.request.ClienteUpdateRequest;
import com.deliverytech.delivery.dto.cliente.response.ClienteResponse;
import com.deliverytech.delivery.dto.shared.response.RankingClienteResponse;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.exception.ConflictException;
import com.deliverytech.delivery.exception.ResourceNotFoundException;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.service.ClienteService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  public ClienteResponse findById(Long id) {
    return clienteRepository.findById(id)
        .map(this::mapearParaResponse)
        .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ClienteResponse> findAll(Pageable pageable) {
    return clienteRepository.findAll(pageable)
        .map(this::mapearParaResponse);
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

  @Override
  @Transactional
  public ClienteResponse atualizarCliente(Long id, ClienteUpdateRequest request) {
    var cliente = clienteRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

    if (clienteRepository.existsByEmailIgnoreCaseAndIdNot(request.getEmail(), id)) {
      throw new ConflictException("Email já está registrado no sistema");
    }
    if (clienteRepository.existsByCpfAndIdNot(request.getCpf(), id)) {
      throw new ConflictException("CPF já está registrado no sistema");
    }

    cliente.setNome(request.getNome());
    cliente.setEmail(request.getEmail());
    cliente.setTelefone(request.getTelefone());
    cliente.setCpf(request.getCpf());
    cliente.setAtivo(request.getAtivo());

    var atualizado = clienteRepository.save(cliente);
    return mapearParaResponse(atualizado);
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
