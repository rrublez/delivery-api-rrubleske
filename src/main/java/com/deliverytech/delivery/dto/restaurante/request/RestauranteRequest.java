package com.deliverytech.delivery.dto.restaurante.request;

import com.deliverytech.delivery.validation.UniqueCnpj;
import com.deliverytech.delivery.validation.UniqueTelefoneRestaurante;
import com.deliverytech.delivery.validation.UniqueNomeRestaurante;
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
public class RestauranteRequest {

  @NotBlank(message = "Nome é obrigatório")
  @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
  @UniqueNomeRestaurante(message = "Nome de restaurante já está registrado no sistema")
  private String nome;

  @NotBlank(message = "Endereço é obrigatório")
  @Size(min = 5, max = 255, message = "Endereço deve ter entre 5 e 255 caracteres")
  private String endereco;

  @NotBlank(message = "Telefone é obrigatório")
  @Size(min = 10, max = 15, message = "Telefone deve ter entre 10 e 15 caracteres")
  @UniqueTelefoneRestaurante(message = "Telefone já está registrado no sistema")
  private String telefone;

  @NotBlank(message = "CNPJ é obrigatório")
  @CNPJ(message = "CNPJ deve ser válido")
  @Size(min = 14, max = 14, message = "CNPJ deve ter 14 dígitos")
  @UniqueCnpj(message = "CNPJ já está registrado no sistema")
  private String cnpj;

  @NotBlank(message = "Ramo de atividade é obrigatório")
  @Size(min = 3, max = 20, message = "Ramo de atividade deve ter entre 3 e 20 caracteres")
  private String ramoAtividade;

  @NotNull(message = "Status ativo/inativo é obrigatório")
  private Boolean ativo;

  @NotNull(message = "Taxa de entrega é obrigatória")
  @DecimalMin(value = "0.0", message = "Taxa de entrega não pode ser negativa")
  private BigDecimal taxaEntrega;

}
