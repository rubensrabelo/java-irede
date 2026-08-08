SET NAMES utf8mb4;

INSERT INTO transactions (transaction_kind, transaction_date, transaction_type, category, amount, month_year) 
VALUES 
('MonthlyTransaction', '2026-08-01', 'INCOME', 'Salário Fixo', 5200.00, '2026-08'),
('MonthlyTransaction', '2026-08-02', 'OUTCOME', 'Aluguel Centro', 1300.00, '2026-08'),
('Transaction',        '2026-08-03', 'INCOME', 'Freelance JavaFX', 2500.00, NULL),
('Transaction',        '2026-08-03', 'OUTCOME', 'Supermercado', 620.50, NULL),
('Transaction',        '2026-08-04', 'OUTCOME', 'Posto de Gasolina', 150.00, NULL),
('MonthlyTransaction', '2026-08-05', 'OUTCOME', 'Plano de Internet', 120.00, '2026-08'),
('Transaction',        '2026-08-05', 'INCOME', 'Venda de Monitor', 450.00, NULL),
('Transaction',        '2026-08-06', 'OUTCOME', 'Restaurante Jantar', 180.00, NULL),
('MonthlyTransaction', '2026-08-06', 'INCOME', 'Rendimento Investimentos', 350.25, '2026-08'),
('Transaction',        '2026-08-07', 'OUTCOME', 'Farmácia', 85.40, NULL),
('Transaction',        '2026-08-07', 'INCOME', 'Consultoria TI', 1800.00, NULL),
('Transaction',        '2026-08-08', 'OUTCOME', 'Uber Viagens', 45.20, NULL),
('MonthlyTransaction', '2026-08-08', 'OUTCOME', 'Academia Mensal', 110.00, '2026-08'),
('Transaction',        '2026-08-08', 'OUTCOME', 'Assinatura Streaming', 55.90, NULL),
('Transaction',        '2026-08-08', 'INCOME', 'Premiação Projeto', 1000.00, NULL);
