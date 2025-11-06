package com.deliverytech.delivery.dto.request;

import java.math.BigDecimal;
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
public class ProdutoRequest {

  @NotBlank(message = "Nome é obrigatório")
  @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
  private String nome;

  @NotBlank(message = "Descrição é obrigatória")
  @Size(min = 5, max = 255, message = "Descrição deve ter entre 5 e 255 caracteres")
  private String descricao;

  @NotNull(message = "Preço é obrigatório")
  @DecimalMin(value = "0.01", message = "Preço deve ser maior que 0")
  private BigDecimal preco;

  @NotNull(message = "Disponibilidade é obrigatória")
  private Boolean disponivel;

  @NotBlank(message = "Categoria é obrigatória")
  @Size(min = 3, max = 20, message = "Categoria deve ter entre 3 e 20 caracteres")
  private String categoria;

}
