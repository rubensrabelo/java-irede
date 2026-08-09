package app.service;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.application.service.TransactionService;
import app.domain.Transaction;
import app.repository.GenericRepository;
import app.shared.exceptions.InvalidInputException;

public class TransactionServiceTest {

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
        Transaction transaction = new Transaction(LocalDate.now(), "Entrada", "Salário", new BigDecimal("5000.00"));
        
        Transaction saved = service.save(transaction);
        
        assertNotNull(saved.getId());
        assertEquals(1, service.findAll().size());
        assertEquals(new BigDecimal("5000.00"), service.findAll().get(0).getAmount());
    }

    @Test
    public void testSaveFailureWithZeroAmountThrowsException() {
        Transaction zeroTransaction = new Transaction(LocalDate.now(), "Entrada", "Investimento", BigDecimal.ZERO);
        
        InvalidInputException ex = assertThrows(InvalidInputException.class, () -> service.save(zeroTransaction));
        assertEquals("O valor da transação deve ser maior que zero.", ex.getMessage());
    }

    @Test
    public void testSaveFailureWithNegativeAmountThrowsException() {
        Transaction negativeTransaction = new Transaction(LocalDate.now(), "Saída", "Lanche", new BigDecimal("-25.50"));
        
        InvalidInputException ex = assertThrows(InvalidInputException.class, () -> service.save(negativeTransaction));
        assertEquals("O valor da transação deve ser maior que zero.", ex.getMessage());
    }

    @Test
    public void testUpdateSuccessWithPositiveAmount() {
        Transaction transaction = new Transaction(LocalDate.now(), "Entrada", "Freelance", new BigDecimal("1000.00"));
        Transaction saved = service.save(transaction);
        
        saved.setCategory("Freelance Java Avançado");
        saved.setAmount(new BigDecimal("1500.00"));
        service.update(saved);
        
        Transaction updated = service.findById(saved.getId()).orElse(null);
        assertNotNull(updated);
        assertEquals("Freelance Java Avançado", updated.getCategory());
        assertEquals(new BigDecimal("1500.00"), updated.getAmount());
    }

    @Test
    public void testUpdateFailureWithZeroAmountThrowsException() {
        Transaction transaction = new Transaction(LocalDate.now(), "Entrada", "Freelance", new BigDecimal("1000.00"));
        Transaction saved = service.save(transaction);
        
        saved.setAmount(BigDecimal.ZERO);
        assertThrows(InvalidInputException.class, () -> service.update(saved));
    }

    @Test
    public void testCalculateTotalBalanceWithMixedTransactions() {
        Transaction t1 = new Transaction(LocalDate.now(), "Entrada", "Salário", new BigDecimal("3000.00"));
        Transaction t2 = new Transaction(LocalDate.now(), "Saída", "Aluguel", new BigDecimal("1200.00"));
        Transaction t3 = new Transaction(LocalDate.now(), "Entrada", "Freelance", new BigDecimal("500.00"));
        Transaction t4 = new Transaction(LocalDate.now(), "Saída", "Internet", new BigDecimal("100.00"));
        
        service.save(t1);
        service.save(t2);
        service.save(t3);
        service.save(t4);

        BigDecimal expectedBalance = new BigDecimal("2200.00");
        assertEquals(expectedBalance, service.calculateTotalBalance());
    }

    @Test
    public void testDeleteByIdSuccessfully() {
        Transaction transaction = new Transaction(LocalDate.now(), "Saída", "Academia", new BigDecimal("120.00"));
        Transaction saved = service.save(transaction);
        
        boolean deleted = service.deleteById(saved.getId());
        
        assertTrue(deleted);
        assertTrue(service.findAll().isEmpty());
    }
}
