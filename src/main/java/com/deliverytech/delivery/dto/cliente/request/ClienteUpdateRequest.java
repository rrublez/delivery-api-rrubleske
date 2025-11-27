package com.deliverytech.delivery.dto.cliente.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.br.CPF;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ClienteUpdateRequest", description = "Request para atualizar um cliente existente")
public class ClienteUpdateRequest {

  @NotBlank(message = "Nome é obrigatório")
  @Size(min = 3, max = 50, message = "Nome deve ter entre 3 e 50 caracteres")
  @Schema(description = "Nome completo do cliente", example = "João Silva", minLength = 3, maxLength = 50)
  private String nome;

  @NotBlank(message = "Email é obrigatório")
  @Email(message = "Email deve ser válido")
  @Size(max = 50, message = "Email não pode exceder 50 caracteres")
  @Schema(description = "Email único do cliente", example = "joao@example.com", maxLength = 50)
  private String email;

  @NotBlank(message = "Telefone é obrigatório")
  @Size(min = 10, max = 15, message = "Telefone deve ter entre 10 e 15 caracteres")
  @Schema(description = "Número de telefone com DDD", example = "11987654321", minLength = 10, maxLength = 15)
  private String telefone;

  @CPF(message = "CPF deve ser válido")
  @NotBlank(message = "CPF é obrigatório")
  @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos")
  @Schema(description = "CPF único (11 dígitos, apenas números)", example = "12345678901", minLength = 11, maxLength = 11)
  private String cpf;

  @NotNull(message = "Status ativo/inativo é obrigatório")
  @Schema(description = "Status do cliente (ativo ou inativo)", example = "true")
  private Boolean ativo;

}
