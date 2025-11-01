package com.deliverytech.delivery.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstabelecimentoRequestDTO {

    @Size(max = 75)
    @NotBlank(message = "Nome não pode ser vazio")
    private String nome;

    @NotBlank(message = "CNPJ não pode ser vazio")
    @Pattern(regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$", message = "CNPJ deve estar no formato: XX.XXX.XXX/XXXX-XX")
    private String cnpj;

    @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$", message = "Telefone deve estar em um formato válido")
    @NotBlank(message = "Telefone não pode ser vazio")
    private String telefone;

    @Email(message = "Email deve ser válido")
    @Size(max = 35)
    @NotBlank(message = "Email não pode ser vazio")
    private String email;

    @NotNull(message = "ID do ramo não pode ser vazio")
    private String ramoId;

}
