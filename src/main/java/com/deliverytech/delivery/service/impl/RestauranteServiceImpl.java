package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.dto.request.RestauranteRequest;
import com.deliverytech.delivery.dto.response.RestauranteResponse;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.service.RestauranteService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RestauranteServiceImpl implements RestauranteService {

  private final RestauranteRepository restauranteRepository;

  public RestauranteServiceImpl(RestauranteRepository restauranteRepository) {
    this.restauranteRepository = restauranteRepository;
  }

  @Override
  public RestauranteResponse criarRestaurante(RestauranteRequest request) {
    var restaurante = new Restaurante();
    restaurante.setNome(request.getNome());
    restaurante.setEndereco(request.getEndereco());
    restaurante.setTelefone(request.getTelefone());
    restaurante.setCnpj(request.getCnpj());
    restaurante.setRamoAtividade(request.getRamoAtividade());
    restaurante.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);
    restaurante.setTaxaEntrega(request.getTaxaEntrega());

    var restauranteSalvo = restauranteRepository.save(restaurante);
    return mapearParaResponse(restauranteSalvo);
  }

  @Override
  @Transactional(readOnly = true)
  public List<RestauranteResponse> findByRamoAtividade(String ramoAtividade) {
    return restauranteRepository.findByRamoAtividade(ramoAtividade)
        .stream()
        .map(this::mapearParaResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<RestauranteResponse> findByAtivoTrue() {
    return restauranteRepository.findByAtivoTrue()
        .stream()
        .map(this::mapearParaResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<RestauranteResponse> findByTaxaEntregaLessThanEqual(BigDecimal taxa) {
    return restauranteRepository.findByTaxaEntregaLessThanEqual(taxa)
        .stream()
        .map(this::mapearParaResponse)
        .toList();
  }

  private RestauranteResponse mapearParaResponse(Restaurante restaurante) {
    return new RestauranteResponse(
        restaurante.getId(),
        restaurante.getNome(),
        restaurante.getEndereco(),
        restaurante.getTelefone(),
        restaurante.getCnpj(),
        restaurante.getRamoAtividade(),
        restaurante.getAtivo(),
        restaurante.getTaxaEntrega()
    );
  }

}
