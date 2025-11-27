package com.deliverytech.delivery.util;

import com.deliverytech.delivery.dto.cliente.request.ClienteRequest;
import com.deliverytech.delivery.dto.pedido.request.PedidoRequest;
import com.deliverytech.delivery.dto.shared.request.PedidoProdutoRequest;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.PedidoProduto;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Utility class for building test data objects.
 * Provides builder methods for entities and DTOs used in unit tests.
 */
public class TestDataBuilder {

    // ==================== CLIENTE BUILDERS ====================
    
    public static Cliente buildCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setEmail("joao.silva@email.com");
        cliente.setTelefone("11987654321");
        cliente.setCpf("12345678901");
        cliente.setAtivo(true);
        return cliente;
    }
    
    public static Cliente buildCliente(Long id, String nome, String email) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setTelefone("11987654321");
        cliente.setCpf("12345678901");
        cliente.setAtivo(true);
        return cliente;
    }
    
    public static Cliente buildInactiveCliente() {
        Cliente cliente = buildCliente();
        cliente.setAtivo(false);
        return cliente;
    }
    
    public static ClienteRequest buildClienteRequest() {
        ClienteRequest request = new ClienteRequest();
        request.setNome("João Silva");
        request.setEmail("joao.silva@email.com");
        request.setTelefone("11987654321");
        request.setCpf("12345678901");
        request.setAtivo(true);
        return request;
    }
    
    public static ClienteRequest buildClienteRequest(String nome, String email) {
        ClienteRequest request = new ClienteRequest();
        request.setNome(nome);
        request.setEmail(email);
        request.setTelefone("11987654321");
        request.setCpf("12345678901");
        request.setAtivo(true);
        return request;
    }
    
    public static ClienteRequest buildClienteRequestWithoutAtivo() {
        ClienteRequest request = new ClienteRequest();
        request.setNome("João Silva");
        request.setEmail("joao.silva@email.com");
        request.setTelefone("11987654321");
        request.setCpf("12345678901");
        // ativo not set (null)
        return request;
    }

    // ==================== RESTAURANTE BUILDERS ====================
    
    public static Restaurante buildRestaurante() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Bom Sabor");
        restaurante.setEndereco("Rua das Flores, 123");
        restaurante.setTelefone("1133334444");
        restaurante.setCnpj("12345678000199");
        restaurante.setRamoAtividade("ITALIANA");
        restaurante.setAtivo(true);
        restaurante.setTaxaEntrega(new BigDecimal("5.00"));
        restaurante.setProdutos(new HashSet<>());
        return restaurante;
    }
    
    public static Restaurante buildRestaurante(Long id, String nome, BigDecimal taxaEntrega) {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(id);
        restaurante.setNome(nome);
        restaurante.setEndereco("Rua das Flores, 123");
        restaurante.setTelefone("1133334444");
        restaurante.setCnpj("12345678000199");
        restaurante.setRamoAtividade("ITALIANA");
        restaurante.setAtivo(true);
        restaurante.setTaxaEntrega(taxaEntrega);
        restaurante.setProdutos(new HashSet<>());
        return restaurante;
    }

    // ==================== PRODUTO BUILDERS ====================
    
    public static Produto buildProduto() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Pizza Margherita");
        produto.setDescricao("Pizza com molho de tomate, mussarela e manjericão");
        produto.setPreco(new BigDecimal("35.00"));
        produto.setDisponivel(true);
        produto.setEstoque(100);
        produto.setCategoria("PIZZA");
        produto.setRestaurantes(new HashSet<>());
        return produto;
    }
    
    public static Produto buildProduto(Long id, String nome, BigDecimal preco) {
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome(nome);
        produto.setDescricao("Descrição do produto");
        produto.setPreco(preco);
        produto.setDisponivel(true);
        produto.setEstoque(100);
        produto.setCategoria("GERAL");
        produto.setRestaurantes(new HashSet<>());
        return produto;
    }
    
    public static Produto buildProduto(Long id, String nome, BigDecimal preco, Boolean disponivel) {
        Produto produto = buildProduto(id, nome, preco);
        produto.setDisponivel(disponivel);
        return produto;
    }
    
    public static Produto buildProduto(Long id, String nome, BigDecimal preco, Boolean disponivel, Integer estoque) {
        Produto produto = buildProduto(id, nome, preco, disponivel);
        produto.setEstoque(estoque);
        return produto;
    }
    
    public static Produto buildUnavailableProduto() {
        Produto produto = buildProduto();
        produto.setDisponivel(false);
        return produto;
    }
    
    public static Produto buildProdutoWithLowStock() {
        Produto produto = buildProduto();
        produto.setEstoque(5);
        return produto;
    }
    
    public static Produto buildProdutoWithZeroStock() {
        Produto produto = buildProduto();
        produto.setEstoque(0);
        return produto;
    }

    // ==================== PEDIDO BUILDERS ====================
    
    public static Pedido buildPedido() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setNumeroPedido("PED-001");
        pedido.setStatus("PENDENTE");
        pedido.setCliente(buildCliente());
        pedido.setRestaurante(buildRestaurante());
        pedido.setValorTotal(new BigDecimal("40.00"));
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setItens(new ArrayList<>());
        return pedido;
    }
    
    public static Pedido buildPedido(Long id, String numeroPedido, String status) {
        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setNumeroPedido(numeroPedido);
        pedido.setStatus(status);
        pedido.setCliente(buildCliente());
        pedido.setRestaurante(buildRestaurante());
        pedido.setValorTotal(new BigDecimal("40.00"));
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setItens(new ArrayList<>());
        return pedido;
    }
    
    public static Pedido buildPedidoWithItems(Cliente cliente, Restaurante restaurante, List<PedidoProduto> itens) {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setNumeroPedido("PED-001");
        pedido.setStatus("PENDENTE");
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setItens(itens);
        
        // Calculate total
        BigDecimal subtotal = itens.stream()
                .map(PedidoProduto::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido.setValorTotal(subtotal.add(restaurante.getTaxaEntrega()));
        
        return pedido;
    }
    
    public static PedidoRequest buildPedidoRequest() {
        List<PedidoProdutoRequest> itens = new ArrayList<>();
        itens.add(buildPedidoProdutoRequest(1L, 2));
        
        PedidoRequest request = new PedidoRequest();
        request.setNumeroPedido("PED-001");
        request.setStatus("PENDENTE");
        request.setClienteId(1L);
        request.setRestauranteId(1L);
        request.setItens(itens);
        return request;
    }
    
    public static PedidoRequest buildPedidoRequest(Long clienteId, Long restauranteId, List<PedidoProdutoRequest> itens) {
        PedidoRequest request = new PedidoRequest();
        request.setNumeroPedido("PED-001");
        request.setStatus("PENDENTE");
        request.setClienteId(clienteId);
        request.setRestauranteId(restauranteId);
        request.setItens(itens);
        return request;
    }
    
    public static PedidoRequest buildPedidoRequestWithoutStatus(Long clienteId, Long restauranteId) {
        List<PedidoProdutoRequest> itens = new ArrayList<>();
        itens.add(buildPedidoProdutoRequest(1L, 2));
        
        PedidoRequest request = new PedidoRequest();
        request.setNumeroPedido("PED-001");
        // status not set (null)
        request.setClienteId(clienteId);
        request.setRestauranteId(restauranteId);
        request.setItens(itens);
        return request;
    }

    // ==================== PEDIDO PRODUTO BUILDERS ====================
    
    public static PedidoProduto buildPedidoProduto() {
        PedidoProduto item = new PedidoProduto();
        item.setId(1L);
        item.setProduto(buildProduto());
        item.setQuantidade(2);
        item.setPrecoUnitario(new BigDecimal("35.00"));
        item.setSubtotal(new BigDecimal("70.00"));
        item.setObservacoes("");
        return item;
    }
    
    public static PedidoProduto buildPedidoProduto(Produto produto, Integer quantidade) {
        PedidoProduto item = new PedidoProduto();
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());
        item.setSubtotal(produto.getPreco().multiply(new BigDecimal(quantidade)));
        item.setObservacoes("");
        return item;
    }
    
    public static PedidoProdutoRequest buildPedidoProdutoRequest(Long produtoId, Integer quantidade) {
        PedidoProdutoRequest request = new PedidoProdutoRequest();
        request.setProdutoId(produtoId);
        request.setQuantidade(quantidade);
        request.setPrecoUnitario(new BigDecimal("35.00"));
        request.setObservacoes("");
        return request;
    }
    
    public static PedidoProdutoRequest buildPedidoProdutoRequest(Long produtoId, Integer quantidade, String observacoes) {
        PedidoProdutoRequest request = new PedidoProdutoRequest();
        request.setProdutoId(produtoId);
        request.setQuantidade(quantidade);
        request.setPrecoUnitario(new BigDecimal("35.00"));
        request.setObservacoes(observacoes);
        return request;
    }
    
    // ==================== UTILITY METHODS ====================
    
    public static List<PedidoProdutoRequest> buildPedidoProdutoRequestList(Long... produtoIds) {
        List<PedidoProdutoRequest> itens = new ArrayList<>();
        for (Long produtoId : produtoIds) {
            itens.add(buildPedidoProdutoRequest(produtoId, 1));
        }
        return itens;
    }
    
    public static List<Produto> buildProdutoList(int count) {
        List<Produto> produtos = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            produtos.add(buildProduto((long) i, "Produto " + i, new BigDecimal("10.00")));
        }
        return produtos;
    }
    
    public static List<Cliente> buildClienteList(int count) {
        List<Cliente> clientes = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            clientes.add(buildCliente((long) i, "Cliente " + i, "cliente" + i + "@email.com"));
        }
        return clientes;
    }
}
