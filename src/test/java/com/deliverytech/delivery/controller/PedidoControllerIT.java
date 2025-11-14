package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.pedido.request.PedidoRequest;
import com.deliverytech.delivery.dto.pedido.request.AtualizarStatusPedidoRequest;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
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
import java.time.LocalDateTime;
import java.util.HashSet;

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

  @BeforeEach
  void setUp() {
    clienteRepository.deleteAll();
    restauranteRepository.deleteAll();
    produtoRepository.deleteAll();
    pedidoRepository.deleteAll();

    // Criar cliente com dados únicos
    cliente = new Cliente();
    cliente.setNome("João Silva");
    cliente.setEmail("joao@test" + System.currentTimeMillis() + ".com");
    cliente.setCpf("1234567890" + (System.currentTimeMillis() % 10));
    cliente.setTelefone("11999999999");
    cliente.setAtivo(true);
    cliente = clienteRepository.save(cliente);

    // Criar restaurante com dados únicos
    restaurante = new Restaurante();
    restaurante.setNome("Pizzaria Central");
    restaurante.setEndereco("Rua Principal, 100");
    restaurante.setTelefone("113333333" + (System.currentTimeMillis() % 100));
    restaurante.setCnpj("1234567800019" + (System.currentTimeMillis() % 10));
    restaurante.setRamoAtividade("Pizzaria");
    restaurante.setAtivo(true);
    restaurante.setTaxaEntrega(BigDecimal.valueOf(5.00));
    restaurante = restauranteRepository.save(restaurante);

    // Criar produto
    produto = new Produto();
    produto.setNome("Pizza Margherita");
    produto.setDescricao("Pizza tradicional");
    produto.setPreco(BigDecimal.valueOf(35.00));
    produto.setDisponivel(true);
    produto.setCategoria("Pizza");
    produto = produtoRepository.save(produto);

    // Request padrão
    validRequest = new PedidoRequest();
    validRequest.setNumeroPedido("PED-2025-" + (System.currentTimeMillis() % 100));
    validRequest.setClienteId(cliente.getId());
    validRequest.setRestauranteId(restaurante.getId());
    validRequest.setStatus("PENDENTE");
    validRequest.setItens(new java.util.ArrayList<>());
  }

  @Nested
  @DisplayName("POST /api/pedidos - Criar Pedido")
  class CreatePedidoTests {

    @Test
    @DisplayName("✅ Deve criar pedido com sucesso - Status 201")
    @Transactional
    void testCreatePedidoSuccess() throws Exception {
      mockMvc.perform(post("/api/pedidos")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id", notNullValue()))
          .andExpect(jsonPath("$.status", equalTo("PENDENTE")))
          .andExpect(jsonPath("$.enderecoEntrega", equalTo("Rua B, 200")));
    }

    @Test
    @DisplayName("❌ Deve retornar 400 quando cliente não existe")
    void testCreatePedidoWithInvalidClient() throws Exception {
      validRequest.setClienteId(9999L);

      mockMvc.perform(post("/api/pedidos")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("❌ Deve retornar 400 quando restaurante não existe")
    void testCreatePedidoWithInvalidRestaurante() throws Exception {
      validRequest.setRestauranteId(9999L);

      mockMvc.perform(post("/api/pedidos")
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
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andReturn()
          .getResponse()
          .getContentAsString();

      Long pedidoId = objectMapper.readTree(createResponse).get("id").asLong();

      // Buscar pedido
      mockMvc.perform(get("/api/pedidos/" + pedidoId)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id", equalTo(pedidoId.intValue())))
          .andExpect(jsonPath("$.status", equalTo("PENDENTE")));
    }

    @Test
    @DisplayName("❌ Deve retornar 404 quando pedido não existe")
    void testGetPedidoNotFound() throws Exception {
      mockMvc.perform(get("/api/pedidos/9999")
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
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Listar
      mockMvc.perform(get("/api/pedidos")
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
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Listar por cliente
      mockMvc.perform(get("/api/pedidos/clientes/" + cliente.getId())
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
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Listar por restaurante
      mockMvc.perform(get("/api/pedidos/restaurantes/" + restaurante.getId())
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
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Listar por status
      mockMvc.perform(get("/api/pedidos/status/PENDENTE")
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
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andReturn()
          .getResponse()
          .getContentAsString();

      Long pedidoId = objectMapper.readTree(createResponse).get("id").asLong();

      // Cancelar
      mockMvc.perform(delete("/api/pedidos/" + pedidoId)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("❌ Deve retornar 404 quando pedido não existe")
    void testCancelPedidoNotFound() throws Exception {
      mockMvc.perform(delete("/api/pedidos/9999")
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
      String calcRequest = "{\"clienteId\": " + cliente.getId() + 
                          ", \"restauranteId\": " + restaurante.getId() + 
                          ", \"enderecoEntrega\": \"Rua C, 300\"}";

      mockMvc.perform(post("/api/pedidos/calcular")
          .contentType(MediaType.APPLICATION_JSON)
          .content(calcRequest))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.subtotal", notNullValue()))
          .andExpect(jsonPath("$.taxaEntrega", notNullValue()))
          .andExpect(jsonPath("$.total", notNullValue()));
    }
  }
}
