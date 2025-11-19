# 🍕 Documentação dos Endpoints - ProdutoController

> **Versão:** 1.0  
> **Data:** Novembro 2025  
> **Status:** ✅ Implementado e Testado

---

## 🔐 Autenticação e Segurança

- `POST /api/auth/login` e `POST /api/auth/register` são públicos para gerar credenciais com `role` definidos.
- `GET /api/restaurantes`, `GET /api/produtos` e `GET /actuator/health` continuam liberados para leitura pública.
- Todos os demais endpoints requerem `Authorization: Basic <base64(email:senha)>` e um `role` compatível (por exemplo, `ADMIN` ou `RESTAURANTE`).
- As senhas devem ser persistidas com `BCrypt` e apenas `ativo=true` permite autenticação.

```bash
curl -X GET "http://localhost:8080/api/produtos" \
  -H "Authorization: Basic $(echo -n 'usuario@delivery.com:Senha123!' | base64)"
```

---

## 📊 Resumo dos Endpoints

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| `POST` | `/api/produtos` | Criar produto | ✅ |
| `GET` | `/api/produtos/{id}` | Obter por ID | ✅ |
| `PUT` | `/api/produtos/{id}` | Atualizar completo | ✅ |
| `DELETE` | `/api/produtos/{id}` | Remover produto | ✅ |
| `PATCH` | `/api/produtos/{id}/disponibilidade` | Toggle disponibilidade | ✅ |
| `GET` | `/api/restaurantes/{restauranteId}/produtos` | Produtos do restaurante | ✅ |
| `GET` | `/api/produtos/categoria/{categoria}` | Listar por categoria | ✅ |
| `GET` | `/api/produtos/buscar` | Buscar por nome | ✅ |

---

## 🔍 Detalhamento dos Endpoints

### 1️⃣ POST /api/produtos - Criar Produto

**Descrição:** Cria um novo produto com validações

**URL:** `POST http://localhost:8080/api/produtos`

**Content-Type:** `application/json`

**Request Body:**

```json
{
  "nome": "Pizza Margherita",
  "descricao": "Pizza clássica com tomate, mozzarela e manjericão",
  "preco": 45.00,
  "disponivel": true,
  "categoria": "Pizzas"
}
```

**Validações:**
- Nome: 3-100 caracteres
- Descrição: 5-255 caracteres
- Preço: > 0.00
- Categoria: 3-20 caracteres
- Disponível: Boolean (obrigatório)

**cURL:**

```bash
curl -X POST "http://localhost:8080/api/produtos" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pizza Margherita",
    "descricao": "Pizza clássica com tomate, mozzarela e manjericão",
    "preco": 45.00,
    "disponivel": true,
    "categoria": "Pizzas"
  }'
```

**Response (201 Created):**

```json
{
  "id": 1,
  "nome": "Pizza Margherita",
  "descricao": "Pizza clássica com tomate, mozzarela e manjericão",
  "preco": 45.00,
  "disponivel": true,
  "categoria": "Pizzas"
}
```

**Possíveis Erros:**
- `400 Bad Request` - Validação falhou

---

### 2️⃣ GET /api/produtos/{id} - Obter por ID

**Descrição:** Busca um produto específico pelo ID

**URL:** `GET http://localhost:8080/api/produtos/1`

**Path Parameters:**
- `id` (obrigatório): Long - ID do produto

**cURL:**

```bash
curl -X GET "http://localhost:8080/api/produtos/1"
```

**Response (200 OK):**

```json
{
  "id": 1,
  "nome": "Pizza Margherita",
  "descricao": "Pizza clássica com tomate, mozzarela e manjericão",
  "preco": 45.00,
  "disponivel": true,
  "categoria": "Pizzas"
}
```

**Possíveis Erros:**
- `404 Not Found` - Produto não existe

---

### 3️⃣ PUT /api/produtos/{id} - Atualizar Completo

**Descrição:** Atualiza todos os dados de um produto (requer todos os campos)

**URL:** `PUT http://localhost:8080/api/produtos/1`

**Path Parameters:**
- `id` (obrigatório): Long - ID do produto

**Request Body:**

```json
{
  "nome": "Pizza Margherita Premium",
  "descricao": "Pizza italiana premium com ingredientes importados",
  "preco": 55.00,
  "disponivel": true,
  "categoria": "Pizzas Premium"
}
```

**cURL:**

```bash
curl -X PUT "http://localhost:8080/api/produtos/1" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pizza Margherita Premium",
    "descricao": "Pizza italiana premium com ingredientes importados",
    "preco": 55.00,
    "disponivel": true,
    "categoria": "Pizzas Premium"
  }'
```

**Response (200 OK):**

```json
{
  "id": 1,
  "nome": "Pizza Margherita Premium",
  "descricao": "Pizza italiana premium com ingredientes importados",
  "preco": 55.00,
  "disponivel": true,
  "categoria": "Pizzas Premium"
}
```

**Possíveis Erros:**
- `404 Not Found` - Produto não existe
- `400 Bad Request` - Validação falhou

---

### 4️⃣ DELETE /api/produtos/{id} - Remover Produto

**Descrição:** Remove um produto do sistema

**URL:** `DELETE http://localhost:8080/api/produtos/1`

**Path Parameters:**
- `id` (obrigatório): Long - ID do produto

**cURL:**

```bash
curl -X DELETE "http://localhost:8080/api/produtos/1"
```

**Response (204 No Content):** Sem body

**Possíveis Erros:**
- `404 Not Found` - Produto não existe

---

### 5️⃣ PATCH /api/produtos/{id}/disponibilidade - Toggle Disponibilidade

**Descrição:** Altera apenas a disponibilidade (ativo/inativo) de um produto

**URL:** `PATCH http://localhost:8080/api/produtos/1/disponibilidade`

**Path Parameters:**
- `id` (obrigatório): Long - ID do produto

**Request Body:**

```json
{
  "disponivel": false
}
```

**cURL:**

```bash
# Desativar
curl -X PATCH "http://localhost:8080/api/produtos/1/disponibilidade" \
  -H "Content-Type: application/json" \
  -d '{"disponivel": false}'

# Ativar
curl -X PATCH "http://localhost:8080/api/produtos/1/disponibilidade" \
  -H "Content-Type: application/json" \
  -d '{"disponivel": true}'
```

**Response (200 OK):**

```json
{
  "id": 1,
  "nome": "Pizza Margherita",
  "descricao": "Pizza clássica com tomate, mozzarela e manjericão",
  "preco": 45.00,
  "disponivel": false,
  "categoria": "Pizzas"
}
```

**Possíveis Erros:**
- `404 Not Found` - Produto não existe
- `400 Bad Request` - Disponibilidade não informada

---

### 6️⃣ GET /api/restaurantes/{restauranteId}/produtos - Produtos do Restaurante

**Descrição:** Lista todos os produtos de um restaurante específico

**URL:** `GET http://localhost:8080/api/restaurantes/1/produtos`

**Path Parameters:**
- `restauranteId` (obrigatório): Long - ID do restaurante

**cURL:**

```bash
curl -X GET "http://localhost:8080/api/restaurantes/1/produtos"
curl -X GET "http://localhost:8080/api/restaurantes/2/produtos"
```

**Response (200 OK):**

```json
[
  {
    "id": 1,
    "nome": "Pizza Margherita",
    "descricao": "Pizza clássica com tomate, mozzarela e manjericão",
    "preco": 45.00,
    "disponivel": true,
    "categoria": "Pizzas"
  },
  {
    "id": 2,
    "nome": "Pizza Calabresa",
    "descricao": "Pizza com calabresa, cebola e molho especial",
    "preco": 42.00,
    "disponivel": true,
    "categoria": "Pizzas"
  }
]
```

---

### 7️⃣ GET /api/produtos/categoria/{categoria} - Por Categoria

**Descrição:** Lista todos os produtos de uma categoria específica

**URL:** `GET http://localhost:8080/api/produtos/categoria/Pizzas`

**Path Parameters:**
- `categoria` (obrigatório): String - Nome da categoria

**cURL:**

```bash
curl -X GET "http://localhost:8080/api/produtos/categoria/Pizzas"
curl -X GET "http://localhost:8080/api/produtos/categoria/Bebidas"
curl -X GET "http://localhost:8080/api/produtos/categoria/Sobremesas"
```

**Response (200 OK):**

```json
[
  {
    "id": 1,
    "nome": "Pizza Margherita",
    "descricao": "Pizza clássica com tomate, mozzarela e manjericão",
    "preco": 45.00,
    "disponivel": true,
    "categoria": "Pizzas"
  },
  {
    "id": 2,
    "nome": "Pizza Calabresa",
    "descricao": "Pizza com calabresa, cebola e molho especial",
    "preco": 42.00,
    "disponivel": true,
    "categoria": "Pizzas"
  }
]
```

---

### 8️⃣ GET /api/produtos/buscar - Buscar por Nome

**Descrição:** Busca produtos por nome (case-insensitive com LIKE)

**URL:** `GET http://localhost:8080/api/produtos/buscar?nome=Margherita`

**Query Parameters:**
- `nome` (obrigatório): String - Parte do nome do produto

**cURL:**

```bash
# Buscar exato
curl -X GET "http://localhost:8080/api/produtos/buscar?nome=Margherita"

# Busca parcial
curl -X GET "http://localhost:8080/api/produtos/buscar?nome=Pizza"

# Case-insensitive
curl -X GET "http://localhost:8080/api/produtos/buscar?nome=margherita"
curl -X GET "http://localhost:8080/api/produtos/buscar?nome=PIZZA"
```

**Response (200 OK):**

```json
[
  {
    "id": 1,
    "nome": "Pizza Margherita",
    "descricao": "Pizza clássica com tomate, mozzarela e manjericão",
    "preco": 45.00,
    "disponivel": true,
    "categoria": "Pizzas"
  }
]
```

---

## 📝 DTOs Utilizados

### Request DTOs

#### ProdutoRequest
```json
{
  "nome": "string (3-100 caracteres)",
  "descricao": "string (5-255 caracteres)",
  "preco": "decimal (> 0.00)",
  "disponivel": "boolean (obrigatório)",
  "categoria": "string (3-20 caracteres)"
}
```

#### AtualizarDisponibilidadeProdutoRequest
```json
{
  "disponivel": "boolean (obrigatório)"
}
```

### Response DTOs

#### ProdutoResponse
```json
{
  "id": "long",
  "nome": "string",
  "descricao": "string",
  "preco": "decimal",
  "disponivel": "boolean",
  "categoria": "string"
}
```

---

## 🧪 Exemplos de Testes (cURL)

### Script de Teste Completo

```bash
#!/bin/bash

# 1. Criar produto
echo "1. Criando produto..."
curl -X POST "http://localhost:8080/api/produtos" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pizza Margherita",
    "descricao": "Pizza clássica com tomate, mozzarela e manjericão",
    "preco": 45.00,
    "disponivel": true,
    "categoria": "Pizzas"
  }'

# 2. Obter por ID
echo -e "\n2. Obtendo produto por ID..."
curl -X GET "http://localhost:8080/api/produtos/1"

# 3. Atualizar
echo -e "\n3. Atualizando produto..."
curl -X PUT "http://localhost:8080/api/produtos/1" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pizza Margherita Premium",
    "descricao": "Pizza premium com ingredientes importados",
    "preco": 55.00,
    "disponivel": true,
    "categoria": "Pizzas Premium"
  }'

# 4. Toggle disponibilidade
echo -e "\n4. Desativando produto..."
curl -X PATCH "http://localhost:8080/api/produtos/1/disponibilidade" \
  -H "Content-Type: application/json" \
  -d '{"disponivel": false}'

# 5. Buscar por categoria
echo -e "\n5. Buscando por categoria..."
curl -X GET "http://localhost:8080/api/produtos/categoria/Pizzas"

# 6. Buscar por nome
echo -e "\n6. Buscando por nome..."
curl -X GET "http://localhost:8080/api/produtos/buscar?nome=Margherita"

# 7. Produtos do restaurante
echo -e "\n7. Listando produtos do restaurante..."
curl -X GET "http://localhost:8080/api/restaurantes/1/produtos"

# 8. Deletar
echo -e "\n8. Deletando produto..."
curl -X DELETE "http://localhost:8080/api/produtos/1"
```

---

## ✔️ Validações e Tratamento de Erros

### Validações implementadas:

| Campo | Validação |
|-------|-----------|
| Nome | 3-100 caracteres, não-nulo |
| Descrição | 5-255 caracteres, não-nulo |
| Preço | Decimal > 0.00, não-nulo |
| Categoria | 3-20 caracteres, não-nulo |
| Disponível | Boolean, obrigatório |

### Códigos HTTP de Resposta:

| Código | Situação |
|--------|----------|
| 200 | GET/PUT/PATCH sucesso |
| 201 | POST sucesso (criado) |
| 204 | DELETE sucesso (sem conteúdo) |
| 400 | Bad Request - Validação falhou |
| 404 | Not Found - Recurso não existe |
| 500 | Erro interno do servidor |

---

## 🔗 Relacionamentos

**Produto ↔ Restaurante (ManyToMany)**
- Um produto pode estar em vários restaurantes
- Um restaurante pode ter vários produtos
- Join Table: `restaurante_produto`

---

## 🔐 Segurança

- ✅ Validação de entrada em todos os endpoints
- ✅ Sanitização de parâmetros
- ✅ Transações ACID garantidas
- ✅ Tratamento centralizado de exceções

---

**Status:** ✅ Pronto para testes  
**Última atualização:** Novembro 2025  
**Desenvolvedor:** Rafael Rubleske
