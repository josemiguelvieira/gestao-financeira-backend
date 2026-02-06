---

💼 Gestão Financeira — Backend (API)

<p align="center">
API REST para gerenciamento financeiro com autenticação segura, controle de receitas e despesas, organização por categorias e geração de dashboards e relatórios mensais.
</p><p align="center">
<strong>Status:</strong> MVP funcional finalizado<br/>
<strong>Frontend:</strong> <a href="https://github.com/josemiguelvieira/gestao-financeira-frontend">Repositório do frontend</a>
</p>
---

🏗️ Arquitetura

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



---

🧰 Tecnologias

Java 17

Spring Boot

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



---

🗄️ Modelo de Dados

📌 Entidades principais

👤 Usuario

id

nome

email (único)

senha (BCrypt)

createdAt

updatedAt


🗂️ Categoria

id

nome

usuário (ManyToOne)

restrição de unicidade por usuário


💰 Receita

id

valor

descrição

categoria (string)

data

usuário (ManyToOne)


💸 Despesa

id

valor

descrição

data

formaPagamento

observacao

categoria (ManyToOne)

usuário (ManyToOne)


🔐 PasswordResetCode

id

email

codeHash

expiresAt

used

attempts

createdAt



---

🔐 Autenticação e Segurança

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


CORS configurado para frontend Vite (http://localhost:5173)



---

🔌 Endpoints da API

🔑 Autenticação

POST /auth/register
POST /auth/login
POST /auth/forgot-password
POST /auth/reset-password

👤 Usuário

GET    /users/me
PUT    /users/me
PUT    /users/me/password
DELETE /users/me

🗂️ Categorias

GET    /categorias
POST   /categorias
PUT    /categorias/{id}
DELETE /categorias/{id}

💰 Receitas

GET    /receitas
POST   /receitas
PUT    /receitas/{id}
DELETE /receitas/{id}

💸 Despesas

GET    /despesas
GET    /despesas/periodo
POST   /despesas
PUT    /despesas/{id}
DELETE /despesas/{id}

📊 Dashboard e Relatórios

GET /dashboard
GET /dashboard/despesas-por-categoria
GET /relatorios/resumo
GET /relatorios/despesas-por-categoria


---

📏 Regras de Negócio

Todas as operações são isoladas por usuário

Receitas e despesas pertencem exclusivamente ao usuário autenticado

Categorias não podem ser duplicadas por usuário

Valores monetários devem ser maiores que zero

Dashboard calcula:

total de receitas

total de despesas

saldo do mês


Relatórios utilizam filtros por mês e ano

Token inválido ou expirado invalida a sessão automaticamente



---

⚙️ Como rodar o projeto localmente

🔧 Pré-requisitos

Java 17+

Maven

PostgreSQL

SMTP configurado (Gmail ou similar)



---

📄 Configuração

O projeto utiliza Spring Profiles.

Crie o arquivo:

application-local.properties

a partir do exemplo:

copy application-local.example.properties application-local.properties

Preencha com suas configurações locais de:

banco de dados

JWT

SMTP



---

▶️ Execução

mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local

A API estará disponível em:

http://localhost:8080


---

🧪 Testes

Testes manuais via Postman

Testes de autenticação, fluxo JWT e regras de negócio

Dependências preparadas para testes com:

spring-boot-starter-test

spring-security-test




---

🗺️ Roadmap

[x] Deploy no Render (experimento)

[ ] Health check com Spring Actuator

[ ] Melhorar logs e mensagens de erro

[ ] Testes automatizados (JUnit)



---

📄 Licença

Este projeto está sob licença MIT.


---

👨‍💻 Autor

José Miguel Vieira

GitHub:
https://github.com/josemiguelvieira

LinkedIn:
https://www.linkedin.com/in/jos%C3%A9-miguel-vieira-732650349/


---
