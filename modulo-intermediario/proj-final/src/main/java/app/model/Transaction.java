package app.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import app.model.enums.TransactionType;

public class Transaction {
    private Long id;
    private LocalDate date;
    private TransactionType type;
    private String category;
    private BigDecimal amount;

    public Transaction() {
    }

    public Transaction(LocalDate date, String type, String category, BigDecimal amount) {
        this.date = date;
        this.type = TransactionType.fromDescription(type);
        this.category = category;
        this.amount = amount;
    }

    public Transaction(Long id, LocalDate date, String type, String category, BigDecimal amount) {
        this.id = id;
        this.date = date;
        this.type = TransactionType.fromDescription(type);
        this.category = category;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(String type) {
        this.type = TransactionType.fromDescription(type);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transaction other = (Transaction) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Transaction [id=" + id + ", date=" + date + ", type=" + type.getDescription() + ", category=" + category + ", amount=" + amount + "]";
    }
}
