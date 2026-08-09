package app.application.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.Test;

import app.application.dto.MonthlyTransactionRequestDTO;
import app.application.dto.MonthlyTransactionResponseDTO;
import app.domain.MonthlyTransaction;

public class MonthlyTransactionMapperUnitTest {

    @Test
    public void testToEntityWithoutId() {
        YearMonth monthYear = YearMonth.of(2026, 8);
        MonthlyTransactionRequestDTO dto = new MonthlyTransactionRequestDTO(LocalDate.now(), "Saída", "Internet", new BigDecimal("99.90"), monthYear);
        
        MonthlyTransaction entity = MonthlyTransactionMapper.toEntity(dto);
        
        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals(dto.date(), entity.getDate());
        assertEquals(dto.type(), entity.getType().getDescription());
        assertEquals(dto.category(), entity.getCategory());
        assertEquals(dto.amount(), entity.getAmount());
        assertEquals(dto.monthYear(), entity.getMonthYear());
    }

    @Test
    public void testToEntityWithId() {
        Long id = 88L;
        YearMonth monthYear = YearMonth.of(2026, 8);
        MonthlyTransactionRequestDTO dto = new MonthlyTransactionRequestDTO(LocalDate.now(), "Entrada", "Dividendos", new BigDecimal("150.00"), monthYear);
        
        MonthlyTransaction entity = MonthlyTransactionMapper.toEntity(id, dto);
        
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals(dto.date(), entity.getDate());
        assertEquals(dto.type(), entity.getType().getDescription());
        assertEquals(dto.category(), entity.getCategory());
        assertEquals(dto.amount(), entity.getAmount());
        assertEquals(dto.monthYear(), entity.getMonthYear());
    }

    @Test
    public void testToResponseDTO() {
        YearMonth monthYear = YearMonth.of(2026, 8);
        MonthlyTransaction entity = new MonthlyTransaction(55L, LocalDate.now(), "Saída", "Academia", new BigDecimal("130.00"), monthYear);
        
        MonthlyTransactionResponseDTO dto = MonthlyTransactionMapper.toResponseDTO(entity);
        
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getDate(), dto.date());
        assertEquals(entity.getType().getDescription(), dto.typeDescription());
        assertEquals(entity.getCategory(), dto.category());
        assertEquals(entity.getAmount(), dto.amount());
        assertEquals(entity.getMonthYear(), dto.monthYear());
    }

    @Test
    public void testNullInputs() {
        assertNull(MonthlyTransactionMapper.toEntity(null));
        assertNull(MonthlyTransactionMapper.toEntity(1L, null));
        assertNull(MonthlyTransactionMapper.toResponseDTO(null));
    }
}
