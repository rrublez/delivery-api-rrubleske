# 📑 Índice Completo de Documentação - Testes Manuais

## 🎯 Comece por Aqui

### Para Não-Técnicos
1. **[RESUMO_EXECUTIVO.md](./RESUMO_EXECUTIVO.md)** - Visão geral em 5 minutos
2. **[TESTES_MANUAIS.html](./TESTES_MANUAIS.html)** - Versão visual e interativa (abra no navegador)

### Para Técnicos
1. **[TESTES_MANUAIS_README.md](./TESTES_MANUAIS_README.md)** - Guia rápido com exemplos
2. **[GUIA_IMPORTACAO_COLLECTIONS.md](./GUIA_IMPORTACAO_COLLECTIONS.md)** - Como importar nos clientes HTTP
3. **[GUIA_TESTES_MANUAIS.md](./GUIA_TESTES_MANUAIS.md)** - Documentação completa e detalhada

---

## 📂 Estrutura de Arquivos

```
delivery-api-rrubleske/
│
├─ DOCUMENTAÇÃO
│  ├─ INDEX.md .................................. (este arquivo)
│  ├─ RESUMO_EXECUTIVO.md ....................... Visão geral executiva
│  ├─ TESTES_MANUAIS_README.md .................. README principal
│  ├─ GUIA_TESTES_MANUAIS.md .................... Guia completo (45 páginas)
│  ├─ GUIA_IMPORTACAO_COLLECTIONS.md ........... Instruções passo-a-passo
│  ├─ TESTES_MANUAIS.html ....................... Versão visual
│  └─ curl-examples.sh ........................... Exemplos de cURL
│
├─ COLLECTIONS PRONTAS
│  ├─ delivery-api-postman.json ................. Collection Postman (32 testes)
│  └─ delivery-api-bruno.bru .................... Collection Bruno (32 testes)
│
└─ CÓDIGO FONTE
   └─ src/
      └─ main/
         └─ java/com/deliverytech/delivery/
            ├─ controller/ ........................ Endpoints da API
            ├─ service/ .......................... Lógica de negócio
            ├─ repository/ ....................... Acesso a dados
            ├─ entity/ ........................... Modelos de dados
            ├─ dto/ .............................. Transfer objects
            ├─ validation/ ....................... Validadores customizados
            └─ exception/ ........................ Tratamento de erros
```

---

## 📖 Documentação Detalhada

### 1. RESUMO_EXECUTIVO.md
**Para quem quer saber tudo em 5 minutos**
- 📊 Estatísticas dos testes
- 🧪 Estrutura dos 32 testes
- ⏱️ Timeline de execução
- 💡 Dados esperados nos resultados
- ✅ Checklist de verificação

**Quando ler:** Antes de qualquer coisa

---

### 2. TESTES_MANUAIS.html
**Versão visual com interface bonita**
- 🌐 HTML interativo
- 📊 Cards com informações
- 🎯 Navegação intuitiva
- 📈 Estatísticas visuais

**Quando ler:** Para apresentações ou revisão rápida
**Como acessar:** Abra em qualquer navegador web

---

### 3. TESTES_MANUAIS_README.md
**Guia rápido de referência**
- 🎯 Início rápido (3 passos)
- 📊 Dados de teste
- 📋 Validações importantes
- 💻 Exemplos cURL
- 🔧 Requisitos
- 📚 Links para documentação completa

**Quando ler:** Quando vai fazer os testes
**Quanto leva:** 10-15 minutos

---

### 4. GUIA_TESTES_MANUAIS.md
**Documentação completa e detalhada (45 KB)**
- 📖 Visão geral completa
- 🔧 Configuração do ambiente
- 📊 Estrutura de testes (4 grupos)
- 📝 Dados de teste (valores exatos)
- 🎬 Fluxo de testes passo-a-passo
- ✔️ Testes de validação (12 cenários com exemplos JSON)
- 📊 Testes de endpoints complexos (8 relatórios)
- 🐛 Troubleshooting (10+ problemas comuns)
- 📚 Referências úteis

**Quando ler:** Para aprender todos os detalhes
**Quanto leva:** 30-45 minutos
**Recomendado:** Antes de executar os testes

---

### 5. GUIA_IMPORTACAO_COLLECTIONS.md
**Instruções passo-a-passo para importar**
- 📮 Como importar no Postman (4 passos)
- 🎯 Como importar no Bruno (3 passos)
- 🔧 Configurar variáveis de ambiente
- ✅ Como executar os testes
- 🎯 Fluxo recomendado
- 💡 Dicas para Postman e Bruno
- 🐛 Troubleshooting de importação

**Quando ler:** Quando for importar a collection
**Quanto leva:** 5-10 minutos

---

### 6. curl-examples.sh
**Script com exemplos de testes via cURL**
- 🖥️ Validação de ambiente
- 🔍 Exemplos de criação de dados
- 📊 Exemplos de relatórios
- 💻 Pronto para copiar e colar

**Como usar:** 
```bash
# Torne executável
chmod +x curl-examples.sh

# Execute
./curl-examples.sh
```

**Quando usar:** Para testes diretos do terminal ou automação

---

## 🚀 Fluxo de Uso Recomendado

### Primeira Vez (Completo)
```
1. Leia RESUMO_EXECUTIVO.md (5 min)
        ↓
2. Visualize TESTES_MANUAIS.html (10 min)
        ↓
3. Leia GUIA_TESTES_MANUAIS.md (30 min)
        ↓
4. Siga GUIA_IMPORTACAO_COLLECTIONS.md (10 min)
        ↓
5. Execute os testes (45 min)
```

**Tempo total:** ~2 horas

### Segunda Vez (Rápido)
```
1. Leia TESTES_MANUAIS_README.md (5 min)
        ↓
2. Execute os testes (45 min)
```

**Tempo total:** 50 minutos

### Consultando Detalhes
```
1. Problema específico?
   → Consulte GUIA_TESTES_MANUAIS.md (seção relevante)
        ↓
2. Erros de importação?
   → Consulte GUIA_IMPORTACAO_COLLECTIONS.md
        ↓
3. Exemplos cURL?
   → Consulte curl-examples.sh
```

---

## 📊 Os 32 Testes Explicados

### Grupo 1: Validação (12 testes) ❌
```
├─ 1.1: Email inválido
├─ 1.2: CPF inválido
├─ 1.3: Nome muito curto
├─ 1.4: Telefone muito curto
├─ 1.5: CNPJ inválido
├─ 1.6: Taxa entrega negativa
├─ 1.7: Preço zero
├─ 1.8: Preço negativo
├─ 1.9: Número de pedido inválido
├─ 1.10: Status pedido inválido
├─ 1.11: Quantidade zero
└─ 1.12: Pedido sem itens
```
**Status esperado:** 400/422 (devem falhar)

### Grupo 2: Criação de Dados (11 testes) ✅
```
├─ CLIENTES
│  ├─ 2.1.1: João Silva
│  ├─ 2.1.2: Maria Santos
│  └─ 2.1.3: Pedro Oliveira
├─ RESTAURANTES
│  ├─ 2.2.1: Pizza Palace
│  └─ 2.2.2: Sushi House
├─ PRODUTOS
│  ├─ 2.3.1: Pizza Margherita
│  ├─ 2.3.2: Pizza Pepperoni
│  ├─ 2.3.3: Refrigerante 2L
│  ├─ 2.3.4: Combo Sushi Premium
│  ├─ 2.3.5: Temaki Salmão
│  └─ 2.3.6: Sakê 300ml
└─ PEDIDOS
   ├─ 2.4.1: Pedido 1 (poucos itens)
   ├─ 2.4.2: Pedido 2 (muitos itens)
   └─ 2.4.3: Pedido 3 (valor alto)
```
**Status esperado:** 201 Created

### Grupo 3: Consultas (7 testes) 🔍
```
├─ 3.1: Buscar cliente por email
├─ 3.2: Buscar produtos por categoria
├─ 3.3: Listar restaurantes por ramo
├─ 3.4: Listar pedidos por status
├─ 3.5: Listar pedidos por cliente
├─ 3.6: Produtos disponíveis
└─ 3.7: Restaurantes ativos
```
**Status esperado:** 200 OK

### Grupo 4: Relatórios (8 testes) 📊
```
├─ 4.1: Vendas por restaurante
├─ 4.2: Pedidos valor > 100
├─ 4.3: Pedidos valor > 50
├─ 4.4: Relatório período/status (ENTREGUE)
├─ 4.5: Relatório período/status (PENDENTE)
├─ 4.6: Produtos mais vendidos
├─ 4.7: Ranking de clientes
└─ 4.8: Faturamento por categoria
```
**Status esperado:** 200 OK com dados agregados

---

## 🎯 Dados de Teste - Resumo Rápido

### Clientes (3)
| Nome | Email | CPF |
|------|-------|-----|
| João Silva | joao@email.com | 12345678901 |
| Maria Santos | maria@email.com | 98765432109 |
| Pedro Oliveira | pedro@email.com | 55555555555 |

### Restaurantes (2)
| Nome | Tipo | CNPJ |
|------|------|------|
| Pizza Palace | Pizzaria | 11222333000181 |
| Sushi House | Japonesa | 11444555000182 |

### Produtos (6)
| Nome | Preço | Categoria |
|------|-------|----------|
| Pizza Margherita | R$ 45,00 | Pizzas |
| Pizza Pepperoni | R$ 50,00 | Pizzas |
| Refrigerante 2L | R$ 8,00 | Bebidas |
| Combo Sushi Premium | R$ 120,00 | Combos |
| Temaki Salmão | R$ 35,00 | Temakis |
| Sakê 300ml | R$ 25,00 | Bebidas |

### Pedidos (3)
| ID | Status | Total | Itens |
|----|--------|-------|-------|
| 1 | PENDENTE | R$ 61,00 | 2 |
| 2 | ENTREGUE | R$ 282,00 | 9 |
| 3 | ENTREGUE | R$ 395,00 | 7 |

---

## ⌚ Tempo de Leitura Estimado

| Documento | Tempo | Dificuldade |
|-----------|-------|------------|
| RESUMO_EXECUTIVO.md | 5 min | ⭐ Fácil |
| TESTES_MANUAIS.html | 10 min | ⭐ Fácil |
| TESTES_MANUAIS_README.md | 15 min | ⭐⭐ Médio |
| GUIA_IMPORTACAO_COLLECTIONS.md | 10 min | ⭐⭐ Médio |
| GUIA_TESTES_MANUAIS.md | 30 min | ⭐⭐⭐ Complexo |
| curl-examples.sh | 5 min | ⭐⭐ Médio |
| **TOTAL LEITURA** | **~75 min** | |
| **TESTES EXECUÇÃO** | **~45 min** | |
| **TEMPO TOTAL** | **~2h** | |

---

## 🔗 Mapa de Navegação

```
START HERE
    ↓
    ├─→ Rápido? → RESUMO_EXECUTIVO.md (5 min)
    │             ↓
    │             ↓
    │             TESTES_MANUAIS.html (10 min visual)
    │             ↓
    │             ↓
    │             GUIA_IMPORTACAO_COLLECTIONS.md
    │             ↓
    │             ↓
    │             Execute testes (45 min)
    │
    └─→ Completo? → GUIA_TESTES_MANUAIS.md (30 min completo)
                    ↓
                    ↓
                    GUIA_IMPORTACAO_COLLECTIONS.md (10 min)
                    ↓
                    ↓
                    TESTES_MANUAIS_README.md (referência)
                    ↓
                    ↓
                    Execute testes (45 min)
```

---

## 💾 Collections Prontas

### Postman (delivery-api-postman.json)
- ✅ 32 testes em 4 grupos
- ✅ Variáveis de ambiente automáticas
- ✅ Scripts de pré-requisição e pós-teste
- ✅ Salva IDs automaticamente
- 📥 Importe direto no Postman

**Como usar:**
1. Abra Postman
2. Import → Upload Files → delivery-api-postman.json
3. Execute os grupos na ordem

### Bruno (delivery-api-bruno.bru)
- ✅ 32 testes em 4 grupos
- ✅ Formato de texto puro (.bru)
- ✅ Perfeito para Git
- ✅ Navegação intuitiva
- 📥 Importe direto no Bruno

**Como usar:**
1. Abra Bruno
2. File → Import Collection → delivery-api-bruno.bru
3. Execute os testes

---

## 🎓 Aprenda Mais

### Sobre Testes de API
- Status HTTP: 200 (OK), 201 (Created), 400 (Bad Request), 422 (Unprocessable)
- REST: GET, POST, PUT, DELETE
- JSON: Formato de dados padrão

### Sobre Delivery API
- 🏗️ Arquitetura: Spring Boot, Java 21
- 💾 Banco de dados: H2 (em memória)
- 📝 Validações: Jakarta Validation + Hibernate Validator
- 🔍 Relatórios: Agregações com JPA

### Ferramentas
- **Postman**: Cliente HTTP com interface visual
- **Bruno**: Cliente HTTP leve, open-source
- **cURL**: Comando-linha para requisições HTTP
- **H2 Console**: GUI para banco de dados (porta 8080/h2-console)

---

## 📞 Suporte

Se tiver dúvidas:

1. **Problema com importação?**
   → Consulte: GUIA_IMPORTACAO_COLLECTIONS.md

2. **Erro nos testes?**
   → Consulte: GUIA_TESTES_MANUAIS.md (seção Troubleshooting)

3. **Exemplos de teste?**
   → Consulte: curl-examples.sh ou TESTES_MANUAIS_README.md

4. **Dados específicos?**
   → Consulte: RESUMO_EXECUTIVO.md (seção Dados de Teste)

---

## ✅ Quick Checklist

- [ ] Leia o RESUMO_EXECUTIVO.md
- [ ] Visualize o TESTES_MANUAIS.html
- [ ] Leia o GUIA_IMPORTACAO_COLLECTIONS.md
- [ ] Importe a collection (Postman ou Bruno)
- [ ] Inicie a API (./mvnw spring-boot:run)
- [ ] Execute os 32 testes na ordem
- [ ] Valide os resultados conforme documentado
- [ ] Documente quaisquer problemas encontrados

---

**Versão:** 1.0 | **Data:** Novembro 2025  
**API:** Delivery Tech v1.0.0 | **Java:** 21 | **Spring Boot:** 3.4.11

🎉 **Pronto para testar? Comece por [RESUMO_EXECUTIVO.md](./RESUMO_EXECUTIVO.md)**
