package com.deliverytech.delivery.config;

import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.support.IntegrationTestDataFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class IntegrationTestDataConfig {

  @Bean
  public IntegrationTestDataFactory integrationTestDataFactory(
      ClienteRepository clienteRepository,
      RestauranteRepository restauranteRepository,
      ProdutoRepository produtoRepository) {
    return new IntegrationTestDataFactory(clienteRepository, restauranteRepository, produtoRepository);
  }
}
