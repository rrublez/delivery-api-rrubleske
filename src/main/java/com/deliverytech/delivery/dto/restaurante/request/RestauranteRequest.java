package com.deliverytech.delivery.dto.restaurante.request;

import com.deliverytech.delivery.validation.UniqueCnpj;
import com.deliverytech.delivery.validation.UniqueTelefoneRestaurante;
import com.deliverytech.delivery.validation.UniqueNomeRestaurante;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.hibernate.validator.constraints.br.CNPJ;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RestauranteRequest", description = "Request para criação ou atualização de um restaurante")
public class RestauranteRequest {

  @NotBlank(message = "Nome é obrigatório")
  @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
  @UniqueNomeRestaurante(message = "Nome de restaurante já está registrado no sistema")
  @Schema(description = "Nome único do restaurante", example = "Pizzaria Dom Pedro", minLength = 3, maxLength = 100)
  private String nome;

  @NotBlank(message = "Endereço é obrigatório")
  @Size(min = 5, max = 255, message = "Endereço deve ter entre 5 e 255 caracteres")
  @Schema(description = "Endereço completo do restaurante", example = "Rua das Flores, 123, São Paulo - SP", minLength = 5, maxLength = 255)
  private String endereco;

  @NotBlank(message = "Telefone é obrigatório")
  @Size(min = 10, max = 15, message = "Telefone deve ter entre 10 e 15 caracteres")
  @UniqueTelefoneRestaurante(message = "Telefone já está registrado no sistema")
  @Schema(description = "Telefone único do restaurante", example = "1133334444", minLength = 10, maxLength = 15)
  private String telefone;

  @NotBlank(message = "CNPJ é obrigatório")
  @CNPJ(message = "CNPJ deve ser válido")
  @Size(min = 14, max = 14, message = "CNPJ deve ter 14 dígitos")
  @UniqueCnpj(message = "CNPJ já está registrado no sistema")
  @Schema(description = "CNPJ único (14 dígitos, apenas números)", example = "12345678901234", minLength = 14, maxLength = 14)
  private String cnpj;

  @NotBlank(message = "Ramo de atividade é obrigatório")
  @Size(min = 3, max = 20, message = "Ramo de atividade deve ter entre 3 e 20 caracteres")
  @Schema(description = "Ramo de atividade do restaurante", example = "Pizzaria", minLength = 3, maxLength = 20)
  private String ramoAtividade;

  @NotNull(message = "Status ativo/inativo é obrigatório")
  @Schema(description = "Status do restaurante (ativo ou inativo)", example = "true")
  private Boolean ativo;

  @NotNull(message = "Taxa de entrega é obrigatória")
  @DecimalMin(value = "0.0", message = "Taxa de entrega não pode ser negativa")
  @Schema(description = "Taxa de entrega em reais", example = "5.00")
  private BigDecimal taxaEntrega;

}
