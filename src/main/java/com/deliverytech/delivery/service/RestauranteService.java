package com.deliverytech.delivery.service;

import java.math.BigDecimal;
import java.util.List;
import com.deliverytech.delivery.dto.restaurante.request.AtualizarStatusRestauranteRequest;
import com.deliverytech.delivery.dto.restaurante.request.RestauranteRequest;
import com.deliverytech.delivery.dto.restaurante.response.RestaurantePróximoResponse;
import com.deliverytech.delivery.dto.restaurante.response.RestauranteResponse;
import com.deliverytech.delivery.dto.restaurante.response.TaxaEntregaResponse;

public interface RestauranteService {

  // CREATE
  RestauranteResponse criarRestaurante(RestauranteRequest request);

  // READ
  List<RestauranteResponse> listarTodos(String ramoAtividade, Boolean ativo);

  RestauranteResponse obterPorId(Long id);

  List<RestauranteResponse> findByRamoAtividade(String ramoAtividade);

  List<RestauranteResponse> findByAtivoTrue();

  List<RestauranteResponse> findByTaxaEntregaLessThanEqual(BigDecimal taxa);

  // UPDATE
  RestauranteResponse atualizarRestaurante(Long id, RestauranteRequest request);

  RestauranteResponse atualizarStatus(Long id, AtualizarStatusRestauranteRequest request);

  // REPORTS & CÁLCULOS
  TaxaEntregaResponse calcularTaxaEntrega(Long id, String cep);

  List<RestaurantePróximoResponse> listarProximos(String cep, Double raioKm);

}
