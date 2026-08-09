package app.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public record MonthlyTransactionResponseDTO(
    Long id,
    LocalDate date,
    String typeDescription,
    String category,
    BigDecimal amount,
    YearMonth monthYear
) {}
