package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.config.IntegrationTestDataConfig;
import com.deliverytech.delivery.dto.cliente.request.ClienteRequest;
import com.deliverytech.delivery.dto.cliente.request.ClienteUpdateRequest;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Role;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.support.IntegrationTestDataFactory;
import com.deliverytech.delivery.support.TestAuthHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)
@ActiveProfiles("test")
@Import(IntegrationTestDataConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("ClienteController - Testes de Integração")
class ClienteControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ClienteRepository clienteRepository;

  @Autowired
  private IntegrationTestDataFactory dataFactory;

  private String adminToken;

  @BeforeEach
  void setUp() throws Exception {
    clienteRepository.deleteAll();
    adminToken = TestAuthHelper.registerAndLogin(mockMvc, objectMapper, Role.ADMIN);
  }

  private String bearer(String token) {
    return "Bearer " + token;
  }

  @Test
  @DisplayName("✅ Deve criar cliente com sucesso")
  void deveCriarClienteComSucesso() throws Exception {
    ClienteRequest request = dataFactory.buildClienteRequest();

    mockMvc.perform(post("/api/clientes")
            .header("Authorization", bearer(adminToken))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.email", equalTo(request.getEmail())))
        .andExpect(jsonPath("$.ativo", equalTo(true)));
  }

  @Test
  @DisplayName("❌ Deve retornar 400 quando nome está vazio")
  void deveRetornarErroQuandoNomeVazio() throws Exception {
    ClienteRequest request = dataFactory.buildClienteRequest();
    request.setNome("");

    mockMvc.perform(post("/api/clientes")
            .header("Authorization", bearer(adminToken))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorType", equalTo("Validation Error")))
        .andExpect(jsonPath("$.errors[0].field", equalTo("nome")));
  }

  @Test
  @DisplayName("✅ Deve buscar cliente por ID existente")
  void deveBuscarClientePorId() throws Exception {
    Cliente cliente = dataFactory.criarCliente(
        "Cliente Integração",
        "cliente-busca@test.com",
        "11900001111",
        dataFactory.generateCpf(),
        true);

    mockMvc.perform(get("/api/clientes/{id}", cliente.getId())
            .header("Authorization", bearer(adminToken)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(cliente.getId().intValue())))
        .andExpect(jsonPath("$.email", equalTo(cliente.getEmail())));
  }

  @Test
  @DisplayName("❌ Deve retornar 404 quando cliente não existe")
  void deveRetornar404AoBuscarClienteInexistente() throws Exception {
    mockMvc.perform(get("/api/clientes/{id}", Long.MAX_VALUE)
            .header("Authorization", bearer(adminToken)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.statusCode", equalTo(404)))
        .andExpect(jsonPath("$.errorType", equalTo("Not Found")));
  }

  @Test
  @DisplayName("✅ Deve listar clientes com paginação")
  void deveListarClientesComPaginacao() throws Exception {
    for (int i = 0; i < 3; i++) {
      dataFactory.criarCliente(
          "Cliente " + i,
          "cliente-paginacao" + i + "@test.com",
          dataFactory.nextPhone(),
          dataFactory.generateCpf(),
          true);
    }

    mockMvc.perform(get("/api/clientes?page=0&size=2")
            .header("Authorization", bearer(adminToken)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(jsonPath("$.totalElements", equalTo(3)))
        .andExpect(jsonPath("$.content[0].email", notNullValue()));
  }

  @Test
  @DisplayName("✅ Deve atualizar cliente existente")
  void deveAtualizarClienteComSucesso() throws Exception {
    Cliente cliente = dataFactory.criarCliente(
        "Cliente Atualizar",
        "atualiza@test.com",
        "11911112222",
        dataFactory.generateCpf(),
        true);
    ClienteRequest helperRequest = dataFactory.buildClienteRequest();
    ClienteUpdateRequest update = dataFactory.buildClienteUpdateRequest(
        "Cliente Atualizado",
        helperRequest.getEmail(),
        helperRequest.getTelefone(),
        helperRequest.getCpf(),
        true);

    mockMvc.perform(put("/api/clientes/{id}", cliente.getId())
            .header("Authorization", bearer(adminToken))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(cliente.getId().intValue())))
        .andExpect(jsonPath("$.nome", equalTo("Cliente Atualizado")))
        .andExpect(jsonPath("$.email", equalTo(update.getEmail())));
  }
}
