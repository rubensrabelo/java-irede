package app.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import app.model.Transaction;
import app.service.TransactionService;

public class FinTracker {
    private final TransactionService service;

    public FinTracker(TransactionService service) {
        this.service = service;
    }

    public void addTransaction(Transaction transaction) {
        try {
            service.save(transaction);
            System.out.println("Sucesso: Transação registrada!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro de validação: " + e.getMessage());
        }
    }

    public List<Transaction> listTransactions() {
        return service.findAll();
    }

    public Optional<Transaction> getById(Integer id) {
        return service.findById(id);
    }

    public void removeTransaction(Integer id) {
        boolean removed = service.deleteById(id);
        if (removed) {
            System.out.println("Sucesso: Transação removida!");
        } else {
            System.out.println("Erro: ID não encontrado.");
        }
    }

    public BigDecimal displayTotalBalance() {
        return service.calculateTotalBalance();
    }
}
