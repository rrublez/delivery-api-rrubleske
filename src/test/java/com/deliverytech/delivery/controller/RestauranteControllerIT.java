package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.restaurante.request.RestauranteRequest;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.deliverytech.delivery.entity.Role;
import com.deliverytech.delivery.support.TestAuthHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para RestauranteController
 * Cobre CRUD, filtros e cenários obrigatórios
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("RestauranteController - Testes de Integração")
class RestauranteControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private RestauranteRepository restauranteRepository;

  private RestauranteRequest validRequest;
  private String adminToken;

  @BeforeEach
  void setUp() {
    restauranteRepository.deleteAll();

    validRequest = new RestauranteRequest();
    validRequest.setNome("Pizzaria Central");
    validRequest.setEndereco("Rua Principal, 100");
    validRequest.setTelefone("1133333333");
    validRequest.setCnpj("04252011000110");
    validRequest.setRamoAtividade("Pizzaria");
    validRequest.setAtivo(true);
    validRequest.setTaxaEntrega(BigDecimal.valueOf(5.00));

    try {
      adminToken = TestAuthHelper.registerAndLogin(mockMvc, objectMapper, Role.ADMIN);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Nested
  @DisplayName("POST /api/restaurantes - Criar Restaurante")
  class CreateRestauranteTests {

    @Test
    @DisplayName("✅ Deve criar restaurante com sucesso - Status 201")
    @Transactional
    void testCreateRestauranteSuccess() throws Exception {
      mockMvc.perform(post("/api/restaurantes")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id", notNullValue()))
          .andExpect(jsonPath("$.nome", equalTo("Pizzaria Central")));
    }
  }

  @Nested
  @DisplayName("GET /api/restaurantes - Listar com Filtros (Cenários Obrigatórios)")
  class ListRestaurantesTests {

    @Test
    @DisplayName("✅ [CENÁRIO 1] Listar restaurantes com filtros: categoria=Italiana&ativo=true")
    @Transactional
    void testScenario1ListWithCategoryAndStatusFilters() throws Exception {
      // Criar restaurante Italiana
      RestauranteRequest italianaRequest = new RestauranteRequest();
      italianaRequest.setNome("Restaurante Italiano");
      italianaRequest.setEndereco("Rua Roma, 100");
      italianaRequest.setTelefone("1133333334");
      italianaRequest.setCnpj("11222333000181");
      italianaRequest.setRamoAtividade("Italiana");
      italianaRequest.setAtivo(true);
      italianaRequest.setTaxaEntrega(BigDecimal.valueOf(6.00));

        mockMvc.perform(post("/api/restaurantes")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(italianaRequest)))
          .andExpect(status().isCreated());

      // GET /api/restaurantes?categoria=Italiana&ativo=true
      // Resultado Esperado: Lista paginada com metadados
      mockMvc.perform(get("/api/restaurantes?ramo=Italiana&ativo=true")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(greaterThan(0))))
          .andExpect(jsonPath("$[0].ramoAtividade", equalTo("Italiana")))
          .andExpect(jsonPath("$[0].ativo", equalTo(true)))
          .andExpect(jsonPath("$[0].nome", equalTo("Restaurante Italiano")));
    }

    @Test
    @DisplayName("✅ Deve listar todos os restaurantes")
    @Transactional
    void testListAllRestaurantes() throws Exception {
        mockMvc.perform(post("/api/restaurantes")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

        mockMvc.perform(get("/api/restaurantes")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", isA(java.util.ArrayList.class)));
    }
  }

  @Nested
  @DisplayName("GET /api/restaurantes/{id} - Buscar Restaurante")
  class GetRestauranteTests {

    @Test
    @DisplayName("✅ Deve buscar restaurante por ID")
    @Transactional
    void testGetRestauranteSuccess() throws Exception {
      var createResponse = mockMvc.perform(post("/api/restaurantes")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andReturn()
          .getResponse()
          .getContentAsString();

      Long restauranteId = objectMapper.readTree(createResponse).get("id").asLong();

      mockMvc.perform(get("/api/restaurantes/" + restauranteId)
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id", equalTo(restauranteId.intValue())));
    }
  }
}
