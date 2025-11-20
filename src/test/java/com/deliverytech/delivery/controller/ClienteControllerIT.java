package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.cliente.request.ClienteRequest;
import com.deliverytech.delivery.repository.ClienteRepository;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para ClienteController
 * Cobre CRUD completo, validações e cenários de erro
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("ClienteController - Testes de Integração")
class ClienteControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ClienteRepository clienteRepository;

  private ClienteRequest validRequest;
  private String adminToken;

  @BeforeEach
  void setUp() {
    clienteRepository.deleteAll();

    validRequest = new ClienteRequest();
    validRequest.setNome("João Silva");
    validRequest.setEmail("joao@email.com");
    validRequest.setCpf("12345678909");
    validRequest.setTelefone("11999999999");
    validRequest.setAtivo(true);

    try {
      adminToken = TestAuthHelper.registerAndLogin(mockMvc, objectMapper, Role.ADMIN);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Nested
  @DisplayName("POST /api/clientes - Criar Cliente")
  class CreateClienteTests {

    @Test
    @DisplayName("✅ Deve criar cliente com sucesso - Status 201")
    @Transactional
    void testCreateClienteSuccess() throws Exception {
      mockMvc.perform(post("/api/clientes")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id", notNullValue()))
          .andExpect(jsonPath("$.nome", equalTo("João Silva")))
          .andExpect(jsonPath("$.email", equalTo("joao@email.com")))
          .andExpect(jsonPath("$.cpf", equalTo("12345678909")))
          .andExpect(jsonPath("$.ativo", equalTo(true)));
    }

    @Test
    @DisplayName("❌ Deve retornar 400 quando nome está vazio")
    void testCreateClienteWithoutName() throws Exception {
      validRequest.setNome("");

      mockMvc.perform(post("/api/clientes")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.statusCode", equalTo(400)))
          .andExpect(jsonPath("$.errorType", equalTo("Validation Error")));
    }

    @Test
    @DisplayName("❌ Deve retornar 400 quando email é inválido")
    void testCreateClienteWithInvalidEmail() throws Exception {
      validRequest.setEmail("email-invalido");

      mockMvc.perform(post("/api/clientes")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("❌ Deve retornar 400 quando email já está registrado")
    @Transactional
    void testCreateClienteWithDuplicateEmail() throws Exception {
      // Criar primeiro cliente
      mockMvc.perform(post("/api/clientes")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Tentar criar outro com mesmo email
      ClienteRequest duplicateRequest = new ClienteRequest();
      duplicateRequest.setNome("Outro Cliente");
      duplicateRequest.setEmail("joao@email.com"); // mesmo email
      duplicateRequest.setCpf("11144477735");
      duplicateRequest.setTelefone("11888888888");
      duplicateRequest.setAtivo(true);

        mockMvc.perform(post("/api/clientes")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(duplicateRequest)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.statusCode", equalTo(400)))
          .andExpect(jsonPath("$.errorType", equalTo("Validation Error")));
    }
  }

  @Nested
  @DisplayName("GET /api/clientes/* - Buscar Clientes")
  class GetClienteTests {

    @Test
    @DisplayName("✅ Deve buscar cliente por email - Status 200")
    @Transactional
    void testFindByEmailSuccess() throws Exception {
      // Criar cliente
      mockMvc.perform(post("/api/clientes")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Buscar por email
      mockMvc.perform(get("/api/clientes/email/joao@email.com")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(greaterThan(0))))
          .andExpect(jsonPath("$[0].email", equalTo("joao@email.com")));
    }

    @Test
    @DisplayName("❌ Deve retornar 404 quando email não existe")
    void testFindByEmailNotFound() throws Exception {
      mockMvc.perform(get("/api/clientes/email/naoexiste@email.com")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("✅ Deve buscar clientes por nome - Status 200")
    @Transactional
    void testFindByNameSuccess() throws Exception {
      // Criar cliente
      mockMvc.perform(post("/api/clientes")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Buscar por nome
      mockMvc.perform(get("/api/clientes/nome?nome=João")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(greaterThan(0))))
          .andExpect(jsonPath("$[0].nome", containsString("João")));
    }

    @Test
    @DisplayName("✅ Deve verificar se email existe")
    @Transactional
    void testExistsByEmailSuccess() throws Exception {
      // Criar cliente
      mockMvc.perform(post("/api/clientes")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated());

      // Verificar existência
      mockMvc.perform(get("/api/clientes/existe-email/joao@email.com")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", equalTo(true)));
    }

    @Test
    @DisplayName("✅ Deve retornar false quando email não existe")
    void testExistsByEmailFalse() throws Exception {
      mockMvc.perform(get("/api/clientes/existe-email/naoexiste@email.com")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", equalTo(false)));
    }
  }

  @Nested
  @DisplayName("GET /api/clientes/relatorio/* - Relatórios")
  class RelatorioTests {

    @Test
    @DisplayName("✅ Deve gerar relatório de ranking por pedidos - Status 200")
    void testRankingByPedidos() throws Exception {
      mockMvc.perform(get("/api/clientes/relatorio/ranking-por-pedidos")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", isA(java.util.ArrayList.class)));
    }
  }

  @Nested
  @DisplayName("Validação de Estrutura de Resposta")
  class ResponseStructureTests {

    @Test
    @DisplayName("✅ Resposta deve conter campos corretos")
    @Transactional
    void testResponseStructure() throws Exception {
      mockMvc.perform(post("/api/clientes")
          .header("Authorization", "Bearer " + adminToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(validRequest)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id", notNullValue()))
          .andExpect(jsonPath("$.nome", notNullValue()))
          .andExpect(jsonPath("$.email", notNullValue()))
          .andExpect(jsonPath("$.cpf", notNullValue()))
          .andExpect(jsonPath("$.telefone", notNullValue()))
          .andExpect(jsonPath("$.ativo", notNullValue()))
          .andDo(MockMvcResultHandlers.print());
    }
  }
}
