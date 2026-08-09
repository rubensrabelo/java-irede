package app.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.application.dto.TransactionRequestDTO;
import app.application.dto.TransactionResponseDTO;
import app.domain.Transaction;
import app.repository.GenericRepository;
import app.shared.exceptions.EntityNotFoundException;
import app.shared.exceptions.InvalidInputException;

public class TransactionServiceUnitTest {

    private TransactionService service;
    private List<Transaction> storage;
    private long currentId;

    @BeforeEach
    public void setUp() {
        this.storage = new ArrayList<>();
        this.currentId = 1L;

        GenericRepository<Transaction, Long> fakeRepository = new GenericRepository<>() {
            @Override
            public Transaction save(Transaction entity) {
                if (entity.getId() == null) {
                    entity.setId(currentId++);
                }
                storage.remove(entity);
                storage.add(entity);
                return entity;
            }

            @Override
            public void update(Transaction entity) {
                storage.remove(entity);
                storage.add(entity);
            }

            @Override
            public List<Transaction> findAll() {
                return new ArrayList<>(storage);
            }

            @Override
            public Optional<Transaction> findById(Long id) {
                return storage.stream().filter(t -> t.getId().equals(id)).findFirst();
            }

            @Override
            public boolean deleteById(Long id) {
                return storage.removeIf(t -> t.getId().equals(id));
            }

            @Override
            public void saveAll(List<? extends Transaction> entities) {
                if (entities != null) {
                    entities.forEach(this::save);
                }
            }

            @Override
            public void exportTo(List<? super Transaction> destinationList) {
                if (destinationList != null) {
                    destinationList.addAll(storage);
                }
            }
        };

        this.service = new TransactionService(fakeRepository);
    }

    @Test
    public void testSaveSuccessWithPositiveAmount() {
        TransactionRequestDTO request = new TransactionRequestDTO(LocalDate.now(), "Entrada", "Salário", new BigDecimal("5000.00"));
        
        TransactionResponseDTO saved = service.save(request);
        
        assertNotNull(saved.id());
        assertEquals(1, service.findAll().size());
        assertEquals(new BigDecimal("5000.00"), service.findAll().get(0).amount());
    }

    @Test
    public void testSaveFailureWithZeroAmountThrowsException() {
        TransactionRequestDTO zeroRequest = new TransactionRequestDTO(LocalDate.now(), "Entrada", "Investimento", BigDecimal.ZERO);
        
        InvalidInputException ex = assertThrows(InvalidInputException.class, () -> service.save(zeroRequest));
        assertEquals("O valor da transação deve ser maior que zero.", ex.getMessage());
    }

    @Test
    public void testSaveFailureWithNegativeAmountThrowsException() {
        TransactionRequestDTO negativeRequest = new TransactionRequestDTO(LocalDate.now(), "Saída", "Lanche", new BigDecimal("-25.50"));
        
        InvalidInputException ex = assertThrows(InvalidInputException.class, () -> service.save(negativeRequest));
        assertEquals("O valor da transação deve ser maior que zero.", ex.getMessage());
    }

    @Test
    public void testUpdateSuccessWithPositiveAmount() {
        TransactionRequestDTO request = new TransactionRequestDTO(LocalDate.now(), "Entrada", "Freelance", new BigDecimal("1000.00"));
        TransactionResponseDTO saved = service.save(request);
        
        TransactionRequestDTO updateRequest = new TransactionRequestDTO(LocalDate.now(), "Entrada", "Freelance Java Avançado", new BigDecimal("1500.00"));
        service.update(saved.id(), updateRequest);
        
        TransactionResponseDTO updated = service.findById(saved.id());
        assertNotNull(updated);
        assertEquals("Freelance Java Avançado", updated.category());
        assertEquals(new BigDecimal("1500.00"), updated.amount());
    }

    @Test
    public void testUpdateFailureWithZeroAmountThrowsException() {
        TransactionRequestDTO request = new TransactionRequestDTO(LocalDate.now(), "Entrada", "Freelance", new BigDecimal("1000.00"));
        TransactionResponseDTO saved = service.save(request);
        
        TransactionRequestDTO invalidRequest = new TransactionRequestDTO(LocalDate.now(), "Entrada", "Freelance", BigDecimal.ZERO);
        assertThrows(InvalidInputException.class, () -> service.update(saved.id(), invalidRequest));
    }

    @Test
    public void testUpdateFailureWithNonExistentIdThrowsException() {
        TransactionRequestDTO updateRequest = new TransactionRequestDTO(LocalDate.now(), "Entrada", "Freelance", new BigDecimal("1500.00"));
        assertThrows(EntityNotFoundException.class, () -> service.update(999L, updateRequest));
    }

    @Test
    public void testCalculateTotalBalanceWithMixedTransactions() {
        TransactionRequestDTO t1 = new TransactionRequestDTO(LocalDate.now(), "Entrada", "Salário", new BigDecimal("3000.00"));
        TransactionRequestDTO t2 = new TransactionRequestDTO(LocalDate.now(), "Saída", "Aluguel", new BigDecimal("1200.00"));
        TransactionRequestDTO t3 = new TransactionRequestDTO(LocalDate.now(), "Entrada", "Freelance", new BigDecimal("500.00"));
        TransactionRequestDTO t4 = new TransactionRequestDTO(LocalDate.now(), "Saída", "Internet", new BigDecimal("100.00"));
        
        service.save(t1);
        service.save(t2);
        service.save(t3);
        service.save(t4);

        BigDecimal expectedBalance = new BigDecimal("2200.00");
        assertEquals(expectedBalance, service.calculateTotalBalance());
    }

    @Test
    public void testDeleteByIdSuccessfully() {
        TransactionRequestDTO request = new TransactionRequestDTO(LocalDate.now(), "Saída", "Academia", new BigDecimal("120.00"));
        TransactionResponseDTO saved = service.save(request);
        
        boolean deleted = service.deleteById(saved.id());
        
        assertTrue(deleted);
        assertTrue(service.findAll().isEmpty());
    }
}
