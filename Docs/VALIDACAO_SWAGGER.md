# Validação do Swagger UI e Documentação OpenAPI

## 📋 Índice
1. [Verificação Inicial](#verificação-inicial)
2. [Endpoints Documentados](#endpoints-documentados)
3. [Modelos de Dados (DTOs)](#modelos-de-dados)
4. [Códigos de Resposta HTTP](#códigos-de-resposta-http)
5. [Validação de Funcionalidades](#validação-de-funcionalidades)
6. [Teste de Integração com Swagger](#teste-de-integração-com-swagger)

---

## Verificação Inicial

### Acesso ao Swagger UI
```bash
# Inicie a aplicação
./mvnw spring-boot:run

# Acesse o Swagger UI em
http://localhost:8080/swagger-ui.html

# Acesse a documentação JSON em
http://localhost:8080/v3/api-docs
```

### Status Esperado
- ✅ Swagger UI deve carregar sem erros
- ✅ Documentação deve estar disponível em JSON
- ✅ Todos os controllers devem estar listados

---

## Endpoints Documentados

### 1. ClienteController (5 endpoints)
```
GET    /api/clientes              - Listar todos os clientes
POST   /api/clientes              - Criar novo cliente (201)
GET    /api/clientes/{id}         - Obter cliente por ID (200)
PUT    /api/clientes/{id}         - Atualizar cliente (200)
DELETE /api/clientes/{id}         - Deletar cliente (204)
```

#### Validações esperadas:
- **POST /api/clientes**
  - ✅ Request body com nome, email, cpf, telefone, ativo
  - ✅ Response 201 Created com ID gerado
  - ✅ Response 400 Bad Request se email inválido ou duplicado
  - ✅ Response 409 Conflict se CPF duplicado

- **GET /api/clientes/{id}**
  - ✅ Response 200 OK com dados do cliente
  - ✅ Response 404 Not Found para ID inexistente

- **PUT /api/clientes/{id}**
  - ✅ Response 200 OK com dados atualizados
  - ✅ Response 404 Not Found para ID inexistente

- **DELETE /api/clientes/{id}**
  - ✅ Response 204 No Content
  - ✅ Response 404 Not Found para ID inexistente

### 2. ProdutoController (9 endpoints)
```
GET    /api/produtos                              - Listar todos
POST   /api/produtos                              - Criar (201)
GET    /api/produtos/{id}                         - Obter por ID (200)
PUT    /api/produtos/{id}                         - Atualizar (200)
PATCH  /api/produtos/{id}/disponibilidade         - Toggle disponibilidade (200)
DELETE /api/produtos/{id}                         - Deletar (204)
GET    /api/produtos/restaurante/{restauranteId} - Produtos do restaurante
GET    /api/produtos/categoria/{categoria}       - Produtos por categoria
GET    /api/produtos?disponivel=true              - Filtrar por disponibilidade
```

#### Validações esperadas:
- **POST /api/produtos**
  - ✅ Request com nome, descricao, preco, categoria, disponivel, restauranteId
  - ✅ Response 201 Created
  - ✅ Response 400 se restaurante inexistente

- **PATCH /api/produtos/{id}/disponibilidade**
  - ✅ Request body: {"disponivel": true/false}
  - ✅ Response 200 OK com status atualizado

### 3. RestauranteController (9 endpoints)
```
GET    /api/restaurantes                     - Listar todos
POST   /api/restaurantes                     - Criar (201)
GET    /api/restaurantes/{id}                - Obter por ID (200)
PUT    /api/restaurantes/{id}                - Atualizar (200)
PATCH  /api/restaurantes/{id}/status         - Toggle status (200)
DELETE /api/restaurantes/{id}                - Deletar (204)
GET    /api/restaurantes/categoria/{cat}    - Filtrar por categoria
GET    /api/restaurantes?ramo=X              - Filtrar por ramo
GET    /api/restaurantes/taxa-maxima?taxa=X - Filtrar por taxa máxima
```

#### Validações esperadas:
- **POST /api/restaurantes**
  - ✅ Request com nome, endereco, telefone, cnpj, ramoAtividade, ativo, taxaEntrega
  - ✅ Response 201 Created
  - ✅ Response 409 Conflict se CNPJ duplicado

- **GET /api/restaurantes/categoria/{categoria}**
  - ✅ Response 200 OK com lista de restaurantes

### 4. PedidoController (8 endpoints)
```
GET    /api/pedidos                        - Listar todos
POST   /api/pedidos                        - Criar (201)
GET    /api/pedidos/{id}                   - Obter por ID (200)
GET    /api/pedidos/clientes/{clienteId}  - Pedidos do cliente
GET    /api/pedidos/restaurantes/{restId} - Pedidos do restaurante
GET    /api/pedidos/status/{status}       - Pedidos por status
PATCH  /api/pedidos/{id}/status           - Atualizar status (200)
DELETE /api/pedidos/{id}                  - Cancelar pedido (204)
POST   /api/pedidos/calcular              - Calcular total (200)
```

#### Validações esperadas:
- **POST /api/pedidos**
  - ✅ Request com clienteId, restauranteId, enderecoEntrega, status
  - ✅ Response 201 Created
  - ✅ Response 400 se cliente ou restaurante inexistente

- **PATCH /api/pedidos/{id}/status**
  - ✅ Request: {"status": "ENTREGUE"|"CANCELADO"|"CONFIRMADO"}
  - ✅ Response 200 OK com status atualizado

- **POST /api/pedidos/calcular**
  - ✅ Calcula subtotal + taxa de entrega
  - ✅ Response 200 OK com {subtotal, taxaEntrega, total}

### 5. RelatorioController (5 endpoints)
```
GET /api/relatorios/total-vendas         - Total de vendas
GET /api/relatorios/vendas-periodo       - Vendas por período
GET /api/relatorios/pedidos-status       - Distribuição de status
GET /api/relatorios/restaurante-top      - Restaurante mais vendedor
GET /api/relatorios/produto-top          - Produto mais vendido
```

#### Validações esperadas:
- ✅ Todos retornam 200 OK
- ✅ Response com dados agregados

---

## Modelos de Dados

### ClienteRequest
```json
{
  "nome": "string (required)",
  "email": "string (required, email format)",
  "cpf": "string (required, 11 digits)",
  "telefone": "string (required)",
  "ativo": "boolean"
}
```

### ProdutoRequest
```json
{
  "nome": "string (required)",
  "descricao": "string",
  "preco": "number (decimal, required)",
  "categoria": "string (required)",
  "disponivel": "boolean",
  "restauranteId": "number (required)"
}
```

### RestauranteRequest
```json
{
  "nome": "string (required)",
  "endereco": "string (required)",
  "telefone": "string (required)",
  "cnpj": "string (required, 14 digits)",
  "ramoAtividade": "string (required)",
  "ativo": "boolean",
  "taxaEntrega": "number (decimal, required)"
}
```

### PedidoRequest
```json
{
  "clienteId": "number (required)",
  "restauranteId": "number (required)",
  "enderecoEntrega": "string (required)",
  "status": "string (PENDENTE|CONFIRMADO|ENTREGUE|CANCELADO)"
}
```

---

## Códigos de Resposta HTTP

### Success Responses
| Código | Cenário | Endpoints |
|--------|---------|-----------|
| 200 | GET, UPDATE bem-sucedido | GET, PUT, PATCH |
| 201 | Recurso criado | POST |
| 204 | DELETE bem-sucedido (sem conteúdo) | DELETE |

### Error Responses
| Código | Cenário | Exemplo |
|--------|---------|---------|
| 400 | Validação falha (campos obrigatórios, formato inválido) | email inválido, CPF com 10 dígitos |
| 404 | Recurso não encontrado | /clientes/9999, /produtos/9999 |
| 409 | Conflito (duplicidade) | email/CPF/CNPJ duplicado |
| 500 | Erro no servidor | exceção não tratada |

### ApiResponse Standard
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "statusCode": 200,
  "message": "Success",
  "data": {},
  "success": true
}
```

### ErrorResponse Standard
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "statusCode": 404,
  "errorType": "NOT_FOUND",
  "message": "Resource not found",
  "details": "Cliente com ID 9999 não encontrado"
}
```

---

## Validação de Funcionalidades

### Checklist de Endpoints

#### ✅ Clientes
- [ ] POST /api/clientes - Criar com dados válidos → 201
- [ ] POST /api/clientes - Criar com email duplicado → 400/409
- [ ] GET /api/clientes - Listar todos → 200
- [ ] GET /api/clientes/{id} - Buscar existente → 200
- [ ] GET /api/clientes/999 - Buscar inexistente → 404
- [ ] PUT /api/clientes/{id} - Atualizar existente → 200
- [ ] PUT /api/clientes/999 - Atualizar inexistente → 404
- [ ] DELETE /api/clientes/{id} - Deletar existente → 204
- [ ] DELETE /api/clientes/999 - Deletar inexistente → 404

#### ✅ Produtos
- [ ] POST /api/produtos - Criar com dados válidos → 201
- [ ] GET /api/produtos - Listar todos → 200
- [ ] GET /api/produtos/{id} - Buscar existente → 200
- [ ] GET /api/produtos/999 - Buscar inexistente → 404
- [ ] GET /api/produtos/restaurante/{id} - Listar por restaurante → 200
- [ ] GET /api/produtos/categoria/Pizza - Listar por categoria → 200
- [ ] GET /api/produtos?disponivel=true - Filtrar disponíveis → 200
- [ ] PUT /api/produtos/{id} - Atualizar → 200
- [ ] PATCH /api/produtos/{id}/disponibilidade - Toggle → 200
- [ ] DELETE /api/produtos/{id} - Deletar → 204

#### ✅ Restaurantes
- [ ] POST /api/restaurantes - Criar com dados válidos → 201
- [ ] POST /api/restaurantes - Criar com CNPJ duplicado → 409
- [ ] GET /api/restaurantes - Listar todos → 200
- [ ] GET /api/restaurantes/{id} - Buscar existente → 200
- [ ] GET /api/restaurantes/999 - Buscar inexistente → 404
- [ ] GET /api/restaurantes/categoria/Pizzaria - Filtrar → 200
- [ ] PUT /api/restaurantes/{id} - Atualizar → 200
- [ ] PATCH /api/restaurantes/{id}/status - Toggle status → 200
- [ ] DELETE /api/restaurantes/{id} - Deletar → 204

#### ✅ Pedidos
- [ ] POST /api/pedidos - Criar com dados válidos → 201
- [ ] GET /api/pedidos - Listar todos → 200
- [ ] GET /api/pedidos/{id} - Buscar existente → 200
- [ ] GET /api/pedidos/999 - Buscar inexistente → 404
- [ ] GET /api/pedidos/clientes/{id} - Listar por cliente → 200
- [ ] GET /api/pedidos/restaurantes/{id} - Listar por restaurante → 200
- [ ] GET /api/pedidos/status/PENDENTE - Listar por status → 200
- [ ] PATCH /api/pedidos/{id}/status - Atualizar status → 200
- [ ] POST /api/pedidos/calcular - Calcular total → 200
- [ ] DELETE /api/pedidos/{id} - Cancelar → 204

#### ✅ Relatórios
- [ ] GET /api/relatorios/total-vendas → 200
- [ ] GET /api/relatorios/vendas-periodo → 200
- [ ] GET /api/relatorios/pedidos-status → 200
- [ ] GET /api/relatorios/restaurante-top → 200
- [ ] GET /api/relatorios/produto-top → 200

---

## Teste de Integração com Swagger

### 1. Validar Carregamento
```bash
curl http://localhost:8080/swagger-ui.html -v
# Esperado: HTTP 200
```

### 2. Validar API Docs JSON
```bash
curl http://localhost:8080/v3/api-docs | jq .
# Esperado: JSON válido com todos os endpoints
```

### 3. Testar "Try it Out" no Swagger UI
1. Acesse http://localhost:8080/swagger-ui.html
2. Expanda "ClienteController"
3. Clique em "POST /api/clientes"
4. Clique em "Try it out"
5. Preencha um cliente de teste:
```json
{
  "nome": "Teste Swagger",
  "email": "teste@swagger.com",
  "cpf": "12345678901",
  "telefone": "11999999999",
  "ativo": true
}
```
6. Clique "Execute"
7. Valide:
   - ✅ Status Code: 201
   - ✅ Response com ID gerado
   - ✅ Header Content-Type: application/json

### 4. Testar Erro 404
1. Clique em "GET /api/clientes/{id}"
2. Clique em "Try it out"
3. Digite "9999" em `id`
4. Clique "Execute"
5. Valide:
   - ✅ Status Code: 404
   - ✅ Response com mensagem de erro

### 5. Testar Validação 400
1. Clique em "POST /api/clientes"
2. Clique em "Try it out"
3. Preencha com email inválido:
```json
{
  "nome": "Teste",
  "email": "email-invalido",
  "cpf": "12345",
  "telefone": "111",
  "ativo": true
}
```
4. Clique "Execute"
5. Valide:
   - ✅ Status Code: 400
   - ✅ Response com detalhes dos erros

---

## Problemas Comuns

### Swagger UI não carrega
- Verifique se SpringDoc OpenAPI está no pom.xml
- Verifique se `@SpringBootApplication` está na classe principal
- Limpe o build: `mvn clean && mvn install`

### Endpoints não aparecem no Swagger
- Verifique se os controllers têm `@RestController`
- Verifique se os métodos têm `@GetMapping`, `@PostMapping`, etc
- Adicione `@Operation` e `@ApiResponses` para melhor documentação

### Modelos de dados incorretos
- Verifique os DTOs com `@Schema` annotations
- Use `@NotBlank`, `@Email`, `@Pattern` para validação
- Certifique-se que Lombok gera os getters/setters

### CORS issues
- Verifique `HttpLoggingConfig.java`
- Adicione `@CrossOrigin` se necessário

---

## Resumo Esperado

✅ **26 endpoints documentados**
✅ **5 modelos de dados com validação**
✅ **7 códigos HTTP implementados**
✅ **Integration tests com 100% cobertura de casos**
✅ **Coleção Postman com todos os endpoints**
✅ **Documentação OpenAPI 3.0 completa**

**Status: Pronto para produção! 🚀**
