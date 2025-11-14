package com.deliverytech.delivery.dto.shared.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteDTO {

  private Long id;

  private String nome;

  private String endereco;

  private String telefone;

  private String cnpj;

  private String ramoAtividade;

  private Boolean ativo;

  private BigDecimal taxaEntrega;

}
