package app.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Formatter {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat
            .getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    public static LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    public static String formatYearMonth(YearMonth monthYear) {
        return monthYear.format(MONTH_FORMATTER);
    }

    public static YearMonth parseYearMonth(String monthYearStr) {
        return YearMonth.parse(monthYearStr, MONTH_FORMATTER);
    }

    public static String formatCurrency(BigDecimal value) {
        return CURRENCY_FORMATTER.format(value);
    }
}
