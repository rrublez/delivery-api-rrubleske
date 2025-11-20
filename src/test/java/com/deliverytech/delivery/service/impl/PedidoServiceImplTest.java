package com.deliverytech.delivery.service.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.entity.Role;
import com.deliverytech.delivery.entity.Usuario;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

  @Mock
  private PedidoRepository pedidoRepository;

  @Mock
  private ClienteRepository clienteRepository;

  @Mock
  private RestauranteRepository restauranteRepository;

  @Mock
  private ProdutoRepository produtoRepository;

  private PedidoServiceImpl pedidoService;

  @BeforeEach
  void setUp() {
    pedidoService = new PedidoServiceImpl(pedidoRepository, clienteRepository, restauranteRepository, produtoRepository);
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void canAccessAllowsAdmin() {
    Usuario admin = new Usuario();
    admin.setRole(Role.ADMIN);
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(admin, null,
            List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
    assertTrue(pedidoService.canAccess(1L));
  }

  @Test
  void canAccessAllowsClienteWhenEmailsMatch() {
    Usuario clienteUsuario = new Usuario();
    clienteUsuario.setRole(Role.CLIENTE);
    clienteUsuario.setEmail("cliente@example.com");
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(clienteUsuario, null,
            List.of(new SimpleGrantedAuthority("ROLE_CLIENTE"))));

    Cliente cliente = new Cliente();
    cliente.setEmail(clienteUsuario.getEmail());

    Pedido pedido = new Pedido();
    pedido.setId(5L);
    pedido.setCliente(cliente);

    when(pedidoRepository.findById(5L)).thenReturn(Optional.of(pedido));

    assertTrue(pedidoService.canAccess(5L));
  }

  @Test
  void canAccessAllowsRestauranteWhenIdsMatch() {
    Usuario restauranteUsuario = new Usuario();
    restauranteUsuario.setRole(Role.RESTAURANTE);
    restauranteUsuario.setRestauranteId(99L);
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(restauranteUsuario, null,
            List.of(new SimpleGrantedAuthority("ROLE_RESTAURANTE"))));

    Restaurante restaurante = new Restaurante();
    restaurante.setId(99L);

    Pedido pedido = new Pedido();
    pedido.setId(7L);
    pedido.setRestaurante(restaurante);

    when(pedidoRepository.findById(7L)).thenReturn(Optional.of(pedido));

    assertTrue(pedidoService.canAccess(7L));
  }

  @Test
  void canAccessDeniesNonOwners() {
    Usuario entregador = new Usuario();
    entregador.setRole(Role.ENTREGADOR);
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(entregador, null,
            List.of(new SimpleGrantedAuthority("ROLE_ENTREGADOR"))));

    Pedido pedido = new Pedido();
    pedido.setId(8L);

    when(pedidoRepository.findById(8L)).thenReturn(Optional.of(pedido));

    assertFalse(pedidoService.canAccess(8L));
  }
}
