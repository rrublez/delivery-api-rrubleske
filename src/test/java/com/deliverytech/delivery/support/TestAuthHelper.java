package com.deliverytech.delivery.support;

import com.deliverytech.delivery.entity.Role;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public final class TestAuthHelper {

  private TestAuthHelper() {}

  public static String registerAndLogin(MockMvc mockMvc, ObjectMapper objectMapper, Role role) throws Exception {
    return registerAndLogin(mockMvc, objectMapper, role, null);
  }

  public static String registerAndLogin(MockMvc mockMvc, ObjectMapper objectMapper, Role role, Long restauranteId) throws Exception {
    String unique = String.valueOf(System.nanoTime());
    String email = "it-" + role.name().toLowerCase() + "+" + unique + "@test.com";
    String senha = "Passw0rd!";

    ObjectNode register = objectMapper.createObjectNode();
    register.put("nome", "User " + role.name());
    register.put("email", email);
    register.put("senha", senha);
    register.put("role", role.name());
    if (role == Role.RESTAURANTE && restauranteId != null) {
      register.put("restauranteId", restauranteId);
    }

    try {
      mockMvc.perform(post("/api/auth/register")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(register)))
          .andExpect(status().isCreated());
    } catch (AssertionError ignored) {
      // In case of parallel runs or re-use, proceed to login
    }

    ObjectNode login = objectMapper.createObjectNode();
    login.put("email", email);
    login.put("senha", senha);

    MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(login)))
        .andExpect(status().isOk())
        .andReturn();

    String content = loginResult.getResponse().getContentAsString();
    JsonNode node = objectMapper.readTree(content);
    String token = node.get("token").asText();
    return token;
  }
}
