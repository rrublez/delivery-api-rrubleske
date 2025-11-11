package com.deliverytech.delivery.controller;

import java.math.BigDecimal;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Restaurantes", description = "APIs para gerenciar restaurantes e delivery")
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
  @Operation(summary = "Listar restaurantes", description = "Retorna lista de restaurantes com filtros opcionais por ramo e status")
  @ApiResponse(responseCode = "200", description = "Restaurantes listados com sucesso")
  public ResponseEntity<List<RestauranteResponse>> listarRestaurantes(
      @Parameter(description = "Ramo de atividade (ex: Pizzaria, Burger, Churrascaria)") @RequestParam(required = false) String ramo,
      @Parameter(description = "Status ativo/inativo") @RequestParam(required = false) Boolean ativo) {
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
  @Operation(summary = "Criar novo restaurante", description = "Cria um novo restaurante com validação de CNPJ único")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos ou CNPJ já registrado")
  })
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
  @Operation(summary = "Obter restaurante por ID", description = "Retorna os detalhes de um restaurante específico")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
      @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
  })
  public ResponseEntity<RestauranteResponse> obterRestaurante(
      @Parameter(description = "ID do restaurante") @PathVariable Long id) {
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
  @Operation(summary = "Atualizar restaurante completo", description = "Atualiza todos os campos de um restaurante existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
      @ApiResponse(responseCode = "409", description = "Conflito de dados")
  })
  public ResponseEntity<RestauranteResponse> atualizarRestaurante(
      @Parameter(description = "ID do restaurante") @PathVariable Long id,
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
  @Operation(summary = "Atualizar status do restaurante", description = "Altera o status de ativo/inativo de um restaurante")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
      @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
  })
  public ResponseEntity<RestauranteResponse> atualizarStatusRestaurante(
      @Parameter(description = "ID do restaurante") @PathVariable Long id,
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
  @Operation(summary = "Listar restaurantes por categoria", description = "Retorna restaurantes de uma categoria/ramo de atividade específica")
  @ApiResponse(responseCode = "200", description = "Restaurantes encontrados")
  public ResponseEntity<List<RestauranteResponse>> listarPorCategoria(
      @Parameter(description = "Categoria/ramo de atividade") @PathVariable String categoria) {
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
  @Operation(summary = "Calcular taxa de entrega", description = "Calcula a taxa de entrega para um CEP específico baseada no restaurante")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Taxa calculada com sucesso"),
      @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
  })
  public ResponseEntity<TaxaEntregaResponse> calcularTaxaEntrega(
      @Parameter(description = "ID do restaurante") @PathVariable Long id,
      @Parameter(description = "CEP do cliente") @PathVariable String cep) {
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
  @Operation(summary = "Listar restaurantes próximos", description = "Retorna restaurantes próximos a um CEP, ordenados por distância")
  @ApiResponse(responseCode = "200", description = "Restaurantes próximos listados")
  public ResponseEntity<List<RestaurantePróximoResponse>> listarProximos(
      @Parameter(description = "CEP do cliente") @PathVariable String cep,
      @Parameter(description = "Raio de busca em km (padrão: 5)") @RequestParam(required = false) Double raio) {
    List<RestaurantePróximoResponse> response = restauranteService.listarProximos(cep, raio);
    return ResponseEntity.ok(response);
  }

  // ==================== MÉTODOS MANTIDOS PARA COMPATIBILIDADE ====================

  @GetMapping("/ramo/{ramoAtividade}")
  @Operation(summary = "Buscar restaurantes por ramo de atividade", description = "Retorna restaurantes de um ramo específico")
  @ApiResponse(responseCode = "200", description = "Restaurantes encontrados")
  public ResponseEntity<List<RestauranteResponse>> findByRamoAtividade(
      @Parameter(description = "Ramo de atividade") @PathVariable String ramoAtividade) {
    var response = restauranteService.findByRamoAtividade(ramoAtividade);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/ativo")
  @Operation(summary = "Listar restaurantes ativos", description = "Retorna todos os restaurantes com status ativo")
  @ApiResponse(responseCode = "200", description = "Restaurantes ativos listados")
  public ResponseEntity<List<RestauranteResponse>> findByAtivoTrue() {
    var response = restauranteService.findByAtivoTrue();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/taxa-maxima")
  @Operation(summary = "Buscar restaurantes por taxa máxima", description = "Retorna restaurantes com taxa de entrega menor ou igual à informada")
  @ApiResponse(responseCode = "200", description = "Restaurantes encontrados")
  public ResponseEntity<List<RestauranteResponse>> findByTaxaEntregaLessThanEqual(
      @Parameter(description = "Taxa máxima de entrega") @RequestParam BigDecimal taxa) {
    var response = restauranteService.findByTaxaEntregaLessThanEqual(taxa);
    return ResponseEntity.ok(response);
  }

}
