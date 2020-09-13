package ru.mherarsh;

import javax.json.Json;
import javax.json.JsonValue;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PrimitiveToJsonValueConverter {
    private static final Map<Class<?>, Function<Object, JsonValue>> converterMap = new HashMap<>();

    static {
        converterMap.put(Byte.class, value -> Json.createValue((Byte) value));
        converterMap.put(byte.class, value -> Json.createValue((byte) value));
        converterMap.put(Short.class, value -> Json.createValue((Short) value));
        converterMap.put(short.class, value -> Json.createValue((short) value));
        converterMap.put(Integer.class, value -> Json.createValue((Integer) value));
        converterMap.put(int.class, value -> Json.createValue((int) value));
        converterMap.put(Long.class, value -> Json.createValue((Long) value));
        converterMap.put(long.class, value -> Json.createValue((long) value));
        converterMap.put(Character.class, value -> Json.createValue(String.valueOf(value)));
        converterMap.put(char.class, value -> Json.createValue(String.valueOf(value)));
        converterMap.put(Float.class, value -> Json.createValue((Float) value));
        converterMap.put(float.class, value -> Json.createValue((float) value));
        converterMap.put(Double.class, value -> Json.createValue((Double) value));
        converterMap.put(double.class, value -> Json.createValue((double) value));
        converterMap.put(Boolean.class, value -> Json.createValue(String.valueOf(value)));
        converterMap.put(boolean.class, value -> Json.createValue(String.valueOf(value)));
        converterMap.put(String.class, value -> Json.createValue((String) value));
    }

    public static JsonValue toJsonValue(Class<?> type, Object value) {
        if(type == null || !converterMap.containsKey(type)){
            throw new IllegalArgumentException(String.format("Type '%s' not supported", type));
        }

        return converterMap.get(type).apply(value);
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
