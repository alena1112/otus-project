package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> entityClassMetaData;
    private String selectAllSql;
    private String selectByIdSql;
    private String insertSql;
    private String updateSql;

    private static final String SELECT_ALL_SQL = "SELECT %s FROM %s e;";
    private static final String SELECT_BY_ID_SQL = "SELECT %s FROM %s e WHERE e.%s = ?;";
    private static final String INSERT_SQL = "INSERT INTO %s (%s) VALUES (%s);";
    private static final String UPDATE_SQL = "UPDATE %s SET %s WHERE %s = ?;";

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        if (selectAllSql == null) {
            selectAllSql = String.format(SELECT_ALL_SQL, generateColumnsNames(true), entityClassMetaData.getName());
        }
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        if (selectByIdSql == null) {
            selectByIdSql = String.format(SELECT_BY_ID_SQL, generateColumnsNames(true), entityClassMetaData.getName(),
                    entityClassMetaData.getIdField().getName());
        }
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        if (insertSql == null) {
            String columnsNames = generateColumnsNames(false);
            insertSql = String.format(INSERT_SQL, entityClassMetaData.getName(), columnsNames,
                    generateInsertValues(entityClassMetaData.getFieldsWithoutId().size()));
        }
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        if (updateSql == null) {
            String setState = generateUpdateSet();
            updateSql = String.format(UPDATE_SQL, entityClassMetaData.getName(), setState,
                    entityClassMetaData.getIdField().getName());
        }
        return updateSql;
    }

    private String generateUpdateSet() {
        return entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> String.format("%s = ?", field.getName()))
                .collect(Collectors.joining(","));
    }

    private String generateColumnsNames(boolean withId) {
        return (withId ? entityClassMetaData.getAllFields() : entityClassMetaData.getFieldsWithoutId())
                .stream().map(Field::getName)
                .collect(Collectors.joining(","));
    }

    private String generateInsertValues(int paramsAmount) {
        return IntStream.range(0, paramsAmount)
                .mapToObj(f -> "?")
                .collect(Collectors.joining(","));
    }
}
