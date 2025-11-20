package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.pedido.request.PedidoRequest;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.entity.Role;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
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
 * Testes de integração para PedidoController
 * Cobre CRUD de pedidos com cenários complexos
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("PedidoController - Testes de Integração")
class PedidoControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private PedidoRepository pedidoRepository;

  @Autowired
  private ClienteRepository clienteRepository;

  @Autowired
  private RestauranteRepository restauranteRepository;

  @Autowired
  private ProdutoRepository produtoRepository;

  private Cliente cliente;
  private Restaurante restaurante;
  private Produto produto;
  private PedidoRequest validRequest;
  private String clienteToken;
  private String adminToken;

    @BeforeEach
  void setUp() {
    pedidoRepository.deleteAll();
    produtoRepository.deleteAll();
    restauranteRepository.deleteAll();
    clienteRepository.deleteAll();

    // Criar cliente usando setters (dados únicos para evitar colisões)
    String unique = String.valueOf(System.nanoTime());
    String uniqueCpf = (unique + "00000000000").substring(0, 11);
    String uniqueEmail = "joao.pedido+" + unique + "@test.com";
    String uniqueTelefone = ("11" + unique).substring(0, 11);

    cliente = new Cliente();
    cliente.setNome("João Silva");
    cliente.setEmail(uniqueEmail);
    cliente.setCpf(uniqueCpf);
    cliente.setTelefone(uniqueTelefone);
    cliente.setAtivo(true);
    cliente = clienteRepository.save(cliente);

    // Criar restaurante usando setters
    restaurante = new Restaurante();
    restaurante.setNome("Pizzaria Central");
    restaurante.setEndereco("Rua Principal, 100");
    String restTelefone = ("11" + (unique + "0")).substring(0, 11);
    String restCnpj = (unique + "00000000000000").substring(0, 14);
    restaurante.setTelefone(restTelefone);
    restaurante.setCnpj(restCnpj);
    restaurante.setRamoAtividade("Pizzaria");
    restaurante.setAtivo(true);
    restaurante.setTaxaEntrega(BigDecimal.valueOf(5.00));
    restaurante = restauranteRepository.save(restaurante);

    // Criar produto usando setters
    produto = new Produto();
    produto.setNome("Pizza Margherita");
    produto.setDescricao("Pizza tradicional");
    produto.setPreco(BigDecimal.valueOf(35.00));
    produto.setDisponivel(true);
    produto.setCategoria("Pizza");
    produto = produtoRepository.save(produto);

    // Request padrão
    validRequest = new PedidoRequest();
    validRequest.setNumeroPedido("PED-2025-001");
    validRequest.setClienteId(cliente.getId());
    validRequest.setRestauranteId(restaurante.getId());
    validRequest.setStatus("PENDENTE");
    
    // Criar itens do pedido
    java.util.List<com.deliverytech.delivery.dto.shared.request.PedidoProdutoRequest> itens = new java.util.ArrayList<>();
    com.deliverytech.delivery.dto.shared.request.PedidoProdutoRequest item = new com.deliverytech.delivery.dto.shared.request.PedidoProdutoRequest();
    item.setProdutoId(produto.getId());
    item.setQuantidade(2);
    item.setPrecoUnitario(produto.getPreco());
    itens.add(item);
    validRequest.setItens(itens);

    try {
      // Create tokens for CLIENTE and ADMIN roles
      clienteToken = TestAuthHelper.registerAndLogin(mockMvc, objectMapper, Role.CLIENTE);
      adminToken = TestAuthHelper.registerAndLogin(mockMvc, objectMapper, Role.ADMIN);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Nested
  @DisplayName("POST /api/pedidos - Criar Pedido")
  class CreatePedidoTests {

        @Test
    @DisplayName("✅ Deve criar pedido com sucesso - Status 201")
    @Transactional
    void testCreatePedidoSuccess() throws Exception {
      mockMvc.perform(post("/api/pedidos")
          .header("Authorization", "Bearer " + clienteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id", notNullValue()))
          .andExpect(jsonPath("$.status", equalTo("PENDENTE")));
    }

    @Test
    @DisplayName("❌ Deve retornar 400 quando cliente não existe")
    void testCreatePedidoWithInvalidClient() throws Exception {
      validRequest.setClienteId(9999L);

      mockMvc.perform(post("/api/pedidos")
          .header("Authorization", "Bearer " + clienteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("❌ Deve retornar 400 quando restaurante não existe")
    void testCreatePedidoWithInvalidRestaurante() throws Exception {
      validRequest.setRestauranteId(9999L);

      mockMvc.perform(post("/api/pedidos")
          .header("Authorization", "Bearer " + clienteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isBadRequest());
    }
  }

  @Nested
  @DisplayName("GET /api/pedidos/{id} - Buscar Pedido")
  class GetPedidoTests {

    @Test
    @DisplayName("✅ Deve buscar pedido existente - Status 200")
    @Transactional
    void testGetPedidoSuccess() throws Exception {
      // Criar pedido
      var createResponse = mockMvc.perform(post("/api/pedidos")
          .header("Authorization", "Bearer " + clienteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andReturn()
          .getResponse()
          .getContentAsString();

      Long pedidoId = objectMapper.readTree(createResponse).get("id").asLong();

      // Buscar pedido
        mockMvc.perform(get("/api/pedidos/" + pedidoId)
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id", equalTo(pedidoId.intValue())))
          .andExpect(jsonPath("$.status", equalTo("PENDENTE")));
    }

    @Test
    @DisplayName("❌ Deve retornar 404 quando pedido não existe")
    void testGetPedidoNotFound() throws Exception {
      mockMvc.perform(get("/api/pedidos/9999")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("GET /api/pedidos - Listar com Filtros")
  class ListPedidosTests {

    @Test
    @DisplayName("✅ Deve listar todos os pedidos - Status 200")
    @Transactional
    void testListAllPedidos() throws Exception {
      // Criar pedido
      mockMvc.perform(post("/api/pedidos")
        .header("Authorization", "Bearer " + clienteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Listar
      mockMvc.perform(get("/api/pedidos")
        .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", isA(java.util.ArrayList.class)));
    }

    @Test
    @DisplayName("✅ Deve listar pedidos por cliente - Status 200")
    @Transactional
    void testListByCliente() throws Exception {
      // Criar pedido
      mockMvc.perform(post("/api/pedidos")
        .header("Authorization", "Bearer " + clienteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Listar por cliente
      mockMvc.perform(get("/api/pedidos/clientes/" + cliente.getId())
        .header("Authorization", "Bearer " + clienteToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("✅ Deve listar pedidos por restaurante - Status 200")
    @Transactional
    void testListByRestaurante() throws Exception {
      // Criar pedido
      mockMvc.perform(post("/api/pedidos")
        .header("Authorization", "Bearer " + clienteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Listar por restaurante
      mockMvc.perform(get("/api/pedidos/restaurantes/" + restaurante.getId())
        .header("Authorization", "Bearer " + clienteToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("✅ Deve listar pedidos por status - Status 200")
    @Transactional
    void testListByStatus() throws Exception {
      // Criar pedido
      mockMvc.perform(post("/api/pedidos")
        .header("Authorization", "Bearer " + clienteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Listar por status
      mockMvc.perform(get("/api/pedidos/status/PENDENTE")
        .header("Authorization", "Bearer " + clienteToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }
  }

  @Nested
  @DisplayName("PATCH /api/pedidos/{id}/status - Atualizar Status")
  class UpdateStatusTests {

    @Test
    @DisplayName("✅ Deve atualizar status do pedido - Status 200")
    @Transactional
    void testUpdateStatusSuccess() throws Exception {
      // Criar pedido
      var createResponse = mockMvc.perform(post("/api/pedidos")
          .header("Authorization", "Bearer " + clienteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andReturn()
          .getResponse()
          .getContentAsString();

      Long pedidoId = objectMapper.readTree(createResponse).get("id").asLong();

      // Atualizar status
      String updateRequest = "{\"status\": \"ENTREGUE\"}";
        mockMvc.perform(patch("/api/pedidos/" + pedidoId + "/status")
          .header("Authorization", "Bearer " + clienteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(updateRequest))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.status", equalTo("ENTREGUE")));
    }

    @Test
    @DisplayName("❌ Deve retornar 404 quando pedido não existe")
    void testUpdateStatusNotFound() throws Exception {
      String updateRequest = "{\"status\": \"ENTREGUE\"}";
      mockMvc.perform(patch("/api/pedidos/9999/status")
          .header("Authorization", "Bearer " + clienteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(updateRequest))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("DELETE /api/pedidos/{id} - Cancelar Pedido")
  class CancelPedidoTests {

    @Test
    @DisplayName("✅ Deve cancelar pedido com sucesso - Status 204")
    @Transactional
    void testCancelPedidoSuccess() throws Exception {
      // Criar pedido
      var createResponse = mockMvc.perform(post("/api/pedidos")
          .header("Authorization", "Bearer " + clienteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andReturn()
          .getResponse()
          .getContentAsString();

      Long pedidoId = objectMapper.readTree(createResponse).get("id").asLong();

      // Cancelar
        mockMvc.perform(delete("/api/pedidos/" + pedidoId)
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("❌ Deve retornar 404 quando pedido não existe")
    void testCancelPedidoNotFound() throws Exception {
      mockMvc.perform(delete("/api/pedidos/9999")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("POST /api/pedidos/calcular - Calcular Total")
  class CalculateTotalTests {

    @Test
    @DisplayName("✅ Deve calcular total do pedido - Status 200")
    void testCalculateTotal() throws Exception {
      String calcRequest = "{" +
          "\"restauranteId\": " + restaurante.getId() + "," +
          "\"itens\": [" +
            "{" +
              "\"produtoId\": " + produto.getId() + "," +
              "\"quantidade\": 1," +
              "\"precoUnitario\": " + produto.getPreco() +
            "}" +
          "]" +
        "}";

      mockMvc.perform(post("/api/pedidos/calcular")
          .header("Authorization", "Bearer " + clienteToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(calcRequest))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.subtotal", notNullValue()))
          .andExpect(jsonPath("$.taxaEntrega", notNullValue()))
          .andExpect(jsonPath("$.total", notNullValue()));
    }
  }
}
