# Organização dos Arquivos e Diretórios

O projeto segue a estrutura padrão do Apache Maven integrada ao sistema de módulos do Java (JPMS). A arquitetura foi refatorada para adotar um padrão baseado em camadas limpas, isolando componentes de infraestrutura, utilitários globais e regras de negócio.

```bash
.
├── docker
│   ├── docker-compose.yml
│   └── migrations
│       ├── 01_schema.sql
│       └── 02_seed.sql
├── docs
│   ├── ARCHITECTURE.md
│   ├── DATA_MODEL.md
│   ├── img
│   │   ├── form-screen.png
│   │   └── main-screen.png
│   ├── PATTERNS.md
│   ├── SCREENS.md
│   ├── SCRIPTS.md
│   └── STRUCTURE.md
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── java
    │   │   ├── app
    │   │   │   ├── application
    │   │   │   │   ├── controller
    │   │   │   │   │   ├── FormViewController.java
    │   │   │   │   │   └── MainViewController.java
    │   │   │   │   └── service
    │   │   │   │       └── TransactionService.java
    │   │   │   ├── config
    │   │   │   │   └── MySQLConfig.java
    │   │   │   ├── domain
    │   │   │   │   ├── enums
    │   │   │   │   │   └── TransactionType.java
    │   │   │   │   ├── MonthlyTransaction.java
    │   │   │   │   └── Transaction.java
    │   │   │   ├── Main.java
    │   │   │   ├── repository
    │   │   │   │   ├── GenericRepository.java
    │   │   │   │   ├── TransactionDAO.java
    │   │   │   │   └── TransactionDbRepository.java
    │   │   │   └── shared
    │   │   │       ├── exceptions
    │   │   │       │   ├── DatabaseException.java
    │   │   │       │   ├── EntityNotFoundException.java
    │   │   │       │   ├── FactoryInstantiationException.java
    │   │   │       │   ├── InitializationException.java
    │   │   │       │   ├── InvalidInputException.java
    │   │   │       │   ├── TransactionPersistenceException.java
    │   │   │       │   └── VisualRenderingException.java
    │   │   │       ├── factory
    │   │   │       │   └── ControllerFactory.java
    │   │   │       └── utils
    │   │   │           └── Formatter.java
    │   │   └── module-info.java
    │   └── resources
    │       └── app
    │           └── view
    │               ├── css
    │               │   ├── form-style.css
    │               │   └── main-style.css
    │               ├── form-view.fxml
    │               └── main-view.fxml
    └── test
        └── java
            └── app
                ├── repository
                │   └── TransactionDAOIntegrationTest.java
                └── service
                    └── TransactionServiceTest.java
```

## Descrição dos Módulos e Camadas

### 1. Raiz do Projeto (`/`)
* **`docker/`**: Infraestrutura conteinerizada. Contém o ambiente do banco de dados MySQL e os scripts SQL automáticos de migração (`schema`) e dados iniciais (`seed`).
* **`docs/`**: Documentação técnica do projeto, contendo diagramas, guias de padrões de código adotados, visões de telas e o modelo de dados.

### 2. Código Fonte (`src/main/java/app/`)
* **`Main.java`**: Ponto de entrada (Bootstrap) da aplicação JavaFX.
* **`module-info.java`**: Configuração do Java Platform Module System (JPMS), gerenciando estritamente a visibilidade e encapsulamento dos pacotes para o JavaFX.
* **`application/`**: Camada que gerencia o fluxo operacional e de controle do app.
  * **`controller/`**: Controladores de interface visual JavaFX que tratam as interações dos arquivos FXML.
  * **`service/`**: Orquestradores de fluxo e intermediadores entre os controllers e a camada de dados. *(Nota: Próxima expansão incluirá pacotes de `dto` e `mapper` nesta seção para isolamento completo do Domínio).*
* **`config/`**: Classes de configuração do ecossistema, como definições de conexões e credenciais de infraestrutura (MySQL).
* **`domain/`**: O coração do software (antigo pacote `model`). Contém as entidades de negócio puras, modelos financeiros e enums. É isolada de frameworks ou acessos externos diretos.
* **`repository/`**: Camada de persistência de dados. Implementa padrões de acesso como DAO (Data Access Object) e Repositories estruturados para comunicação direta com a base SQL.
* **`shared/`**: Recursos globais e transversais reutilizados por múltiplas camadas da aplicação.
  * **`exceptions/`**: Centralização de todas as exceções personalizadas de negócio e infraestrutura do sistema.
  * **`factory/`**: Fábricas de objetos responsáveis pela inversão de controle e instanciação de controladores.
  * **`utils/`**: Formatadores e funções auxiliares genéricas compartilhadas.

### 3. Recursos Visuais (`src/main/resources/app/view/`)
* Mantém simetria estrita com a camada de apresentação do código java. Armazena as views estruturadas em arquivos `.fxml` e as folhas de estilo em diretórios `.css` isolados.

### 4. Testes (`src/test/`)
* **`repository/` & `service/`**: Testes automatizados (unitários e de integração) isolados e espelhados conforme a árvore de pacotes do sistema principal.
