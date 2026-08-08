# Guia de Infraestrutura e Execucao

## Variaveis de Ambiente (.env)

O projeto utiliza o isolamento de variaveis de ambiente para garantir a seguranca das credenciais de acesso ao banco de dados. Certifique-se de configurar os arquivos nas seguintes localizacoes:

### 1. Raiz do projeto (proj-final/.env)
Arquivo consumido pela aplicacao Java para estabelecer a conexao JDBC:

```bash
DB_URL=jdbc:mysql://localhost:3306/fintrack_db?useUnicode=true&characterEncoding=UTF-8
DB_USER=rubens_dev
DB_PASSWORD=suasenhajava
```

### 2. Pasta de infraestrutura (proj-final/docker/.env)
Arquivo consumido pelo orquestrador do Docker para provisionar o container:

```bash
MYSQL_VERSION=26.7
MYSQL_ROOT_PASSWORD=suasenharoot
MYSQL_DATABASE=fintrack_db
MYSQL_USER=rubens_dev
MYSQL_PASSWORD=suasenhajava
MYSQL_PORT=3306
```

---

## Comandos para Execucao do Projeto

Todos os comandos listados devem ser executados a partir do terminal na pasta raiz do projeto (proj-final):

### 1. Inicializar o Banco de Dados (Docker)
Este comando realiza o download da imagem oficial do MySQL 26.7, cria o container e executa de forma automatica os scripts de migracao e carga inicial (01_schema.sql e 02_seed.sql), inserindo 15 registros de teste:

```bash
docker compose -f docker/docker-compose.yml up -d
```

### 2. Executar as Suites de Testes Automatizados (JUnit 5)
Para rodar os testes unitarios da camada de servico e os testes de integracao relacional utilizando SQLite em memoria:

```bash
mvn test
```

### 3. Compilar e Iniciar a Interface Grafica (JavaFX)
Comando responsavel por limpar compilacoes anteriores, resolver as dependencias do Module Path e inicializar o sistema integrado ao MySQL:

```bash
mvn clean javafx:run
```

---

## Gerenciamento da Infraestrutura

### Verificar o status do container

```bash
docker container ls

```
### Desligar o banco de dados mantendo os volumes salvos

```bash
docker compose -f docker/docker-compose.yml down
```

### Reiniciar o banco de dados limpando volumes e dados antigos

```bash
docker compose -f docker/docker-compose.yml down -v
```