package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.dto.request.AtualizarStatusRestauranteRequest;
import com.deliverytech.delivery.dto.request.RestauranteRequest;
import com.deliverytech.delivery.dto.response.RestauranteResponse;
import com.deliverytech.delivery.dto.response.RestaurantePróximoResponse;
import com.deliverytech.delivery.dto.response.TaxaEntregaResponse;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.service.RestauranteService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
  public List<RestauranteResponse> listarTodos(String ramoAtividade, Boolean ativo) {
    List<Restaurante> restaurantes;

    if (ramoAtividade != null && ativo != null) {
      // Filtro por categoria e status
      restaurantes = restauranteRepository.findAll().stream()
          .filter(r -> r.getRamoAtividade().equalsIgnoreCase(ramoAtividade))
          .filter(r -> r.getAtivo().equals(ativo))
          .toList();
    } else if (ramoAtividade != null) {
      // Filtro por categoria
      restaurantes = restauranteRepository.findByRamoAtividade(ramoAtividade);
    } else if (ativo != null) {
      // Filtro por status
      restaurantes = ativo 
          ? restauranteRepository.findByAtivoTrue() 
          : restauranteRepository.findAll().stream()
              .filter(r -> !r.getAtivo())
              .toList();
    } else {
      // Retorna todos
      restaurantes = restauranteRepository.findAll();
    }

    return restaurantes.stream()
        .map(this::mapearParaResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public RestauranteResponse obterPorId(Long id) {
    var restaurante = restauranteRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, 
            "Restaurante não encontrado com ID: " + id
        ));
    return mapearParaResponse(restaurante);
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

  @Override
  public RestauranteResponse atualizarRestaurante(Long id, RestauranteRequest request) {
    var restaurante = restauranteRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Restaurante não encontrado com ID: " + id
        ));

    // Verifica duplicidade apenas se o nome/email foi alterado
    if (!restaurante.getNome().equals(request.getNome()) && 
        restauranteRepository.existsByNomeIgnoreCase(request.getNome())) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Nome de restaurante já está registrado no sistema"
      );
    }

    if (!restaurante.getCnpj().equals(request.getCnpj()) && 
        restauranteRepository.existsByCnpj(request.getCnpj())) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "CNPJ já está registrado no sistema"
      );
    }

    if (!restaurante.getTelefone().equals(request.getTelefone()) && 
        restauranteRepository.existsByTelefone(request.getTelefone())) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Telefone já está registrado no sistema"
      );
    }

    restaurante.setNome(request.getNome());
    restaurante.setEndereco(request.getEndereco());
    restaurante.setTelefone(request.getTelefone());
    restaurante.setCnpj(request.getCnpj());
    restaurante.setRamoAtividade(request.getRamoAtividade());
    restaurante.setAtivo(request.getAtivo());
    restaurante.setTaxaEntrega(request.getTaxaEntrega());

    var restauranteAtualizado = restauranteRepository.save(restaurante);
    return mapearParaResponse(restauranteAtualizado);
  }

  @Override
  public RestauranteResponse atualizarStatus(Long id, AtualizarStatusRestauranteRequest request) {
    var restaurante = restauranteRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Restaurante não encontrado com ID: " + id
        ));

    restaurante.setAtivo(request.getAtivo());
    var restauranteAtualizado = restauranteRepository.save(restaurante);
    return mapearParaResponse(restauranteAtualizado);
  }

  @Override
  @Transactional(readOnly = true)
  public TaxaEntregaResponse calcularTaxaEntrega(Long id, String cep) {
    var restaurante = restauranteRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Restaurante não encontrado com ID: " + id
        ));

    // Simulação de cálculo de distância baseado no CEP
    // Em produção, usar API de geolocalização (Google Maps, etc.)
    Double distanciaKm = calcularDistancia(restaurante.getEndereco(), cep);

    return new TaxaEntregaResponse(
        restaurante.getId(),
        restaurante.getNome(),
        cep,
        restaurante.getTaxaEntrega(),
        distanciaKm,
        String.format("Taxa de entrega de R$ %.2f para %s km de distância",
            restaurante.getTaxaEntrega(), distanciaKm)
    );
  }

  @Override
  @Transactional(readOnly = true)
  public List<RestaurantePróximoResponse> listarProximos(String cep, Double raioKm) {
    Double raioAtual = raioKm != null ? raioKm : 5.0; // 5km de raio padrão

    return restauranteRepository.findByAtivoTrue()
        .stream()
        .map(restaurante -> {
          Double distancia = calcularDistancia(restaurante.getEndereco(), cep);
          return new RestaurantePróximoResponse(
              restaurante.getId(),
              restaurante.getNome(),
              restaurante.getEndereco(),
              restaurante.getRamoAtividade(),
              restaurante.getTaxaEntrega(),
              distancia,
              restaurante.getAtivo()
          );
        })
        .filter(r -> r.getDistanciaKm() <= raioAtual)
        .sorted((r1, r2) -> Double.compare(r1.getDistanciaKm(), r2.getDistanciaKm()))
        .toList();
  }

  /**
   * Calcula distância simulada entre endereço e CEP
   * Em produção, usar API de geolocalização real (Google Maps, OpenStreetMap, etc.)
   */
  private Double calcularDistancia(String endereco, String cep) {
    // Simulação: gera distância entre 1 e 10 km baseado no hash do CEP
    int hashCep = Math.abs(cep.hashCode());
    int hashEnd = Math.abs(endereco.hashCode());
    double distancia = 1.0 + ((hashCep + hashEnd) % 90) / 10.0;
    return Math.round(distancia * 100.0) / 100.0; // Arredonda para 2 casas decimais
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
