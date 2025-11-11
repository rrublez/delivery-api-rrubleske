package com.deliverytech.delivery.dto.cliente.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ClienteResponse", description = "Response com os dados do cliente")
public class ClienteResponse {

  @Schema(description = "Identificador único do cliente", example = "1")
  private Long id;

  @Schema(description = "Nome completo do cliente", example = "João Silva")
  private String nome;

  @Schema(description = "Email do cliente", example = "joao@example.com")
  private String email;

  @Schema(description = "Telefone do cliente", example = "11987654321")
  private String telefone;

  @Schema(description = "CPF do cliente", example = "12345678901")
  private String cpf;

  @Schema(description = "Status do cliente", example = "true")
  private Boolean ativo;

}
