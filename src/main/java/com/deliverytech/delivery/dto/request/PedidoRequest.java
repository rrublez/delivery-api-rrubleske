package com.deliverytech.delivery.dto.request;

import java.util.ArrayList;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {

  @NotBlank(message = "Número do pedido é obrigatório")
  @Size(min = 5, max = 20, message = "Número do pedido deve ter entre 5 e 20 caracteres")
  @Pattern(regexp = "^[A-Z0-9-]+$", message = "Número do pedido deve conter apenas letras maiúsculas, números e hífen")
  private String numeroPedido;

  @NotBlank(message = "Status é obrigatório")
  @Pattern(regexp = "PENDENTE|ENTREGUE|CANCELADO", message = "Status deve ser PENDENTE, ENTREGUE ou CANCELADO")
  private String status;

  @NotNull(message = "ID do cliente é obrigatório")
  private Long clienteId;

  @NotNull(message = "ID do restaurante é obrigatório")
  private Long restauranteId;

  @NotEmpty(message = "Pedido deve conter pelo menos um item")
  @Valid
  private List<PedidoProdutoRequest> itens = new ArrayList<>();

}
