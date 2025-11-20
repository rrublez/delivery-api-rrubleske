# language: pt
Funcionalidade: Autenticação e Autorização
  Como usuário da API de delivery
  Eu quero autenticação e autorização adequadas
  Para que os recursos sejam protegidos de acordo com os papéis

  Cenário: Registrar um novo usuário cliente
    Dado que não existe usuário com email "newclient@test.com"
    Quando eu registro o usuário "New Client" com email "newclient@test.com" e papel "CLIENTE"
    Então o status da resposta deve ser 201

  Cenário: Login com credenciais válidas
    Dado que não existe usuário com email "validuser@test.com"
    Quando eu registro o usuário "Valid User" com email "validuser@test.com" e papel "CLIENTE"
    E faço login com email "validuser@test.com" e senha "123456"
    Então o status da resposta deve ser 200
    E a resposta contém token

  Cenário: Login com credenciais inválidas
    Quando faço login com email "nonexistent@test.com" e senha "wrongpass"
    Então o status da resposta deve ser 401

  Cenário: Acessar endpoint protegido sem token
    Quando chamo GET "/api/pedidos/meus" sem token
    Então a resposta deve ser não autorizada

  Cenário: Acessar endpoint protegido com token válido
    Dado que não existe usuário com email "autheduser@test.com"
    Quando eu registro o usuário "Authed User" com email "autheduser@test.com" e papel "CLIENTE"
    E faço login com email "autheduser@test.com" e senha "123456"
    E chamo GET "/api/auth/me" com token para "autheduser@test.com"
    Então o status da resposta deve ser 200

  Cenário: Admin cria restaurante e associa usuário de restaurante
    Dado que o token do admin está disponível
    Quando que o admin criou o restaurante apelido "TestRestaurant"
    E o usuário do restaurante "restaurant@test.com" está registrado para o apelido "TestRestaurant"
    Então o status da resposta deve ser 201

  Cenário: Usuário restaurante gerencia seus próprios produtos
    Dado que o token do admin está disponível
    E que o admin criou o restaurante apelido "MyRestaurant"
    E o usuário do restaurante "myrest@test.com" está registrado para o apelido "MyRestaurant"
    Quando faço login com email "myrest@test.com" e senha "123456"
    E crio o produto "TestProduct" com token para "myrest@test.com"
    Então o status da resposta deve ser 201

  Cenário: Cliente cria pedido com produto do restaurante
    Dado que o token do admin está disponível
    E que o admin criou o restaurante apelido "OrderRestaurant"
    E o usuário do restaurante "orderrest@test.com" está registrado para o apelido "OrderRestaurant"
    E faço login com email "orderrest@test.com" e senha "123456"
    E que o restaurante "OrderRestaurant" possui o produto "OrderProduct"
    E que não existe usuário com email "orderclient@test.com"
    E eu registro o usuário "Order Client" com email "orderclient@test.com" e papel "CLIENTE"
    Quando faço login com email "orderclient@test.com" e senha "123456"
    E o cliente "orderclient@test.com" cria um pedido para o restaurante "OrderRestaurant" com o produto "OrderProduct"
    Então o status da resposta deve ser 201
    E o pedido referencia o restaurante "OrderRestaurant" e o produto "OrderProduct"

  Cenário: Acessar endpoint com token expirado
    Dado que não existe usuário com email "expireduser@test.com"
    Quando eu registro o usuário "Expired User" com email "expireduser@test.com" e papel "CLIENTE"
    E chamo GET "/api/auth/me" com token expirado para "expireduser@test.com"
    Então a resposta deve ser não autorizada

  Cenário: Endpoint público acessível sem autenticação
    Quando chamo GET "/api/restaurantes" sem token
    Então o status da resposta deve ser 200

  Cenário: Acesso negado por papel ao criar produto como CLIENTE
    Dado que não existe usuário com email "client-prod@test.com"
    Quando eu registro o usuário "Client Prod" com email "client-prod@test.com" e papel "CLIENTE"
    E faço login com email "client-prod@test.com" e senha "123456"
    E crio o produto "ClientBlockedProduct" com token para "client-prod@test.com"
    Então o status da resposta deve ser 403

  Cenário: Proibido atualizar produto de outro restaurante
    Dado que o token do admin está disponível
    E que o admin criou o restaurante apelido "OwnerR"
    E o usuário do restaurante "owner@test.com" está registrado para o apelido "OwnerR"
    Quando faço login com email "owner@test.com" e senha "123456"
    E crio o produto "OwnedProduct" com token para "owner@test.com"
    E que o admin criou o restaurante apelido "OtherR"
    E o usuário do restaurante "other@test.com" está registrado para o apelido "OtherR"
    E faço login com email "other@test.com" e senha "123456"
    E atualizo o produto "OwnedProduct" com token para "other@test.com"
    Então o status da resposta deve ser 403
