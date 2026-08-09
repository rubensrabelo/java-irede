package app.shared.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import app.application.dto.TransactionRequestDTO;
import app.application.dto.TransactionResponseDTO;
import app.shared.exceptions.CsvDeserializationException;
import app.shared.exceptions.CsvSerializationException;

public final class TransactionCsvParser {

    private TransactionCsvParser() {
        throw new UnsupportedOperationException();
    }

    public static List<TransactionRequestDTO> deserialize(String csvData) {
        List<TransactionRequestDTO> dtos = new ArrayList<>();
        if (csvData == null || csvData.isBlank()) {
            return dtos;
        }
        try {
            String[] lines = csvData.split("\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.isEmpty() || i == 0 && line.toLowerCase().contains("date")) {
                    continue;
                }
                String[] tokens = line.split(";");
                if (tokens.length >= 4) {
                    LocalDate date = LocalDate.parse(tokens[0].trim());
                    String type = tokens[1].trim();
                    String category = tokens[2].trim();
                    BigDecimal amount = new BigDecimal(tokens[3].trim());
                    dtos.add(new TransactionRequestDTO(date, type, category, amount));
                }
            }
            return dtos;
        } catch (Exception e) {
            throw new CsvDeserializationException("Falha na análise dos dados do CSV. Verifique a formatação das linhas.", e);
        }
    }

    public static String serialize(List<TransactionResponseDTO> dtos) {
        try {
            if (dtos == null || dtos.isEmpty()) {
                return "id;date;type;category;amount\n";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("id;date;type;category;amount\n");
            for (TransactionResponseDTO dto : dtos) {
                sb.append(dto.id()).append(";")
                  .append(dto.date()).append(";")
                  .append(dto.typeDescription()).append(";")
                  .append(dto.category()).append(";")
                  .append(dto.amount()).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            throw new CsvSerializationException("Falha na geração do texto estruturado do CSV.", e);
        }
    }
}
