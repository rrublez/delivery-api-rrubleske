package com.deliverytech.delivery.dto.restaurante.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para calcular a taxa de entrega baseado no CEP
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxaEntregaResponse {

  private Long restauranteId;

  private String nomeRestaurante;

  private String cep;

  private BigDecimal taxaEntrega;

  private Double distanciaKm;

  private String mensagem;

}
