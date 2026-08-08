package app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import app.controller.FinTracker;
import app.exceptions.InvalidInputException;
import app.model.MonthlyTransaction;
import app.model.Transaction;
import app.utils.Formatter;

public class Main {
    private static final FinTracker tracker = new FinTracker();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            int option = 1;
            while (option != 5) {
                String menu = """
                        ===== FINTRACK - SEU CONTROLE FINANCEIRO =====
                            1. Adicionar nova transação
                            2. Listar transações
                            3. Mostrar saldo atual
                            4. Remover transação
                            5. Sair
                        """;
                
                System.out.println();
                System.out.println(menu);
                System.out.print("Escolha uma opção:\s");
                
                try {
                    option = scanner.nextInt();
                    scanner.nextLine();
                } catch (InputMismatchException e) {
                    scanner.nextLine();
                    throw new InvalidInputException("digite um valor númerico entre 1 e 5.");
                }

                if (option < 1 || option > 5) {
                    throw new InvalidInputException("Número inválido! Escolha apenas de 1 a 5.");
                }

                executeOption(option, scanner);
            }
        } finally {
            scanner.close();
        }
    }

    private static void executeOption(int option, Scanner scanner) {
        switch (option) {
            case 1 -> addTransactionFlow(scanner);
            case 2 -> listTransactionsFlow();
            case 3 -> showBalanceFlow();
            case 4 -> removeTransactionFlow(scanner);
        }
    }

    private static void addTransactionFlow(Scanner scanner) {
        System.out.print("Tipo: ");
        String type = scanner.nextLine().toUpperCase();

        System.out.print("Categoria: ");
        String category = scanner.nextLine();

        System.out.print("Valor: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        System.out.print("Mensal? (S/N): ");
        String isMonthly = scanner.nextLine().trim().toUpperCase();

        if ("S".equals(isMonthly)) {
            System.out.print("Mês/Ano (MM/AAAA): ");
            YearMonth monthYear = Formatter.parseYearMonth(scanner.nextLine());
            LocalDate date = monthYear.atDay(1);
            tracker.addTransaction(new MonthlyTransaction(date, type, category, amount, monthYear));
        } else {
            System.out.print("Data (DD/MM/AAAA): ");
            LocalDate date = Formatter.parseDate(scanner.nextLine());
            tracker.addTransaction(new Transaction(date, type, category, amount));
        }
        System.out.println("Transação adicionada com sucesso.");
    }

    private static void listTransactionsFlow() {
        List<Transaction> list = tracker.listTransactions();
        if (list.isEmpty()) {
            System.out.println("Nenhuma transação cadastrada.");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            Transaction t = list.get(i);
            String output = "[" + i + "] " + t.getType().getDescription() + " | " + t.getCategory() + " | " + Formatter.formatCurrency(t.getAmount());
            if (t instanceof MonthlyTransaction mt) {
                output += " | Período: " + Formatter.formatYearMonth(mt.getMonthYear());
            } else {
                output += " | Data: " + Formatter.formatDate(t.getDate());
            }
            System.out.println(output);
        }
    }

    private static void showBalanceFlow() {
        BigDecimal balance = tracker.calculateTotalBalance();
        System.out.println("Saldo Geral: " + Formatter.formatCurrency(balance));
    }

    private static void removeTransactionFlow(Scanner scanner) {
        List<Transaction> list = tracker.listTransactions();
        if (list.isEmpty()) {
            System.out.println("Nenhuma transação para remover.");
            return;
        }
        listTransactionsFlow();
        System.out.print("Digite o índice para remover: ");
        int index = Integer.parseInt(scanner.nextLine());
        if (index >= 0 && index < list.size()) {
            tracker.removeTransaction(list.get(index));
            System.out.println("Transação removida com sucesso.");
        } else {
            System.out.println("Índice inválido.");
        }
    }
}
