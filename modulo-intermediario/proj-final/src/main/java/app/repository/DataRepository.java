package app.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DataRepository<T> implements GenericRepository<T, Integer> {
    private final Map<Integer, T> datasource = new HashMap<>();
    private int currentId = 1;

    @Override
    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("A entidade não pode ser nula.");
        }
        datasource.put(currentId, entity);
        currentId++;
        return entity;
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(datasource.values());
    }

    @Override
    public Optional<T> findById(Integer id) {
        return Optional.ofNullable(datasource.get(id));
    }

    @Override
    public boolean deleteById(Integer id) {
        if (datasource.containsKey(id)) {
            datasource.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public void saveAll(List<? extends T> entities) {
        if (entities != null) {
            for (T entity : entities) {
                this.save(entity);
            }
        }
    }

    @Override
    public void exportTo(List<? super T> destinationList) {
        if (destinationList != null) {
            destinationList.addAll(datasource.values());
        }
    }
}
