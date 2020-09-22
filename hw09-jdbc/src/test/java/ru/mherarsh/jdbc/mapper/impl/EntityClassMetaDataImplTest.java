package ru.mherarsh.jdbc.mapper.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mherarsh.core.model.User;
import ru.mherarsh.jdbc.mapper.EntityClassMetaData;
import ru.mherarsh.jdbc.mapper.Id;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mherarsh
 */
class EntityClassMetaDataImplTest {
    private EntityClassMetaData<User> classMetaData;
    private final Class<User> clazz = User.class;

    @BeforeEach
    void setUp() {
        classMetaData = new EntityClassMetaDataImpl<>(clazz);
    }

    @Test
    void getName() {
        assertEquals(classMetaData.getName(), clazz.getSimpleName());
    }

    @Test
    void getConstructor() throws NoSuchMethodException {
        assertEquals(classMetaData.getConstructor(), clazz.getConstructor());
    }

    @Test
    void getIdField() {
        assertTrue(classMetaData.getIdField().isAnnotationPresent(Id.class));
    }

    @Test
    void getAllFields() {
        assertEquals(classMetaData.getAllFields(), Arrays.asList(clazz.getDeclaredFields()));
    }

    @Test
    void getFieldsWithoutId() {
        var idField = classMetaData.getIdField();
        var fields = Arrays.stream(clazz.getDeclaredFields()).filter(x -> !x.equals(idField)).collect(Collectors.toList());

        assertEquals(classMetaData.getFieldsWithoutId(), fields);
    }
}