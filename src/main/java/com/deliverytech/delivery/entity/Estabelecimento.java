package com.deliverytech.delivery.entity;

import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "estabelecimentos")
public class Estabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 75)
    @NotBlank
    private String nome;

    @CNPJ
    @NotBlank(message = "CNPJ não pode ser vazio")
    private String cnpj;

    @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$", message = "Telefone deve estar em um formato válido")
    @NotBlank(message = "Telefone não pode ser vazio")
    private String telefone;

    @Email(message = "Email deve ser válido")
    @Size(max = 35)
    @NotBlank(message = "Email não pode ser vazio")
    private String email;

    @ManyToOne
    @JoinColumn(name = "ramo_id", nullable = false)
    private RamoEstabelecimento ramo;

    @OneToMany(mappedBy = "estabelecimento")
    private List<ProdutoEstabelecimento> produtos;

}
