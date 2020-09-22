package ru.mherarsh.jdbc.mapper.impl;

import ru.mherarsh.jdbc.mapper.EntityClassMetaData;
import ru.mherarsh.jdbc.mapper.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mherarsh
 */
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final String name;

    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        name = clazz.getSimpleName();

        allFields = Arrays.asList(clazz.getDeclaredFields());

        idField = getAnnotatedIdFiled(allFields);

        fieldsWithoutId = Arrays.stream(clazz.getDeclaredFields()).filter(x -> !x.equals(idField)).collect(Collectors.toList());

        if (fieldsWithoutId.size() == 0) {
            throw new RuntimeException(clazz.getName() + " has no fields");
        }

        try {
            constructor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }

    private Field getAnnotatedIdFiled(List<Field> fields) {
        for (var field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        throw new RuntimeException("Тhe field marked with the annotation @id was not found");
    }
}
