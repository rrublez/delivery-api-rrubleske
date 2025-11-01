package com.deliverytech.delivery.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "produto_estabelecimento",
    uniqueConstraints = @UniqueConstraint(columnNames = {"produto_id", "estabelecimento_id"})
)
public class ProdutoEstabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "estabelecimento_id", nullable = false)
    private Estabelecimento estabelecimento;

    @NotNull(message = "Preço unitário não pode ser vazio")
    private BigDecimal precoUnitario;

    private BigDecimal precoPromocional;

    private LocalDateTime dataHoraInicioPromocao;

    private LocalDateTime dataHoraFimPromocao;

    @OneToMany(mappedBy = "produtoEstabelecimento")
    private List<ItemPedido> itens;

}
