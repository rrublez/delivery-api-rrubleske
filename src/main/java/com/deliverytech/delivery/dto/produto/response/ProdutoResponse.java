package com.deliverytech.delivery.dto.produto.response;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ProdutoResponse", description = "Response com os dados do produto")
public class ProdutoResponse {

  @Schema(description = "Identificador único do produto", example = "1")
  private Long id;

  @Schema(description = "Nome do produto", example = "Pizza Margherita")
  private String nome;

  @Schema(description = "Descrição detalhada do produto", example = "Deliciosa pizza com mozzarella, tomate e manjericão")
  private String descricao;

  @Schema(description = "Preço do produto em reais", example = "45.90")
  private BigDecimal preco;

  @Schema(description = "Se o produto está disponível para venda", example = "true")
  private Boolean disponivel;

  @Schema(description = "Categoria do produto", example = "Pizza")
  private String categoria;

  @Schema(description = "Quantidade em estoque", example = "100")
  private Integer estoque;

}
