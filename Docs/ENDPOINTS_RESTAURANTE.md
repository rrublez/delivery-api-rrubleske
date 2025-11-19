# 📋 Documentação dos Novos Endpoints - RestauranteController

> **Versão:** 1.0  
> **Data:** Novembro 2025  
> **Status:** ✅ Implementado e Testado

---

## 📊 Resumo dos Endpoints

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| `GET` | `/api/restaurantes` | Listar com filtros | ✅ |
| `POST` | `/api/restaurantes` | Criar restaurante | ✅ |
| `GET` | `/api/restaurantes/{id}` | Obter por ID | ✅ |
| `PUT` | `/api/restaurantes/{id}` | Atualizar completo | ✅ |
| `PATCH` | `/api/restaurantes/{id}/status` | Ativar/desativar | ✅ |
| `GET` | `/api/restaurantes/categoria/{categoria}` | Listar por categoria | ✅ |
| `GET` | `/api/restaurantes/{id}/taxa-entrega/{cep}` | Calcular taxa | ✅ |
| `GET` | `/api/restaurantes/proximos/{cep}` | Restaurantes próximos | ✅ |

---

## 🔐 Autenticação e Segurança

- `POST /api/auth/register` e `POST /api/auth/login` são públicos para criar credenciais e validar dados; `GET /api/auth/me` e todos os demais endpoints exigem `Authorization: Bearer <token>` retornado pelo login.
- O `LoginResponse` inclui o JWT (`token`), o tipo (`Bearer`), o `expiresAt` e o objeto `user` com os dados públicos do usuário autenticado.
- Usuários com `role=RESTAURANTE` podem informar o campo `restauranteId`; outras roles devem omitir esse campo.
- Senhas são armazenadas com `BCrypt` e apenas contas com `ativo=true` conseguem acessar os recursos protegidos.

```bash
curl -X GET "http://localhost:8080/api/restaurantes" \
  -H "Authorization: Bearer $TOKEN"
```


## 🔍 Detalhamento dos Endpoints

### 1️⃣ GET /api/restaurantes - Listar com Filtros

**Descrição:** Lista todos os restaurantes com possibilidade de filtrar por categoria (ramo) e status (ativo/inativo)

**URL:** `GET http://localhost:8080/api/restaurantes`

**Query Parameters:**
- `ramo` (opcional): String - Categoria/ramo de atividade (ex: Pizzaria, Burger, Churrascaria)
- `ativo` (opcional): Boolean - true para ativos, false para inativos

**Exemplos de Requisição:**

```bash
# Listar todos
curl -X GET "http://localhost:8080/api/restaurantes"

# Filtrar por categoria
curl -X GET "http://localhost:8080/api/restaurantes?ramo=Pizzaria"

# Filtrar por status
curl -X GET "http://localhost:8080/api/restaurantes?ativo=true"

# Filtrar por categoria E status
curl -X GET "http://localhost:8080/api/restaurantes?ramo=Pizzaria&ativo=true"
```

**Response (200 OK):**

```json
[
  {
    "id": 1,
    "nome": "Pizza Palace",
    "endereco": "Avenida Paulista, 1000",
    "telefone": "1133334444",
    "cnpj": "11222333000181",
    "ramoAtividade": "Pizzaria",
    "ativo": true,
    "taxaEntrega": 5.00
  },
  {
    "id": 2,
    "nome": "Burger King",
    "endereco": "Rua Augusta, 500",
    "telefone": "1133335555",
    "cnpj": "11333444000182",
    "ramoAtividade": "Burger",
    "ativo": true,
    "taxaEntrega": 4.50
  }
]
```

---

### 2️⃣ POST /api/restaurantes - Criar Restaurante

**Descrição:** Cria um novo restaurante com validações

**URL:** `POST http://localhost:8080/api/restaurantes`

**Content-Type:** `application/json`

**Request Body:**

```json
{
  "nome": "Pizza Palace",
  "endereco": "Avenida Paulista, 1000",
  "telefone": "1133334444",
  "cnpj": "11222333000181",
  "ramoAtividade": "Pizzaria",
  "ativo": true,
  "taxaEntrega": 5.00
}
```

**Validações:**
- Nome: 3-100 caracteres, único
- Endereço: 5-255 caracteres
- Telefone: 10-15 caracteres, único
- CNPJ: 14 dígitos, válido, único
- Ramo: 3-20 caracteres
- Taxa: ≥ 0

**cURL:**

```bash
curl -X POST "http://localhost:8080/api/restaurantes" \
  -d '{
    "nome": "Pizza Palace",
    "endereco": "Avenida Paulista, 1000",
    "taxaEntrega": 5.00
  }'
```

**Response (201 Created):**

{
  "id": 1,
  "nome": "Pizza Palace",
  "endereco": "Avenida Paulista, 1000",
  "telefone": "1133334444",
  "cnpj": "11222333000181",
  "ramoAtividade": "Pizzaria",
  "ativo": true,
  "taxaEntrega": 5.00
}
```

**Possíveis Erros:**
- `400 Bad Request` - Validação falhou

---

### 3️⃣ GET /api/restaurantes/{id} - Obter por ID

**Descrição:** Busca um restaurante específico pelo ID

**URL:** `GET http://localhost:8080/api/restaurantes/1`

**Path Parameters:**
- `id` (obrigatório): Long - ID do restaurante

**cURL:**

```bash
curl -X GET "http://localhost:8080/api/restaurantes/1"
```

**Response (200 OK):**

```json
{
  "id": 1,
  "nome": "Pizza Palace",
  "endereco": "Avenida Paulista, 1000",
  "telefone": "1133334444",
  "cnpj": "11222333000181",
  "ramoAtividade": "Pizzaria",
  "ativo": true,
  "taxaEntrega": 5.00
}
```

**Possíveis Erros:**
- `404 Not Found` - Restaurante não existe

---

### 4️⃣ PUT /api/restaurantes/{id} - Atualizar Completo

**Descrição:** Atualiza todos os dados de um restaurante (requer todos os campos)

**URL:** `PUT http://localhost:8080/api/restaurantes/1`

**Path Parameters:**
- `id` (obrigatório): Long - ID do restaurante

**Request Body:**

```json
{
  "nome": "Pizza Palace Premium",
  "endereco": "Avenida Paulista, 2000",
  "telefone": "1133334445",
  "cnpj": "11222333000181",
  "ramoAtividade": "Pizzaria Premium",
  "ativo": true,
  "taxaEntrega": 7.50
}
```

**cURL:**

```bash
curl -X PUT "http://localhost:8080/api/restaurantes/1" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pizza Palace Premium",
    "endereco": "Avenida Paulista, 2000",
    "telefone": "1133334445",
    "cnpj": "11222333000181",
    "ramoAtividade": "Pizzaria Premium",
    "ativo": true,
    "taxaEntrega": 7.50
  }'
```

**Response (200 OK):**

```json
{
  "id": 1,
  "nome": "Pizza Palace Premium",
  "endereco": "Avenida Paulista, 2000",
  "telefone": "1133334445",
  "cnpj": "11222333000181",
  "ramoAtividade": "Pizzaria Premium",
  "ativo": true,
  "taxaEntrega": 7.50
}
```

**Possíveis Erros:**
- `404 Not Found` - Restaurante não existe
- `409 Conflict` - Email/CNPJ/Telefone já existe (em outro restaurante)
- `400 Bad Request` - Validação falhou

---

### 5️⃣ PATCH /api/restaurantes/{id}/status - Ativar/Desativar

**Descrição:** Altera apenas o status (ativo/inativo) de um restaurante

**URL:** `PATCH http://localhost:8080/api/restaurantes/1/status`

**Path Parameters:**
- `id` (obrigatório): Long - ID do restaurante

**Request Body:**

```json
{
  "ativo": false
}
```

**cURL:**

```bash
# Desativar
curl -X PATCH "http://localhost:8080/api/restaurantes/1/status" \
  -H "Content-Type: application/json" \
  -d '{"ativo": false}'

# Ativar
curl -X PATCH "http://localhost:8080/api/restaurantes/1/status" \
  -H "Content-Type: application/json" \
  -d '{"ativo": true}'
```

**Response (200 OK):**

```json
{
  "id": 1,
  "nome": "Pizza Palace",
  "endereco": "Avenida Paulista, 1000",
  "telefone": "1133334444",
  "cnpj": "11222333000181",
  "ramoAtividade": "Pizzaria",
  "ativo": false,
  "taxaEntrega": 5.00
}
```

**Possíveis Erros:**
- `404 Not Found` - Restaurante não existe
- `400 Bad Request` - Status não informado

---

### 6️⃣ GET /api/restaurantes/categoria/{categoria} - Por Categoria

**Descrição:** Lista todos os restaurantes de uma categoria específica

**URL:** `GET http://localhost:8080/api/restaurantes/categoria/Pizzaria`

**Path Parameters:**
- `categoria` (obrigatório): String - Nome da categoria/ramo

**cURL:**

```bash
curl -X GET "http://localhost:8080/api/restaurantes/categoria/Pizzaria"
curl -X GET "http://localhost:8080/api/restaurantes/categoria/Burger"
curl -X GET "http://localhost:8080/api/restaurantes/categoria/Churrascaria"
```

**Response (200 OK):**

```json
[
  {
    "id": 1,
    "nome": "Pizza Palace",
    "endereco": "Avenida Paulista, 1000",
    "telefone": "1133334444",
    "cnpj": "11222333000181",
    "ramoAtividade": "Pizzaria",
    "ativo": true,
    "taxaEntrega": 5.00
  },
  {
    "id": 3,
    "nome": "Pizza Hut",
    "endereco": "Rua Oscar Freire, 300",
    "telefone": "1133336666",
    "cnpj": "11444555000183",
    "ramoAtividade": "Pizzaria",
    "ativo": true,
    "taxaEntrega": 6.00
  }
]
```

---

### 7️⃣ GET /api/restaurantes/{id}/taxa-entrega/{cep} - Calcular Taxa

**Descrição:** Calcula a taxa de entrega para um restaurante baseado no CEP do cliente

**URL:** `GET http://localhost:8080/api/restaurantes/1/taxa-entrega/90010100`

**Path Parameters:**
- `id` (obrigatório): Long - ID do restaurante
- `cep` (obrigatório): String - CEP do cliente (formato: 8 dígitos)

**cURL:**

```bash
curl -X GET "http://localhost:8080/api/restaurantes/1/taxa-entrega/90010100"
curl -X GET "http://localhost:8080/api/restaurantes/1/taxa-entrega/90020100"
```

**Response (200 OK):**

```json
{
  "restauranteId": 1,
  "nomeRestaurante": "Pizza Palace",
  "cep": "90010100",
  "taxaEntrega": 5.00,
  "distanciaKm": 3.45,
  "mensagem": "Taxa de entrega de R$ 5.00 para 3.45 km de distância"
}
```

**Observações:**
- A distância é calculada simuladamente (em produção, integrar com API de geolocalização)
- A taxa é a taxa padrão do restaurante (não varia com distância neste MVP)

**Possíveis Erros:**
- `404 Not Found` - Restaurante não existe

---

### 8️⃣ GET /api/restaurantes/proximos/{cep} - Restaurantes Próximos

**Descrição:** Lista todos os restaurantes próximos ao CEP fornecido, ordenados por distância

**URL:** `GET http://localhost:8080/api/restaurantes/proximos/90010100`

**Path Parameters:**
- `cep` (obrigatório): String - CEP para buscar restaurantes (8 dígitos)

**Query Parameters:**
- `raio` (opcional): Double - Raio de busca em km (padrão: 5km)

**cURL:**

```bash
# Com raio padrão (5km)
curl -X GET "http://localhost:8080/api/restaurantes/proximos/90010100"

# Com raio customizado (10km)
curl -X GET "http://localhost:8080/api/restaurantes/proximos/90010100?raio=10"

# Raio maior (20km)
curl -X GET "http://localhost:8080/api/restaurantes/proximos/90010100?raio=20"
```

**Response (200 OK):**

```json
[
  {
    "id": 1,
    "nome": "Pizza Palace",
    "endereco": "Avenida Paulista, 1000",
    "ramoAtividade": "Pizzaria",
    "taxaEntrega": 5.00,
    "distanciaKm": 2.15,
    "ativo": true
  },
  {
    "id": 4,
    "nome": "Burger Express",
    "endereco": "Rua Santa Catarina, 800",
    "ramoAtividade": "Burger",
    "taxaEntrega": 4.50,
    "distanciaKm": 3.82,
    "ativo": true
  },
  {
    "id": 2,
    "nome": "Churrascaria Paulista",
    "endereco": "Rua Bom Retiro, 1200",
    "ramoAtividade": "Churrascaria",
    "taxaEntrega": 7.00,
    "distanciaKm": 4.95,
    "ativo": true
  }
]
```

**Características:**
- Retorna apenas restaurantes **ativos**
- Resultado ordenado por **distância crescente** (mais próximos primeiro)
- Filtra por **raio de distância** (padrão 5km)
- Distância é calculada simuladamente (usar API real em produção)

---

## 📝 DTOs Utilizados

### Request DTOs

#### RestauranteRequest
```json
{
  "nome": "string (3-100 caracteres, único)",
  "endereco": "string (5-255 caracteres)",
  "telefone": "string (10-15 caracteres, único)",
  "cnpj": "string (14 dígitos, válido, único)",
  "ramoAtividade": "string (3-20 caracteres)",
  "ativo": "boolean",
  "taxaEntrega": "decimal (≥ 0)"
}
```

#### AtualizarStatusRestauranteRequest
```json
{
  "ativo": "boolean (obrigatório)"
}
```

### Response DTOs

#### RestauranteResponse
```json
{
  "id": "long",
  "nome": "string",
  "endereco": "string",
  "telefone": "string",
  "cnpj": "string",
  "ramoAtividade": "string",
  "ativo": "boolean",
  "taxaEntrega": "decimal"
}
```

#### TaxaEntregaResponse
```json
{
  "restauranteId": "long",
  "nomeRestaurante": "string",
  "cep": "string",
  "taxaEntrega": "decimal",
  "distanciaKm": "double",
  "mensagem": "string"
}
```

#### RestaurantePróximoResponse
```json
{
  "id": "long",
  "nome": "string",
  "endereco": "string",
  "ramoAtividade": "string",
  "taxaEntrega": "decimal",
  "distanciaKm": "double",
  "ativo": "boolean"
}
```

---

## 🧪 Exemplos de Testes (cURL)

### Script de Teste Completo

```bash
#!/bin/bash

# 1. Criar restaurante
echo "1. Criando restaurante..."
curl -X POST "http://localhost:8080/api/restaurantes" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pizza Palace",
    "endereco": "Avenida Paulista, 1000",
    "telefone": "1133334444",
    "cnpj": "11222333000181",
    "ramoAtividade": "Pizzaria",
    "ativo": true,
    "taxaEntrega": 5.00
  }' | jq .

# 2. Listar todos
echo -e "\n2. Listando todos os restaurantes..."
curl -X GET "http://localhost:8080/api/restaurantes" | jq .

# 3. Listar com filtro
echo -e "\n3. Listando por categoria..."
curl -X GET "http://localhost:8080/api/restaurantes?ramo=Pizzaria" | jq .

# 4. Obter por ID
echo -e "\n4. Obtendo restaurante por ID..."
curl -X GET "http://localhost:8080/api/restaurantes/1" | jq .

# 5. Atualizar
echo -e "\n5. Atualizando restaurante..."
curl -X PUT "http://localhost:8080/api/restaurantes/1" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pizza Palace Premium",
    "endereco": "Avenida Paulista, 2000",
    "telefone": "1133334445",
    "cnpj": "11222333000181",
    "ramoAtividade": "Pizzaria Premium",
    "ativo": true,
    "taxaEntrega": 7.50
  }' | jq .

# 6. Atualizar status
echo -e "\n6. Desativando restaurante..."
curl -X PATCH "http://localhost:8080/api/restaurantes/1/status" \
  -H "Content-Type: application/json" \
  -d '{"ativo": false}' | jq .

# 7. Calcular taxa
echo -e "\n7. Calculando taxa de entrega..."
curl -X GET "http://localhost:8080/api/restaurantes/1/taxa-entrega/90010100" | jq .

# 8. Listar próximos
echo -e "\n8. Listando restaurantes próximos..."
curl -X GET "http://localhost:8080/api/restaurantes/proximos/90010100" | jq .

# 9. Listar próximos com raio customizado
echo -e "\n9. Listando restaurantes próximos (raio 10km)..."
curl -X GET "http://localhost:8080/api/restaurantes/proximos/90010100?raio=10" | jq .
```

---

## ✔️ Validações e Tratamento de Erros

### Validações implementadas:

| Campo | Validação |
|-------|-----------|
| Nome | 3-100 caracteres, único, não-nulo |
| Endereço | 5-255 caracteres, não-nulo |
| Telefone | 10-15 caracteres, único, não-nulo |
| CNPJ | 14 dígitos, formato válido, único, não-nulo |
| Ramo | 3-20 caracteres, não-nulo |
| Ativo | Boolean, obrigatório |
| Taxa | Decimal ≥ 0, obrigatório |

### Códigos HTTP de Resposta:

| Código | Situação |
|--------|----------|
| 200 | GET/PUT/PATCH sucesso |
| 201 | POST sucesso (criado) |
| 400 | Bad Request - Validação falhou |
| 404 | Not Found - Recurso não existe |
| 409 | Conflict - Duplicidade (nome/CNPJ/telefone) |
| 500 | Erro interno do servidor |

---

## 🔐 Segurança

- ✅ Validação de entrada em todos os endpoints
- ✅ Sanitização de parâmetros
- ✅ Transações ACID garantidas
- ✅ Verificação de duplicidade
- ✅ Tratamento centralizado de exceções

---

**Status:** ✅ Pronto para testes  
**Última atualização:** Novembro 2025  
**Desenvolvedor:** Rafael Rubleske
