package com.deliverytech.delivery.dto.pedido.request;

import com.deliverytech.delivery.dto.shared.request.PedidoProdutoRequest;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalcularPedidoRequest {

  @NotNull(message = "ID do restaurante é obrigatório")
  private Long restauranteId;

  @NotEmpty(message = "Pedido deve conter pelo menos um item")
  @Valid
  private List<PedidoProdutoRequest> itens = new ArrayList<>();

}
