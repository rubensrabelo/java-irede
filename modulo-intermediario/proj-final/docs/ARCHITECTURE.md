# Diagrama de Arquitetura do Novo Sistema (C4 Model)

Este diagrama de componentes ilustra a topologia atualizada do ecossistema do aplicativo, evidenciando o fluxo de dados reativo entre a interface grafica JavaFX e as camadas desacopladas de negocio e persistencia:

```mermaid
graph TD
    subgraph UI [Camada de Interface Grafica - JavaFX]
        Main[Main Application]
        Factory[ControllerFactory Callback]
        MVC_Main[MainViewController]
        MVC_Form[FormViewController]
    end

    subgraph Core [Camada de Negocio e Persistencia]
        Service[TransactionService]
        Repo[TransactionDbRepository]
        DAO[TransactionDAO]
    end

    subgraph Data [Camada de Infraestrutura]
        Env[.env Config]
        MySQLConfig[MySQLConfig]
        DB[(MySQL Database 26.7)]
    end

    Main -->|Registra| Factory
    Factory -->|Instancia via DI| MVC_Main
    Factory -->|Instancia via DI| MVC_Form
    MVC_Main -->|Abre Janela| MVC_Form

    Factory -->|1. Cria e Injeta| DAO
    DAO -->|2. Injeta no Construtor| Repo
    Repo -->|3. Injeta no Construtor| Service

    MVC_Main -->|Consome| Service
    MVC_Form -->|Consome| Service
    Service -->|Delega Operacoes| Repo
    Repo -->|Controla Transacoes ACID| DAO
    DAO -->|Executa PreparedStatements| MySQLConfig
    MySQLConfig -->|Le Credenciais| Env
    MySQLConfig -->|Distribui Conexoes| DB
```
