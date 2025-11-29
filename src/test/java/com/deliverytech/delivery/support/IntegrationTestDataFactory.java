package com.deliverytech.delivery.support;

import com.deliverytech.delivery.dto.cliente.request.ClienteRequest;
import com.deliverytech.delivery.dto.cliente.request.ClienteUpdateRequest;
import com.deliverytech.delivery.dto.pedido.request.CalcularPedidoRequest;
import com.deliverytech.delivery.dto.pedido.request.PedidoRequest;
import com.deliverytech.delivery.dto.shared.request.PedidoProdutoRequest;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class IntegrationTestDataFactory {

  private final ClienteRepository clienteRepository;
  private final RestauranteRepository restauranteRepository;
  private final ProdutoRepository produtoRepository;

  public IntegrationTestDataFactory(
      ClienteRepository clienteRepository,
      RestauranteRepository restauranteRepository,
      ProdutoRepository produtoRepository) {
    this.clienteRepository = clienteRepository;
    this.restauranteRepository = restauranteRepository;
    this.produtoRepository = produtoRepository;
  }

  public ClienteRequest buildClienteRequest() {
    ClienteRequest request = new ClienteRequest();
    request.setNome("Cliente Teste " + randomDigits(3));
    request.setEmail("cliente" + System.nanoTime() + "@test.com");
    request.setCpf(generateCpf());
    request.setTelefone("11" + randomDigits(9));
    request.setAtivo(true);
    return request;
  }

  public ClienteUpdateRequest buildClienteUpdateRequest(String nome, String email, String telefone, String cpf, Boolean ativo) {
    ClienteUpdateRequest request = new ClienteUpdateRequest();
    request.setNome(nome);
    request.setEmail(email);
    request.setTelefone(telefone);
    request.setCpf(cpf);
    request.setAtivo(ativo);
    return request;
  }

  public Cliente criarCliente(String nome, String email, String telefone, String cpf, boolean ativo) {
    Cliente cliente = new Cliente();
    cliente.setNome(nome);
    cliente.setEmail(email);
    cliente.setTelefone(telefone);
    cliente.setCpf(cpf);
    cliente.setAtivo(ativo);
    return clienteRepository.save(cliente);
  }

  public Restaurante criarRestaurante(String nome) {
    Restaurante restaurante = new Restaurante();
    restaurante.setNome(nome);
    restaurante.setEndereco("Rua " + randomDigits(3) + " Teste");
    restaurante.setTelefone("11" + randomDigits(8));
    restaurante.setCnpj(randomDigits(14));
    restaurante.setRamoAtividade("Culinária");
    restaurante.setAtivo(true);
    restaurante.setTaxaEntrega(BigDecimal.valueOf(5.00));
    return restauranteRepository.save(restaurante);
  }

  public Produto criarProduto(Restaurante restaurante, String nome, BigDecimal preco, int estoque) {
    Produto produto = new Produto();
    produto.setNome(nome);
    produto.setDescricao("Produto criado para testes");
    produto.setPreco(preco);
    produto.setDisponivel(true);
    produto.setCategoria("Teste");
    produto.setEstoque(estoque);
    produto.getRestaurantes().add(restaurante);
    restaurante.getProdutos().add(produto);
    var salvo = produtoRepository.save(produto);
    restauranteRepository.save(restaurante);
    return salvo;
  }

  public PedidoProdutoRequest buildPedidoItem(Long produtoId, int quantidade, BigDecimal precoUnitario, String observacoes) {
    PedidoProdutoRequest item = new PedidoProdutoRequest();
    item.setProdutoId(produtoId);
    item.setQuantidade(quantidade);
    item.setPrecoUnitario(precoUnitario);
    item.setObservacoes(observacoes);
    return item;
  }

  public PedidoRequest buildPedidoRequest(Long clienteId, Long restauranteId, List<PedidoProdutoRequest> itens) {
    PedidoRequest request = new PedidoRequest();
    request.setNumeroPedido("PED-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))); 
    request.setStatus("PENDENTE");
    request.setClienteId(clienteId);
    request.setRestauranteId(restauranteId);
    request.setItens(new ArrayList<>(itens));
    return request;
  }

  public CalcularPedidoRequest buildCalcularRequest(Long restauranteId, List<PedidoProdutoRequest> itens) {
    CalcularPedidoRequest request = new CalcularPedidoRequest();
    request.setRestauranteId(restauranteId);
    request.setItens(new ArrayList<>(itens));
    return request;
  }

  public String generateCpf() {
    int[] digits = new int[11];
    ThreadLocalRandom random = ThreadLocalRandom.current();

    for (int i = 0; i < 9; i++) {
      digits[i] = random.nextInt(0, 10);
    }

    digits[9] = calculateCpfDigit(digits, 9);
    digits[10] = calculateCpfDigit(digits, 10);

    StringBuilder builder = new StringBuilder(11);
    for (int digit : digits) {
      builder.append(digit);
    }
    return builder.toString();
  }

  private int calculateCpfDigit(int[] digits, int length) {
    int sum = 0;
    for (int i = 0; i < length; i++) {
      sum += digits[i] * ((length + 1) - i);
    }
    int remainder = sum % 11;
    return remainder < 2 ? 0 : 11 - remainder;
  }

  public String nextPhone() {
    return "11" + randomDigits(8);
  }

  private String randomDigits(int length) {
    StringBuilder builder = new StringBuilder(length);
    ThreadLocalRandom random = ThreadLocalRandom.current();
    for (int i = 0; i < length; i++) {
      builder.append(random.nextInt(0, 10));
    }
    return builder.toString();
  }

}
