package app.model;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public class MonthlyTransaction {
    private YearMonth monthYear;
    private List<Transaction> transactions;
    private BigDecimal totalRevenue;
    private BigDecimal totalExpense;
    private BigDecimal balance;

    public MonthlyTransaction() {
        this.totalRevenue = BigDecimal.ZERO;
        this.totalExpense = BigDecimal.ZERO;
        this.balance = BigDecimal.ZERO;
    }

    public MonthlyTransaction(YearMonth monthYear, List<Transaction> transactions) {
        this.monthYear = monthYear;
        setTransactions(transactions);
    }

    private void calculateTotals() {
        this.totalRevenue = BigDecimal.ZERO;
        this.totalExpense = BigDecimal.ZERO;

        if (this.transactions != null) {
            for (Transaction t : this.transactions) {
                if (t.getAmount() != null && t.getType() != null) {
                    if ("REVENUE".equalsIgnoreCase(t.getType())) {
                        this.totalRevenue = this.totalRevenue.add(t.getAmount());
                    } else if ("EXPENSE".equalsIgnoreCase(t.getType())) {
                        this.totalExpense = this.totalExpense.add(t.getAmount());
                    }
                }
            }
        }
        this.balance = this.totalRevenue.subtract(this.totalExpense);
    }

    public YearMonth getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(YearMonth monthYear) {
        this.monthYear = monthYear;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        calculateTotals();
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((monthYear == null) ? 0 : monthYear.hashCode());
        result = prime * result + ((totalRevenue == null) ? 0 : totalRevenue.hashCode());
        result = prime * result + ((totalExpense == null) ? 0 : totalExpense.hashCode());
        result = prime * result + ((balance == null) ? 0 : balance.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MonthlyTransaction other = (MonthlyTransaction) obj;
        if (monthYear == null) {
            if (other.monthYear != null)
                return false;
        } else if (!monthYear.equals(other.monthYear))
            return false;
        if (totalRevenue == null) {
            if (other.totalRevenue != null)
                return false;
        } else if (!totalRevenue.equals(other.totalRevenue))
            return false;
        if (totalExpense == null) {
            if (other.totalExpense != null)
                return false;
        } else if (!totalExpense.equals(other.totalExpense))
            return false;
        if (balance == null) {
            if (other.balance != null)
                return false;
        } else if (!balance.equals(other.balance))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "MonthlyTransaction [monthYear=" + monthYear + ", transactions=" + transactions + ", totalRevenue="
                + totalRevenue + ", totalExpense=" + totalExpense + ", balance=" + balance + "]";
    }
}
