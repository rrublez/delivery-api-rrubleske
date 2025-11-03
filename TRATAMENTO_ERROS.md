# Tratamento de Erros da API - v1.2.3

## Visão Geral

A partir da versão 1.2.3, a API implementa um tratamento global e consistente de erros, retornando status HTTP apropriados e estruturas padronizadas de resposta.

## Padrão de Resposta de Erro

### Erros de Validação (4xx) - SEM stack trace

```json
{
  "timestamp": "2025-11-03T00:06:53.845238079",
  "status": 400,
  "message": "Erro de validação nos dados fornecidos",
  "path": "/api/v1/clientes",
  "validationErrors": {
    "email": ["Email deve ser válido"],
    "nome": ["Nome deve ter entre 3 e 100 caracteres"]
  }
}
```

**Características:**
- ✅ Status HTTP apropriado (400, 404, 409, etc.)
- ✅ Campo `trace` **NUNCA** aparece
- ✅ Campo `validationErrors` contém erros por campo (apenas em 400)
- ✅ Mensagem clara e útil

### Erros de Servidor (5xx) - COM stack trace

```json
{
  "timestamp": "2025-11-03T00:06:53.845238079",
  "status": 500,
  "message": "Erro interno do servidor",
  "path": "/api/v1/clientes",
  "trace": "java.lang.NullPointerException: Connection refused\n\tat com.deliverytech.delivery.service.ClienteService.create...\n..."
}
```

**Características:**
- ✅ Status HTTP 500
- ✅ Campo `trace` **SÓ** aparece em erros 5xx
- ✅ Stack trace completo para debugging

## Status HTTP Retornados

| Status | Tipo | Descrição | Exemplo |
|--------|------|-----------|---------|
| **400** | Bad Request | Validação inválida ou argumento inválido | Email duplicado, campos obrigatórios ausentes |
| **404** | Not Found | Recurso não encontrado | Cliente com ID inexistente |
| **409** | Conflict | Conflito de negócio | Violação de constraint |
| **500** | Internal Server Error | Erro não capturado/inesperado | Erro de banco de dados, null pointer, etc. |

## Testes

### Teste 1: Validação de Campos (400)

```bash
curl -X POST http://localhost:8080/api/v1/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Jo",
    "email": "invalid"
  }'
```

**Resultado Esperado:**
- Status: **400**
- Campo `trace`: **ausente** ✅
- Campo `validationErrors`: **presente** com detalhes ✅

### Teste 2: Recurso Não Encontrado (404)

```bash
curl http://localhost:8080/api/v1/clientes/cliente-inexistente-123
```

**Resultado Esperado:**
- Status: **404**
- Campo `trace`: **ausente** ✅
- Mensagem: "Cliente não encontrado"

### Teste 3: Violação de Constraint (400)

```bash
# Primeiro cliente
curl -X POST http://localhost:8080/api/v1/clientes \
  -H "Content-Type: application/json" \
  -d '{...}'

# Tentativa de criar com email duplicado
curl -X POST http://localhost:8080/api/v1/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@test.com", // já existe
    ...
  }'
```

**Resultado Esperado:**
- Status: **400**
- Campo `trace`: **ausente** ✅
- Mensagem: "Email já cadastrado no sistema"

### Teste 4: Erro de Servidor (500)

Erros 500 aparecem quando há exceções não capturadas (ex: erro de banco de dados, null pointer, etc).

**Resultado Esperado:**
- Status: **500**
- Campo `trace`: **PRESENTE** com stack trace ✅
- Campo `validationErrors`: **ausente**

## Implementação

### Classes Principais

1. **GlobalExceptionHandler** (`exception/GlobalExceptionHandler.java`)
   - Intercepta todas as exceções
   - Mapeia para status HTTP apropriado
   - Define estratégia de inclusão de `trace`

2. **ApiErrorResponse** (`dto/ApiErrorResponse.java`)
   - DTO padronizado para respostas de erro
   - Usa `@JsonInclude(NON_NULL)` para excluir campos não obrigatórios

3. **Exceções Customizadas**
   - `ResourceNotFoundException` → 404
   - `BusinessException` → 409 (ou status customizado)
   - `IllegalArgumentException` → 400

### Configuração (application.properties)

```properties
# Desabilitar stack trace automático do Spring Boot
server.error.include-stacktrace=never
server.error.include-message=always
server.error.include-binding-errors=always
```

## Migração de Services

Todos os services foram atualizados para usar:

- **`ResourceNotFoundException`** para erros "não encontrado" → **404**
- **`IllegalArgumentException`** para validações → **400**
- **`BusinessException`** para regras de negócio → **409** (ou customizado)

### Exemplo Antes

```java
Cliente cliente = clienteRepository.findById(id)
    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
```

### Exemplo Depois

```java
Cliente cliente = clienteRepository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
```

## Comportamento Esperado

| Cenário | Status | Trace Visível |
|---------|--------|---|
| Email inválido | 400 | ❌ Não |
| Campos obrigatórios ausentes | 400 | ❌ Não |
| Email/Documento duplicado | 400 | ❌ Não |
| Recurso não encontrado | 404 | ❌ Não |
| Erro de banco de dados | 500 | ✅ Sim |
| Null pointer exception | 500 | ✅ Sim |
| Erro não capturado | 500 | ✅ Sim |

## Benefícios

✅ **Consistência**: Todas as respostas de erro seguem o mesmo padrão
✅ **Segurança**: Stack trace não é exposto para erros 4xx (validação)
✅ **Debugging**: Stack trace disponível apenas para erros críticos (5xx)
✅ **Clareza**: Mensagens e detalhes apropriados para cada situação
✅ **Conformidade**: Segue convenções HTTP (4xx para cliente, 5xx para servidor)

## Próximos Passos

- Implementar logging estruturado para auditoria
- Adicionar codes de erro internos para categorização
- Implementar tratamento de rate limiting (429)
