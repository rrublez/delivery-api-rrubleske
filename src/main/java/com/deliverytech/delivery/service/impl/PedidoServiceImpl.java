package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.dto.request.AtualizarStatusPedidoRequest;
import com.deliverytech.delivery.dto.request.CalcularPedidoRequest;
import com.deliverytech.delivery.dto.request.PedidoRequest;
import com.deliverytech.delivery.dto.request.PedidoProdutoRequest;
import com.deliverytech.delivery.dto.response.CalcularPedidoResponse;
import com.deliverytech.delivery.dto.response.PedidoRelatorioResponse;
import com.deliverytech.delivery.dto.response.PedidoResponse;
import com.deliverytech.delivery.dto.response.PedidoProdutoResponse;
import com.deliverytech.delivery.dto.response.ProdutoResponse;
import com.deliverytech.delivery.dto.response.VendasPorRestauranteResponse;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.PedidoProduto;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.service.PedidoService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

  private final PedidoRepository pedidoRepository;
  private final ClienteRepository clienteRepository;
  private final RestauranteRepository restauranteRepository;
  private final ProdutoRepository produtoRepository;

  public PedidoServiceImpl(
      PedidoRepository pedidoRepository,
      ClienteRepository clienteRepository,
      RestauranteRepository restauranteRepository,
      ProdutoRepository produtoRepository) {
    this.pedidoRepository = pedidoRepository;
    this.clienteRepository = clienteRepository;
    this.restauranteRepository = restauranteRepository;
    this.produtoRepository = produtoRepository;
  }

  @Override
  public PedidoResponse criarPedido(PedidoRequest request) {
    var cliente = clienteRepository.findById(request.getClienteId())
        .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    var restaurante = restauranteRepository.findById(request.getRestauranteId())
        .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

    var pedido = new Pedido();
    pedido.setNumeroPedido(request.getNumeroPedido());
    pedido.setStatus(request.getStatus() != null ? request.getStatus() : "PENDENTE");
    pedido.setCliente(cliente);
    pedido.setRestaurante(restaurante);
    pedido.setDataPedido(LocalDateTime.now());

    var itens = new ArrayList<PedidoProduto>();
    var valorTotal = BigDecimal.ZERO;

    for (PedidoProdutoRequest itemRequest : request.getItens()) {
      var produto = produtoRepository.findById(itemRequest.getProdutoId())
          .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

      var item = new PedidoProduto();
      item.setPedido(pedido);
      item.setProduto(produto);
      item.setQuantidade(itemRequest.getQuantidade());
      item.setPrecoUnitario(itemRequest.getPrecoUnitario());

      var subtotal = itemRequest.getPrecoUnitario()
          .multiply(BigDecimal.valueOf(itemRequest.getQuantidade()));
      item.setSubtotal(subtotal);
      item.setObservacoes(itemRequest.getObservacoes());

      itens.add(item);
      valorTotal = valorTotal.add(subtotal);
    }

    pedido.setItens(itens);
    pedido.setValorTotal(valorTotal);

    var pedidoSalvo = pedidoRepository.save(pedido);
    return mapearParaResponse(pedidoSalvo);
  }

  @Override
  @Transactional(readOnly = true)
  public List<PedidoResponse> findByClienteId(Long clienteId) {
    return pedidoRepository.findByClienteId(clienteId)
        .stream()
        .map(this::mapearParaResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<PedidoResponse> findByStatus(String status) {
    return pedidoRepository.findByStatus(status)
        .stream()
        .map(this::mapearParaResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<PedidoResponse> findTop10ByOrderByValorTotalDesc() {
    return pedidoRepository.findTop10ByOrderByValorTotalDesc()
        .stream()
        .map(this::mapearParaResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<PedidoResponse> findByDataPedidoBetween(LocalDateTime dataInicial,
      LocalDateTime dataFinal) {
    return pedidoRepository.findByDataPedidoBetween(dataInicial, dataFinal)
        .stream()
        .map(this::mapearParaResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<PedidoResponse> findTop5MaioresPedidosPorRestaurante(Long restauranteId) {
    return pedidoRepository.findTop5MaioresPedidosPorRestaurante(restauranteId)
        .stream()
        .map(this::mapearParaResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<VendasPorRestauranteResponse> obterVendasPorRestaurante() {
    return pedidoRepository.obterVendasPorRestaurante();
  }

  @Override
  @Transactional(readOnly = true)
  public List<PedidoResponse> findPedidosComValorAcimaDe(BigDecimal valor) {
    return pedidoRepository.findPedidosComValorAcimaDe(valor)
        .stream()
        .map(this::mapearParaResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<PedidoRelatorioResponse> obterRelatorioByPeriodoAndStatus(
      LocalDateTime dataInicial, LocalDateTime dataFinal, String status) {
    return pedidoRepository.obterRelatorioByPeriodoAndStatus(dataInicial, dataFinal, status);
  }

  @Override
  @Transactional(readOnly = true)
  public PedidoResponse obterPorId(Long id) {
    return pedidoRepository.findById(id)
        .map(this::mapearParaResponse)
        .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
  }

  @Override
  @Transactional(readOnly = true)
  public List<PedidoResponse> listarComFiltros(String status, LocalDateTime dataInicial, 
      LocalDateTime dataFinal) {
    if (status != null && dataInicial != null && dataFinal != null) {
      return pedidoRepository.findByStatusAndDataPedidoBetween(status, dataInicial, dataFinal)
          .stream()
          .map(this::mapearParaResponse)
          .toList();
    } else if (status != null) {
      return findByStatus(status);
    } else if (dataInicial != null && dataFinal != null) {
      return findByDataPedidoBetween(dataInicial, dataFinal);
    }
    return new ArrayList<>();
  }

  @Override
  @Transactional
  public PedidoResponse atualizarStatus(Long id, AtualizarStatusPedidoRequest request) {
    var pedido = pedidoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    pedido.setStatus(request.getStatus());
    var pedidoAtualizado = pedidoRepository.save(pedido);
    return mapearParaResponse(pedidoAtualizado);
  }

  @Override
  @Transactional
  public void cancelarPedido(Long id) {
    var pedido = pedidoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    pedido.setStatus("CANCELADO");
    pedidoRepository.save(pedido);
  }

  @Override
  @Transactional(readOnly = true)
  public List<PedidoResponse> pedidosPorCliente(Long clienteId) {
    return findByClienteId(clienteId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<PedidoResponse> pedidosPorRestaurante(Long restauranteId) {
    return pedidoRepository.findByRestauranteId(restauranteId)
        .stream()
        .map(this::mapearParaResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public CalcularPedidoResponse calcularTotal(CalcularPedidoRequest request) {
    var restaurante = restauranteRepository.findById(request.getRestauranteId())
        .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

    var itens = new ArrayList<PedidoProdutoResponse>();
    var subtotal = BigDecimal.ZERO;

    for (PedidoProdutoRequest itemRequest : request.getItens()) {
      var produto = produtoRepository.findById(itemRequest.getProdutoId())
          .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

      var precoUnitario = itemRequest.getPrecoUnitario();
      var quantidade = itemRequest.getQuantidade();
      var itemSubtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));

      var produtoResponse = new ProdutoResponse(
          produto.getId(),
          produto.getNome(),
          produto.getDescricao(),
          produto.getPreco(),
          produto.getDisponivel(),
          produto.getCategoria()
      );

      itens.add(new PedidoProdutoResponse(
          null,
          produtoResponse,
          quantidade,
          precoUnitario,
          itemSubtotal,
          itemRequest.getObservacoes()
      ));

      subtotal = subtotal.add(itemSubtotal);
    }

    var taxaEntrega = restaurante.getTaxaEntrega();
    var valorTotal = subtotal.add(taxaEntrega);

    var response = new CalcularPedidoResponse();
    response.setItens(itens);
    response.setSubtotal(subtotal);
    response.setTaxaEntrega(taxaEntrega);
    response.setValorTotal(valorTotal);

    return response;
  }

  private PedidoResponse mapearParaResponse(Pedido pedido) {
    var response = new PedidoResponse();
    response.setId(pedido.getId());
    response.setNumeroPedido(pedido.getNumeroPedido());
    response.setStatus(pedido.getStatus());
    response.setValorTotal(pedido.getValorTotal());
    response.setDataPedido(pedido.getDataPedido());

    // Mapear Cliente
    if (pedido.getCliente() != null) {
      var clienteResponse = new com.deliverytech.delivery.dto.response.ClienteResponse(
          pedido.getCliente().getId(),
          pedido.getCliente().getNome(),
          pedido.getCliente().getEmail(),
          pedido.getCliente().getTelefone(),
          pedido.getCliente().getCpf(),
          pedido.getCliente().getAtivo()
      );
      response.setCliente(clienteResponse);
    }

    // Mapear Restaurante
    if (pedido.getRestaurante() != null) {
      var restauranteResponse = new com.deliverytech.delivery.dto.response.RestauranteResponse(
          pedido.getRestaurante().getId(),
          pedido.getRestaurante().getNome(),
          pedido.getRestaurante().getEndereco(),
          pedido.getRestaurante().getTelefone(),
          pedido.getRestaurante().getCnpj(),
          pedido.getRestaurante().getRamoAtividade(),
          pedido.getRestaurante().getAtivo(),
          pedido.getRestaurante().getTaxaEntrega()
      );
      response.setRestaurante(restauranteResponse);
    }

    // Mapear Itens
    var itens = pedido.getItens()
        .stream()
        .map(item -> {
          var produtoResponse = new ProdutoResponse(
              item.getProduto().getId(),
              item.getProduto().getNome(),
              item.getProduto().getDescricao(),
              item.getProduto().getPreco(),
              item.getProduto().getDisponivel(),
              item.getProduto().getCategoria()
          );
          return new PedidoProdutoResponse(
              item.getId(),
              produtoResponse,
              item.getQuantidade(),
              item.getPrecoUnitario(),
              item.getSubtotal(),
              item.getObservacoes()
          );
        })
        .toList();
    response.setItens(new ArrayList<>(itens));

    return response;
  }

}
