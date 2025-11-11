package com.deliverytech.delivery.dto.restaurante.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para listar restaurantes próximos com distância
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantePróximoResponse {

  private Long id;

  private String nome;

  private String endereco;

  private String ramoAtividade;

  private BigDecimal taxaEntrega;

  private Double distanciaKm;

  private Boolean ativo;

}
