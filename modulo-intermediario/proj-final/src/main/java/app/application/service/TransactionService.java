package app.application.service;

import java.math.BigDecimal;
import java.util.List;

import app.application.dto.TransactionRequestDTO;
import app.application.dto.TransactionResponseDTO;
import app.application.mapper.TransactionMapper;
import app.domain.Transaction;
import app.domain.enums.TransactionType;
import app.repository.GenericRepository;
import app.shared.exceptions.EntityNotFoundException;
import app.shared.exceptions.InvalidInputException;

public class TransactionService {
    private final GenericRepository<Transaction, Long> repository;

    public TransactionService(GenericRepository<Transaction, Long> repository) {
        this.repository = repository;
    }

    public TransactionResponseDTO save(TransactionRequestDTO dto) {
        if (dto.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidInputException("O valor da transação deve ser maior que zero.");
        }
        Transaction transaction = TransactionMapper.toEntity(dto);
        Transaction savedTransaction = repository.save(transaction);
        return TransactionMapper.toResponseDTO(savedTransaction);
    }

    public void update(Long id, TransactionRequestDTO dto) {
        if (dto.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidInputException("O valor da transação deve ser maior que zero.");
        }
        repository.findById(id).orElseThrow(() -> 
            new EntityNotFoundException("A entidade com o id = " + id + " não foi encontrada")
        );
        Transaction transaction = TransactionMapper.toEntity(id, dto);
        repository.update(transaction);
    }

    public List<TransactionResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(TransactionMapper::toResponseDTO)
                .toList();
    }

    public TransactionResponseDTO findById(Long id) {
        return repository.findById(id)
                .map(TransactionMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("A entidade com o id = " + id + " não foi encontrada"));
    }

    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }
        return repository.deleteById(id);
    }

    public BigDecimal calculateTotalBalance() {
        return repository.findAll().stream()
                .map(t -> t.getType() == TransactionType.INCOME ? t.getAmount() : t.getAmount().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void saveAll(List<TransactionRequestDTO> dtos) {
        List<Transaction> transactions = dtos.stream()
                .map(TransactionMapper::toEntity)
                .toList();
        repository.saveAll(transactions);
    }
}
