package org.consoleApp.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T> {
    Optional<T> findById(int id);
    List<T> findAll();
    void insertBatch(List<T> obj);
    boolean insert(T entity);
    boolean update(T entity);
    boolean delete(T entity);
}
