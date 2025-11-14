# 📋 Documentação dos Endpoints - PedidoController

> **Versão:** 1.0  
> **Data:** Novembro 2025  
> **Status:** ✅ Implementado e Testado

---

## 📊 Resumo dos Endpoints

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| `POST` | `/api/pedidos` | Criar pedido | ✅ |
| `GET` | `/api/pedidos/{id}` | Obter pedido completo | ✅ |
| `GET` | `/api/pedidos` | Listar com filtros | ✅ |
| `PATCH` | `/api/pedidos/{id}/status` | Atualizar status | ✅ |
| `DELETE` | `/api/pedidos/{id}` | Cancelar pedido | ✅ |
| `GET` | `/api/clientes/{clienteId}/pedidos` | Histórico do cliente | ✅ |
| `GET` | `/api/restaurantes/{restauranteId}/pedidos` | Pedidos do restaurante | ✅ |
| `POST` | `/api/pedidos/calcular` | Calcular total | ✅ |

---

## 🔍 Detalhamento dos Endpoints

### 1️⃣ POST /api/pedidos - Criar Pedido

**Descrição:** Cria um novo pedido com validações

**URL:** `POST http://localhost:8080/api/pedidos`

**Content-Type:** `application/json`

**Request Body:**

```json
{
  "numeroPedido": "PED-2025-001",
  "status": "PENDENTE",
  "clienteId": 1,
  "restauranteId": 1,
  "itens": [
    {
      "produtoId": 1,
      "quantidade": 2,
      "precoUnitario": 45.00,
      "observacoes": "Sem cebola"
    },
    {
      "produtoId": 2,
      "quantidade": 1,
      "precoUnitario": 5.00,
      "observacoes": "Gelada"
    }
  ]
}
```

**Validações:**
- Número do pedido: 5-20 caracteres, apenas maiúsculas, números e hífen
- Status: PENDENTE, CONFIRMADO, PREPARANDO, SAIU_ENTREGA, ENTREGUE ou CANCELADO
- Cliente ID: Deve existir no sistema
- Restaurante ID: Deve existir no sistema
- Itens: Mínimo 1 item, produtos devem existir
- Preço unitário: > 0.00
- Quantidade: > 0

**cURL:**

```bash
curl -X POST "http://localhost:8080/api/pedidos" \
  -H "Content-Type: application/json" \
  -d '{
    "numeroPedido": "PED-2025-001",
    "status": "PENDENTE",
    "clienteId": 1,
    "restauranteId": 1,
    "itens": [
      {
        "produtoId": 1,
        "quantidade": 2,
        "precoUnitario": 45.00,
        "observacoes": "Sem cebola"
      },
      {
        "produtoId": 2,
        "quantidade": 1,
        "precoUnitario": 5.00,
        "observacoes": "Gelada"
      }
    ]
  }'
```

**Response (201 Created):**

```json
{
  "id": 1,
  "numeroPedido": "PED-2025-001",
  "status": "PENDENTE",
  "cliente": {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@example.com",
    "telefone": "11999999999",
    "cpf": "12345678900",
    "ativo": true
  },
  "restaurante": {
    "id": 1,
    "nome": "Pizzaria do João",
    "endereco": "Rua A, 123",
    "telefone": "1133333333",
    "cnpj": "12345678000190",
    "ramoAtividade": "Alimentação",
    "ativo": true,
    "taxaEntrega": 5.00
  },
  "valorTotal": 100.00,
  "dataPedido": "2025-11-10T14:30:00",
  "itens": [
    {
      "id": 1,
      "produto": {
        "id": 1,
        "nome": "Pizza Margherita",
        "descricao": "Pizza clássica",
        "preco": 45.00,
        "disponivel": true,
        "categoria": "Pizzas"
      },
      "quantidade": 2,
      "precoUnitario": 45.00,
      "subtotal": 90.00,
      "observacoes": "Sem cebola"
    },
    {
      "id": 2,
      "produto": {
        "id": 2,
        "nome": "Refrigerante",
        "descricao": "Coca Cola 2L",
        "preco": 5.00,
        "disponivel": true,
        "categoria": "Bebidas"
      },
      "quantidade": 1,
      "precoUnitario": 5.00,
      "subtotal": 5.00,
      "observacoes": "Gelada"
    }
  ]
}
```

**Possíveis Erros:**
- `400 Bad Request` - Validação falhou
- `404 Not Found` - Cliente ou restaurante não encontrado

---

### 2️⃣ GET /api/pedidos/{id} - Obter Pedido Completo

**Descrição:** Busca um pedido específico com todos seus detalhes

**URL:** `GET http://localhost:8080/api/pedidos/1`

**Path Parameters:**
- `id` (obrigatório): Long - ID do pedido

**cURL:**

```bash
curl -X GET "http://localhost:8080/api/pedidos/1"
```

**Response (200 OK):**

```json
{
  "id": 1,
  "numeroPedido": "PED-2025-001",
  "status": "PENDENTE",
  "cliente": {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@example.com",
    "telefone": "11999999999",
    "cpf": "12345678900",
    "ativo": true
  },
  "restaurante": {
    "id": 1,
    "nome": "Pizzaria do João",
    "endereco": "Rua A, 123",
    "telefone": "1133333333",
    "cnpj": "12345678000190",
    "ramoAtividade": "Alimentação",
    "ativo": true,
    "taxaEntrega": 5.00
  },
  "valorTotal": 100.00,
  "dataPedido": "2025-11-10T14:30:00",
  "itens": [
    {
      "id": 1,
      "produto": {
        "id": 1,
        "nome": "Pizza Margherita",
        "descricao": "Pizza clássica",
        "preco": 45.00,
        "disponivel": true,
        "categoria": "Pizzas"
      },
      "quantidade": 2,
      "precoUnitario": 45.00,
      "subtotal": 90.00,
      "observacoes": "Sem cebola"
    }
  ]
}
```

**Possíveis Erros:**
- `404 Not Found` - Pedido não existe

---

### 3️⃣ GET /api/pedidos - Listar com Filtros

**Descrição:** Lista pedidos com filtros por status e período

**URL:** `GET http://localhost:8080/api/pedidos`

**Query Parameters:**
- `status` (opcional): String - Status do pedido (PENDENTE, CONFIRMADO, PREPARANDO, SAIU_ENTREGA, ENTREGUE, CANCELADO)
- `dataInicial` (opcional): LocalDateTime - Data inicial (formato: 2025-11-10T00:00:00)
- `dataFinal` (opcional): LocalDateTime - Data final (formato: 2025-11-10T23:59:59)

**cURL:**

```bash
# Sem filtros
curl -X GET "http://localhost:8080/api/pedidos"

# Por status
curl -X GET "http://localhost:8080/api/pedidos?status=PENDENTE"

# Por período
curl -X GET "http://localhost:8080/api/pedidos?dataInicial=2025-11-01T00:00:00&dataFinal=2025-11-30T23:59:59"

# Status e período
curl -X GET "http://localhost:8080/api/pedidos?status=ENTREGUE&dataInicial=2025-11-01T00:00:00&dataFinal=2025-11-30T23:59:59"
```

**Response (200 OK):**

```json
[
  {
    "id": 1,
    "numeroPedido": "PED-2025-001",
    "status": "PENDENTE",
    "cliente": {
      "id": 1,
      "nome": "João Silva",
      "email": "joao@example.com",
      "telefone": "11999999999",
      "cpf": "12345678900",
      "ativo": true
    },
    "restaurante": {
      "id": 1,
      "nome": "Pizzaria do João",
      "endereco": "Rua A, 123",
      "telefone": "1133333333",
      "cnpj": "12345678000190",
      "ramoAtividade": "Alimentação",
      "ativo": true,
      "taxaEntrega": 5.00
    },
    "valorTotal": 100.00,
    "dataPedido": "2025-11-10T14:30:00",
    "itens": []
  }
]
```

---

### 4️⃣ PATCH /api/pedidos/{id}/status - Atualizar Status

**Descrição:** Atualiza o status de um pedido

**URL:** `PATCH http://localhost:8080/api/pedidos/1/status`

**Path Parameters:**
- `id` (obrigatório): Long - ID do pedido

**Request Body:**

```json
{
  "status": "CONFIRMADO"
}
```

**Status permitidos:**
- PENDENTE
- CONFIRMADO
- PREPARANDO
- SAIU_ENTREGA
- ENTREGUE
- CANCELADO

**cURL:**

```bash
# Confirmar pedido
curl -X PATCH "http://localhost:8080/api/pedidos/1/status" \
  -H "Content-Type: application/json" \
  -d '{"status": "CONFIRMADO"}'

# Marcar como saiu para entrega
curl -X PATCH "http://localhost:8080/api/pedidos/1/status" \
  -H "Content-Type: application/json" \
  -d '{"status": "SAIU_ENTREGA"}'

# Marcar como entregue
curl -X PATCH "http://localhost:8080/api/pedidos/1/status" \
  -H "Content-Type: application/json" \
  -d '{"status": "ENTREGUE"}'
```

**Response (200 OK):** Retorna pedido com status atualizado (igual ao GET /{id})

**Possíveis Erros:**
- `404 Not Found` - Pedido não existe
- `400 Bad Request` - Status inválido

---

### 5️⃣ DELETE /api/pedidos/{id} - Cancelar Pedido

**Descrição:** Cancela um pedido (muda status para CANCELADO)

**URL:** `DELETE http://localhost:8080/api/pedidos/1`

**Path Parameters:**
- `id` (obrigatório): Long - ID do pedido

**cURL:**

```bash
curl -X DELETE "http://localhost:8080/api/pedidos/1"
```

**Response (204 No Content):** Sem body

**Possíveis Erros:**
- `404 Not Found` - Pedido não existe

---

### 6️⃣ GET /api/clientes/{clienteId}/pedidos - Histórico do Cliente

**Descrição:** Lista todos os pedidos de um cliente específico

**URL:** `GET http://localhost:8080/api/clientes/1/pedidos`

**Path Parameters:**
- `clienteId` (obrigatório): Long - ID do cliente

**cURL:**

```bash
curl -X GET "http://localhost:8080/api/clientes/1/pedidos"
curl -X GET "http://localhost:8080/api/clientes/2/pedidos"
```

**Response (200 OK):**

```json
[
  {
    "id": 1,
    "numeroPedido": "PED-2025-001",
    "status": "ENTREGUE",
    "cliente": {
      "id": 1,
      "nome": "João Silva",
      "email": "joao@example.com",
      "telefone": "11999999999",
      "cpf": "12345678900",
      "ativo": true
    },
    "restaurante": {
      "id": 1,
      "nome": "Pizzaria do João",
      "endereco": "Rua A, 123",
      "telefone": "1133333333",
      "cnpj": "12345678000190",
      "ramoAtividade": "Alimentação",
      "ativo": true,
      "taxaEntrega": 5.00
    },
    "valorTotal": 100.00,
    "dataPedido": "2025-11-09T14:30:00",
    "itens": []
  },
  {
    "id": 2,
    "numeroPedido": "PED-2025-002",
    "status": "PENDENTE",
    "cliente": {
      "id": 1,
      "nome": "João Silva",
      "email": "joao@example.com",
      "telefone": "11999999999",
      "cpf": "12345678900",
      "ativo": true
    },
    "restaurante": {
      "id": 2,
      "nome": "Hamburgaria XYZ",
      "endereco": "Rua B, 456",
      "telefone": "1144444444",
      "cnpj": "98765432000180",
      "ramoAtividade": "Alimentação",
      "ativo": true,
      "taxaEntrega": 3.00
    },
    "valorTotal": 55.00,
    "dataPedido": "2025-11-10T18:00:00",
    "itens": []
  }
]
```

---

### 7️⃣ GET /api/restaurantes/{restauranteId}/pedidos - Pedidos do Restaurante

**Descrição:** Lista todos os pedidos recebidos por um restaurante

**URL:** `GET http://localhost:8080/api/restaurantes/1/pedidos`

**Path Parameters:**
- `restauranteId` (obrigatório): Long - ID do restaurante

**cURL:**

```bash
curl -X GET "http://localhost:8080/api/restaurantes/1/pedidos"
curl -X GET "http://localhost:8080/api/restaurantes/2/pedidos"
```

**Response (200 OK):** Lista de pedidos do restaurante

```json
[
  {
    "id": 1,
    "numeroPedido": "PED-2025-001",
    "status": "PREPARANDO",
    "cliente": {
      "id": 1,
      "nome": "João Silva",
      "email": "joao@example.com",
      "telefone": "11999999999",
      "cpf": "12345678900",
      "ativo": true
    },
    "restaurante": {
      "id": 1,
      "nome": "Pizzaria do João",
      "endereco": "Rua A, 123",
      "telefone": "1133333333",
      "cnpj": "12345678000190",
      "ramoAtividade": "Alimentação",
      "ativo": true,
      "taxaEntrega": 5.00
    },
    "valorTotal": 100.00,
    "dataPedido": "2025-11-10T14:30:00",
    "itens": []
  }
]
```

---

### 8️⃣ POST /api/pedidos/calcular - Calcular Total

**Descrição:** Calcula o total de um pedido sem salvá-lo no banco

**URL:** `POST http://localhost:8080/api/pedidos/calcular`

**Content-Type:** `application/json`

**Request Body:**

```json
{
  "restauranteId": 1,
  "itens": [
    {
      "produtoId": 1,
      "quantidade": 2,
      "precoUnitario": 45.00,
      "observacoes": "Sem cebola"
    },
    {
      "produtoId": 2,
      "quantidade": 1,
      "precoUnitario": 5.00,
      "observacoes": "Gelada"
    }
  ]
}
```

**cURL:**

```bash
curl -X POST "http://localhost:8080/api/pedidos/calcular" \
  -H "Content-Type: application/json" \
  -d '{
    "restauranteId": 1,
    "itens": [
      {
        "produtoId": 1,
        "quantidade": 2,
        "precoUnitario": 45.00,
        "observacoes": "Sem cebola"
      },
      {
        "produtoId": 2,
        "quantidade": 1,
        "precoUnitario": 5.00,
        "observacoes": "Gelada"
      }
    ]
  }'
```

**Response (200 OK):**

```json
{
  "itens": [
    {
      "id": null,
      "produto": {
        "id": 1,
        "nome": "Pizza Margherita",
        "descricao": "Pizza clássica",
        "preco": 45.00,
        "disponivel": true,
        "categoria": "Pizzas"
      },
      "quantidade": 2,
      "precoUnitario": 45.00,
      "subtotal": 90.00,
      "observacoes": "Sem cebola"
    },
    {
      "id": null,
      "produto": {
        "id": 2,
        "nome": "Refrigerante",
        "descricao": "Coca Cola 2L",
        "preco": 5.00,
        "disponivel": true,
        "categoria": "Bebidas"
      },
      "quantidade": 1,
      "precoUnitario": 5.00,
      "subtotal": 5.00,
      "observacoes": "Gelada"
    }
  ],
  "subtotal": 95.00,
  "taxaEntrega": 5.00,
  "valorTotal": 100.00
}
```

**Possíveis Erros:**
- `400 Bad Request` - Validação falhou
- `404 Not Found` - Restaurante ou produto não encontrado

---

## 📝 DTOs Utilizados

### Request DTOs

#### PedidoRequest
```json
{
  "numeroPedido": "string (5-20 caracteres)",
  "status": "string (PENDENTE|CONFIRMADO|PREPARANDO|SAIU_ENTREGA|ENTREGUE|CANCELADO)",
  "clienteId": "long (obrigatório)",
  "restauranteId": "long (obrigatório)",
  "itens": [
    {
      "produtoId": "long",
      "quantidade": "int (> 0)",
      "precoUnitario": "decimal (> 0.00)",
      "observacoes": "string (até 255 caracteres)"
    }
  ]
}
```

#### AtualizarStatusPedidoRequest
```json
{
  "status": "string (PENDENTE|CONFIRMADO|PREPARANDO|SAIU_ENTREGA|ENTREGUE|CANCELADO)"
}
```

#### CalcularPedidoRequest
```json
{
  "restauranteId": "long (obrigatório)",
  "itens": [
    {
      "produtoId": "long",
      "quantidade": "int (> 0)",
      "precoUnitario": "decimal (> 0.00)",
      "observacoes": "string (até 255 caracteres)"
    }
  ]
}
```

### Response DTOs

#### PedidoResponse
```json
{
  "id": "long",
  "numeroPedido": "string",
  "status": "string",
  "cliente": {
    "id": "long",
    "nome": "string",
    "email": "string",
    "telefone": "string",
    "cpf": "string",
    "ativo": "boolean"
  },
  "restaurante": {
    "id": "long",
    "nome": "string",
    "endereco": "string",
    "telefone": "string",
    "cnpj": "string",
    "ramoAtividade": "string",
    "ativo": "boolean",
    "taxaEntrega": "decimal"
  },
  "valorTotal": "decimal",
  "dataPedido": "datetime",
  "itens": [
    {
      "id": "long",
      "produto": {...},
      "quantidade": "int",
      "precoUnitario": "decimal",
      "subtotal": "decimal",
      "observacoes": "string"
    }
  ]
}
```

#### CalcularPedidoResponse
```json
{
  "itens": [],
  "subtotal": "decimal",
  "taxaEntrega": "decimal",
  "valorTotal": "decimal"
}
```

---

## 🧪 Exemplos de Testes (cURL)

### Script de Teste Completo

```bash
#!/bin/bash

echo "1. Criando novo pedido..."
PEDIDO=$(curl -s -X POST "http://localhost:8080/api/pedidos" \
  -H "Content-Type: application/json" \
  -d '{
    "numeroPedido": "PED-2025-001",
    "status": "PENDENTE",
    "clienteId": 1,
    "restauranteId": 1,
    "itens": [
      {
        "produtoId": 1,
        "quantidade": 2,
        "precoUnitario": 45.00,
        "observacoes": "Sem cebola"
      }
    ]
  }')
echo "$PEDIDO" | jq .

# Extrair ID do pedido (supondo que jq está disponível)
ID=$(echo "$PEDIDO" | jq -r '.id')

echo -e "\n2. Obtendo pedido por ID..."
curl -s -X GET "http://localhost:8080/api/pedidos/$ID" | jq .

echo -e "\n3. Listando pedidos com filtro por status..."
curl -s -X GET "http://localhost:8080/api/pedidos?status=PENDENTE" | jq .

echo -e "\n4. Atualizando status do pedido..."
curl -s -X PATCH "http://localhost:8080/api/pedidos/$ID/status" \
  -H "Content-Type: application/json" \
  -d '{"status": "CONFIRMADO"}' | jq .

echo -e "\n5. Pedidos do cliente..."
curl -s -X GET "http://localhost:8080/api/clientes/1/pedidos" | jq .

echo -e "\n6. Pedidos do restaurante..."
curl -s -X GET "http://localhost:8080/api/restaurantes/1/pedidos" | jq .

echo -e "\n7. Calculando total (sem salvar)..."
curl -s -X POST "http://localhost:8080/api/pedidos/calcular" \
  -H "Content-Type: application/json" \
  -d '{
    "restauranteId": 1,
    "itens": [
      {
        "produtoId": 1,
        "quantidade": 1,
        "precoUnitario": 45.00
      }
    ]
  }' | jq .

echo -e "\n8. Cancelando pedido..."
curl -s -X DELETE "http://localhost:8080/api/pedidos/$ID"
echo "Pedido cancelado!"
```

---

## ✔️ Validações e Tratamento de Erros

### Validações implementadas:

| Campo | Validação |
|-------|-----------|
| numeroPedido | 5-20 caracteres, padrão: ^[A-Z0-9-]+$ |
| status | PENDENTE, CONFIRMADO, PREPARANDO, SAIU_ENTREGA, ENTREGUE, CANCELADO |
| clienteId | Must exist in database |
| restauranteId | Must exist in database |
| itens | Minimum 1 item |
| produtoId | Must exist in database |
| quantidade | Integer > 0 |
| precoUnitario | Decimal > 0.00 |
| observacoes | até 255 caracteres |

### Códigos HTTP de Resposta:

| Código | Situação |
|--------|----------|
| 200 | GET/PATCH sucesso |
| 201 | POST sucesso (criado) |
| 204 | DELETE sucesso (sem conteúdo) |
| 400 | Bad Request - Validação falhou |
| 404 | Not Found - Recurso não existe |
| 500 | Erro interno do servidor |

---

## 🔗 Relacionamentos

**Pedido → Cliente (ManyToOne)**
- Um pedido pertence a um cliente
- Um cliente pode ter vários pedidos

**Pedido → Restaurante (ManyToOne)**
- Um pedido é de um restaurante
- Um restaurante pode ter vários pedidos

**Pedido → PedidoProduto (OneToMany)**
- Um pedido possui vários itens (PedidoProduto)
- Cada item pertence a um pedido

**PedidoProduto → Produto (ManyToOne)**
- Cada item do pedido referencia um produto
- Um produto pode estar em vários pedidos

---

## 🔐 Segurança

- ✅ Validação de entrada em todos os endpoints
- ✅ Sanitização de parâmetros
- ✅ Transações ACID garantidas
- ✅ Tratamento centralizado de exceções
- ✅ Verificação de existência de recursos relacionados

---

## 📈 Fluxo de Estados do Pedido

```
PENDENTE → CONFIRMADO → PREPARANDO → SAIU_ENTREGA → ENTREGUE
           ↓
        CANCELADO (em qualquer momento)
```

---

**Status:** ✅ Pronto para testes  
**Última atualização:** Novembro 2025  
**Desenvolvedor:** Rafael Rubleske
