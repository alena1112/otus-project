package ru.otus.db.core.repository;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface DataTemplate<T> {
    Optional<T> findById(Connection connection, long id);

    List<Long> findAll(Connection connection);

    long insert(Connection connection, T object);

    void update(Connection connection, T object);
}
