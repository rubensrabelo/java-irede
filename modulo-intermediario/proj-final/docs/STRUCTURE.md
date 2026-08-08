# Organizacao dos Arquivos e Diretorios

O projeto segue a estrutura padrao do Apache Maven integrada ao sistema de modulos do Java (JPMS):

```bash
├── docker
│   ├── docker-compose.yml
│   └── migrations
│       ├── 01_schema.sql
│       └── 02_seed.sql
├── docs
│   ├── DIAGRAMS.md
│   ├── SCRIPTS.md
│   └── STRUCTURE.md
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── java
    │   │   ├── app
    │   │   │   ├── config
    │   │   │   │   └── MySQLConfig.java
    │   │   │   ├── controller
    │   │   │   │   ├── FormViewController.java
    │   │   │   │   └── MainViewController.java
    │   │   │   ├── exceptions
    │   │   │   │   ├── DatabaseException.java
    │   │   │   │   ├── EntityNotFoundException.java
    │   │   │   │   ├── FactoryInstantiationException.java
    │   │   │   │   ├── InitializationException.java
    │   │   │   │   ├── InvalidInputException.java
    │   │   │   │   ├── TransactionPersistenceException.java
    │   │   │   │   └── VisualRenderingException.java
    │   │   │   ├── factory
    │   │   │   │   └── ControllerFactory.java
    │   │   │   ├── Main.java
    │   │   │   ├── model
    │   │   │   │   ├── enums
    │   │   │   │   │   └── TransactionType.java
    │   │   │   │   ├── MonthlyTransaction.java
    │   │   │   │   └── Transaction.java
    │   │   │   ├── repository
    │   │   │   │   ├── GenericRepository.java
    │   │   │   │   ├── TransactionDAO.java
    │   │   │   │   └── TransactionDbRepository.java
    │   │   │   ├── service
    │   │   │   │   └── TransactionService.java
    │   │   │   └── utils
    │   │   │       └── Formatter.java
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
