package app.application.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import app.application.dto.TransactionRequestDTO;
import app.application.dto.TransactionResponseDTO;
import app.domain.Transaction;

public class TransactionMapperUnitTest {

    @Test
    public void testToEntityWithoutId() {
        TransactionRequestDTO dto = new TransactionRequestDTO(LocalDate.now(), "Entrada", "Salário", new BigDecimal("3500.00"));
        
        Transaction entity = TransactionMapper.toEntity(dto);
        
        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals(dto.date(), entity.getDate());
        assertEquals(dto.type(), entity.getType().getDescription());
        assertEquals(dto.category(), entity.getCategory());
        assertEquals(dto.amount(), entity.getAmount());
    }

    @Test
    public void testToEntityWithId() {
        Long id = 42L;
        TransactionRequestDTO dto = new TransactionRequestDTO(LocalDate.now(), "Saída", "Aluguel", new BigDecimal("1200.00"));
        
        Transaction entity = TransactionMapper.toEntity(id, dto);
        
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals(dto.date(), entity.getDate());
        assertEquals(dto.type(), entity.getType().getDescription());
        assertEquals(dto.category(), entity.getCategory());
        assertEquals(dto.amount(), entity.getAmount());
    }

    @Test
    public void testToResponseDTO() {
        Transaction entity = new Transaction(10L, LocalDate.now(), "Entrada", "Investimento", new BigDecimal("500.00"));
        
        TransactionResponseDTO dto = TransactionMapper.toResponseDTO(entity);
        
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getDate(), dto.date());
        assertEquals(entity.getType().getDescription(), dto.typeDescription());
        assertEquals(entity.getCategory(), dto.category());
        assertEquals(entity.getAmount(), dto.amount());
    }

    @Test
    public void testNullInputs() {
        assertNull(TransactionMapper.toEntity(null));
        assertNull(TransactionMapper.toEntity(1L, null));
        assertNull(TransactionMapper.toResponseDTO(null));
    }
}
