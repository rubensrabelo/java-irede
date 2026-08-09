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

import app.domain.MonthlyTransaction;
import app.domain.Transaction;

public class TransactionDAO {

    public Transaction insert(Connection conn, Transaction entity) throws SQLException {
        String sql = "INSERT INTO transactions (transaction_kind, transaction_date, transaction_type, category, amount, month_year) VALUES (?, ?, ?, ?, ?, ?)";
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
            return entity;
        }
    }

    public int update(Connection conn, Transaction entity) throws SQLException {
        String sql = "UPDATE transactions SET transaction_kind = ?, transaction_date = ?, transaction_type = ?, category = ?, amount = ?, month_year = ? WHERE id = ?";
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

            return stmt.executeUpdate();
        }
    }

    public List<Transaction> findAll(Connection conn) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
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
        }
        return transactions;
    }

    public Optional<Transaction> findById(Connection conn, Long id) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        }
        return Optional.empty();
    }

    public int deleteById(Connection conn, Long id) throws SQLException {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate();
        }
    }
}
