# Gestão Financeira — Backend (API)

API REST responsável pelo gerenciamento financeiro, incluindo autenticação de usuários, controle de receitas e despesas, organização por categorias e geração de relatórios e dashboards mensais.


> Status: Backend finalizado (MVP funcional) 
> Frontend:  



## 🏗️ Arquitetura

API REST baseada em Spring Boot
Arquitetura em camadas:
Controller → Service → Repository
Uso extensivo de DTOs para entrada e saída de dados
Separação clara entre:
regras de negócio
acesso a dados
autenticação e segurança
Autenticação stateless via JWT
Persistência com JPA / Hibernate


## 🧰 Tecnologias

Java 17
Spring Boot 4
Spring Web
Spring Data JPA
Spring Security
Bean Validation (Jakarta Validation)
JWT (JJWT)
PostgreSQL
Hibernate
Maven
Java Mail Sender (SMTP)
Postman (testes de endpoints)


## 🗄️ Modelo de Dados


### Entidades principais

#### Usuario
id
nome
email (único)
senha (BCrypt)
createdAt
updatedAt

#### Categoria
id
nome
usuário (ManyToOne)
restrição de unicidade por usuário

#### Receita
id
valor
descrição
categoria (string)
data
usuário (ManyToOne)

#### Despesa
id
valor
descrição
data
formaPagamento
observacao
categoria (ManyToOne)
usuário (ManyToOne)  

#### PasswordResetCode
id
email
codeHash
expiresAt
used
attempts
createdAt


## 🔐 Autenticação e Segurança

Autenticação via JWT
Tokens assinados com HS256
Senhas criptografadas com BCrypt
Filtro JWT customizado (JwtAuthFilter)
Controle de rotas públicas e privadas via SecurityFilterChain
Sessão STATELESS
Recuperação de senha com:
código temporário
hash do código
expiração
controle de tentativas
CORS configurado para frontend Vite (localhost:5173)


## 🔌 Endpoints da API

### Autenticação
POST /auth/register
POST /auth/login
POST /auth/forgot-password
POST /auth/reset-password


### Usuário
GET /users/me
PUT /users/me
PUT /users/me/password
DELETE /users/me


### Categorias
GET /categorias
POST /categorias
PUT /categorias/{id}
DELETE /categorias/{id}
 

### Receitas
GET /receitas
POST /receitas
PUT /receitas/{id}
DELETE /receitas/{id}

### Despesas
GET /despesas
GET /despesas/periodo
POST /despesas
PUT /despesas/{id}
DELETE /despesas/{id}


### Dashboard / Relatórios
GET /dashboard
GET /dashboard/despesas-por-categoria
GET /relatorios/resumo
GET /relatorios/despesas-por-categoria


## 📏 Regras de Negócio

Todas as operações são isoladas por usuário
Receitas e despesas pertencem exclusivamente ao usuário autenticado
Categorias não podem ser duplicadas por usuário
Valores monetários devem ser maiores que zero
Dashboard calcula:
total de receitas
total de despesas
saldo do mês
Relatórios usam filtros por mês e ano
Token inválido ou expirado invalida a sessão automaticamente


## 🚀 Como rodar o projeto

### Pré-requisitos
Java 17+
Maven
PostgreSQL
SMTP configurado (Gmail ou similar)


### Instalação
```bash
mvn clean install
mvn spring-boot:run

A API sobe em:
http://localhost:8080

Configuração

Configurar application.properties:
datasource (PostgreSQL)
JWT secret (app.jwt.secret)
SMTP (spring.mail.*)


Testes

Testes manuais via Postman
Testes de autenticação, fluxo JWT e regras de negócio
Dependências preparadas para testes com:
spring-boot-starter-test
spring-security-test


Roadmap

[X] Deploy no Render 
[ ] Health check com Spring Actuator
[ ] Melhorar logs e mensagens de erro
[ ] Testes automatizados (JUnit)



Licença

Este projeto está sob licença MIT.

Autor

José Miguel Vieira

GitHub: https://github.com/josemiguelvieira  
LinkedIn: https://www.linkedin.com/in/jos%C3%A9-miguel-vieira-732650349/