<h1 align="center">sboot-cms-api-gateway</h1>

<p align="center" style="margin-bottom: 20;">
    <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
    <img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white" alt="Springboot" />
    <img src="https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white" alt="Apache Maven" />
    <img src="https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white" alt="Postgres" /> 
    <img src="https://img.shields.io/badge/Flyway-CC0200.svg?style=for-the-badge&logo=Flyway&logoColor=white" alt="Flyway" />
    <img src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens" alt="JWT" />
</p>

<p align="center">O <b>sboot-cms-api-gateway</b> é o Gateway API do <b>Sistema de Gerenciamento de Cursos</b>, responsável por rotear requisições para os microsserviços internos e gerenciar a autenticação via tokens JWT. Este serviço é construído utilizando o Spring Cloud Gateway, permitindo a comunicação centralizada e a aplicação de filtros nas requisições.</p>

<h2>📌 Visão Geral</h2>

<h4>🔹 Principais funcionalidades</h4>

- Atua como ponto de entrada único para os microsserviços.
- Implementa autenticação baseada em JWT, validando credenciais de usuários no banco de dados.
- Redireciona requisições para os serviços apropriados (Teacher, Student e Course).
- Utiliza Eureka Client para registrar-se no Service Discovery.

<h2>🚀 Tecnologias Utilizadas</h2>

- <b>Java 21 + Spring Boot 3.4.2</b>
- <b>Spring Cloud Gateway</b> (<code>spring-cloud-starter-gateway</code>)
- <b>Spring Security</b> (<code>spring-boot-starter-security</code>)
- <b>Spring WebFlux</b> (<code>spring-boot-starter-webflux</code>)
- <b>Spring Data R2DBC</b> (<code>spring-boot-starter-data-r2dbc</code>)
- <b>PostgreSQL</b> (banco de dados exclusivo do serviço)
- <b>Java JWT (auth0)</b> (para manipulação de tokens)
- <b>Lombok</b> (redução de código boilerplate)
- <b>Eureka Client</b> (registro no Service Discovery)

<h2>🏗️ Estrutura do Projeto</h2>

```bash
sboot-cms-api-gateway
│-- src/main/java/com/portfolio/luisfmdc/sboot_cms_api_gateway
│   ├── controller/      # Camada de controle (endpoints REST)
│   ├── domain/          # Classes de domínio e DTOs
│   ├── infra/
│   │   ├── security/    # Configuração de autenticação e JWT
│   │   ├── exception/   # Tratamento personalizado de exceções
│   ├── repository/      # Repositório de usuários
│   ├── service/         # Regras de negócio (autenticação, geração de tokens)
│   ├── SbootApiGatewayApplication.java  # Classe principal
│-- src/main/resources
│   ├── application-local.yml  # Configuração do gateway e banco de dados
│-- pom.xml  # Dependências do projeto
```

<h2>🛠️ Configuração e Execução</h2>

<h3>📌 Pré-requisitos</h3>
<p>Antes de iniciar o serviço, é necessário:</p>

1. Ter o <b>PostgreSQL</b> instalado e configurado.
2. Criar manualmente o banco de dados <code>dbStudent</code> e tabela <code>DbUser</code>.
3. Ter Java 21 e Maven instalados.

<h3>📜 Configuração (<code>application-local.yml</code>)</h3>

```yml
server:
  port: 8081

spring:
  application:
    name: sboot-gateway-cms

  r2dbc:
    url: r2dbc:postgresql://localhost:5432/dbUser
    username: seu_usuario
    password: sua_senha

  cloud:
    gateway:
      routes:
        - id: teacher-service
          uri: lb://sboot-cms-teacher-ms
          predicates:
            - Path=/professor/**
          filters:
            - RewritePath=/professor/(?<segment>.*), /professor/${segment}

        - id: student-service
          uri: lb://sboot-cms-student-ms
          predicates:
            - Path=/aluno/**
          filters:
            - RewritePath=/aluno/(?<segment>.*), /aluno/${segment}

        - id: course-service
          uri: lb://sboot-cms-course-ms
          predicates:
            - Path=/curso/**
          filters:
            - RewritePath=/curso/(?<segment>.*), /curso/${segment}

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8082/eureka
```

<h4>📌 Observações</h4>

- O banco de dados PostgreSQL utilizado para autenticação precisa ser criado manualmente.
- O Spring WebFlux é utilizado, tornando o projeto reativo, e por isso Flyway não foi integrado.

<h4>🛠️ Configuração do Banco de Dados</h4>

<p>Como o Spring Data R2DBC é utilizado, o banco de dados precisa ser criado manualmente antes da primeira execução.</p>

```sql
CREATE DATABASE dbUser;

CREATE TABLE users (
   id INT GENERATED ALWAYS AS IDENTITY,
   email VARCHAR(75) UNIQUE NOT NULL,
   password VARCHAR(75) NOT NULL,
   role VARCHAR(30) NOT NULL,
   PRIMARY KEY (id)
);
```

<h4>🔑 Criando um Usuário de Teste com Role ADMIN</h4>

```sql
INSERT INTO users (email, password, role)
VALUES ('admin@teste.com', '$2a$06$PnQ14fZc/w5PLDfRLxts8O5bBzdRQQ/zPVCYzhDgn3Q7OzZh9YfT2', 'ADMIN');
```

<p>Esse usuário não pode ser criado via API, pois a role ADMIN não está permitida no endpoint de registro.</p>

<h2>🚀 Executando o Serviço</h2>

1. Clone o repositório e navegue até o diretório <code>sboot-cms-api-gateway</code>:

```sh
git clone https://github.com/luisfmaiadc/sboot-cms-api-gateway.git
cd sboot-cms-api-gateway
```

2. Compile e execute o projeto com:

```sh
mvn clean install
mvn spring-boot:run
```

3. O serviço estará disponível na porta 8081 conforme configurada em <code>application-local.yml</code>.

<h2>📡 Endpoints Principais</h2>

| Método | Endpoint | Descrição |
|     :---     |     :---      |      :---     |
| POST         | <code>/auth/login</code>     | Fazer login com usuário cadastrado    |
| POST         | <code>/auth/register</code>  | Cadastrar um novo usuário             |

<h2>🔒 Autenticação</h2>
<p>Conforme informado acima, o API Gateway possui dois endpoints principais para autenticação de usuários:</p>

<h3>1️⃣ Login (<code>auth/login</code>)</h3>

- <b>Método:</b> POST
- <b>Descrição:</b> Autentica um usuário com e-mail e senha, retornando um token JWT válido.
- <b>Exemplo de Requisição:</b>

```json
{
  "email": "admin@teste.com",
  "password": "123456"
}
```

- <b>Resposta:</b>

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

<h3>2️⃣ Registro (<code>auth/register</code>)</h3>

- <b>Método:</b> POST
- <b>Descrição:</b> Registra um novo usuário no sistema com a role STUDENT ou TEACHER (ADMIN não permitido).
- <b>Senha armazenada com hash Bcrypt.</b>
- <b>Exemplo de Requisição:</b>

```json
{
  "email": "user@teste.com",
  "password": "123456",
  "role": "STUDENT"
}
```

<p>Após autenticado, o usuário deve incluir o token JWT no cabeçalho Authorization para acessar os microsserviços internos.</p>

<h2>📚 Mais Informações</h2>
<p>Para uma visão completa do sistema, incluindo a arquitetura, os demais microsserviços e detalhes sobre o desenvolvimento, acesse o README principal do projeto:</p>

➡️ [Visão Geral do Sistema](https://github.com/luisfmaiadc/pom-base-course-management-system) 