package com.deliverytech.delivery.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.NonNull;

@Configuration
@EnableCaching
public class RedisCacheConfig {

  public static final String CACHE_PRODUTO_POR_ID = "produtoPorId";
  public static final String CACHE_PRODUTOS_POR_CATEGORIA = "produtosPorCategoria";
  public static final String CACHE_PRODUTOS_POR_RESTAURANTE = "produtosPorRestaurante";
  public static final String CACHE_PRODUTOS_POR_NOME = "produtosPorNome";
  public static final String CACHE_RESTAURANTE_POR_ID = "restaurantePorId";
  public static final String CACHE_RESTAURANTES_LISTA = "restaurantesLista";
  public static final String CACHE_RESTAURANTES_POR_RAMO = "restaurantesPorRamo";

  @Bean
  public RedisCacheConfiguration redisCacheConfiguration() {
    return RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
          RedisSerializer.java()))
        .disableCachingNullValues()
        .entryTtl(minutes(5));
  }

  @Bean
  public RedisCacheManager redisCacheManager(@NonNull RedisConnectionFactory connectionFactory,
                                             @NonNull RedisCacheConfiguration redisCacheConfiguration) {
    Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
    cacheConfigurations.put(CACHE_PRODUTO_POR_ID, redisCacheConfiguration.entryTtl(minutes(15)));
    cacheConfigurations.put(CACHE_PRODUTOS_POR_CATEGORIA, redisCacheConfiguration.entryTtl(minutes(10)));
    cacheConfigurations.put(CACHE_PRODUTOS_POR_RESTAURANTE, redisCacheConfiguration.entryTtl(minutes(10)));
    cacheConfigurations.put(CACHE_PRODUTOS_POR_NOME, redisCacheConfiguration.entryTtl(minutes(5)));
    cacheConfigurations.put(CACHE_RESTAURANTE_POR_ID, redisCacheConfiguration.entryTtl(minutes(15)));
    cacheConfigurations.put(CACHE_RESTAURANTES_LISTA, redisCacheConfiguration.entryTtl(minutes(5)));
    cacheConfigurations.put(CACHE_RESTAURANTES_POR_RAMO, redisCacheConfiguration.entryTtl(minutes(10)));

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(redisCacheConfiguration)
        .withInitialCacheConfigurations(cacheConfigurations)
        .transactionAware()
        .build();
  }

  private @NonNull Duration minutes(long value) {
    return Objects.requireNonNull(Duration.ofMinutes(value));
  }
}
