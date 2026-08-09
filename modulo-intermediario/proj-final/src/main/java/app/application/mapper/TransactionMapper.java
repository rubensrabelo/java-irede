package app.application.mapper;

import app.application.dto.TransactionRequestDTO;
import app.application.dto.TransactionResponseDTO;
import app.domain.Transaction;

public final class TransactionMapper {

    private TransactionMapper() {
        throw new UnsupportedOperationException();
    }

    public static Transaction toEntity(TransactionRequestDTO dto) {
        if (dto == null) return null;
        return new Transaction(
            dto.date(),
            dto.type(),
            dto.category(),
            dto.amount()
        );
    }

    public static Transaction toEntity(Long id, TransactionRequestDTO dto) {
        if (dto == null) return null;
        return new Transaction(
            id,
            dto.date(),
            dto.type(),
            dto.category(),
            dto.amount()
        );
    }

    public static TransactionResponseDTO toResponseDTO(Transaction entity) {
        if (entity == null) return null;
        return new TransactionResponseDTO(
            entity.getId(),
            entity.getDate(),
            entity.getType() != null ? entity.getType().getDescription() : null,
            entity.getCategory(),
            entity.getAmount()
        );
    }
}
