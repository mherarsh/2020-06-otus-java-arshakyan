package ru.mherarsh;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

import static ru.mherarsh.PrimitiveToJsonValueConverter.isPrimitive;
import static ru.mherarsh.PrimitiveToJsonValueConverter.toJsonValue;

public class MyGson {
    public String toJson(Object value) throws Exception {
        return serializeByType(value).toString();
    }

    private JsonValue serializeByType(Object value) throws Exception {
        if (value == null) {
            return JsonValue.NULL;
        }

        var valueType = value.getClass();

        if (isPrimitive(valueType)) {
            return serializePrimitives(valueType, value);
        } else if (valueType.isArray()) {
            return serializeArrays(value);
        } else if (Iterable.class.isAssignableFrom(valueType)) {
            return serializeIterables(value);
        } else {
            return serializeObject(value);
        }
    }

    private JsonValue serializeObject(Object value) throws Exception {
        var jsonObject = Json.createObjectBuilder();

        var fields = getFields(value);
        for (var field : fields) {
            addJsonValue(jsonObject, field, value);
        }

        return jsonObject.build();
    }

    private void addJsonValue(JsonObjectBuilder jsonObject, Field field, Object object) throws Exception {
        field.setAccessible(true);

        var fieldValue = field.get(object);
        if (fieldValue == null) {
            return;
        }

        jsonObject.add(field.getName(), serializeByType(fieldValue));
    }

    private JsonValue serializePrimitives(Type type, Object value) {
        return toJsonValue(type, value);
    }

    private JsonArray serializeArrays(Object array) throws Exception {
        var jsonArray = Json.createArrayBuilder();
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            var value = Array.get(array, i);
            jsonArray.add(serializeByType(value));
        }

        return jsonArray.build();
    }

    private JsonArray serializeIterables(Object iterable) throws Exception {
        var jsonArray = Json.createArrayBuilder();

        for (var value : (Iterable) iterable) {
            jsonArray.add(serializeByType(value));
        }

        return jsonArray.build();
    }

    private Field[] getFields(Object obj) {
        return obj.getClass().getDeclaredFields();
    }
}

