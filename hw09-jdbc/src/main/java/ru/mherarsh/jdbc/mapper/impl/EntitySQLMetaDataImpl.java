package ru.mherarsh.jdbc.mapper.impl;

import ru.mherarsh.jdbc.mapper.EntityClassMetaData;
import ru.mherarsh.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

/**
 * @author mherarsh
 */
public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final EntityClassMetaData<T> classMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> classMetaData) {
        this.classMetaData = classMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("select * from %s",
                classMetaData.getName())
                .toUpperCase();
    }

    @Override
    public String getSelectByIdSql() {
        return String.format("select * from %s where %s = ?",
                classMetaData.getName(), classMetaData.getIdField().getName())
                .toUpperCase();
    }

    @Override
    public String getInsertSql() {
        var fieldsWithoutId = classMetaData.getFieldsWithoutId();
        var fieldsPattern = fieldsWithoutId.stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));

        var valuesPattern = "?" + ", ?".repeat(fieldsWithoutId.size() - 1);

        return String.format("insert into %s (%s) values (%s)",
                classMetaData.getName(), fieldsPattern, valuesPattern)
                .toUpperCase();
    }

    @Override
    public String getUpdateSql() {
        var fieldsPattern = classMetaData.getFieldsWithoutId().stream()
                .map(field -> field.getName() + " = " + "?")
                .collect(Collectors.joining(", "));

        return String.format("UPDATE %s SET %s WHERE %s = ?",
                classMetaData.getName(), fieldsPattern, classMetaData.getIdField().getName())
                .toUpperCase();
    }
}
