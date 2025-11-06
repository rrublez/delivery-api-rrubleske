package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.request.RestauranteRequest;
import com.deliverytech.delivery.dto.response.RestauranteResponse;
import com.deliverytech.delivery.service.RestauranteService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurantes")
public class RestauranteController {

  private final RestauranteService restauranteService;

  public RestauranteController(RestauranteService restauranteService) {
    this.restauranteService = restauranteService;
  }

  @PostMapping
  public ResponseEntity<RestauranteResponse> criar(@Valid @RequestBody RestauranteRequest request) {
    var response = restauranteService.criarRestaurante(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/ramo/{ramoAtividade}")
  public ResponseEntity<List<RestauranteResponse>> findByRamoAtividade(
      @PathVariable String ramoAtividade) {
    var response = restauranteService.findByRamoAtividade(ramoAtividade);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/ativo")
  public ResponseEntity<List<RestauranteResponse>> findByAtivoTrue() {
    var response = restauranteService.findByAtivoTrue();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/taxa-maxima")
  public ResponseEntity<List<RestauranteResponse>> findByTaxaEntregaLessThanEqual(
      @RequestParam BigDecimal taxa) {
    var response = restauranteService.findByTaxaEntregaLessThanEqual(taxa);
    return ResponseEntity.ok(response);
  }

}
