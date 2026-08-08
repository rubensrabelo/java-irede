package app.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import app.config.MySQLConfig;
import app.exceptions.DatabaseException;
import app.exceptions.EntityNotFoundException;
import app.model.Transaction;

public class TransactionDbRepository implements GenericRepository<Transaction, Long> {

    private final TransactionDAO transacaoDAO;

    public TransactionDbRepository(TransactionDAO transacaoDAO) {
        this.transacaoDAO = transacaoDAO;
    }

    @Override
    public Transaction save(Transaction entity) {
        if (entity == null) {
            throw new IllegalArgumentException("A entidade não pode ser nula.");
        }

        try (Connection conn = MySQLConfig.getConnection()) {
            try {
                Transaction saved = transacaoDAO.insert(conn, entity);
                conn.commit();
                return saved;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar transação via DAO no banco de dados.", e);
        }
    }

    @Override
    public void update(Transaction entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("A entidade ou o ID não podem ser nulos para atualização.");
        }

        try (Connection conn = MySQLConfig.getConnection()) {
            try {
                int rowsAffected = transacaoDAO.update(conn, entity);
                if (rowsAffected == 0) {
                    throw new EntityNotFoundException("Transação com ID " + entity.getId() + " não foi encontrada para atualização.");
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar transação via DAO no banco de dados.", e);
        }
    }

    @Override
    public List<Transaction> findAll() {
        try (Connection conn = MySQLConfig.getConnection()) {
            List<Transaction> list = transacaoDAO.findAll(conn);
            conn.commit();
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar transações via DAO.", e);
        }
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        try (Connection conn = MySQLConfig.getConnection()) {
            Optional<Transaction> transaction = transacaoDAO.findById(conn, id);
            conn.commit();
            return transaction;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar transação por ID via DAO.", e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Connection conn = MySQLConfig.getConnection()) {
            try {
                int rowsAffected = transacaoDAO.deleteById(conn, id);
                if (rowsAffected == 0) {
                    throw new EntityNotFoundException("Transação com ID " + id + " não foi encontrada para remoção.");
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao remover transação por ID via DAO.", e);
        }
    }

    @Override
    public void saveAll(List<? extends Transaction> entities) {
        if (entities == null || entities.isEmpty()) {
            return;
        }
        try (Connection conn = MySQLConfig.getConnection()) {
            try {
                for (Transaction entity : entities) {
                    transacaoDAO.insert(conn, entity);
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new DatabaseException("Erro ao salvar lote de transações via DAO.", e);
        }
    }

    @Override
    public void exportTo(List<? super Transaction> destinationList) {
        if (destinationList != null) {
            destinationList.addAll(this.findAll());
        }
    }
}
