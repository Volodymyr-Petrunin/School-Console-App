package org.consoleApp.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T> {
    Optional<T> findById(int id);
    List<T> findAll();
    void insertBatch(List<T> obj);
    void insert(T entity);
    void update(T entity);
    void delete(T entity);
}
