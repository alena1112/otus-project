package ru.otus.jdbc.mapper;

import ru.otus.db.core.repository.DataTemplate;
import ru.otus.db.core.repository.DataTemplateException;
import ru.otus.db.core.repository.executor.DbExecutor;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

import static ru.otus.db.core.repository.executor.DbExecutorImpl.NULL_VALUE;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createEntity(rs);
                }
                return null;
            } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), (Function<ResultSet, List<T>>) rs -> {
            var entityList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    entityList.add(createEntity(rs));
                }
                return entityList;
            } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T entity) {
        try {
            List<Object> fieldValues = getFieldValuesWithoutId(entity);
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), fieldValues);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T entity) {
        try {
            List<Object> params = getFieldValuesWithoutId(entity);
            params.add(getIdValue(entity));
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T createEntity(ResultSet rs) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        T entity = entityClassMetaData.getConstructor().newInstance();
        entityClassMetaData.getAllFields().forEach(field -> {
            try {
                Object fieldValue = rs.getObject(field.getName());
                field.set(entity, fieldValue);
            } catch (SQLException | IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        });
        return entity;
    }

    private List<Object> getFieldValuesWithoutId(T entity) {
        var fieldValues = new ArrayList<>();
        entityClassMetaData.getFieldsWithoutId().forEach(field -> {
            try {
                Object value = field.get(entity);
                fieldValues.add(Objects.requireNonNullElse(value, NULL_VALUE));
            } catch (IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        });
        return fieldValues;
    }

    private Object getIdValue(T entity) {
        try {
            return entityClassMetaData.getIdField().get(entity);
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
    }
}
