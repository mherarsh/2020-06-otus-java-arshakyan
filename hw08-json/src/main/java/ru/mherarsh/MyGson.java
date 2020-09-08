package ru.mherarsh;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class MyGson {
    public String toJson(Object obj) throws Exception {
        return serializeObject(obj).toString();
    }

    private JsonValue serializeObject(Object obj) throws Exception {
        var jsonObject = Json.createObjectBuilder();

        if (obj == null) {
            return jsonObject.build();
        }

        var fields = getFields(obj);
        for (var field : fields) {
            field.setAccessible(true);

            var fieldValue = field.get(obj);
            if (fieldValue == null) {
                continue;
            }

            var fieldName = field.getName();
            var fieldType = field.getType();

            if (isPrimitive(fieldType)) {
                serializePrimitives(jsonObject, fieldName, fieldValue);
            } else if (fieldType.isArray()) {
                serializeArrays(jsonObject, fieldName, fieldValue);
            } else if (Iterable.class.isAssignableFrom(fieldType)) {
                serializeIterables(jsonObject, fieldName, fieldValue);
            } else {
                jsonObject.add(fieldName, serializeObject(fieldValue));
            }
        }

        return jsonObject.build();
    }

    private void serializePrimitives(JsonObjectBuilder jsonObject, String fieldName, Object fieldValue) {
        jsonObject.add(fieldName, Json.createValue("" + fieldValue));
    }

    private void serializeArrays(JsonObjectBuilder jsonObject, String fieldName, Object fieldValue) throws Exception {
        var jsonArray = Json.createArrayBuilder();
        int length = Array.getLength(fieldValue);
        for (int i = 0; i < length; i++) {
            var value = Array.get(fieldValue, i);
            validateFieldType(value.getClass(), fieldName);

            jsonArray.add("" + value);
        }

        jsonObject.add(fieldName, jsonArray.build());
    }

    private void serializeIterables(JsonObjectBuilder jsonObject, String fieldName, Object fieldValue) {
        var jsonArray = Json.createArrayBuilder();

        for (var value : (Iterable) fieldValue) {
            validateFieldType(value.getClass(), fieldName);

            jsonArray.add("" + value);
        }

        jsonObject.add(fieldName, jsonArray.build());
    }

    private void validateFieldType(Class<?> type, String fieldName) {
        if (!isPrimitive(type)) {
            throw new IllegalArgumentException("field '" + fieldName + "' contains not supported data types");
        }
    }

    private Field[] getFields(Object obj) {
        return obj.getClass().getDeclaredFields();
    }

    private boolean isPrimitive(Class<?> type) {
        return type.isPrimitive()
                || Number.class.isAssignableFrom(type)
                || type.equals(String.class)
                || type.equals(Character.class);
    }
}
