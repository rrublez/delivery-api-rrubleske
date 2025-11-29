package com.deliverytech.delivery.cache;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.service.RestauranteService;
import java.math.BigDecimal;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class RestauranteCacheIntegrationTest {

  private static final Logger log = LoggerFactory.getLogger(RestauranteCacheIntegrationTest.class);

    @Container
    @SuppressWarnings("resource")
    static final GenericContainer<?> redis = new GenericContainer<>("redis:7.4-alpine")
      .withExposedPorts(6379);

  @DynamicPropertySource
  static void configurarRedis(DynamicPropertyRegistry registry) {
    if (!redis.isRunning()) {
      redis.start();
    }
    registry.add("spring.cache.type", () -> "redis");
    registry.add("spring.data.redis.host", redis::getHost);
    registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    registry.add("spring.data.redis.password", () -> "");
  }

  @SpyBean
  @SuppressWarnings("removal")
  private RestauranteRepository restauranteRepository;

  @Autowired
  private RestauranteService restauranteService;

  private long restauranteId;

  @BeforeEach
  void setUp() {
    restauranteRepository.deleteAll();

    Restaurante restaurante = new Restaurante();
    restaurante.setNome("Sabores do Sul");
    restaurante.setEndereco("Rua das Laranjeiras, 200");
    restaurante.setTelefone(gerarTelefoneUnico());
    restaurante.setCnpj(gerarCnpjUnico());
    restaurante.setRamoAtividade("Gourmet");
    restaurante.setAtivo(true);
    restaurante.setTaxaEntrega(BigDecimal.valueOf(7.50));

    restauranteId = restauranteRepository.save(restaurante).getId();
  }

  @Test
  @DisplayName("Deve usar cache para listar restaurantes")
  void deveUsarCacheNaListagem() {
    var primeiraExecucao = medir(() -> restauranteService.listarTodos(null, null));
    var segundaExecucao = medir(() -> restauranteService.listarTodos(null, null));

    log.info("Listagem cacheada: primeira={} ns | segunda={} ns",
      primeiraExecucao.tempoNs(), segundaExecucao.tempoNs());

    assertThat(segundaExecucao.valor())
      .containsExactlyElementsOf(primeiraExecucao.valor());
    verify(restauranteRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("Deve reaproveitar cache ao buscar por ID")
  void deveReaproveitarCacheAoBuscarPorId() {
    var primeiraExecucao = medir(() -> restauranteService.obterPorId(restauranteId));
    var segundaExecucao = medir(() -> restauranteService.obterPorId(restauranteId));

    log.info("Busca por ID cacheada: primeira={} ns | segunda={} ns",
        primeiraExecucao.tempoNs(), segundaExecucao.tempoNs());

    assertThat(segundaExecucao.valor())
        .usingRecursiveComparison()
        .isEqualTo(primeiraExecucao.valor());
    verify(restauranteRepository, times(1)).findById(restauranteId);
  }

  private <T> Execucao<T> medir(Supplier<T> acao) {
    long inicio = System.nanoTime();
    T valor = acao.get();
    return new Execucao<>(valor, System.nanoTime() - inicio);
  }

  private record Execucao<T>(T valor, long tempoNs) {}

  private String gerarTelefoneUnico() {
    long numero = System.nanoTime() % 1_000_000_000L;
    return String.format("519%09d", numero);
  }

  private String gerarCnpjUnico() {
    long numero = System.nanoTime() % 1_000_000_000_000L;
    return String.format("%014d", numero);
  }
}
