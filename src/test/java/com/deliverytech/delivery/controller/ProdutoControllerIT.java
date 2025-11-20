package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.produto.request.ProdutoRequest;
import com.deliverytech.delivery.entity.Role;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.support.TestAuthHelper;
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
 * Testes de integração para ProdutoController
 * Cobre CRUD completo de produtos com validações
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("ProdutoController - Testes de Integração")
class ProdutoControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ProdutoRepository produtoRepository;

  @Autowired
  private RestauranteRepository restauranteRepository;

  private ProdutoRequest validRequest;
  private Restaurante restaurante;
  private Long restauranteId;
  private String adminToken;
  private String restauranteToken;

  @BeforeEach
  void setUp() {
    restauranteRepository.deleteAll();
    produtoRepository.deleteAll();

    // Criar restaurante usando setters (sem @Builder)
    restaurante = new Restaurante();
    restaurante.setNome("Pizzaria Central");
    restaurante.setEndereco("Rua Principal, 100");
    // Garantir unicidade para evitar colisões em execuções consecutivas
    String uniqueSuffix = String.valueOf(System.nanoTime());
    String uniqueTelefone = ("11" + uniqueSuffix).substring(0, Math.min(11 + uniqueSuffix.length(), 11));
    String uniqueCnpj = (uniqueSuffix + "00000000000000").substring(0, 14);
    restaurante.setTelefone(uniqueTelefone);
    restaurante.setCnpj(uniqueCnpj);
    restaurante.setRamoAtividade("Pizzaria");
    restaurante.setAtivo(true);
    restaurante.setTaxaEntrega(BigDecimal.valueOf(5.00));
    restaurante = restauranteRepository.save(restaurante);
    restauranteId = restaurante.getId();

    validRequest = new ProdutoRequest();
    validRequest.setNome("Pizza Margherita");
    validRequest.setDescricao("Pizza tradicional com molho de tomate");
    validRequest.setPreco(BigDecimal.valueOf(35.00));
    validRequest.setCategoria("Pizza");
    validRequest.setDisponivel(true);

    try {
      adminToken = TestAuthHelper.registerAndLogin(mockMvc, objectMapper, Role.ADMIN);
      restauranteToken = TestAuthHelper.registerAndLogin(mockMvc, objectMapper, Role.RESTAURANTE, restauranteId);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Nested
  @DisplayName("POST /api/produtos - Criar Produto")
  class CreateProdutoTests {

    @Test
    @DisplayName("✅ Deve criar produto com sucesso - Status 201")
    @Transactional
    void testCreateProdutoSuccess() throws Exception {
      mockMvc.perform(post("/api/produtos")
          .header("Authorization", "Bearer " + restauranteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id", notNullValue()))
          .andExpect(jsonPath("$.nome", equalTo("Pizza Margherita")))
          .andExpect(jsonPath("$.preco", equalTo(35.00)))
          .andExpect(jsonPath("$.disponivel", equalTo(true)));
    }

    @Test
    @DisplayName("❌ Deve retornar 400 quando nome está vazio")
    void testCreateProdutoWithoutName() throws Exception {
      validRequest.setNome("");

      mockMvc.perform(post("/api/produtos")
          .header("Authorization", "Bearer " + restauranteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("❌ Deve retornar 400 quando preço é negativo")
    void testCreateProdutoWithNegativePrice() throws Exception {
      validRequest.setPreco(BigDecimal.valueOf(-10.00));

      mockMvc.perform(post("/api/produtos")
          .header("Authorization", "Bearer " + restauranteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isBadRequest());
    }
  }

  @Nested
  @DisplayName("GET /api/produtos/{id} - Buscar Produto")
  class GetProdutoTests {

    @Test
    @DisplayName("✅ Deve buscar produto existente - Status 200")
    @Transactional
    void testGetProdutoSuccess() throws Exception {
      // Criar produto
      var response = mockMvc.perform(post("/api/produtos")
          .header("Authorization", "Bearer " + restauranteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andReturn()
          .getResponse()
          .getContentAsString();

      Long produtoId = objectMapper.readTree(response).get("id").asLong();

      // Buscar produto
        mockMvc.perform(get("/api/produtos/" + produtoId)
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id", equalTo(produtoId.intValue())))
          .andExpect(jsonPath("$.nome", equalTo("Pizza Margherita")));
    }

    @Test
    @DisplayName("❌ Deve retornar 404 quando produto não existe")
    void testGetProdutoNotFound() throws Exception {
      mockMvc.perform(get("/api/produtos/9999")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("PUT /api/produtos/{id} - Atualizar Produto")
  class UpdateProdutoTests {

    @Test
    @DisplayName("✅ Deve atualizar produto com sucesso - Status 200")
    @Transactional
    void testUpdateProdutoSuccess() throws Exception {
      // Criar produto
      var createResponse = mockMvc.perform(post("/api/produtos")
          .header("Authorization", "Bearer " + restauranteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andReturn()
          .getResponse()
          .getContentAsString();

      Long produtoId = objectMapper.readTree(createResponse).get("id").asLong();

      // Atualizar
      ProdutoRequest updateRequest = validRequest;
      updateRequest.setNome("Pizza Quattro Formaggi");
      updateRequest.setPreco(BigDecimal.valueOf(45.00));

        mockMvc.perform(put("/api/produtos/" + produtoId)
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(updateRequest)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.nome", equalTo("Pizza Quattro Formaggi")))
          .andExpect(jsonPath("$.preco", equalTo(45.00)));
    }

    @Test
    @DisplayName("❌ Deve retornar 404 quando produto não existe")
    void testUpdateProdutoNotFound() throws Exception {
      mockMvc.perform(put("/api/produtos/9999")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("DELETE /api/produtos/{id} - Deletar Produto")
  class DeleteProdutoTests {

    @Test
    @DisplayName("✅ Deve deletar produto com sucesso - Status 204")
    @Transactional
    void testDeleteProdutoSuccess() throws Exception {
      // Criar produto
      var createResponse = mockMvc.perform(post("/api/produtos")
          .header("Authorization", "Bearer " + restauranteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andReturn()
          .getResponse()
          .getContentAsString();

      Long produtoId = objectMapper.readTree(createResponse).get("id").asLong();

      // Deletar
        mockMvc.perform(delete("/api/produtos/" + produtoId)
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent());

      // Verificar se foi deletado
        mockMvc.perform(get("/api/produtos/" + produtoId)
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("❌ Deve retornar 404 quando produto não existe")
    void testDeleteProdutoNotFound() throws Exception {
      mockMvc.perform(delete("/api/produtos/9999")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("PATCH /api/produtos/{id}/disponibilidade - Atualizar Disponibilidade")
  class UpdateAvailabilityTests {

    @Test
    @DisplayName("✅ Deve atualizar disponibilidade - Status 200")
    @Transactional
    void testUpdateAvailabilitySuccess() throws Exception {
      // Criar produto
      var createResponse = mockMvc.perform(post("/api/produtos")
          .header("Authorization", "Bearer " + restauranteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andReturn()
          .getResponse()
          .getContentAsString();

      Long produtoId = objectMapper.readTree(createResponse).get("id").asLong();

      // Desativar
      String requestBody = "{\"disponivel\": false}";
        mockMvc.perform(patch("/api/produtos/" + produtoId + "/disponibilidade")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(requestBody))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.disponivel", equalTo(false)));
    }
  }

  @Nested
  @DisplayName("GET /api/produtos/* - Filtros e Buscas")
  class FilterAndSearchTests {

    @Test
    @DisplayName("✅ Deve listar produtos disponíveis")
    @Transactional
    void testListAvailableProducts() throws Exception {
      // Criar produto
      mockMvc.perform(post("/api/produtos")
        .header("Authorization", "Bearer " + restauranteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Listar disponíveis
      mockMvc.perform(get("/api/produtos/disponivel")
        .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("✅ Deve buscar produtos por categoria")
    @Transactional
    void testSearchByCategory() throws Exception {
      // Criar produto
      mockMvc.perform(post("/api/produtos")
        .header("Authorization", "Bearer " + restauranteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Buscar por categoria
      mockMvc.perform(get("/api/produtos/categoria/Pizza")
        .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("✅ Deve buscar produtos por nome")
    @Transactional
    void testSearchByName() throws Exception {
      // Criar produto
      mockMvc.perform(post("/api/produtos")
        .header("Authorization", "Bearer " + restauranteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Buscar por nome
      mockMvc.perform(get("/api/produtos/buscar?nome=Pizza")
        .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("✅ Deve listar produtos por restaurante")
    @Transactional
    void testListByRestaurante() throws Exception {
      // Criar produto
      var response = mockMvc.perform(post("/api/produtos")
          .header("Authorization", "Bearer " + restauranteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andReturn()
          .getResponse()
          .getContentAsString();

      Long produtoId = objectMapper.readTree(response).get("id").asLong();

      // Vincular produto ao restaurante
      var produto = produtoRepository.findById(produtoId).orElseThrow();
      var rest = restauranteRepository.findById(restauranteId).orElseThrow();
      rest.getProdutos().add(produto);
      produto.getRestaurantes().add(rest);
      restauranteRepository.save(rest);

      // Listar por restaurante
      mockMvc.perform(get("/api/produtos/restaurante/" + restauranteId)
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }
  }
}
