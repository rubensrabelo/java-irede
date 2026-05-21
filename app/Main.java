package app;

import java.util.InputMismatchException;
import java.util.Scanner;

import app.exceptions.InvalidInputException;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            String menu = """
                    ===== FINTRACK - SEU CONTROLE FINANCEIRO =====
                        1. Adicionar nova transação
                        2. Listar transações
                        3. Mostrar saldo atual
                        4. Remover transação
                        5. Sair
                    """;
            System.out.println(menu);
            System.out.print("Escolha uma opção:\s");
            int option = scanner.nextInt();

            if (option < 1 || option > 5) {
                throw new InvalidInputException("Número inválido! Escolha apenas de 1 a 5.");
            }

            System.out.println("Você escolheu a opção: " + option);
        } catch (InputMismatchException e) {
            scanner.next();
            throw new InvalidInputException("digite um valor númerico entre 1 e 5.");
        } finally {
            scanner.close();
        }
    }
}