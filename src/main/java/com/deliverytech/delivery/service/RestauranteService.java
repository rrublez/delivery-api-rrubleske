package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.request.AtualizarStatusRestauranteRequest;
import com.deliverytech.delivery.dto.request.RestauranteRequest;
import com.deliverytech.delivery.dto.response.RestauranteResponse;
import com.deliverytech.delivery.dto.response.RestaurantePróximoResponse;
import com.deliverytech.delivery.dto.response.TaxaEntregaResponse;
import java.math.BigDecimal;
import java.util.List;

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
