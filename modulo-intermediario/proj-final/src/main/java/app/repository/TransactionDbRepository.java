package app.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import app.config.MySQLConfig;
import app.exceptions.DatabaseException;
import app.exceptions.EntityNotFoundException;
import app.model.MonthlyTransaction;
import app.model.Transaction;

public class TransactionDbRepository implements GenericRepository<Transaction, Long> {

    @Override
    public Transaction save(Transaction entity) {
        if (entity == null) {
            throw new IllegalArgumentException("A entidade não pode ser nula.");
        }

        String sql = "INSERT INTO transactions (transaction_kind, transaction_date, transaction_type, category, amount, month_year) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = MySQLConfig.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                
                if (entity instanceof MonthlyTransaction) {
                    stmt.setString(1, "MonthlyTransaction");
                    MonthlyTransaction mt = (MonthlyTransaction) entity;
                    stmt.setString(6, mt.getMonthYear() != null ? mt.getMonthYear().toString() : null);
                } else {
                    stmt.setString(1, "Transaction");
                    stmt.setNull(6, java.sql.Types.VARCHAR);
                }

                stmt.setDate(2, Date.valueOf(entity.getDate()));
                stmt.setString(3, entity.getType().name());
                stmt.setString(4, entity.getCategory());
                stmt.setBigDecimal(5, entity.getAmount());

                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getLong(1));
                    }
                }
                conn.commit();
                return entity;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar transação no banco de dados.", e);
        }
    }

    @Override
    public void update(Transaction entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("A entidade ou o ID não podem ser nulos para atualização.");
        }

        String sql = "UPDATE transactions SET transaction_kind = ?, transaction_date = ?, transaction_type = ?, category = ?, amount = ?, month_year = ? WHERE id = ?";
        
        try (Connection conn = MySQLConfig.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                if (entity instanceof MonthlyTransaction) {
                    stmt.setString(1, "MonthlyTransaction");
                    MonthlyTransaction mt = (MonthlyTransaction) entity;
                    stmt.setString(6, mt.getMonthYear() != null ? mt.getMonthYear().toString() : null);
                } else {
                    stmt.setString(1, "Transaction");
                    stmt.setNull(6, java.sql.Types.VARCHAR);
                }

                stmt.setDate(2, Date.valueOf(entity.getDate()));
                stmt.setString(3, entity.getType().name());
                stmt.setString(4, entity.getCategory());
                stmt.setBigDecimal(5, entity.getAmount());
                stmt.setLong(7, entity.getId());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new EntityNotFoundException("Transação com ID " + entity.getId() + " não foi encontrada para atualização.");
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar transação no banco de dados.", e);
        }
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        try (Connection conn = MySQLConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Long id = rs.getLong("id");
                String kind = rs.getString("transaction_kind");
                LocalDate date = rs.getDate("transaction_date").toLocalDate();
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

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar transações do banco de dados.", e);
        }
        return transactions;
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try (Connection conn = MySQLConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String kind = rs.getString("transaction_kind");
                    LocalDate date = rs.getDate("transaction_date").toLocalDate();
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
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar transação por ID.", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = MySQLConfig.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setLong(1, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new EntityNotFoundException("Transação com ID " + id + " não foi encontrada para remoção.");
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao remover transação do banco de dados.", e);
        }
    }

    @Override
    public void saveAll(List<? extends Transaction> entities) {
        if (entities == null || entities.isEmpty()) {
            return;
        }
        try (Connection conn = MySQLConfig.getConnection()) {
            try {
                for (Transaction entity : entities) {
                    this.save(entity);
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new DatabaseException("Erro ao salvar lote de transações no banco de dados.", e);
        }
    }

    @Override
    public void exportTo(List<? super Transaction> destinationList) {
        if (destinationList != null) {
            destinationList.addAll(this.findAll());
        }
    }
}
