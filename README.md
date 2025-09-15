# Game Management System

Um sistema de gerenciamento de jogos desenvolvido em Java com arquitetura Domain-Driven Design (DDD), utilizando Swing para interface gráfica e PostgreSQL como banco de dados.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Configuração](#instalação-e-configuração)
- [Como Executar](#como-executar)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Banco de Dados](#banco-de-dados)
- [Contribuição](#contribuição)

## 🎮 Sobre o Projeto

O Game Management System é uma aplicação desktop desenvolvida para gerenciar uma biblioteca de jogos. O sistema permite aos usuários organizar, visualizar e gerenciar informações sobre seus jogos, incluindo funcionalidades como biblioteca de jogos, favoritos e controle de jogos finalizados.

### Funcionalidades Principais

- 📚 **Biblioteca de Jogos**: Visualize e gerencie sua coleção de jogos
- ⭐ **Favoritos**: Marque seus jogos favoritos para acesso rápido
- ✅ **Jogos Finalizados**: Controle quais jogos você já completou
- 🎨 **Interface Moderna**: Interface gráfica com tema escuro usando FlatLaf

## 🛠 Tecnologias Utilizadas

- **Java 17/21**: Linguagem de programação principal
- **Maven**: Gerenciamento de dependências e build
- **Swing**: Framework para interface gráfica
- **FlatLaf**: Look and Feel moderno para Swing
- **PostgreSQL**: Sistema de gerenciamento de banco de dados
- **HikariCP**: Pool de conexões de banco de dados
- **Docker & Docker Compose**: Containerização do banco de dados
- **JUnit 5**: Framework de testes

## 🏗 Arquitetura

O projeto segue os princípios do Domain-Driven Design (DDD) e está organizado em módulos:

```
├── core/           # Módulo principal com lógica de negócio
├── app/            # Módulo da aplicação com interface gráfica
└── docker-compose.yml # Configuração do banco de dados
```

## 📋 Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- **Java JDK 17 ou superior**
- **Maven 3.6 ou superior**
- **Docker e Docker Compose**
- **Git** (para clonar o repositório)

### Verificando as instalações

```bash
java --version
mvn --version
docker --version
docker-compose --version
```

## 🚀 Instalação e Configuração

### 1. Clone o repositório

```bash
git clone <url-do-repositorio>
cd cp4-domain-driven-design
```

### 2. Inicie o banco de dados

O projeto utiliza PostgreSQL executando em Docker. Para iniciar o banco de dados:

```bash
docker-compose up -d postgres
```

Isso irá:
- Criar um container PostgreSQL na porta `5432`
- Configurar o banco de dados `games_db`
- Usuário: `postgres`
- Senha: `postgres123`

### 3. (Opcional) Inicie o PgAdmin

Para gerenciar o banco de dados através de uma interface web:

```bash
docker-compose --profile admin up -d pgadmin
```

Acesse: http://localhost:8080
- Email: `admin@example.com`
- Senha: `admin123`

### 4. Compile o projeto

```bash
mvn clean compile
```

## ▶️ Como Executar

### Executar a aplicação

```bash
mvn exec:java -pl app -Dexec.mainClass="br.com.fiap.cp4.app.Main"
```

### Executar testes

```bash
mvn test
```

### Gerar JAR executável

```bash
mvn clean package
java -jar app/target/app-1.0.0.jar
```

## 📁 Estrutura do Projeto

```
cp4-domain-driven-design/
├── app/                                    # Módulo da aplicação
│   └── src/main/java/br/com/fiap/cp4/app/
│       ├── Main.java                       # Classe principal
│       ├── controller/                     # Controladores
│       └── view/                          # Interface gráfica
│           ├── frames/                    # Janelas principais
│           ├── pages/                     # Páginas da aplicação
│           └── components/                # Componentes reutilizáveis
├── core/                                  # Módulo principal
│   └── src/main/java/br/com/fiap/cp4/core/
│       ├── domain/                        # Entidades de domínio
│       ├── usecase/                       # Casos de uso
│       ├── repository/                    # Interfaces de repositório
│       ├── infrastructure/                # Implementações de infraestrutura
│       └── database/                      # Configurações de banco
├── src/main/resources/sql/                # Scripts SQL de inicialização
├── docker-compose.yml                     # Configuração do Docker
└── pom.xml                               # Configuração Maven principal
```

## 🗄️ Banco de Dados

### Configuração

O banco de dados PostgreSQL é configurado automaticamente via Docker Compose com as seguintes especificações:

- **Host**: localhost
- **Porta**: 5432
- **Database**: games_db
- **Usuário**: postgres
- **Senha**: postgres123

### Comandos úteis do Docker

```bash
# Iniciar apenas o PostgreSQL
docker-compose up -d postgres

# Iniciar PostgreSQL + PgAdmin
docker-compose --profile admin up -d

# Parar os serviços
docker-compose down

# Ver logs do banco
docker-compose logs postgres

# Acessar o container do PostgreSQL
docker exec -it games_db_postgres psql -U postgres -d games_db
```

### Scripts de Inicialização

Os scripts SQL de inicialização devem ser colocados em `src/main/resources/sql/` e serão executados automaticamente quando o container for criado pela primeira vez.

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Notas Adicionais

- O projeto utiliza o padrão Domain-Driven Design (DDD)
- A interface gráfica usa o tema escuro FlatLaf por padrão
- O pool de conexões é gerenciado pelo HikariCP para melhor performance
- Os dados são persistidos automaticamente no volume Docker `postgres_data`

## 🐛 Solução de Problemas

### Erro de conexão com banco de dados
- Verifique se o Docker está rodando: `docker ps`
- Reinicie o container: `docker-compose restart postgres`

### Porta 5432 já está em uso
- Pare outros serviços PostgreSQL ou altere a porta no `docker-compose.yml`

### Problemas de permissão no Docker
- No Linux/Mac, execute com `sudo` se necessário
- No Windows, certifique-se de que o Docker Desktop está rodando

---

**Desenvolvido com ❤️ para FIAP - Checkpoint 4**
