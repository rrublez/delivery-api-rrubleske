# Delivery Tech API

Sistema de delivery desenvolvido com Spring Boot e Java 21.

## Tecnologias
- **Java 21 LTS** (versão mais recente)
- Spring Boot 3.4.11
- Spring Web
- Spring Data JPA
- H2 Database
- Maven
- Logback (Logging)

## Recursos Modernos Utilizados
- Records (Java 14+)
- Text Blocks (Java 15+)
- Pattern Matching (Java 17+)
- Virtual Threads (Java 21)

## Como executar
1. **Pré-requisitos:** JDK 21 instalado
2. Clone o repositório
```bash
git clone https://github.com/rrublez/delivery-api-rrubleske.git
cd delivery-api-rrubleske
```
3. Execute: 
```bash
./mvnw spring-boot:run
```
4. Acesse: http://localhost:8080/health

## Endpoints

### Endpoints Principais
- **GET /health** - Status da aplicação (inclui versão Java e status do serviço)
- **GET /info** - Informações da aplicação e desenvolvedor
- **GET /h2-console** - Console do banco H2 (http://localhost:8080/h2-console)

### Endpoints de Demonstração (new Java Features)
- **GET /demo/pattern-matching?type=string&value=hello** - Demonstra Pattern Matching (Java 17+)
  - Parâmetros: `type` (string/int/double), `value` (valor a converter)
  - Exemplo: `?type=int&value=-42`
  
- **GET /demo/virtual-threads?tasks=5** - Demonstra Virtual Threads (Java 21)
  - Parâmetro: `tasks` (número de threads virtuais a criar)
  - Executa múltiplas tarefas em paralelo usando threads leves
  
- **GET /demo/threads-comparison?tasks=10** - Compara Virtual Threads vs Platform Threads
  - Parâmetro: `tasks` (número de tarefas a executar)
  - Mostra a diferença de performance entre os dois tipos

## Configuração
- **Porta:** 8080
- **Banco:** H2 em memória
- **Profile:** development
- **Logs:** Salvos em `logs/app.log` com rotação automática

## Logging
A aplicação utiliza **Logback** para gerenciamento de logs:
- Logs no console (coloridos)
- Logs em arquivo com rotação (1MB por arquivo, máximo 2 dias)
- Logs detalhados de requisições HTTP (método, URL, status, tempo de execução)
- Logs de SQL (DEBUG)

## Desenvolvedor
**Rafael Rubleske** - Análise e Desenvolvimento de Sistemas - UniRitter

Desenvolvido com **JDK 21** e **Spring Boot 3.4.11**

