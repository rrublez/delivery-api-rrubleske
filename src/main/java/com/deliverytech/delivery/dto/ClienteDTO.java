package com.deliverytech.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

  private Long id;

  private String nome;

  private String email;

  private String telefone;

  private Boolean ativo;

}
