package com.deliverytech.delivery.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    @Size(min = 10, max = 20, message = "Telefone deve ter entre 10 e 20 caracteres")
    private String telefone;

    @NotBlank(message = "Documento de identidade é obrigatório")
    @Size(min = 11, max = 14, message = "Documento deve ter entre 11 e 14 caracteres")
    private String documentoIdentificacao;

    @Size(max = 500, message = "Observações não pode exceder 500 caracteres")
    private String observacoes;

    /**
     * Lista de endereços do cliente.
     * - Mínimo: 1 endereço
     * - Máximo: 3 endereços
     * - Todos os endereços devem ser válidos
     */
    @NotEmpty(message = "Cliente deve ter pelo menos 1 endereço")
    @Size(max = 3, message = "Cliente pode ter no máximo 3 endereços")
    @Valid
    private List<EnderecoRequestDTO> enderecos;

}
