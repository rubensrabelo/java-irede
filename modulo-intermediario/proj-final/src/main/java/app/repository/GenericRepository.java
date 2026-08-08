package app.repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID> {
    T save(T entity);
    void update(T entity);
    List<T> findAll();
    Optional<T> findById(ID id);
    boolean deleteById(ID id);
    void saveAll(List<? extends T> entities);
    void exportTo(List<? super T> destinationList);
}
