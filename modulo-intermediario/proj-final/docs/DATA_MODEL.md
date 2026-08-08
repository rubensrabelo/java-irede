# Modelagem de Dados do Banco de Dados (MySQL)

Este diagrama representa a estrutura de classes que mapeia diretamente a tabela transactions persistida no banco de dados MySQL, com suporte ao identificador unico por chave primaria e heranca via Tabela Unica (Single Table):

```mermaid
classDiagram

class Transaction {
    - Long id
    - LocalDate date
    - TransactionType type
    - String category
    - BigDecimal amount
    + Transaction()
    + Transaction(LocalDate date, String type, String category, BigDecimal amount)
    + Transaction(Long id, LocalDate date, String type, String category, BigDecimal amount)
    + Long getId()
    + void setId(Long id)
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
    + MonthlyTransaction(LocalDate date, String type, String category, BigDecimal amount, YearMonth monthYear)
    + MonthlyTransaction(Long id, LocalDate date, String type, String category, BigDecimal amount, YearMonth monthYear)
    + YearMonth getMonthYear()
    + void setMonthYear(YearMonth monthYear)
    + String toString()
}

class TransactionType {
    <<enumeration>>
    INCOME
    OUTCOME
    + String getDescription()
    + TransactionType fromDescription(String description)
}

MonthlyTransaction --|> Transaction
Transaction --> TransactionType : uses
```
