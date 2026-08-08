package app.model;

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

    public YearMonth getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(YearMonth monthYear) {
        this.monthYear = monthYear;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((monthYear == null) ? 0 : monthYear.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        MonthlyTransaction other = (MonthlyTransaction) obj;
        if (monthYear == null) {
            if (other.monthYear != null)
                return false;
        } else if (!monthYear.equals(other.monthYear))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "MonthlyTransaction [monthYear=" + monthYear + ", date=" + getDate() + ", type=" + getType().getDescription()
                + ", category=" + getCategory() + ", amount=" + getAmount() + "]";
    }
}
