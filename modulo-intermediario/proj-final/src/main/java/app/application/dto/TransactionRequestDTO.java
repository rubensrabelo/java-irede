package app.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequestDTO(
    LocalDate date,
    String type,
    String category,
    BigDecimal amount
) {}
