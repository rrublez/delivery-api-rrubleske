package com.deliverytech.delivery.entity;

import com.deliverytech.delivery.commons.TipoEndereco;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "enderecos")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 75)
    @NotBlank(message = "Rua não pode ser vazia")
    private String rua;

    @Size(max = 6)
    @NotBlank(message = "Número não pode ser vazio")
    private String numero;

    @Size(max = 50)
    @NotBlank(message = "Cidade não pode ser vazia")
    private String cidade;

    @Size(max = 2)
    @NotBlank(message = "Estado não pode ser vazio")
    private String estado;

    @Size(max = 8)
    private String cep;

    @Size(max = 25)
    private String complemento;

    @Size(max = 25)
    @NotBlank(message = "Bairro não pode ser vazio")
    private String bairro;

    @Size(max = 50)
    private String pontoReferencia;

    // Tipo do endereço, ex: Residencial ou Comercial
    @NotNull(message = "Tipo de endereço não pode ser vazio")
    @Enumerated(EnumType.STRING)
    private TipoEndereco tipoEndereco;

}
