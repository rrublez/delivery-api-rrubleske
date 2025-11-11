package com.deliverytech.delivery.dto.produto.request;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "ProdutoRequest", description = "Request para criação ou atualização de um produto")
public class ProdutoRequest {

  @NotBlank(message = "Nome é obrigatório")
  @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
  @Schema(description = "Nome do produto", example = "Pizza Margherita", minLength = 3, maxLength = 100)
  private String nome;

  @NotBlank(message = "Descrição é obrigatória")
  @Size(min = 5, max = 255, message = "Descrição deve ter entre 5 e 255 caracteres")
  @Schema(description = "Descrição detalhada do produto", example = "Deliciosa pizza com mozzarella, tomate e manjericão", minLength = 5, maxLength = 255)
  private String descricao;

  @NotNull(message = "Preço é obrigatório")
  @DecimalMin(value = "0.01", message = "Preço deve ser maior que 0")
  @Schema(description = "Preço do produto em reais", example = "45.90")
  private BigDecimal preco;

  @NotNull(message = "Disponibilidade é obrigatória")
  @Schema(description = "Se o produto está disponível para venda", example = "true")
  private Boolean disponivel;

  @NotBlank(message = "Categoria é obrigatória")
  @Size(min = 3, max = 20, message = "Categoria deve ter entre 3 e 20 caracteres")
  @Schema(description = "Categoria do produto", example = "Pizza", minLength = 3, maxLength = 20)
  private String categoria;

}
