package app.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import app.model.Transaction;
import app.model.enums.TransactionType;

public class FinTracker {
    private final List<Transaction> transactions;

    public FinTracker() {
        this.transactions = new ArrayList<>();
    }

    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) {
            return false;
        }
        return this.transactions.add(transaction);
    }

    public List<Transaction> listTransactions() {
        return new ArrayList<>(this.transactions);
    }

    public boolean removeTransaction(Transaction transaction) {
        if (transaction == null) {
            return false;
        }
        return this.transactions.remove(transaction);
    }

    public BigDecimal calculateTotalBalance() {
        BigDecimal balance = BigDecimal.ZERO;

        for (Transaction t : this.transactions) {
            if (t.getAmount() != null && t.getType() != null) {
                if (t.getType() == TransactionType.INCOME) {
                    balance = balance.add(t.getAmount());
                } else if (t.getType() == TransactionType.OUTCOME) {
                    balance = balance.subtract(t.getAmount());
                }
            }
        }
        return balance;
    }
}
