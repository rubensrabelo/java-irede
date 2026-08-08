package app.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import app.model.Transaction;
import app.model.enums.TransactionType;
import app.repository.GenericRepository;

public class TransactionService {
    private final GenericRepository<Transaction, Long> repository;

    public TransactionService(GenericRepository<Transaction, Long> repository) {
        this.repository = repository;
    }

    public Transaction save(Transaction transaction) {
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transação deve ser maior que zero.");
        }
        return repository.save(transaction);
    }

    public void update(Transaction transaction) {
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transação deve ser maior que zero.");
        }
        repository.update(transaction);
    }

    public List<Transaction> findAll() {
        return repository.findAll();
    }

    public Optional<Transaction> findById(Long id) {
        return repository.findById(id);
    }

    public boolean deleteById(Long id) {
        return repository.deleteById(id);
    }

    public BigDecimal calculateTotalBalance() {
        return repository.findAll().stream()
                .map(t -> t.getType() == TransactionType.INCOME ? t.getAmount() : t.getAmount().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void saveAll(List<? extends Transaction> transactions) {
        repository.saveAll(transactions);
    }
}
