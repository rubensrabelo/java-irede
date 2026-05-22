# Projeto: FinTrack

## Descrição do Projeto (Versão Inicial)
O FinTrack é um sistema de controle de ﬁnanças pessoais via console, permitindo ao usuário:

* Cadastrar entradas (receitas) e saídas (despesas)
* Listar todas as transações
* Exibir saldo atual
* Remover uma transação

O projeto será feito com Java puro, com foco na prática de POO e estruturas básicas, e poderá ser expandido futuramente com relatórios gráﬁcos, persistência em banco de dados e integração com APIs.

## Diagrama de Classes

```mermaid
classDiagram

class Transaction {
    - LocalDate date
    - TransactionType type
    - String category
    - BigDecimal amount

    + Transaction()
    + Transaction(LocalDate date, String type, String category, BigDecimal amount)

    + LocalDate getDate()
    + void setDate(LocalDate date)

    + TransactionType getType()
    + void setType(String type)

    + String getCategory()
    + void setCategory(String category)

    + BigDecimal getAmount()
    + void setAmount(BigDecimal amount)

    + int hashCode()
    + boolean equals(Object obj)

    + String toString()
}

class MonthlyTransaction {
    - YearMonth monthYear

    + MonthlyTransaction()

    + MonthlyTransaction(
        LocalDate date,
        String type,
        String category,
        BigDecimal amount,
        YearMonth monthYear
      )

    + YearMonth getMonthYear()
    + void setMonthYear(YearMonth monthYear)

    + int hashCode()
    + boolean equals(Object obj)

    + String toString()
}

class FinTracker {
    - ArrayList~Transaction~ transactions

    + void addTransaction(Transaction transaction)
    + void listTransactions()
    + void removeTransaction(int index)
    + BigDecimal calculateTotalBalance()
}

class InvalidInputException {
    + InvalidInputException(String message)
}

class FormatterUtil {
    + String formatCurrency(BigDecimal amount)
    + String formatDate(LocalDate date)
}

class Main {
    + main(String[] args)
}

class TransactionType {
    <<enumeration>>

    INCOME
    EXPENSE

    + String getDescription()
    + TransactionType fromDescription(String description)
}

MonthlyTransaction --|> Transaction
FinTracker --> Transaction : manages
Transaction --> TransactionType : uses
Main --> FinTracker : starts
FinTracker ..> InvalidInputException : throws
FinTracker ..> FormatterUtil : uses
```

## Organização dos Arquivos

O projeto possui a seguinte estrutura de diretórios e arquivos:

```bash
├── app
│   ├── controller
│   │   ├── FinTracker.class
│   │   └── FinTracker.java
│   ├── exceptions
│   │   ├── InvalidInputException.class
│   │   └── InvalidInputException.java
│   ├── Main.class
│   ├── Main.java
│   ├── model
│   │   ├── MonthlyTransaction.class
│   │   ├── MonthlyTransaction.java
│   │   ├── Transaction.class
│   │   └── Transaction.java
│   └── utils
│       ├── Formatter.class
│       └── Formatter.java
├── docs
│   ├── SCRIPTS.md
│   └── TEST_SCRIPT.md
└── README.md
```

## Comandos para Execução

[Link para os comandos de execução](./docs/SCRIPTS.md)

## Roteiro Prático de Teste

[Link para os testes](./docs/TEST_SCRIPT.md)
