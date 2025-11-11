package com.deliverytech.delivery.dto.restaurante.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RestauranteResponse", description = "Response com os dados do restaurante")
public class RestauranteResponse {

  @Schema(description = "Identificador único do restaurante", example = "1")
  private Long id;

  @Schema(description = "Nome do restaurante", example = "Pizzaria Dom Pedro")
  private String nome;

  @Schema(description = "Endereço completo do restaurante", example = "Rua das Flores, 123, São Paulo - SP")
  private String endereco;

  @Schema(description = "Telefone do restaurante", example = "1133334444")
  private String telefone;

  @Schema(description = "CNPJ do restaurante", example = "12345678901234")
  private String cnpj;

  @Schema(description = "Ramo de atividade", example = "Pizzaria")
  private String ramoAtividade;

  @Schema(description = "Status do restaurante", example = "true")
  private Boolean ativo;

  @Schema(description = "Taxa de entrega em reais", example = "5.00")
  private BigDecimal taxaEntrega;

}
