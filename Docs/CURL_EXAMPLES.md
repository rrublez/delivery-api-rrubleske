# CURL Examples - Cenários de Teste Obrigatórios

## 🎯 Cenários de Teste Obrigatórios

### Cenário 1: Listar Restaurantes com Filtros
**Endpoint:** `GET /api/restaurantes?ramo=Italiana&ativo=true`  
**Descrição:** Listar restaurantes com filtros de categoria (ramo de atividade) e status ativo  
**Resultado Esperado:** Lista paginada com metadados

#### CURL
```bash
# Listar restaurantes Italianos ativos
curl -X GET "http://localhost:8080/api/restaurantes?ramo=Italiana&ativo=true" \
  -H "Accept: application/json"

# Resposta esperada (Status 200 OK)
# [
#   {
#     "id": 1,
#     "nome": "Restaurante Italiano",
#     "endereco": "Rua Roma, 100",
#     "ramoAtividade": "Italiana",
#     "ativo": true,
#     "taxaEntrega": 6.00,
#     "telefone": "1133333334"
#   }
# ]
```

---

### Cenário 2: Buscar Produtos de um Restaurante
**Endpoint:** `GET /api/produtos/restaurante/{restauranteId}?disponivel=true`  
**Descrição:** Listar todos os produtos disponíveis de um restaurante específico  
**Resultado Esperado:** Lista de produtos disponíveis

#### CURL
```bash
# Pré-requisito: Criar um restaurante
RESTAURANTE_ID=$(curl -X POST "http://localhost:8080/api/restaurantes" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pizzaria Premium",
    "endereco": "Avenida Paulista, 1000",
    "telefone": "1133334444",
    "cnpj": "99999999000190",
    "ramoAtividade": "Pizzaria",
    "ativo": true,
    "taxaEntrega": 5.50
  }' | jq -r '.id')

# Buscar produtos do restaurante (disponíveis)
curl -X GET "http://localhost:8080/api/produtos/restaurante/$RESTAURANTE_ID" \
  -H "Accept: application/json"

# Resposta esperada (Status 200 OK)
# [
#   {
#     "id": 1,
#     "nome": "Pizza Margherita",
#     "descricao": "Pizza tradicional com molho de tomate",
#     "preco": 35.90,
#     "categoria": "Pizza",
#     "disponivel": true
#   },
#   {
#     "id": 2,
#     "nome": "Pizza Calabresa",
#     "descricao": "Pizza com calabresa e cebola",
#     "preco": 38.90,
#     "categoria": "Pizza",
#     "disponivel": true
#   }
# ]
```

---

### Cenário 3: Criar Pedido Completo
**Endpoint:** `POST /api/pedidos`  
**Descrição:** Criar pedido com múltiplos itens (produtos)  
**Resultado Esperado:** Pedido criado com status 201

#### CURL
```bash
# Pré-requisitos: Cliente, Restaurante e Produtos criados
# Obter IDs dos recursos
CLIENTE_ID=1
RESTAURANTE_ID=1
PRODUTO_ID_1=1
PRODUTO_ID_2=2

# Criar pedido completo com itens
curl -X POST "http://localhost:8080/api/pedidos" \
  -H "Content-Type: application/json" \
  -d '{
    "numeroPedido": "PED-2025-001",
    "clienteId": '$CLIENTE_ID',
    "restauranteId": '$RESTAURANTE_ID',
    "status": "PENDENTE",
    "itens": [
      {
        "produtoId": '$PRODUTO_ID_1',
        "quantidade": 2
      },
      {
        "produtoId": '$PRODUTO_ID_2',
        "quantidade": 1
      }
    ]
  }'

# Resposta esperada (Status 201 Created)
# {
#   "id": 1,
#   "numeroPedido": "PED-2025-001",
#   "clienteId": 1,
#   "restauranteId": 1,
#   "status": "PENDENTE",
#   "dataCreacao": "2025-01-15T10:30:00",
#   "itens": [
#     {
#       "produtoId": 1,
#       "quantidade": 2,
#       "preco": 35.90,
#       "subtotal": 71.80
#     },
#     {
#       "produtoId": 2,
#       "quantidade": 1,
#       "preco": 38.90,
#       "subtotal": 38.90
#     }
#   ],
#   "subtotal": 110.70,
#   "taxaEntrega": 5.50,
#   "total": 116.20
# }
```

---

### Cenário 4: Relatório de Vendas por Período
**Endpoint:** `GET /api/pedidos/relatorio/vendas-por-restaurante?dataInicio=2024-01-01&dataFim=2024-01-31`  
**Descrição:** Obter relatório de vendas dos restaurantes em um período específico  
**Resultado Esperado:** Relatório com vendas agregadas do período

#### CURL
```bash
# Relatório de vendas por período
curl -X GET "http://localhost:8080/api/pedidos/relatorio/vendas-por-restaurante?dataInicio=2024-01-01&dataFim=2024-01-31" \
  -H "Accept: application/json"

# Resposta esperada (Status 200 OK)
# {
#   "periodo": {
#     "dataInicio": "2024-01-01",
#     "dataFim": "2024-01-31"
#   },
#   "restaurantes": [
#     {
#       "restauranteId": 1,
#       "restauranteNome": "Pizzaria Central",
#       "quantidadePedidos": 15,
#       "valorTotal": 1500.00,
#       "valorMedio": 100.00,
#       "taxasEntrega": 82.50
#     },
#     {
#       "restauranteId": 2,
#       "restauranteNome": "Restaurante Italiano",
#       "quantidadePedidos": 22,
#       "valorTotal": 2480.00,
#       "valorMedio": 112.73,
#       "taxasEntrega": 132.00
#     }
#   ],
#   "resumo": {
#     "quantidadeTotalPedidos": 37,
#     "valorTotalVendas": 3980.00,
#     "taxasTotalEntrega": 214.50,
#     "valorMedioTicket": 107.57
#   }
# }
```

---

### Cenário 5: Validação Swagger UI
**Endpoint:** `GET http://localhost:8080/swagger-ui/index.html`  
**Descrição:** Interface Swagger para testar endpoints diretamente  
**Resultado Esperado:** Interface funcional com todos os endpoints documentados

#### Instruções:
```bash
# 1. Inicie a aplicação
./mvnw spring-boot:run

# 2. Acesse o Swagger UI em seu navegador
#    URL: http://localhost:8080/swagger-ui/index.html
#
# 3. Verifique se todos os Controllers aparecem:
#    - ClienteController (5 endpoints)
#    - ProdutoController (9 endpoints)
#    - RestauranteController (9 endpoints)
#    - PedidoController (8 endpoints)
#    - RelatorioController (5 endpoints)
#
# 4. Teste a funcionalidade "Try it out" em um endpoint qualquer
#    Exemplo: POST /api/restaurantes
#
#    Request Body:
#    {
#      "nome": "Teste Swagger",
#      "endereco": "Rua Teste, 123",
#      "telefone": "1199999999",
#      "cnpj": "88888888000190",
#      "ramoAtividade": "Testaria",
#      "ativo": true,
#      "taxaEntrega": 4.50
#    }
#
# 5. Clique "Execute"
# 6. Verifique:
#    - Status Code: 201 Created
#    - Response body com ID gerado
#    - Headers corretos (Content-Type: application/json)

# Alternativa via CURL - Validar Swagger JSON
curl -X GET "http://localhost:8080/v3/api-docs" \
  -H "Accept: application/json" | jq '.paths | keys'

# Resposta esperada: lista de todos os endpoints em formato OpenAPI 3.0
```

---

## 📊 Resumo dos Cenários

| # | Cenário | Método | Endpoint | Status | Descrição |
|---|---------|--------|----------|--------|-----------|
| 1 | Filtros | GET | `/api/restaurantes?ramo=X&ativo=true` | 200 | Lista paginada com metadados |
| 2 | Produtos | GET | `/api/produtos/restaurante/{id}` | 200 | Produtos disponíveis |
| 3 | Criar | POST | `/api/pedidos` | 201 | Pedido com múltiplos itens |
| 4 | Relatório | GET | `/api/pedidos/relatorio/vendas-por-restaurante` | 200 | Vendas por período |
| 5 | Swagger | GET | `/swagger-ui/index.html` | 200 | Interface funcional |

---

## 🔧 Scripts de Teste Completo

### Script 1: Criar Dados de Teste
```bash
#!/bin/bash

# Cores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}=== Criando dados de teste ===${NC}"

# 1. Criar Cliente
echo -e "${BLUE}1. Criando cliente...${NC}"
CLIENTE_RESPONSE=$(curl -s -X POST "http://localhost:8080/api/clientes" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@test.com",
    "cpf": "12345678901",
    "telefone": "11999999999",
    "ativo": true
  }')
CLIENTE_ID=$(echo $CLIENTE_RESPONSE | jq -r '.id')
echo -e "${GREEN}✓ Cliente criado: ID $CLIENTE_ID${NC}"

# 2. Criar Restaurante Pizzaria
echo -e "${BLUE}2. Criando restaurante (Pizzaria)...${NC}"
RESTAURANTE_RESPONSE=$(curl -s -X POST "http://localhost:8080/api/restaurantes" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pizzaria Central",
    "endereco": "Rua Principal, 100",
    "telefone": "1133333333",
    "cnpj": "12345678000190",
    "ramoAtividade": "Pizzaria",
    "ativo": true,
    "taxaEntrega": 5.00
  }')
RESTAURANTE_ID=$(echo $RESTAURANTE_RESPONSE | jq -r '.id')
echo -e "${GREEN}✓ Restaurante criado: ID $RESTAURANTE_ID${NC}"

# 3. Criar Restaurante Italiano
echo -e "${BLUE}3. Criando restaurante (Italiana)...${NC}"
RESTAURANTE_IT_RESPONSE=$(curl -s -X POST "http://localhost:8080/api/restaurantes" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Restaurante Italiano",
    "endereco": "Rua Roma, 100",
    "telefone": "1133333334",
    "cnpj": "11111111000191",
    "ramoAtividade": "Italiana",
    "ativo": true,
    "taxaEntrega": 6.00
  }')
RESTAURANTE_IT_ID=$(echo $RESTAURANTE_IT_RESPONSE | jq -r '.id')
echo -e "${GREEN}✓ Restaurante Italiano criado: ID $RESTAURANTE_IT_ID${NC}"

# 4. Criar Produtos
echo -e "${BLUE}4. Criando produtos...${NC}"
PRODUTO1=$(curl -s -X POST "http://localhost:8080/api/produtos" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pizza Margherita",
    "descricao": "Pizza tradicional com molho de tomate",
    "preco": 35.90,
    "categoria": "Pizza",
    "disponivel": true
  }' | jq -r '.id')
echo -e "${GREEN}✓ Produto 1 criado: ID $PRODUTO1${NC}"

PRODUTO2=$(curl -s -X POST "http://localhost:8080/api/produtos" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pizza Calabresa",
    "descricao": "Pizza com calabresa e cebola",
    "preco": 38.90,
    "categoria": "Pizza",
    "disponivel": true
  }' | jq -r '.id')
echo -e "${GREEN}✓ Produto 2 criado: ID $PRODUTO2${NC}"

# 5. Criar Pedido
echo -e "${BLUE}5. Criando pedido...${NC}"
PEDIDO=$(curl -s -X POST "http://localhost:8080/api/pedidos" \
  -H "Content-Type: application/json" \
  -d '{
    "numeroPedido": "PED-2025-001",
    "clienteId": '$CLIENTE_ID',
    "restauranteId": '$RESTAURANTE_ID',
    "status": "PENDENTE",
    "itens": [
      {
        "produtoId": '$PRODUTO1',
        "quantidade": 2
      },
      {
        "produtoId": '$PRODUTO2',
        "quantidade": 1
      }
    ]
  }' | jq -r '.id')
echo -e "${GREEN}✓ Pedido criado: ID $PEDIDO${NC}"

echo -e "${GREEN}✅ Dados de teste criados com sucesso!${NC}"
echo ""
echo "IDs para referência:"
echo "  Cliente: $CLIENTE_ID"
echo "  Restaurante Pizzaria: $RESTAURANTE_ID"
echo "  Restaurante Italiana: $RESTAURANTE_IT_ID"
echo "  Produto 1: $PRODUTO1"
echo "  Produto 2: $PRODUTO2"
echo "  Pedido: $PEDIDO"
```

### Script 2: Executar Cenários de Teste
```bash
#!/bin/bash

echo "=== CENÁRIO 1: Listar Restaurantes com Filtros ==="
curl -i -X GET "http://localhost:8080/api/restaurantes?ramo=Italiana&ativo=true"
echo -e "\n"

echo "=== CENÁRIO 2: Buscar Produtos de um Restaurante ==="
curl -i -X GET "http://localhost:8080/api/produtos/restaurante/1"
echo -e "\n"

echo "=== CENÁRIO 3: Criar Pedido Completo ==="
curl -i -X POST "http://localhost:8080/api/pedidos" \
  -H "Content-Type: application/json" \
  -d '{
    "numeroPedido": "PED-2025-TEST",
    "clienteId": 1,
    "restauranteId": 1,
    "status": "PENDENTE",
    "itens": [
      {"produtoId": 1, "quantidade": 2}
    ]
  }'
echo -e "\n"

echo "=== CENÁRIO 4: Relatório de Vendas ==="
curl -i -X GET "http://localhost:8080/api/pedidos/relatorio/vendas-por-restaurante?dataInicio=2024-01-01&dataFim=2024-12-31"
echo -e "\n"

echo "=== CENÁRIO 5: Validação Swagger ==="
echo "Acesse: http://localhost:8080/swagger-ui/index.html"
```

---

## ✅ Checklist de Validação

- [ ] **Cenário 1**: GET restaurantes com filtros retorna status 200
- [ ] **Cenário 1**: Resposta contém apenas restaurantes da categoria filtrada
- [ ] **Cenário 1**: Resposta contém apenas restaurantes ativos
- [ ] **Cenário 2**: GET produtos por restaurante retorna status 200
- [ ] **Cenário 2**: Resposta contém lista de produtos disponíveis
- [ ] **Cenário 3**: POST pedido retorna status 201
- [ ] **Cenário 3**: Resposta contém ID gerado automaticamente
- [ ] **Cenário 3**: Resposta contém todos os itens do pedido
- [ ] **Cenário 3**: Cálculo de total está correto
- [ ] **Cenário 4**: GET relatório retorna status 200
- [ ] **Cenário 4**: Resposta contém dados agregados por restaurante
- [ ] **Cenário 4**: Resposta contém resumo geral (totais)
- [ ] **Cenário 5**: Swagger UI carrega sem erros (status 200)
- [ ] **Cenário 5**: Todos os 5 controllers aparecem na listagem
- [ ] **Cenário 5**: Funcionalidade "Try it out" funciona corretamente

---

## 📝 Headers Esperados

Todos os endpoints devem retornar:
```
Content-Type: application/json; charset=UTF-8
Access-Control-Allow-Origin: *
X-Content-Type-Options: nosniff
```

---

## 🐛 Troubleshooting

| Problema | Solução |
|----------|---------|
| Conexão recusada | Verifique se a aplicação está rodando na porta 8080 |
| CNPJ duplicado | Use CNPJ único em cada teste (adicione timestamp) |
| Email duplicado | Use email único ou limpe dados com DELETE |
| Produto não encontrado | Verifique se os IDs estão corretos |
| Swagger não carrega | Verifique se SpringDoc OpenAPI está no pom.xml |

---

**Status: ✅ Pronto para Produção**
