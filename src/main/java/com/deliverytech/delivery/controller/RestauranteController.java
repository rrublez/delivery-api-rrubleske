package com.deliverytech.delivery.controller;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.deliverytech.delivery.dto.restaurante.request.AtualizarStatusRestauranteRequest;
import com.deliverytech.delivery.dto.restaurante.request.RestauranteRequest;
import com.deliverytech.delivery.dto.restaurante.response.RestaurantePróximoResponse;
import com.deliverytech.delivery.dto.restaurante.response.RestauranteResponse;
import com.deliverytech.delivery.dto.restaurante.response.TaxaEntregaResponse;
import com.deliverytech.delivery.service.RestauranteService;
import jakarta.validation.Valid;

/**
 * Controller REST para gerenciar Restaurantes
 * Endpoints: CRUD completo + relatórios e cálculos
 */
@RestController
@RequestMapping("/api/restaurantes")
public class RestauranteController {

  private final RestauranteService restauranteService;

  public RestauranteController(RestauranteService restauranteService) {
    this.restauranteService = restauranteService;
  }

  /**
   * GET /api/restaurantes - Listar restaurantes com filtros opcionais
   * Query params: ramo (categoria), ativo (true/false)
   * 
   * Exemplos:
   * GET /api/restaurantes
   * GET /api/restaurantes?ramo=Pizzaria
   * GET /api/restaurantes?ativo=true
   * GET /api/restaurantes?ramo=Pizzaria&ativo=true
   */
  @GetMapping
  public ResponseEntity<List<RestauranteResponse>> listarRestaurantes(
      @RequestParam(required = false) String ramo,
      @RequestParam(required = false) Boolean ativo) {
    List<RestauranteResponse> response = restauranteService.listarTodos(ramo, ativo);
    return ResponseEntity.ok(response);
  }

  /**
   * POST /api/restaurantes - Criar novo restaurante
   * Body: RestauranteRequest (nome, endereco, telefone, cnpj, ramoAtividade, ativo, taxaEntrega)
   * 
   * Response: 201 Created com RestauranteResponse
   */
  @PostMapping
  public ResponseEntity<RestauranteResponse> criarRestaurante(
      @Valid @RequestBody RestauranteRequest request) {
    RestauranteResponse response = restauranteService.criarRestaurante(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * GET /api/restaurantes/{id} - Buscar restaurante por ID
   * Path param: id
   * 
   * Response: 200 OK com RestauranteResponse
   * Error: 404 Not Found se não existir
   */
  @GetMapping("/{id}")
  public ResponseEntity<RestauranteResponse> obterRestaurante(@PathVariable Long id) {
    RestauranteResponse response = restauranteService.obterPorId(id);
    return ResponseEntity.ok(response);
  }

  /**
   * PUT /api/restaurantes/{id} - Atualizar restaurante completo
   * Path param: id
   * Body: RestauranteRequest (todos os campos)
   * 
   * Response: 200 OK com RestauranteResponse atualizado
   * Error: 404 Not Found, 409 Conflict
   */
  @PutMapping("/{id}")
  public ResponseEntity<RestauranteResponse> atualizarRestaurante(
      @PathVariable Long id,
      @Valid @RequestBody RestauranteRequest request) {
    RestauranteResponse response = restauranteService.atualizarRestaurante(id, request);
    return ResponseEntity.ok(response);
  }

  /**
   * PATCH /api/restaurantes/{id}/status - Ativar/desativar restaurante
   * Path param: id
   * Body: AtualizarStatusRestauranteRequest { ativo: true/false }
   * 
   * Response: 200 OK com RestauranteResponse atualizado
   * Error: 404 Not Found
   * 
   * Exemplo:
   * PATCH /api/restaurantes/1/status
   * { "ativo": false }
   */
  @PatchMapping("/{id}/status")
  public ResponseEntity<RestauranteResponse> atualizarStatusRestaurante(
      @PathVariable Long id,
      @Valid @RequestBody AtualizarStatusRestauranteRequest request) {
    RestauranteResponse response = restauranteService.atualizarStatus(id, request);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/restaurantes/categoria/{categoria} - Listar por categoria (ramo)
   * Path param: categoria (ex: Pizzaria, Burger, Churrascaria)
   * 
   * Response: 200 OK com lista de RestauranteResponse
   */
  @GetMapping("/categoria/{categoria}")
  public ResponseEntity<List<RestauranteResponse>> listarPorCategoria(
      @PathVariable String categoria) {
    List<RestauranteResponse> response = restauranteService.findByRamoAtividade(categoria);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/restaurantes/{id}/taxa-entrega/{cep} - Calcular taxa de entrega
   * Path params: id (restaurante), cep (CEP do cliente)
   * 
   * Response: 200 OK com TaxaEntregaResponse
   * Error: 404 Not Found se restaurante não existe
   * 
   * Exemplo:
   * GET /api/restaurantes/1/taxa-entrega/90010100
   */
  @GetMapping("/{id}/taxa-entrega/{cep}")
  public ResponseEntity<TaxaEntregaResponse> calcularTaxaEntrega(
      @PathVariable Long id,
      @PathVariable String cep) {
    TaxaEntregaResponse response = restauranteService.calcularTaxaEntrega(id, cep);
    return ResponseEntity.ok(response);
  }

  /**
   * GET /api/restaurantes/proximos/{cep} - Listar restaurantes próximos
   * Path param: cep (CEP do cliente)
   * Query param: raio (opcional, padrão 5km)
   * 
   * Response: 200 OK com lista de RestaurantePróximoResponse (ordenada por distância)
   * 
   * Exemplos:
   * GET /api/restaurantes/proximos/90010100
   * GET /api/restaurantes/proximos/90010100?raio=10
   */
  @GetMapping("/proximos/{cep}")
  public ResponseEntity<List<RestaurantePróximoResponse>> listarProximos(
      @PathVariable String cep,
      @RequestParam(required = false) Double raio) {
    List<RestaurantePróximoResponse> response = restauranteService.listarProximos(cep, raio);
    return ResponseEntity.ok(response);
  }

  // ==================== MÉTODOS MANTIDOS PARA COMPATIBILIDADE ====================

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
