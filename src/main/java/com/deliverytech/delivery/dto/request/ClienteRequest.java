package com.deliverytech.delivery.dto.request;

import com.deliverytech.delivery.validation.UniqueEmail;
import org.hibernate.validator.constraints.br.CPF;
import com.deliverytech.delivery.validation.UniqueCpf;
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
public class ClienteRequest {

  @NotBlank(message = "Nome é obrigatório")
  @Size(min = 3, max = 50, message = "Nome deve ter entre 3 e 50 caracteres")
  private String nome;

  @NotBlank(message = "Email é obrigatório")
  @Email(message = "Email deve ser válido")
  @Size(max = 50, message = "Email não pode exceder 50 caracteres")
  @UniqueEmail(message = "Email já está registrado no sistema")
  private String email;

  @NotBlank(message = "Telefone é obrigatório")
  @Size(min = 10, max = 15, message = "Telefone deve ter entre 10 e 15 caracteres")
  private String telefone;

  @CPF(message = "CPF deve ser válido")
  @NotBlank(message = "CPF é obrigatório")
  @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos")
  @UniqueCpf(message = "CPF já está registrado no sistema")
  private String cpf;

  @NotNull(message = "Status ativo/inativo é obrigatório")
  private Boolean ativo;

}
