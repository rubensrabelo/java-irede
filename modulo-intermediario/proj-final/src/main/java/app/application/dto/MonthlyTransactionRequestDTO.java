package app.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public record MonthlyTransactionRequestDTO(
    LocalDate date,
    String type,
    String category,
    BigDecimal amount,
    YearMonth monthYear
) {}
