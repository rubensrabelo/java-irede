package app.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponseDTO(
    Long id,
    LocalDate date,
    String typeDescription,
    String category,
    BigDecimal amount
) {}
