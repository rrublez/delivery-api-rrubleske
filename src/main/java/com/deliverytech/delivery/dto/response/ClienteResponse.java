package com.deliverytech.delivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {

  private Long id;

  private String nome;

  private String email;

  private String telefone;

  private String cpf;

  private Boolean ativo;

}
