package com.deliverytech.delivery.dto;

import com.deliverytech.delivery.commons.TipoEndereco;
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
public class EnderecoRequestDTO {

    @NotBlank(message = "Rua é obrigatória")
    @Size(min = 3, max = 100, message = "Rua deve ter entre 3 e 100 caracteres")
    private String rua;

    @NotBlank(message = "Número é obrigatório")
    @Size(min = 1, max = 10, message = "Número deve ter entre 1 e 10 caracteres")
    private String numero;

    @Size(max = 100, message = "Complemento não pode exceder 100 caracteres")
    private String complemento;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(min = 3, max = 50, message = "Cidade deve ter entre 3 e 50 caracteres")
    private String cidade;

    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres (ex: SP, RJ)")
    private String estado;

    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve estar no formato XXXXX-XXX ou XXXXXXX")
    private String cep;

    @NotBlank(message = "Bairro é obrigatório")
    @Size(min = 3, max = 50, message = "Bairro deve ter entre 3 e 50 caracteres")
    private String bairro;

    @Size(max = 100, message = "Ponto de referência não pode exceder 100 caracteres")
    private String pontoReferencia;

    @NotNull(message = "Tipo de endereço é obrigatório")
    private TipoEndereco tipoEndereco;

}
