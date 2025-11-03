package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.dto.ClienteRequestDTO;
import com.deliverytech.delivery.dto.ClienteResponseDTO;
import com.deliverytech.delivery.dto.EnderecoResponseDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Endereco;
import com.deliverytech.delivery.repository.ClienteRepository;
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

    @Override
    public ClienteResponseDTO create(ClienteRequestDTO requestDTO) {
        log.info("Criando novo cliente com email: {}", requestDTO.getEmail());

        if (emailExists(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado no sistema");
        }

        if (documentoExists(requestDTO.getDocumentoIdentificacao())) {
            throw new IllegalArgumentException("Documento de identidade já cadastrado");
        }

        // Validar número de endereços (máximo 3)
        if (requestDTO.getEnderecos() == null || requestDTO.getEnderecos().isEmpty()) {
            throw new IllegalArgumentException("Cliente deve ter pelo menos 1 endereço");
        }

        if (requestDTO.getEnderecos().size() > 3) {
            throw new IllegalArgumentException("Cliente pode ter no máximo 3 endereços");
        }

        Cliente cliente = Cliente.builder()
                .id(UUID.randomUUID().toString())
                .nome(requestDTO.getNome())
                .email(requestDTO.getEmail())
                .telefone(requestDTO.getTelefone())
                .documentoIdentificacao(requestDTO.getDocumentoIdentificacao())
                .observacoes(requestDTO.getObservacoes())
                .enderecos(new java.util.ArrayList<>())
                .build();

        // Criar endereços associados ao cliente
        for (var enderecoDTO : requestDTO.getEnderecos()) {
            log.info("Criando endereço de tipo {} para o cliente", enderecoDTO.getTipoEndereco());
            
            Endereco endereco = Endereco.builder()
                    .id(UUID.randomUUID().toString())
                    .rua(enderecoDTO.getRua())
                    .numero(enderecoDTO.getNumero())
                    .complemento(enderecoDTO.getComplemento())
                    .cidade(enderecoDTO.getCidade())
                    .estado(enderecoDTO.getEstado())
                    .cep(enderecoDTO.getCep())
                    .bairro(enderecoDTO.getBairro())
                    .pontoReferencia(enderecoDTO.getPontoReferencia())
                    .tipoEndereco(enderecoDTO.getTipoEndereco())
                    .cliente(cliente)  // Associar o cliente
                    .build();

            cliente.getEnderecos().add(endereco);
        }

        Cliente clienteSalvo = clienteRepository.save(cliente);
        log.info("Cliente criado com sucesso com {} endereço(s): {}", 
                clienteSalvo.getEnderecos().size(), clienteSalvo.getId());

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

        // Validar número de endereços (máximo 3)
        if (requestDTO.getEnderecos() != null && requestDTO.getEnderecos().size() > 3) {
            throw new IllegalArgumentException("Cliente pode ter no máximo 3 endereços");
        }

        cliente.setNome(requestDTO.getNome());
        cliente.setEmail(requestDTO.getEmail());
        cliente.setTelefone(requestDTO.getTelefone());
        cliente.setDocumentoIdentificacao(requestDTO.getDocumentoIdentificacao());
        cliente.setObservacoes(requestDTO.getObservacoes());

        // Atualizar endereços se fornecidos
        if (requestDTO.getEnderecos() != null && !requestDTO.getEnderecos().isEmpty()) {
            cliente.getEnderecos().clear();
            
            for (var enderecoDTO : requestDTO.getEnderecos()) {
                Endereco endereco = Endereco.builder()
                        .id(UUID.randomUUID().toString())
                        .rua(enderecoDTO.getRua())
                        .numero(enderecoDTO.getNumero())
                        .complemento(enderecoDTO.getComplemento())
                        .cidade(enderecoDTO.getCidade())
                        .estado(enderecoDTO.getEstado())
                        .cep(enderecoDTO.getCep())
                        .bairro(enderecoDTO.getBairro())
                        .pontoReferencia(enderecoDTO.getPontoReferencia())
                        .tipoEndereco(enderecoDTO.getTipoEndereco())
                        .cliente(cliente)
                        .build();
                
                cliente.getEnderecos().add(endereco);
            }
        }

        Cliente clienteAtualizado = clienteRepository.save(cliente);
        log.info("Cliente atualizado com sucesso com {} endereço(s): {}", 
                clienteAtualizado.getEnderecos().size(), id);

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
        List<EnderecoResponseDTO> enderecosDTO = cliente.getEnderecos().stream()
                .map(endereco -> EnderecoResponseDTO.builder()
                        .id(endereco.getId())
                        .rua(endereco.getRua())
                        .numero(endereco.getNumero())
                        .complemento(endereco.getComplemento())
                        .cidade(endereco.getCidade())
                        .estado(endereco.getEstado())
                        .cep(endereco.getCep())
                        .bairro(endereco.getBairro())
                        .pontoReferencia(endereco.getPontoReferencia())
                        .tipoEndereco(endereco.getTipoEndereco())
                        .build())
                .collect(Collectors.toList());

        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .email(cliente.getEmail())
                .telefone(cliente.getTelefone())
                .documentoIdentificacao(cliente.getDocumentoIdentificacao())
                .observacoes(cliente.getObservacoes())
                .enderecos(enderecosDTO)
                .build();
    }
}
