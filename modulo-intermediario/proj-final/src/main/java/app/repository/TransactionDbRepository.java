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
import app.model.MonthlyTransaction;
import app.model.Transaction;

public class TransactionDbRepository implements GenericRepository<Transaction, Long> {

    @Override
    public Transaction save(Transaction entity) {
        if (entity == null) {
            throw new IllegalArgumentException("A entidade não pode ser nula.");
        }

        String sql = "INSERT INTO transactions (transaction_kind, transaction_date, transaction_type, category, amount, month_year) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = MySQLConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
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
            return entity;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar transação no banco de dados.", e);
        }
    }

    public void updateWithExplicitId(Transaction entity, long id) {
        String sql = "UPDATE transactions SET transaction_kind = ?, transaction_date = ?, transaction_type = ?, category = ?, amount = ?, month_year = ? WHERE id = ?";
        try (Connection conn = MySQLConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
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
            stmt.setLong(7, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar transação no banco de dados.", e);
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
                String kind = rs.getString("transaction_kind");
                LocalDate date = rs.getDate("transaction_date").toLocalDate();
                String typeStr = rs.getString("transaction_type");
                String category = rs.getString("category");
                BigDecimal amount = rs.getBigDecimal("amount");

                String mappedType = typeStr.equalsIgnoreCase("INCOME") ? "Entrada" : "Saída";

                if ("MonthlyTransaction".equals(kind)) {
                    String myStr = rs.getString("month_year");
                    YearMonth monthYear = myStr != null ? YearMonth.parse(myStr) : YearMonth.now();
                    transactions.add(new MonthlyTransaction(date, mappedType, category, amount, monthYear));
                } else {
                    transactions.add(new Transaction(date, mappedType, category, amount));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar transações do banco de dados.", e);
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
                        return Optional.of(new MonthlyTransaction(date, mappedType, category, amount, monthYear));
                    } else {
                        return Optional.of(new Transaction(date, mappedType, category, amount));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar transação por ID.", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = MySQLConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover transação do banco de dados.", e);
        }
    }

    @Override
    public void saveAll(List<? extends Transaction> entities) {
        if (entities != null) {
            for (Transaction entity : entities) {
                this.save(entity);
            }
        }
    }

    @Override
    public void exportTo(List<? super Transaction> destinationList) {
        if (destinationList != null) {
            destinationList.addAll(this.findAll());
        }
    }
}
