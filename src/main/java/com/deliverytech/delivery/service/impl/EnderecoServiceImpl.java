package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.dto.EnderecoRequestDTO;
import com.deliverytech.delivery.dto.EnderecoResponseDTO;
import com.deliverytech.delivery.entity.Endereco;
import com.deliverytech.delivery.repository.EnderecoRepository;
import com.deliverytech.delivery.service.EnderecoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Endereço.
 * Responsável pela lógica de negócio e operações CRUD.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EnderecoServiceImpl implements EnderecoService {

    private final EnderecoRepository enderecoRepository;

    @Override
    public EnderecoResponseDTO create(EnderecoRequestDTO requestDTO) {
        log.info("Criando novo endereço na cidade: {}", requestDTO.getCidade());

        Endereco endereco = Endereco.builder()
                .id(UUID.randomUUID().toString())
                .rua(requestDTO.getRua())
                .numero(requestDTO.getNumero())
                .complemento(requestDTO.getComplemento())
                .cidade(requestDTO.getCidade())
                .estado(requestDTO.getEstado())
                .cep(requestDTO.getCep())
                .bairro(requestDTO.getBairro())
                .pontoReferencia(requestDTO.getPontoReferencia())
                .tipoEndereco(requestDTO.getTipoEndereco())
                .build();

        Endereco enderecoSalvo = enderecoRepository.save(endereco);
        log.info("Endereço criado com sucesso: {}", enderecoSalvo.getId());

        return toResponseDTO(enderecoSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public EnderecoResponseDTO findById(String id) {
        log.info("Buscando endereço com ID: {}", id);

        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Endereço não encontrado: {}", id);
                    return new IllegalArgumentException("Endereço não encontrado");
                });

        return toResponseDTO(endereco);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoResponseDTO> findAll() {
        log.info("Buscando todos os endereços");

        return enderecoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoResponseDTO> findByCidade(String cidade) {
        log.info("Buscando endereços na cidade: {}", cidade);

        return enderecoRepository.findByCidade(cidade)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoResponseDTO> findByCep(String cep) {
        log.info("Buscando endereços com CEP: {}", cep);

        return enderecoRepository.findByCep(cep)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EnderecoResponseDTO update(String id, EnderecoRequestDTO requestDTO) {
        log.info("Atualizando endereço com ID: {}", id);

        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Endereço não encontrado: {}", id);
                    return new IllegalArgumentException("Endereço não encontrado");
                });

        endereco.setRua(requestDTO.getRua());
        endereco.setNumero(requestDTO.getNumero());
        endereco.setComplemento(requestDTO.getComplemento());
        endereco.setCidade(requestDTO.getCidade());
        endereco.setEstado(requestDTO.getEstado());
        endereco.setCep(requestDTO.getCep());
        endereco.setBairro(requestDTO.getBairro());
        endereco.setPontoReferencia(requestDTO.getPontoReferencia());
        endereco.setTipoEndereco(requestDTO.getTipoEndereco());

        Endereco enderecoAtualizado = enderecoRepository.save(endereco);
        log.info("Endereço atualizado com sucesso: {}", id);

        return toResponseDTO(enderecoAtualizado);
    }

    @Override
    public void delete(String id) {
        log.info("Deletando endereço com ID: {}", id);

        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Endereço não encontrado: {}", id);
                    return new IllegalArgumentException("Endereço não encontrado");
                });

        enderecoRepository.delete(endereco);
        log.info("Endereço deletado com sucesso: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean cepExists(String cep) {
        return enderecoRepository.existsByCep(cep);
    }

    /**
     * Converte uma entidade Endereco para EnderecoResponseDTO.
     *
     * @param endereco entidade a converter
     * @return DTO de resposta
     */
    private EnderecoResponseDTO toResponseDTO(Endereco endereco) {
        return EnderecoResponseDTO.builder()
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
                .build();
    }
}
