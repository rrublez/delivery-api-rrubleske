# 👋 Bem-vindo ao Guia de Testes Manuais - Delivery API

## 🎯 Comece Aqui em 3 Passos

### 1️⃣ Leia Rápido (5 minutos)
Abra um destes arquivos de acordo com seu tempo disponível:

- ⚡ **Muito ocupado?** → [RESUMO_EXECUTIVO.md](./RESUMO_EXECUTIVO.md)
- 🖥️ **Prefere visual?** → [TESTES_MANUAIS.html](./TESTES_MANUAIS.html) (abra no navegador)
- 📚 **Quer guia completo?** → [GUIA_TESTES_MANUAIS.md](./GUIA_TESTES_MANUAIS.md)
- 🗺️ **Perdido?** → [INDEX_DOCUMENTACAO.md](./INDEX_DOCUMENTACAO.md)

### 2️⃣ Importe a Collection (5 minutos)
Escolha seu cliente HTTP:

**Postman:**
- Import → `delivery-api-postman.json`
- [Instruções detalhadas](./GUIA_IMPORTACAO_COLLECTIONS.md#-importação-no-postman)

**Bruno:**
- File → Import Collection → `delivery-api-bruno.bru`
- [Instruções detalhadas](./GUIA_IMPORTACAO_COLLECTIONS.md#-importação-no-bruno)

### 3️⃣ Execute os Testes (45 minutos)
```bash
# 1. Inicie a API
./mvnw spring-boot:run

# 2. Na collection, execute na ordem:
# - Grupo 0: Setup e Validação
# - Grupo 1: Testes de Validação
# - Grupo 2: Criação de Dados
# - Grupo 3: Consultas Simples
# - Grupo 4: Relatórios
```

---

## 📦 O Que Você Tem

### ✅ 32 Testes Prontos
- 12 testes de validação
- 11 testes de criação de dados
- 7 testes de consultas
- 8 testes de relatórios

### 🎯 2 Collections
- **Postman** (43 KB) - Com variáveis automáticas
- **Bruno** (11 KB) - Leve e minimalista

### 📚 6 Guias de Documentação
| Arquivo | Descrição | Tempo |
|---------|-----------|-------|
| RESUMO_EXECUTIVO.md | Visão geral em 5 min | ⚡⚡⚡ |
| TESTES_MANUAIS.html | Interface visual | ⚡⚡⚡ |
| TESTES_MANUAIS_README.md | Guia rápido | ⚡⚡ |
| GUIA_IMPORTACAO_COLLECTIONS.md | Passo-a-passo | ⚡⚡ |
| GUIA_TESTES_MANUAIS.md | Guia completo | ⏱️ |
| INDEX_DOCUMENTACAO.md | Mapa de navegação | ⚡ |

### 🖥️ Scripts de Exemplo
- `curl-examples.sh` - Exemplos cURL para terminal

---

## 🚀 Fluxo Rápido (Para os Apressados)

```bash
# Passo 1: Inicie a API
./mvnw spring-boot:run

# Passo 2: Abra outro terminal
cd delivery-api-rrubleske

# Passo 3: Teste rapidamente com cURL
curl http://localhost:8080/health

# Passo 4: Execute o script de exemplo
chmod +x curl-examples.sh
./curl-examples.sh
```

---

## 📊 Dados de Teste Inclusos

### 3 Clientes
- João Silva (joao@email.com)
- Maria Santos (maria@email.com)
- Pedro Oliveira (pedro@email.com)

### 2 Restaurantes
- Pizza Palace (Pizzaria)
- Sushi House (Japonesa)

### 6 Produtos
- Pizza Margherita (R$ 45)
- Pizza Pepperoni (R$ 50)
- Refrigerante 2L (R$ 8)
- Combo Sushi Premium (R$ 120)
- Temaki Salmão (R$ 35)
- Sakê 300ml (R$ 25)

### 3 Pedidos
- Pedido 1: 2 itens, R$ 61
- Pedido 2: 9 itens, R$ 282
- Pedido 3: 7 itens, R$ 395

---

## ❓ Dúvidas Frequentes

### P: Por onde começo?
R: Se tem pouco tempo, leia [RESUMO_EXECUTIVO.md](./RESUMO_EXECUTIVO.md) (5 min).

### P: Como importo a collection?
R: Consulte [GUIA_IMPORTACAO_COLLECTIONS.md](./GUIA_IMPORTACAO_COLLECTIONS.md).

### P: Posso usar cURL?
R: Sim! Veja [curl-examples.sh](./curl-examples.sh) ou [TESTES_MANUAIS_README.md](./TESTES_MANUAIS_README.md#-exemplos-curl).

### P: Todos os dados estão inclusos?
R: Sim! Basta importar a collection e executar.

### P: Quanto tempo leva?
R: ~45 minutos para executar todos os 32 testes.

### P: Que cliente HTTP devo usar?
R: Postman ou Bruno. Ambas as collections estão prontas!

### P: E se der erro?
R: Consulte a seção "Troubleshooting" em [GUIA_TESTES_MANUAIS.md](./GUIA_TESTES_MANUAIS.md#-troubleshooting).

---

## 🎓 O Que Você Aprenderá

### Técnicas de Teste
- ✅ Testes de validação
- ✅ Testes de negócio
- ✅ Testes de relatórios/agregações
- ✅ Testes de filtros e buscas

### Endpoints da API
- 📮 POST - Criar recursos
- 🔍 GET - Buscar e listar
- 📊 GET - Relatórios complexos
- ✔️ Validações de entrada

### Boas Práticas
- 🎯 Organização de testes
- 📚 Documentação clara
- 🔄 Reutilização de dados
- 📈 Automação de testes

---

## 📈 Estatísticas

| Métrica | Valor |
|---------|-------|
| Total de testes | 32 |
| Total de endpoints | 20+ |
| Objetos testados | 4 (Cliente, Restaurante, Produto, Pedido) |
| Validações | 50+ |
| Linhas de documentação | 2000+ |
| Exemplos | 100+ |

---

## 🗂️ Estrutura de Documentação

```
📁 Documentação
├─ ESTA_PÁGINA (bem-vindos.txt)
├─ INDEX_DOCUMENTACAO.md ......... Mapa completo
├─ RESUMO_EXECUTIVO.md .......... Versão executiva
├─ TESTES_MANUAIS_README.md ..... Guia rápido
├─ GUIA_TESTES_MANUAIS.md ....... Guia completo
├─ GUIA_IMPORTACAO_COLLECTIONS.md . Como importar
└─ TESTES_MANUAIS.html .......... Versão visual

📁 Collections
├─ delivery-api-postman.json .... Postman (32 testes)
└─ delivery-api-bruno.bru ....... Bruno (32 testes)

📁 Scripts
└─ curl-examples.sh ............ Exemplos cURL
```

---

## 🔗 Links Rápidos

| Objetivo | Arquivo |
|----------|---------|
| Visão geral rápida | [RESUMO_EXECUTIVO.md](./RESUMO_EXECUTIVO.md) |
| Versão visual | [TESTES_MANUAIS.html](./TESTES_MANUAIS.html) |
| Instruções Postman | [GUIA_IMPORTACAO_COLLECTIONS.md](./GUIA_IMPORTACAO_COLLECTIONS.md#-importação-no-postman) |
| Instruções Bruno | [GUIA_IMPORTACAO_COLLECTIONS.md](./GUIA_IMPORTACAO_COLLECTIONS.md#-importação-no-bruno) |
| Guia completo | [GUIA_TESTES_MANUAIS.md](./GUIA_TESTES_MANUAIS.md) |
| Mapa de documentação | [INDEX_DOCUMENTACAO.md](./INDEX_DOCUMENTACAO.md) |
| Exemplos cURL | [curl-examples.sh](./curl-examples.sh) |

---

## ⏱️ Tempo Total Estimado

| Fase | Tempo |
|------|-------|
| Leitura de documentação | 15-30 min |
| Importação da collection | 5 min |
| Execução dos testes | 45 min |
| **Total** | **~1h15min** |

---

## ✨ Características Principais

✅ **32 testes prontos** - Basta importar e executar  
✅ **Dados inclusos** - Sem necessidade de preparação  
✅ **2 collections** - Escolha Postman ou Bruno  
✅ **Documentação completa** - 2000+ linhas  
✅ **Exemplos práticos** - cURL, JSON, Markdown  
✅ **Fácil de seguir** - 4 grupos lógicos  
✅ **Troubleshooting** - Soluções para problemas comuns  
✅ **Relatórios de teste** - 8 endpoints de análise  

---

## 🎯 Próximos Passos

### Agora:
1. Abra [RESUMO_EXECUTIVO.md](./RESUMO_EXECUTIVO.md)
2. Ou abra [TESTES_MANUAIS.html](./TESTES_MANUAIS.html) no navegador

### Depois:
1. Siga [GUIA_IMPORTACAO_COLLECTIONS.md](./GUIA_IMPORTACAO_COLLECTIONS.md)
2. Importe a collection no Postman ou Bruno
3. Execute os testes

### Se tiver dúvidas:
1. Consulte [INDEX_DOCUMENTACAO.md](./INDEX_DOCUMENTACAO.md)
2. Procure pelo tópico em [GUIA_TESTES_MANUAIS.md](./GUIA_TESTES_MANUAIS.md)

---

## 💡 Dica Final

**Não leia tudo de uma vez!** 

Comece pelo [RESUMO_EXECUTIVO.md](./RESUMO_EXECUTIVO.md) (5 min), depois vá importando e testando. Consulte os guias conforme necessário.

---

**Versão:** 1.0 | **Data:** Novembro 2025  
**API:** Delivery Tech v1.0.0 | **Java:** 21 | **Spring Boot:** 3.4.11

🚀 **Pronto para começar? Vá para [RESUMO_EXECUTIVO.md](./RESUMO_EXECUTIVO.md)**
