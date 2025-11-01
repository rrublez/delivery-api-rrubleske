package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.dto.ClienteRequestDTO;
import com.deliverytech.delivery.dto.ClienteResponseDTO;
import com.deliverytech.delivery.dto.EnderecoResponseDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Endereco;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.EnderecoRepository;
import com.deliverytech.delivery.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Cliente.
 * Responsável pela lógica de negócio e operações CRUD.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;

    @Override
    public ClienteResponseDTO create(ClienteRequestDTO requestDTO) {
        log.info("Criando novo cliente com email: {}", requestDTO.getEmail());

        if (emailExists(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado no sistema");
        }

        if (documentoExists(requestDTO.getDocumentoIdentificacao())) {
            throw new IllegalArgumentException("Documento de identidade já cadastrado");
        }

        Endereco endereco = null;
        if (requestDTO.getEndereco() != null) {
            log.info("Criando endereço associado ao cliente");
            endereco = Endereco.builder()
                    .id(UUID.randomUUID().toString())
                    .rua(requestDTO.getEndereco().getRua())
                    .numero(requestDTO.getEndereco().getNumero())
                    .complemento(requestDTO.getEndereco().getComplemento())
                    .cidade(requestDTO.getEndereco().getCidade())
                    .estado(requestDTO.getEndereco().getEstado())
                    .cep(requestDTO.getEndereco().getCep())
                    .bairro(requestDTO.getEndereco().getBairro())
                    .pontoReferencia(requestDTO.getEndereco().getPontoReferencia())
                    .tipoEndereco(requestDTO.getEndereco().getTipoEndereco())
                    .build();

            endereco = enderecoRepository.save(endereco);
        }

        Cliente cliente = Cliente.builder()
                .id(UUID.randomUUID().toString())
                .nome(requestDTO.getNome())
                .email(requestDTO.getEmail())
                .telefone(requestDTO.getTelefone())
                .documentoIdentificacao(requestDTO.getDocumentoIdentificacao())
                .observacoes(requestDTO.getObservacoes())
                .endereco(endereco)
                .build();

        Cliente clienteSalvo = clienteRepository.save(cliente);
        log.info("Cliente criado com sucesso: {}", clienteSalvo.getId());

        return toResponseDTO(clienteSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO findById(String id) {
        log.info("Buscando cliente com ID: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente não encontrado: {}", id);
                    return new IllegalArgumentException("Cliente não encontrado");
                });

        return toResponseDTO(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> findAll() {
        log.info("Buscando todos os clientes");

        return clienteRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO findByEmail(String email) {
        log.info("Buscando cliente com email: {}", email);

        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Cliente não encontrado com email: {}", email);
                    return new IllegalArgumentException("Cliente não encontrado");
                });

        return toResponseDTO(cliente);
    }

    @Override
    public ClienteResponseDTO update(String id, ClienteRequestDTO requestDTO) {
        log.info("Atualizando cliente com ID: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente não encontrado: {}", id);
                    return new IllegalArgumentException("Cliente não encontrado");
                });

        // Validar email se foi alterado
        if (!cliente.getEmail().equals(requestDTO.getEmail()) && emailExists(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado no sistema");
        }

        // Validar documento se foi alterado
        if (!cliente.getDocumentoIdentificacao().equals(requestDTO.getDocumentoIdentificacao())
                && documentoExists(requestDTO.getDocumentoIdentificacao())) {
            throw new IllegalArgumentException("Documento de identidade já cadastrado");
        }

        cliente.setNome(requestDTO.getNome());
        cliente.setEmail(requestDTO.getEmail());
        cliente.setTelefone(requestDTO.getTelefone());
        cliente.setDocumentoIdentificacao(requestDTO.getDocumentoIdentificacao());
        cliente.setObservacoes(requestDTO.getObservacoes());

        Cliente clienteAtualizado = clienteRepository.save(cliente);
        log.info("Cliente atualizado com sucesso: {}", id);

        return toResponseDTO(clienteAtualizado);
    }

    @Override
    public void delete(String id) {
        log.info("Deletando cliente com ID: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente não encontrado: {}", id);
                    return new IllegalArgumentException("Cliente não encontrado");
                });

        clienteRepository.delete(cliente);
        log.info("Cliente deletado com sucesso: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return clienteRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean documentoExists(String documento) {
        return clienteRepository.existsByDocumentoIdentificacao(documento);
    }

    /**
     * Converte uma entidade Cliente para ClienteResponseDTO.
     *
     * @param cliente entidade a converter
     * @return DTO de resposta
     */
    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        EnderecoResponseDTO enderecoDTO = null;

        if (cliente.getEndereco() != null) {
            enderecoDTO = EnderecoResponseDTO.builder()
                    .id(cliente.getEndereco().getId())
                    .rua(cliente.getEndereco().getRua())
                    .numero(cliente.getEndereco().getNumero())
                    .complemento(cliente.getEndereco().getComplemento())
                    .cidade(cliente.getEndereco().getCidade())
                    .estado(cliente.getEndereco().getEstado())
                    .cep(cliente.getEndereco().getCep())
                    .bairro(cliente.getEndereco().getBairro())
                    .pontoReferencia(cliente.getEndereco().getPontoReferencia())
                    .tipoEndereco(cliente.getEndereco().getTipoEndereco())
                    .build();
        }

        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .email(cliente.getEmail())
                .telefone(cliente.getTelefone())
                .documentoIdentificacao(cliente.getDocumentoIdentificacao())
                .observacoes(cliente.getObservacoes())
                .endereco(enderecoDTO)
                .build();
    }
}
