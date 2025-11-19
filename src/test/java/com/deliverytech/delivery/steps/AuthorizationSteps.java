package com.deliverytech.delivery.steps;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Role;
import com.deliverytech.delivery.entity.Usuario;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.repository.UsuarioRepository;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.ScenarioScope;
import java.util.Optional;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpStatusCodeException;

@ScenarioScope
public class AuthorizationSteps {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private RestauranteRepository restauranteRepository;

  @Autowired
  private ProdutoRepository produtoRepository;

  @Autowired
  private PedidoRepository pedidoRepository;

  @Autowired
  private ClienteRepository clienteRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Value("${security.jwt.secret}")
  private String jwtSecret;

  private final Map<String, String> tokens = new HashMap<>();
  private final Map<String, Long> userIds = new HashMap<>();
  private final Map<String, Long> restaurantIds = new HashMap<>();
  private final Map<String, String> restaurantAliasEmails = new HashMap<>();
  private final Map<String, Long> productIds = new HashMap<>();
  private final Map<String, Long> clienteIds = new HashMap<>();
  private final Set<Long> createdRestaurantIds = new HashSet<>();
  private final Set<Long> createdProductIds = new HashSet<>();
  private final Set<Long> createdPedidoIds = new HashSet<>();
  private final Set<String> createdUserEmails = new HashSet<>();
  private final Set<Long> createdClienteIds = new HashSet<>();
  private final Deque<String> availableCnpjs = new ArrayDeque<>(List.of(
      "11444777000161",
      "19131243000197",
      "27865757000102",
      "62425678000130",
      "02273273000106"
  ));
  private ResponseEntity<String> lastResponse;
  private final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
  };

  @Before
  public void beforeScenario() {
    lastResponse = null;
    tokens.clear();
    userIds.clear();
    restaurantIds.clear();
    restaurantAliasEmails.clear();
    productIds.clear();
    clienteIds.clear();
    createdRestaurantIds.clear();
    createdProductIds.clear();
    createdPedidoIds.clear();
    createdUserEmails.clear();
    createdClienteIds.clear();
  }

  @After
  public void afterScenario() {
    createdPedidoIds.forEach(pedidoRepository::deleteById);
    createdClienteIds.forEach(clienteRepository::deleteById);
    createdProductIds.forEach(produtoRepository::deleteById);
    createdRestaurantIds.forEach(restauranteRepository::deleteById);
    createdUserEmails.stream()
        .map(usuarioRepository::findByEmail)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .forEach(usuarioRepository::delete);
  }

  @Given("no user with email {string}")
  public void removeExistingUser(String email) {
    usuarioRepository.findByEmail(email).ifPresent(usuarioRepository::delete);
    clienteRepository.findByEmail(email).forEach(clienteRepository::delete);
  }

  @Given("the admin token is available")
  public void ensureAdminToken() {
    ensureUserExists("Admin Sistema", "admin@delivery.com", Role.ADMIN, null);
    loginAs("admin@delivery.com", "123456");
    Assertions.assertNotNull(lastResponse, "Admin login não retornou resposta");
    Assertions.assertEquals(HttpStatus.OK.value(), lastResponse.getStatusCode().value(), "Login do admin deve retornar 200");
  }

  @When("I register user {string} with email {string} and role {string}")
  public void registerUser(String name, String email, String role) {
    registerNewUser(name, email, role, null);
  }

  @When("restaurant user {string} is registered for alias {string}")
  public void registerRestaurantUser(String email, String alias) {
    Long restauranteId = restaurantIds.get(alias);
    Assertions.assertNotNull(restauranteId, "Restaurante alias must exist");
    registerNewUser(alias + " Manager", email, "RESTAURANTE", restauranteId);
    restaurantAliasEmails.put(alias, email);
  }

  private void registerNewUser(String name, String email, String role, Long restauranteId) {
    Map<String, Object> body = new HashMap<>();
    body.put("nome", name);
    body.put("email", email);
    body.put("senha", "123456");
    body.put("role", role);
    if (restauranteId != null) {
      body.put("restauranteId", restauranteId);
    }
    lastResponse = exchange("/api/auth/register", HttpMethod.POST, body, null);
    if (lastResponse.getStatusCode().is2xxSuccessful()) {
      Map<String, Object> payload = parse(lastResponse.getBody());
      if (payload != null) {
        Long id = toLong(payload.get("id"));
        if (id != null) {
          userIds.put(email, id);
        }
        String resolvedRole = role;
        Object roleValue = payload.get("role");
        if (roleValue instanceof String stringRole) {
          resolvedRole = stringRole;
        }
        if ("CLIENTE".equalsIgnoreCase(resolvedRole)) {
          Long clienteId = ensureClienteExists(name, email);
          clienteIds.put(email, clienteId);
        }
      }
      createdUserEmails.add(email);
    }
  }

  @SuppressWarnings("null")
  private void ensureUserExists(String name, String email, Role role, Long restauranteId) {
    if (usuarioRepository.findByEmail(email).isEmpty()) {
      Usuario usuario = Usuario.builder()
          .nome(name)
          .email(email)
          .senha(passwordEncoder.encode("123456"))
          .role(role)
          .restauranteId(restauranteId)
          .ativo(true)
          .build();
      usuarioRepository.save(usuario);
    }
  }

  @When("I login with email {string} and password {string}")
  public void loginAs(String email, String senha) {
    Map<String, String> body = Map.of("email", email, "senha", senha);
    lastResponse = exchange("/api/auth/login", HttpMethod.POST, body, null);
    if (lastResponse.getStatusCode().is2xxSuccessful()) {
      Map<String, Object> payload = parse(lastResponse.getBody());
      if (payload != null) {
        String token = (String) payload.get("token");
        if (token != null) {
          tokens.put(email, token);
        }
        Map<String, Object> userMap = castToMap(payload.get("user"));
        if (userMap != null) {
          Long id = toLong(userMap.get("id"));
          if (id != null) {
            userIds.put(email, id);
            createdUserEmails.add(email);
          }
          if ("CLIENTE".equalsIgnoreCase((String) userMap.get("role"))) {
            Long clienteId = ensureClienteExists((String) userMap.get("nome"), email);
            clienteIds.put(email, clienteId);
          }
          Long restauranteId = toLong(userMap.get("restauranteId"));
          if (restauranteId != null) {
            restaurantIds.put("restaurante-user" + email, restauranteId);
          }
        }
      }
    }
  }

  @Then("response status should be {int}")
  public void verifyStatus(int expected) {
    Assertions.assertNotNull(lastResponse, "No response registered yet");
    Assertions.assertEquals(expected, lastResponse.getStatusCode().value());
  }

  @Then("response contains token")
  public void responseContainsToken() {
    Assertions.assertNotNull(lastResponse, "No response to inspect");
    Map<String, Object> payload = parse(lastResponse.getBody());
    Assertions.assertNotNull(payload);
    Assertions.assertTrue(payload.containsKey("token"));
  }

  @When("I call GET {string} without token")
  public void callGetWithoutToken(String path) {
    lastResponse = exchange(path, HttpMethod.GET, null, null);
  }

  @When("I call GET {string} with token for {string}")
  public void callGetWithToken(String path, String email) {
    lastResponse = exchange(path, HttpMethod.GET, null, tokens.get(email));
  }

  @When("I create product {string} with token for {string}")
  public void createProduct(String productName, String email) {
    Map<String, Object> body = new HashMap<>();
    body.put("nome", productName);
    body.put("descricao", "Produto automatizado " + productName);
    body.put("preco", new BigDecimal("45.90"));
    body.put("disponivel", true);
    body.put("categoria", "Cozinha");
    String token = tokens.get(email);
    lastResponse = exchange("/api/produtos", HttpMethod.POST, body, token);
    if (lastResponse.getStatusCode().is2xxSuccessful()) {
      Map<String, Object> payload = parse(lastResponse.getBody());
      Long id = toLong(payload.get("id"));
      productIds.put(productName, id);
      createdProductIds.add(id);
    }
  }

  @When("I update product {string} with token for {string}")
  public void updateProduct(String productName, String email) {
    Long productId = productIds.get(productName);
    Assertions.assertNotNull(productId, "Product must be created before update");
    Map<String, Object> body = new HashMap<>();
    body.put("nome", productName + " Atualizado");
    body.put("descricao", "Atualização automática");
    body.put("preco", new BigDecimal("55.00"));
    body.put("disponivel", true);
    body.put("categoria", "Atualizado");
    String token = tokens.get(email);
    lastResponse = exchange("/api/produtos/" + productId, HttpMethod.PUT, body, token);
  }

  @When("I call GET {string} with expired token for {string}")
  public void callWithExpiredToken(String path, String email) {
    String expired = buildExpiredToken(email);
    lastResponse = exchange(path, HttpMethod.GET, null, expired);
  }

  @Given("admin has created restaurant alias {string}")
  public void adminCreatesRestaurant(String alias) {
    String token = tokens.get("admin@delivery.com");
    Assertions.assertNotNull(token, "Admin must be logged in first");
    Map<String, Object> body = new HashMap<>();
    body.put("nome", alias + " " + randomNumeric(3));
    body.put("endereco", "Rua Teste, 123");
    body.put("telefone", "11" + randomNumeric(8));
    body.put("cnpj", nextValidCnpj());
    body.put("ramoAtividade", "Culinária");
    body.put("ativo", true);
    body.put("taxaEntrega", new BigDecimal("5.00"));
    lastResponse = exchange("/api/restaurantes", HttpMethod.POST, body, token);
    Assertions.assertTrue(lastResponse.getStatusCode().is2xxSuccessful());
    Map<String, Object> payload = parse(lastResponse.getBody());
    Long id = toLong(payload.get("id"));
    restaurantIds.put(alias, id);
    createdRestaurantIds.add(id);
  }

  @Given("restaurant {string} has product {string}")
  public void restaurantHasProduct(String alias, String productName) {
    String email = restaurantAliasEmails.get(alias);
    Assertions.assertNotNull(email, "Restaurant user must exist");
    createProduct(productName, email);
  }

  @When("client {string} creates a pedido for restaurant {string} with product {string}")
  public void clientCreatesPedido(String clientEmail, String restaurantAlias, String productName) {
    Long clientId = clienteIds.getOrDefault(clientEmail, userIds.get(clientEmail));
    Long restauranteId = restaurantIds.get(restaurantAlias);
    Long produtoId = productIds.get(productName);
    Assertions.assertNotNull(clientId);
    Assertions.assertNotNull(restauranteId);
    Assertions.assertNotNull(produtoId);
    Map<String, Object> item = new HashMap<>();
    item.put("produtoId", produtoId);
    item.put("quantidade", 2);
    item.put("precoUnitario", new BigDecimal("60.00"));
    item.put("observacoes", "Sem cebola");
    Map<String, Object> body = new HashMap<>();
    body.put("numeroPedido", "PED-" + randomNumeric(4));
    body.put("status", "PENDENTE");
    body.put("clienteId", clientId);
    body.put("restauranteId", restauranteId);
    body.put("itens", List.of(item));
    String token = tokens.get(clientEmail);
    lastResponse = exchange("/api/pedidos", HttpMethod.POST, body, token);
    if (lastResponse.getStatusCode().is2xxSuccessful()) {
      Map<String, Object> payload = parse(lastResponse.getBody());
      Long id = toLong(payload.get("id"));
      createdPedidoIds.add(id);
    }
  }

  @Then("the order references restaurant {string} and product {string}")
  public void verifyOrderReferences(String alias, String productName) {
    Assertions.assertNotNull(lastResponse);
    Map<String, Object> payload = parse(lastResponse.getBody());
    Map<String, Object> restaurante = castToMap(payload.get("restaurante"));
    Assertions.assertNotNull(restaurante);
    Long restId = toLong(restaurante.get("id"));
    Assertions.assertEquals(restaurantIds.get(alias), restId);
    List<Object> itens = castToList(payload.get("itens"));
    Assertions.assertNotNull(itens);
    boolean contains = itens.stream()
        .map(this::castToMap)
        .map(item -> castToMap(item.get("produto")))
        .anyMatch(prod -> toLong(prod.get("id")).equals(productIds.get(productName)));
    Assertions.assertTrue(contains, "Pedido deve conter o produto informado");
  }

  @Then("response should be unauthorized")
  public void responseShouldBeUnauthorized() {
    verifyStatus(401);
  }

  private ResponseEntity<String> exchange(String path, HttpMethod method, Object body, String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    if (token != null) {
      headers.setBearerAuth(token);
    }
    HttpEntity<Object> entity = new HttpEntity<>(body, headers);
    try {
      return restTemplate.exchange(path, method, entity, String.class);
    } catch (HttpStatusCodeException exception) {
      return ResponseEntity
          .status(exception.getStatusCode())
          .headers(exception.getResponseHeaders())
          .body(exception.getResponseBodyAsString());
    }
  }

  private Map<String, Object> parse(String payload) {
    try {
      return payload == null ? null : objectMapper.readValue(payload, MAP_TYPE);
    } catch (Exception cause) {
      throw new IllegalStateException("Não foi possível mapear o payload", cause);
    }
  }

  private Long toLong(Object value) {
    if (value instanceof Number number) {
      return number.longValue();
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> castToMap(Object value) {
    return value instanceof Map map ? map : null;
  }

  @SuppressWarnings("unchecked")
  private List<Object> castToList(Object value) {
    return value instanceof List list ? list : null;
  }

  private String randomNumeric(int digits) {
    long bound = (long) Math.pow(10, digits);
    long number = ThreadLocalRandom.current().nextLong(bound - 1) + 1;
    return String.format("%0" + digits + "d", number);
  }

  private Long ensureClienteExists(String name, String email) {
    return clienteRepository.findByEmail(email).stream()
        .findFirst()
        .map(Cliente::getId)
        .orElseGet(() -> {
          Cliente cliente = new Cliente();
          cliente.setNome(name);
          cliente.setEmail(email);
          cliente.setTelefone("11" + randomNumeric(8));
          cliente.setCpf(generateUniqueCpf());
          cliente.setAtivo(true);
          Cliente saved = clienteRepository.save(cliente);
          createdClienteIds.add(saved.getId());
          return saved.getId();
        });
  }

  private String generateUniqueCpf() {
    String cpf;
    int attempts = 0;
    do {
      cpf = randomNumeric(11);
      attempts++;
      if (attempts > 20) {
        throw new IllegalStateException("Não foi possível gerar um CPF único para o cliente de teste");
      }
    } while (clienteRepository.existsByCpf(cpf));
    return cpf;
  }

  private String buildExpiredToken(String email) {
    SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    Instant now = Instant.now();
    var builder = Jwts.builder()
        .setSubject(email)
        .setIssuedAt(Date.from(now.minusSeconds(3600)))
        .setExpiration(Date.from(now.minusSeconds(1800)))
        .signWith(key);
    usuarioRepository.findByEmail(email).ifPresent(usuario -> {
      builder.claim("userId", usuario.getId());
      builder.claim("role", usuario.getRole().name());
      if (usuario.getRestauranteId() != null) {
        builder.claim("restauranteId", usuario.getRestauranteId());
      }
    });
    return builder.compact();
  }

  private String nextValidCnpj() {
    if (availableCnpjs.isEmpty()) {
      throw new IllegalStateException("Não há CNPJ válido disponível para o cenário de teste");
    }
    return availableCnpjs.removeFirst();
  }
  }