package com.deliverytech.delivery.dto.restaurante.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "RestaurantePróximoResponse", description = "Response com restaurante próximo e distância")
public class RestaurantePróximoResponse {

  @Schema(description = "ID do restaurante", example = "1")
  private Long id;

  @Schema(description = "Nome do restaurante", example = "Pizzaria Dom Pedro")
  private String nome;

  @Schema(description = "Endereço do restaurante", example = "Rua das Flores, 123")
  private String endereco;

  @Schema(description = "Ramo de atividade", example = "Pizzaria")
  private String ramoAtividade;

  @Schema(description = "Taxa de entrega em reais", example = "5.00")
  private BigDecimal taxaEntrega;

  @Schema(description = "Distância em quilômetros", example = "2.5")
  private Double distanciaKm;

  @Schema(description = "Status do restaurante", example = "true")
  private Boolean ativo;

}
