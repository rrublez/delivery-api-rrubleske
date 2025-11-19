package com.deliverytech.delivery.steps;

import org.springframework.beans.factory.annotation.Autowired;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class AuthorizationStepsPt {

  @Autowired
  private AuthorizationSteps en;

  // Contexto / Dado
  @Dado("que não existe usuário com email {string}")
  public void naoExisteUsuarioComEmail(String email) { en.removeExistingUser(email); }

  @Dado("que o token do admin está disponível")
  public void tokenDoAdminDisponivel() { en.ensureAdminToken(); }

  @Dado("que o admin criou o restaurante apelido {string}")
  public void adminCriouRestauranteApelido(String alias) { en.adminCreatesRestaurant(alias); }

  @Dado("que o restaurante {string} possui o produto {string}")
  public void restaurantePossuiProduto(String alias, String produto) { en.restaurantHasProduct(alias, produto); }

  // Ações / Quando
  @Quando("eu registro o usuário {string} com email {string} e papel {string}")
  public void registroUsuario(String nome, String email, String papel) { en.registerUser(nome, email, papel); }

  @Quando("o usuário do restaurante {string} está registrado para o apelido {string}")
  public void usuarioRestauranteRegistradoParaApelido(String email, String alias) { en.registerRestaurantUser(email, alias); }

  @Quando("faço login com email {string} e senha {string}")
  public void facoLogin(String email, String senha) { en.loginAs(email, senha); }

  @Quando("chamo GET {string} sem token")
  public void chamoGetSemToken(String path) { en.callGetWithoutToken(path); }

  @Quando("chamo GET {string} com token para {string}")
  public void chamoGetComTokenPara(String path, String email) { en.callGetWithToken(path, email); }

  @Quando("crio o produto {string} com token para {string}")
  public void crioProdutoComToken(String produto, String email) { en.createProduct(produto, email); }

  @Quando("atualizo o produto {string} com token para {string}")
  public void atualizoProdutoComToken(String produto, String email) { en.updateProduct(produto, email); }

  @Quando("chamo GET {string} com token expirado para {string}")
  public void chamoGetComTokenExpiradoPara(String path, String email) { en.callWithExpiredToken(path, email); }

  @Quando("o cliente {string} cria um pedido para o restaurante {string} com o produto {string}")
  public void clienteCriaPedido(String cliente, String restauranteAlias, String produto) { en.clientCreatesPedido(cliente, restauranteAlias, produto); }

  // Verificações / Então
  @Entao("o status da resposta deve ser {int}")
  public void statusRespostaDeveSer(int status) { en.verifyStatus(status); }

  @Entao("a resposta contém token")
  public void respostaContemToken() { en.responseContainsToken(); }

  @Entao("a resposta deve ser não autorizada")
  public void respostaNaoAutorizada() { en.responseShouldBeUnauthorized(); }

  @Entao("o pedido referencia o restaurante {string} e o produto {string}")
  public void pedidoReferenciaRestauranteEProduto(String alias, String produto) { en.verifyOrderReferences(alias, produto); }
}
