package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.request.RestauranteRequest;
import com.deliverytech.delivery.dto.response.RestauranteResponse;
import java.math.BigDecimal;
import java.util.List;

public interface RestauranteService {

  RestauranteResponse criarRestaurante(RestauranteRequest request);

  List<RestauranteResponse> findByRamoAtividade(String ramoAtividade);

  List<RestauranteResponse> findByAtivoTrue();

  List<RestauranteResponse> findByTaxaEntregaLessThanEqual(BigDecimal taxa);

}
