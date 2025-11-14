package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.restaurante.request.RestauranteRequest;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * Cobre CRUD, filtros e paginação
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

  @BeforeEach
  void setUp() {
    restauranteRepository.deleteAll();

    validRequest = new RestauranteRequest();
    validRequest.setNome("Pizzaria Central");
    validRequest.setEndereco("Rua Principal, 100");
    validRequest.setTelefone("1133333" + (System.currentTimeMillis() % 1000));
    validRequest.setCnpj("1234567800019" + (System.currentTimeMillis() % 10));
    validRequest.setRamoAtividade("Pizzaria");
    validRequest.setAtivo(true);
    validRequest.setTaxaEntrega(BigDecimal.valueOf(5.00));
  }

  @Nested
  @DisplayName("POST /api/restaurantes - Criar Restaurante")
  class CreateRestauranteTests {

    @Test
    @DisplayName("✅ Deve criar restaurante com sucesso - Status 201")
    @Transactional
    void testCreateRestauranteSuccess() throws Exception {
      mockMvc.perform(post("/api/restaurantes")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id", notNullValue()))
          .andExpect(jsonPath("$.nome", equalTo("Pizzaria Central")))
          .andExpect(jsonPath("$.ramoAtividade", equalTo("Pizzaria")))
          .andExpect(jsonPath("$.ativo", equalTo(true)));
    }

    @Test
    @DisplayName("❌ Deve retornar 400 quando nome está vazio")
    void testCreateRestauranteWithoutName() throws Exception {
      validRequest.setNome("");

      mockMvc.perform(post("/api/restaurantes")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("❌ Deve retornar 409 quando CNPJ já está registrado")
    @Transactional
    void testCreateRestauranteWithDuplicateCNPJ() throws Exception {
      // Criar primeiro
      mockMvc.perform(post("/api/restaurantes")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Tentar criar com mesmo CNPJ
      RestauranteRequest duplicateRequest = new RestauranteRequest();
      duplicateRequest.setNome("Outra Pizzaria");
      duplicateRequest.setEndereco("Rua Secundária, 200");
      duplicateRequest.setTelefone("1144444444");
      duplicateRequest.setCnpj("12345678000190"); // mesmo CNPJ
      duplicateRequest.setRamoAtividade("Pizzaria");
      duplicateRequest.setAtivo(true);
      duplicateRequest.setTaxaEntrega(BigDecimal.valueOf(5.00));

      mockMvc.perform(post("/api/restaurantes")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(duplicateRequest)))
          .andExpect(status().isConflict());
    }
  }

  @Nested
  @DisplayName("GET /api/restaurantes/{id} - Buscar Restaurante")
  class GetRestauranteTests {

    @Test
    @DisplayName("✅ Deve buscar restaurante existente - Status 200")
    @Transactional
    void testGetRestauranteSuccess() throws Exception {
      // Criar restaurante
      var createResponse = mockMvc.perform(post("/api/restaurantes")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andReturn()
          .getResponse()
          .getContentAsString();

      Long restauranteId = objectMapper.readTree(createResponse).get("id").asLong();

      // Buscar
      mockMvc.perform(get("/api/restaurantes/" + restauranteId)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id", equalTo(restauranteId.intValue())))
          .andExpect(jsonPath("$.nome", equalTo("Pizzaria Central")));
    }

    @Test
    @DisplayName("❌ Deve retornar 404 quando restaurante não existe")
    void testGetRestauranteNotFound() throws Exception {
      mockMvc.perform(get("/api/restaurantes/9999")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("GET /api/restaurantes - Listar com Filtros")
  class ListRestaurantesTests {

    @Test
    @DisplayName("✅ Deve listar todos os restaurantes - Status 200")
    @Transactional
    void testListAllRestaurantes() throws Exception {
      // Criar restaurante
      mockMvc.perform(post("/api/restaurantes")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Listar
      mockMvc.perform(get("/api/restaurantes")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", isA(java.util.ArrayList.class)));
    }

    @Test
    @DisplayName("✅ Deve listar restaurantes por ramo - Status 200")
    @Transactional
    void testListByRamo() throws Exception {
      // Criar restaurante
      mockMvc.perform(post("/api/restaurantes")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Listar por ramo
      mockMvc.perform(get("/api/restaurantes?ramo=Pizzaria")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("✅ Deve listar restaurantes ativos - Status 200")
    @Transactional
    void testListActive() throws Exception {
      // Criar restaurante
      mockMvc.perform(post("/api/restaurantes")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Listar ativos
      mockMvc.perform(get("/api/restaurantes?ativo=true")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("✅ Deve listar restaurantes por categoria - Status 200")
    @Transactional
    void testListByCategory() throws Exception {
      // Criar restaurante
      mockMvc.perform(post("/api/restaurantes")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Listar por categoria
      mockMvc.perform(get("/api/restaurantes/categoria/Pizzaria")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("✅ Deve listar restaurantes com taxa máxima - Status 200")
    @Transactional
    void testListByMaxTaxa() throws Exception {
      // Criar restaurante
      mockMvc.perform(post("/api/restaurantes")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Listar com taxa <= 10
      mockMvc.perform(get("/api/restaurantes/taxa-maxima?taxa=10")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }
  }

  @Nested
  @DisplayName("PUT /api/restaurantes/{id} - Atualizar Restaurante")
  class UpdateRestauranteTests {

    @Test
    @DisplayName("✅ Deve atualizar restaurante com sucesso - Status 200")
    @Transactional
    void testUpdateRestauranteSuccess() throws Exception {
      // Criar restaurante
      var createResponse = mockMvc.perform(post("/api/restaurantes")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andReturn()
          .getResponse()
          .getContentAsString();

      Long restauranteId = objectMapper.readTree(createResponse).get("id").asLong();

      // Atualizar
      RestauranteRequest updateRequest = new RestauranteRequest();
      updateRequest.setNome("Pizzaria Premium");
      updateRequest.setEndereco("Rua Principal, 150");
      updateRequest.setTelefone("1133333334");
      updateRequest.setCnpj("12345678000190");
      updateRequest.setRamoAtividade("Pizzaria");
      updateRequest.setAtivo(true);
      updateRequest.setTaxaEntrega(BigDecimal.valueOf(7.00));

      mockMvc.perform(put("/api/restaurantes/" + restauranteId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(updateRequest)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.nome", equalTo("Pizzaria Premium")))
          .andExpect(jsonPath("$.taxaEntrega", equalTo(7.00)));
    }

    @Test
    @DisplayName("❌ Deve retornar 404 quando restaurante não existe")
    void testUpdateRestauranteNotFound() throws Exception {
      mockMvc.perform(put("/api/restaurantes/9999")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("PATCH /api/restaurantes/{id}/status - Atualizar Status")
  class UpdateStatusTests {

    @Test
    @DisplayName("✅ Deve atualizar status do restaurante - Status 200")
    @Transactional
    void testUpdateStatusSuccess() throws Exception {
      // Criar restaurante
      var createResponse = mockMvc.perform(post("/api/restaurantes")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andReturn()
          .getResponse()
          .getContentAsString();

      Long restauranteId = objectMapper.readTree(createResponse).get("id").asLong();

      // Desativar
      String updateRequest = "{\"ativo\": false}";
      mockMvc.perform(patch("/api/restaurantes/" + restauranteId + "/status")
          .contentType(MediaType.APPLICATION_JSON)
          .content(updateRequest))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.ativo", equalTo(false)));
    }
  }
}
