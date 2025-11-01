package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.dto.EstabelecimentoRequestDTO;
import com.deliverytech.delivery.dto.EstabelecimentoResponseDTO;
import com.deliverytech.delivery.dto.RamoEstabelecimentoResponseDTO;
import com.deliverytech.delivery.entity.Estabelecimento;
import com.deliverytech.delivery.entity.RamoEstabelecimento;
import com.deliverytech.delivery.repository.EstabelecimentoRepository;
import com.deliverytech.delivery.repository.RamoEstabelecimentoRepository;
import com.deliverytech.delivery.service.EstabelecimentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Estabelecimento.
 * Responsável pela lógica de negócio e operações CRUD.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EstabelecimentoServiceImpl implements EstabelecimentoService {

    private final EstabelecimentoRepository estabelecimentoRepository;
    private final RamoEstabelecimentoRepository ramoRepository;

    @Override
    public EstabelecimentoResponseDTO create(EstabelecimentoRequestDTO requestDTO) {
        log.info("Criando novo estabelecimento: {}", requestDTO.getNome());

        if (estabelecimentoRepository.existsByCnpj(requestDTO.getCnpj())) {
            log.error("CNPJ já cadastrado: {}", requestDTO.getCnpj());
            throw new IllegalArgumentException("CNPJ já cadastrado no sistema");
        }

        RamoEstabelecimento ramo = ramoRepository.findById(requestDTO.getRamoId())
                .orElseThrow(() -> {
                    log.error("Ramo não encontrado: {}", requestDTO.getRamoId());
                    return new IllegalArgumentException("Ramo não encontrado");
                });

        Estabelecimento estabelecimento = Estabelecimento.builder()
                .id(UUID.randomUUID().toString())
                .nome(requestDTO.getNome())
                .cnpj(requestDTO.getCnpj())
                .telefone(requestDTO.getTelefone())
                .email(requestDTO.getEmail())
                .ramo(ramo)
                .build();

        Estabelecimento estabelecimentoSalvo = estabelecimentoRepository.save(estabelecimento);
        log.info("Estabelecimento criado com sucesso: {}", estabelecimentoSalvo.getId());

        return toResponseDTO(estabelecimentoSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public EstabelecimentoResponseDTO findById(String id) {
        log.info("Buscando estabelecimento com ID: {}", id);

        Estabelecimento estabelecimento = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Estabelecimento não encontrado: {}", id);
                    return new IllegalArgumentException("Estabelecimento não encontrado");
                });

        return toResponseDTO(estabelecimento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstabelecimentoResponseDTO> findAll() {
        log.info("Buscando todos os estabelecimentos");

        return estabelecimentoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstabelecimentoResponseDTO> findByNome(String nome) {
        log.info("Buscando estabelecimentos com nome: {}", nome);

        return estabelecimentoRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstabelecimentoResponseDTO> findByRamo(String ramoId) {
        log.info("Buscando estabelecimentos do ramo: {}", ramoId);

        ramoRepository.findById(ramoId)
                .orElseThrow(() -> {
                    log.error("Ramo não encontrado: {}", ramoId);
                    return new IllegalArgumentException("Ramo não encontrado");
                });

        return estabelecimentoRepository.findByRamoId(ramoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EstabelecimentoResponseDTO update(String id, EstabelecimentoRequestDTO requestDTO) {
        log.info("Atualizando estabelecimento com ID: {}", id);

        Estabelecimento estabelecimento = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Estabelecimento não encontrado: {}", id);
                    return new IllegalArgumentException("Estabelecimento não encontrado");
                });

        // Validar CNPJ se foi alterado
        if (!estabelecimento.getCnpj().equals(requestDTO.getCnpj()) &&
                estabelecimentoRepository.existsByCnpj(requestDTO.getCnpj())) {
            log.error("CNPJ já cadastrado: {}", requestDTO.getCnpj());
            throw new IllegalArgumentException("CNPJ já cadastrado no sistema");
        }

        RamoEstabelecimento ramo = ramoRepository.findById(requestDTO.getRamoId())
                .orElseThrow(() -> {
                    log.error("Ramo não encontrado: {}", requestDTO.getRamoId());
                    return new IllegalArgumentException("Ramo não encontrado");
                });

        estabelecimento.setNome(requestDTO.getNome());
        estabelecimento.setCnpj(requestDTO.getCnpj());
        estabelecimento.setTelefone(requestDTO.getTelefone());
        estabelecimento.setEmail(requestDTO.getEmail());
        estabelecimento.setRamo(ramo);

        Estabelecimento estabelecimentoAtualizado = estabelecimentoRepository.save(estabelecimento);
        log.info("Estabelecimento atualizado com sucesso: {}", id);

        return toResponseDTO(estabelecimentoAtualizado);
    }

    @Override
    public void delete(String id) {
        log.info("Deletando estabelecimento com ID: {}", id);

        Estabelecimento estabelecimento = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Estabelecimento não encontrado: {}", id);
                    return new IllegalArgumentException("Estabelecimento não encontrado");
                });

        estabelecimentoRepository.delete(estabelecimento);
        log.info("Estabelecimento deletado com sucesso: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean cnpjExists(String cnpj) {
        return estabelecimentoRepository.existsByCnpj(cnpj);
    }

    /**
     * Converte uma entidade Estabelecimento para EstabelecimentoResponseDTO.
     *
     * @param estabelecimento entidade a converter
     * @return DTO de resposta
     */
    private EstabelecimentoResponseDTO toResponseDTO(Estabelecimento estabelecimento) {
        RamoEstabelecimentoResponseDTO ramoDTO = RamoEstabelecimentoResponseDTO.builder()
                .id(estabelecimento.getRamo().getId())
                .nome(estabelecimento.getRamo().getNome())
                .descricao(estabelecimento.getRamo().getDescricao())
                .totalEstabelecimentos(0)
                .build();

        return EstabelecimentoResponseDTO.builder()
                .id(estabelecimento.getId())
                .nome(estabelecimento.getNome())
                .cnpj(estabelecimento.getCnpj())
                .telefone(estabelecimento.getTelefone())
                .email(estabelecimento.getEmail())
                .ramo(ramoDTO)
                .build();
    }
}
