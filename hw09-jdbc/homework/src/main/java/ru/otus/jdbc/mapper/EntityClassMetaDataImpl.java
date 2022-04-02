package ru.otus.jdbc.mapper;

import ru.otus.db.core.repository.DataTemplateException;
import ru.otus.db.crm.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;
    private Field idField;
    private List<Field> allFields;
    private List<Field> fieldsWithoutId;
    private Constructor<T> constructor;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor != null ? constructor : findConstructor();
    }

    @Override
    public Field getIdField() {
        return idField != null ? idField : findIdField();
    }

    @Override
    public List<Field> getAllFields() {
        return allFields != null ? allFields : findAllFields();
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId != null ? fieldsWithoutId : findFieldsWithoutId();
    }

    private List<Field> findAllFields() {
        allFields = List.of(clazz.getDeclaredFields());
        allFields.forEach(field -> field.setAccessible(true));
        return allFields;
    }

    private Field findIdField() {
        idField = getAllFields().stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElse(null);
        if (idField == null) {
            throw new DataTemplateException("Id field not found in class " + getName());
        }
        return idField;
    }

    private List<Field> findFieldsWithoutId() {
        fieldsWithoutId = new ArrayList<>(getAllFields());
        fieldsWithoutId.remove(getIdField());
        return fieldsWithoutId;
    }

    private Constructor<T> findConstructor() {
        try {
            constructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new DataTemplateException("Could not find constructor without parameters in class " + getName(), e);
        }
        return constructor;
    }
}
