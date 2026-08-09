package app.application.mapper;

import app.application.dto.MonthlyTransactionRequestDTO;
import app.application.dto.MonthlyTransactionResponseDTO;
import app.domain.MonthlyTransaction;

public final class MonthlyTransactionMapper {

    private MonthlyTransactionMapper() {
        throw new UnsupportedOperationException();
    }

    public static MonthlyTransaction toEntity(MonthlyTransactionRequestDTO dto) {
        if (dto == null) return null;
        return new MonthlyTransaction(
            dto.date(),
            dto.type(),
            dto.category(),
            dto.amount(),
            dto.monthYear()
        );
    }

    public static MonthlyTransaction toEntity(Long id, MonthlyTransactionRequestDTO dto) {
        if (dto == null) return null;
        return new MonthlyTransaction(
            id,
            dto.date(),
            dto.type(),
            dto.category(),
            dto.amount(),
            dto.monthYear()
        );
    }

    public static MonthlyTransactionResponseDTO toResponseDTO(MonthlyTransaction entity) {
        if (entity == null) return null;
        return new MonthlyTransactionResponseDTO(
            entity.getId(),
            entity.getDate(),
            entity.getType() != null ? entity.getType().getDescription() : null,
            entity.getCategory(),
            entity.getAmount(),
            entity.getMonthYear()
        );
    }
}
