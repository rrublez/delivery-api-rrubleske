package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.dto.RamoEstabelecimentoRequestDTO;
import com.deliverytech.delivery.dto.RamoEstabelecimentoResponseDTO;
import com.deliverytech.delivery.entity.RamoEstabelecimento;
import com.deliverytech.delivery.repository.RamoEstabelecimentoRepository;
import com.deliverytech.delivery.service.RamoEstabelecimentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de RamoEstabelecimento.
 * Responsável pela lógica de negócio e operações CRUD.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RamoEstabelecimentoServiceImpl implements RamoEstabelecimentoService {

    private final RamoEstabelecimentoRepository ramoRepository;

    @Override
    public RamoEstabelecimentoResponseDTO create(RamoEstabelecimentoRequestDTO requestDTO) {
        log.info("Criando novo ramo: {}", requestDTO.getNome());

        RamoEstabelecimento ramo = RamoEstabelecimento.builder()
                .id(UUID.randomUUID().toString())
                .nome(requestDTO.getNome())
                .descricao(requestDTO.getDescricao())
                .build();

        RamoEstabelecimento ramoSalvo = ramoRepository.save(ramo);
        log.info("Ramo criado com sucesso: {}", ramoSalvo.getId());

        return toResponseDTO(ramoSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public RamoEstabelecimentoResponseDTO findById(String id) {
        log.info("Buscando ramo com ID: {}", id);

        RamoEstabelecimento ramo = ramoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Ramo não encontrado: {}", id);
                    return new IllegalArgumentException("Ramo não encontrado");
                });

        return toResponseDTO(ramo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RamoEstabelecimentoResponseDTO> findAll() {
        log.info("Buscando todos os ramos");

        return ramoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RamoEstabelecimentoResponseDTO> findByNome(String nome) {
        log.info("Buscando ramos com nome: {}", nome);

        return ramoRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RamoEstabelecimentoResponseDTO update(String id, RamoEstabelecimentoRequestDTO requestDTO) {
        log.info("Atualizando ramo com ID: {}", id);

        RamoEstabelecimento ramo = ramoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Ramo não encontrado: {}", id);
                    return new IllegalArgumentException("Ramo não encontrado");
                });

        ramo.setNome(requestDTO.getNome());
        ramo.setDescricao(requestDTO.getDescricao());

        RamoEstabelecimento ramoAtualizado = ramoRepository.save(ramo);
        log.info("Ramo atualizado com sucesso: {}", id);

        return toResponseDTO(ramoAtualizado);
    }

    @Override
    public void delete(String id) {
        log.info("Deletando ramo com ID: {}", id);

        RamoEstabelecimento ramo = ramoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Ramo não encontrado: {}", id);
                    return new IllegalArgumentException("Ramo não encontrado");
                });

        ramoRepository.delete(ramo);
        log.info("Ramo deletado com sucesso: {}", id);
    }

    /**
     * Converte uma entidade RamoEstabelecimento para RamoEstabelecimentoResponseDTO.
     *
     * @param ramo entidade a converter
     * @return DTO de resposta
     */
    private RamoEstabelecimentoResponseDTO toResponseDTO(RamoEstabelecimento ramo) {
        return RamoEstabelecimentoResponseDTO.builder()
                .id(ramo.getId())
                .nome(ramo.getNome())
                .descricao(ramo.getDescricao())
                .totalEstabelecimentos(0)
                .build();
    }
}
