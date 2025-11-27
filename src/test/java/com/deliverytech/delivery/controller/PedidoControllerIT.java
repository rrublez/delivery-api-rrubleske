package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.config.IntegrationTestDataConfig;
import com.deliverytech.delivery.dto.pedido.request.AtualizarStatusPedidoRequest;
import com.deliverytech.delivery.dto.pedido.request.CalcularPedidoRequest;
import com.deliverytech.delivery.dto.pedido.request.PedidoRequest;
import com.deliverytech.delivery.dto.shared.request.PedidoProdutoRequest;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.entity.Role;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.support.IntegrationTestDataFactory;
import com.deliverytech.delivery.support.TestAuthHelper;
import com.deliverytech.delivery.support.TestAuthHelper.AuthSession;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.closeTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)
@ActiveProfiles("test")
@Import(IntegrationTestDataConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
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

  @Autowired
  private IntegrationTestDataFactory dataFactory;

  private String adminToken;
  private AuthSession clienteSession;
  private Cliente cliente;
  private Restaurante restaurante;
  private Produto produto;

  @BeforeEach
  void setUp() throws Exception {
    pedidoRepository.deleteAll();
    produtoRepository.deleteAll();
    restauranteRepository.deleteAll();
    clienteRepository.deleteAll();

    clienteSession = TestAuthHelper.registerAndLoginWithEmail(mockMvc, objectMapper, Role.CLIENTE);
    adminToken = TestAuthHelper.registerAndLogin(mockMvc, objectMapper, Role.ADMIN);

    cliente = dataFactory.criarCliente(
        "Cliente Pedido",
        clienteSession.email(),
        "11922223333",
        dataFactory.generateCpf(),
        true);
    restaurante = dataFactory.criarRestaurante("Restaurante Teste");
    produto = dataFactory.criarProduto(restaurante, "Combo Teste", BigDecimal.valueOf(42.00), 5);
  }

  private String bearer(String token) {
    return "Bearer " + token;
  }

  private PedidoRequest buildPedidoRequest(int quantidade) {
    PedidoProdutoRequest item = dataFactory.buildPedidoItem(produto.getId(), quantidade, produto.getPreco(), "Sem cebola");
    return dataFactory.buildPedidoRequest(cliente.getId(), restaurante.getId(), List.of(item));
  }

  private Long criarPedido(PedidoRequest request, String token) throws Exception {
    String payload = mockMvc.perform(post("/api/pedidos")
            .header("Authorization", bearer(token))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();
    JsonNode node = objectMapper.readTree(payload);
    return node.get("id").asLong();
  }

  @Test
  @DisplayName("✅ Deve criar pedido completo e retornar valor total")
  void deveCriarPedidoComSucesso() throws Exception {
    PedidoRequest request = buildPedidoRequest(2);
    BigDecimal expectedTotal = produto.getPreco().multiply(BigDecimal.valueOf(2));

    mockMvc.perform(post("/api/pedidos")
            .header("Authorization", bearer(clienteSession.token()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.cliente.email", equalTo(cliente.getEmail())))
        .andExpect(jsonPath("$.restaurante.id", equalTo(restaurante.getId().intValue())))
        .andExpect(jsonPath("$.valorTotal", closeTo(expectedTotal.doubleValue(), 0.01)))
        .andExpect(jsonPath("$.itens[0].produto.id", equalTo(produto.getId().intValue())));
  }

  @Test
  @DisplayName("❌ Deve retornar 404 quando produto não existe")
  void deveRetornar404ParaProdutoInexistente() throws Exception {
    PedidoProdutoRequest item = dataFactory.buildPedidoItem(Long.MAX_VALUE, 1, produto.getPreco(), "")
        ;
    PedidoRequest request = dataFactory.buildPedidoRequest(cliente.getId(), restaurante.getId(), List.of(item));

    mockMvc.perform(post("/api/pedidos")
            .header("Authorization", bearer(clienteSession.token()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorType", equalTo("Not Found")))
        .andExpect(jsonPath("$.message", containsString("Produto não encontrado")))
        .andExpect(jsonPath("$.success", equalTo(false)));
  }

  @Test
  @DisplayName("❌ Deve retornar 400 quando estoque é insuficiente")
  void deveRetornarErroParaEstoqueInsuficiente() throws Exception {
    PedidoRequest request = buildPedidoRequest(produto.getEstoque() + 1);

    mockMvc.perform(post("/api/pedidos")
            .header("Authorization", bearer(clienteSession.token()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorType", equalTo("Bad Request")))
        .andExpect(jsonPath("$.message", containsString("Estoque insuficiente")))
        .andExpect(jsonPath("$.success", equalTo(false)));
  }

  @Test
  @DisplayName("✅ Deve retornar histórico do cliente")
  void deveRetornarHistoricoDoCliente() throws Exception {
    PedidoRequest request = buildPedidoRequest(1);
    criarPedido(request, clienteSession.token());

    mockMvc.perform(get("/api/pedidos/cliente/{clienteId}", cliente.getId())
            .header("Authorization", bearer(clienteSession.token())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].cliente.email", equalTo(cliente.getEmail())));
  }

  @Test
  @DisplayName("✅ Deve atualizar status do pedido")
  void deveAtualizarStatusPedido() throws Exception {
    PedidoRequest request = buildPedidoRequest(1);
    Long pedidoId = criarPedido(request, clienteSession.token());
    AtualizarStatusPedidoRequest body = new AtualizarStatusPedidoRequest("ENTREGUE");

    mockMvc.perform(patch("/api/pedidos/{id}/status", pedidoId)
            .header("Authorization", bearer(adminToken))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("ENTREGUE")));
  }

  @Test
  @DisplayName("✅ Deve calcular valor total considerando taxa")
  void deveCalcularValorTotal() throws Exception {
    PedidoProdutoRequest item = dataFactory.buildPedidoItem(produto.getId(), 1, produto.getPreco(), "Sem cebola");
    CalcularPedidoRequest request = dataFactory.buildCalcularRequest(restaurante.getId(), List.of(item));
    BigDecimal subtotal = produto.getPreco();
    BigDecimal taxa = restaurante.getTaxaEntrega();

    mockMvc.perform(post("/api/pedidos/calcular")
            .header("Authorization", bearer(adminToken))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.subtotal", closeTo(subtotal.doubleValue(), 0.01)))
        .andExpect(jsonPath("$.taxaEntrega", equalTo(taxa.doubleValue())))
        .andExpect(jsonPath("$.valorTotal", closeTo(subtotal.add(taxa).doubleValue(), 0.01)));
  }
}
