package com.deliverytech.delivery.dto.restaurante.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "TaxaEntregaResponse", description = "Response com o cálculo de taxa de entrega")
public class TaxaEntregaResponse {

  @Schema(description = "ID do restaurante", example = "1")
  private Long restauranteId;

  @Schema(description = "Nome do restaurante", example = "Pizzaria Dom Pedro")
  private String nomeRestaurante;

  @Schema(description = "CEP do cliente", example = "90010100")
  private String cep;

  @Schema(description = "Taxa de entrega em reais", example = "5.00")
  private BigDecimal taxaEntrega;

  @Schema(description = "Distância em quilômetros", example = "2.5")
  private Double distanciaKm;

  @Schema(description = "Mensagem informativa", example = "Entrega realizada em 30 minutos")
  private String mensagem;

}
