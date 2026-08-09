package app.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public class MonthlyTransaction extends Transaction {
    private YearMonth monthYear;

    public MonthlyTransaction() {
        super();
    }

    public MonthlyTransaction(LocalDate date, String type, String category, BigDecimal amount, YearMonth monthYear) {
        super(date, type, category, amount);
        this.monthYear = monthYear;
    }

    public MonthlyTransaction(Long id, LocalDate date, String type, String category, BigDecimal amount, YearMonth monthYear) {
        super(id, date, type, category, amount);
        this.monthYear = monthYear;
    }

    public YearMonth getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(YearMonth monthYear) {
        this.monthYear = monthYear;
    }

    @Override
    public String toString() {
        return "MonthlyTransaction [id=" + getId() + ", monthYear=" + monthYear + ", date=" + getDate() + ", type=" + getType().getDescription()
                + ", category=" + getCategory() + ", amount=" + getAmount() + "]";
    }
}
