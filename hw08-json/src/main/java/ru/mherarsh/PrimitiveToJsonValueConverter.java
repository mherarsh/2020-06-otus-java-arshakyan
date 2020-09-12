package ru.mherarsh;

import javax.json.Json;
import javax.json.JsonValue;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PrimitiveToJsonValueConverter {
    private static final Map<Type, ValueConverter> converterMap = new HashMap<>() {{
        put(Byte.class, value -> Json.createValue((Byte) value));
        put(byte.class, value -> Json.createValue((byte) value));
        put(Short.class, value -> Json.createValue((Short) value));
        put(short.class, value -> Json.createValue((short) value));
        put(Integer.class, value -> Json.createValue((Integer) value));
        put(int.class, value -> Json.createValue((int) value));
        put(Long.class, value -> Json.createValue((Long) value));
        put(long.class, value -> Json.createValue((long) value));
        put(Character.class, value -> Json.createValue(String.valueOf(value)));
        put(char.class, value -> Json.createValue(String.valueOf(value)));
        put(Float.class, value -> Json.createValue((Float) value));
        put(float.class, value -> Json.createValue((float) value));
        put(Double.class, value -> Json.createValue((Double) value));
        put(double.class, value -> Json.createValue((double) value));
        put(Boolean.class, value -> Json.createValue(String.valueOf(value)));
        put(boolean.class, value -> Json.createValue(String.valueOf(value)));
        put(String.class, value -> Json.createValue((String) value));
    }};

    public static JsonValue toJsonValue(Type type, Object value) {
        if(type == null || !converterMap.containsKey(type)){
            throw new IllegalArgumentException(String.format("Type '%s' not supported", type));
        }

        return converterMap.get(type).convert(value);
    }

    public static boolean isPrimitive(Class<?> type) {
        return type.isPrimitive()
                || Number.class.isAssignableFrom(type)
                || type.equals(String.class)
                || type.equals(Character.class);
    }

    private interface ValueConverter {
        JsonValue convert(Object value);
    }
}
