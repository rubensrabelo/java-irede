package app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.domain.MonthlyTransaction;
import app.domain.Transaction;
import app.shared.exceptions.EntityNotFoundException;

public class TransactionDAOIntegrationTest {

    private Connection conn;
    private TransactionDAO dao;
    private TransactionDbRepository repository;

    @BeforeEach
    public void setUp() throws Exception {
        this.conn = DriverManager.getConnection("jdbc:sqlite::memory:");
        this.conn.setAutoCommit(false);
        this.dao = new TransactionDAO();
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE transactions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "transaction_kind TEXT NOT NULL, " +
                    "transaction_date INTEGER NOT NULL, " +
                    "transaction_type TEXT CHECK(transaction_type IN ('INCOME', 'OUTCOME')) NOT NULL, " +
                    "category TEXT NOT NULL, " +
                    "amount DECIMAL(15, 2) NOT NULL, " +
                    "month_year TEXT NULL" +
                    ")");
        }
        conn.commit();

        this.repository = new TransactionDbRepository(dao) {
            @Override
            public Transaction save(Transaction entity) {
                if (entity == null) {
                    throw new IllegalArgumentException("A entidade não pode ser nula.");
                }
                try {
                    Transaction saved = dao.insert(conn, entity);
                    conn.commit();
                    return saved;
                } catch (Exception e) {
                    try { conn.rollback(); } catch (Exception ignored) {}
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void update(Transaction entity) {
                if (entity == null || entity.getId() == null) {
                    throw new IllegalArgumentException("A entidade ou o ID não podem ser nulos para atualização.");
                }
                try {
                    int rowsAffected = dao.update(conn, entity);
                    if (rowsAffected == 0) {
                        throw new EntityNotFoundException("Transação com ID " + entity.getId() + " não encontrada.");
                    }
                    conn.commit();
                } catch (EntityNotFoundException e) {
                    throw e;
                } catch (Exception e) {
                    try { conn.rollback(); } catch (Exception ignored) {}
                    throw new RuntimeException(e);
                }
            }

            @Override
            public List<Transaction> findAll() {
                List<Transaction> transactions = new ArrayList<>();
                String sql = "SELECT * FROM transactions";
                try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
                     java.sql.ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Long id = rs.getLong("id");
                        String kind = rs.getString("transaction_kind");
                        
                        long ms = rs.getLong("transaction_date");
                        LocalDate date = Instant.ofEpochMilli(ms).atZone(ZoneId.systemDefault()).toLocalDate();
                        
                        String typeStr = rs.getString("transaction_type");
                        String category = rs.getString("category");
                        BigDecimal amount = rs.getBigDecimal("amount");
                        String mappedType = typeStr.equalsIgnoreCase("INCOME") ? "Entrada" : "Saída";

                        if ("MonthlyTransaction".equals(kind)) {
                            String myStr = rs.getString("month_year");
                            YearMonth monthYear = myStr != null ? YearMonth.parse(myStr) : YearMonth.now();
                            transactions.add(new MonthlyTransaction(id, date, mappedType, category, amount, monthYear));
                        } else {
                            transactions.add(new Transaction(id, date, mappedType, category, amount));
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return transactions;
            }

            @Override
            public Optional<Transaction> findById(Long id) {
                String sql = "SELECT * FROM transactions WHERE id = ?";
                try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setLong(1, id);
                    try (java.sql.ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String kind = rs.getString("transaction_kind");
                            
                            long ms = rs.getLong("transaction_date");
                            LocalDate date = Instant.ofEpochMilli(ms).atZone(ZoneId.systemDefault()).toLocalDate();
                            
                            String typeStr = rs.getString("transaction_type");
                            String category = rs.getString("category");
                            BigDecimal amount = rs.getBigDecimal("amount");
                            String mappedType = typeStr.equalsIgnoreCase("INCOME") ? "Entrada" : "Saída";

                            if ("MonthlyTransaction".equals(kind)) {
                                String myStr = rs.getString("month_year");
                                YearMonth monthYear = myStr != null ? YearMonth.parse(myStr) : YearMonth.now();
                                return Optional.of(new MonthlyTransaction(id, date, mappedType, category, amount, monthYear));
                            } else {
                                return Optional.of(new Transaction(id, date, mappedType, category, amount));
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return Optional.empty();
            }

            @Override
            public boolean deleteById(Long id) {
                try {
                    int rowsAffected = dao.deleteById(conn, id);
                    if (rowsAffected == 0) {
                        throw new EntityNotFoundException("Transação com ID " + id + " não encontrada.");
                    }
                    conn.commit();
                    return true;
                } catch (EntityNotFoundException e) {
                    throw e;
                } catch (Exception e) {
                    try { conn.rollback(); } catch (Exception ignored) {}
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    @Test
    public void testInsertSuccessAndQueryAllMappedFields() {
        Transaction transaction = new Transaction(LocalDate.now(), "Entrada", "Salário Integrado", new BigDecimal("4500.00"));
        
        Transaction saved = repository.save(transaction);
        
        assertNotNull(saved.getId());
        List<Transaction> list = repository.findAll();
        assertEquals(1, list.size());
        assertEquals("Salário Integrado", list.get(0).getCategory());
        assertEquals(0, new BigDecimal("4500.00").compareTo(list.get(0).getAmount()));
    }

    @Test
    public void testUpdateSuccessModifiesDatabaseFields() {
        Transaction transaction = new Transaction(LocalDate.now(), "Saída", "Luz", new BigDecimal("150.00"));
        Transaction saved = repository.save(transaction);
        
        saved.setCategory("Luz Comercial");
        saved.setAmount(new BigDecimal("185.50"));
        repository.update(saved);
        
        Transaction updated = repository.findById(saved.getId()).orElse(null);
        assertNotNull(updated);
        assertEquals("Luz Comercial", updated.getCategory());
        assertEquals(0, new BigDecimal("185.50").compareTo(updated.getAmount()));
    }

    @Test
    public void testUpdateFailureWithNonExistingIdThrowsEntityNotFoundException() {
        Transaction nonExisting = new Transaction(999L, LocalDate.now(), "Entrada", "Nulo", new BigDecimal("10.00"));
        
        assertThrows(EntityNotFoundException.class, () -> repository.update(nonExisting));
    }

    @Test
    public void testDeleteByIdSuccessRemovesRowFromDatabase() {
        Transaction transaction = new Transaction(LocalDate.now(), "Saída", "Uber", new BigDecimal("25.00"));
        Transaction saved = repository.save(transaction);
        
        boolean deleted = repository.deleteById(saved.getId());
        
        assertTrue(deleted);
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    public void testDeleteByIdFailureWithNonExistingIdThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> repository.deleteById(999L));
    }
}
