package com.deliverytech.delivery.cache;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.service.ProdutoService;
import java.math.BigDecimal;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProdutoCacheIntegrationTest {

  private static final Logger log = LoggerFactory.getLogger(ProdutoCacheIntegrationTest.class);

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
  private ProdutoRepository produtoRepository;

  @Autowired
  private ProdutoService produtoService;

  private long produtoId;

  @BeforeEach
  void setUp() {
    produtoRepository.deleteAll();

    var produto = new Produto();
    produto.setNome("Pizza Napoli");
    produto.setDescricao("Pizza com molho especial");
    produto.setPreco(BigDecimal.valueOf(45.90));
    produto.setDisponivel(true);
    produto.setCategoria("Pizzas");
    produto.setEstoque(10);

    produtoId = produtoRepository.save(produto).getId();
  }

  @Test
  @DisplayName("Deve usar cache para busca por ID")
  void deveUsarCacheBuscarPorId() {
    var primeiraExecucao = medir(() -> produtoService.obterPorId(produtoId));
    var segundaExecucao = medir(() -> produtoService.obterPorId(produtoId));

    log.info("Primeira chamada demorou {} ns e a segunda {} ns",
        primeiraExecucao.tempoNs(), segundaExecucao.tempoNs());

    assertThat(segundaExecucao.valor())
        .usingRecursiveComparison()
        .isEqualTo(primeiraExecucao.valor());
    verify(produtoRepository, times(1)).findById(produtoId);
  }

  @Test
  @DisplayName("Deve reutilizar cache ao buscar por categoria")
  void deveReutilizarCacheCategoria() {
    var primeiraExecucao = medir(() -> produtoService.findByCategoria("Pizzas"));
    var segundaExecucao = medir(() -> produtoService.findByCategoria("Pizzas"));

    log.info("Categoria em cache: primeira={} ns | segunda={} ns",
        primeiraExecucao.tempoNs(), segundaExecucao.tempoNs());

    assertThat(segundaExecucao.valor())
        .containsExactlyElementsOf(primeiraExecucao.valor());
    verify(produtoRepository, times(1)).findByCategoria("Pizzas");
  }

  private <T> Execucao<T> medir(Supplier<T> acao) {
    long inicio = System.nanoTime();
    T valor = acao.get();
    return new Execucao<>(valor, System.nanoTime() - inicio);
  }

  private record Execucao<T>(T valor, long tempoNs) {}
}
