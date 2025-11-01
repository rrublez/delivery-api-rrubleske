package com.deliverytech.delivery.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoEstabelecimentoRequestDTO {

    @NotNull(message = "ID do produto não pode ser vazio")
    private String produtoId;

    @NotNull(message = "ID do estabelecimento não pode ser vazio")
    private String estabelecimentoId;

    @NotNull(message = "Preço unitário não pode ser vazio")
    private BigDecimal precoUnitario;

    private BigDecimal precoPromocional;

}
