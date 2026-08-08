SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_kind VARCHAR(30) NOT NULL,
    transaction_date DATE NOT NULL,
    transaction_type ENUM('INCOME', 'OUTCOME') NOT NULL,
    category VARCHAR(150) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    month_year VARCHAR(7) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
