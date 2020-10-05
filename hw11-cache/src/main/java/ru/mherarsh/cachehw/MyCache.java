package ru.mherarsh.cachehw;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
    //Надо реализовать эти методы
    private final Map<HwListener<K, V>, Boolean> listeners = new WeakHashMap<>();
    private final Map<K, SoftReference<V>> values = new HashMap<>();
    private final int heapSize;

    public MyCache(int heapSize) {
        if (heapSize <= 0) {
            throw new IllegalArgumentException("Heap size should be > 0");
        }

        this.heapSize = heapSize;
    }

    @Override
    public void put(K key, V value) {
        deleteDeadReferences();

        if (!values.containsKey(key) && values.size() >= heapSize) {
            deleteRandomValue();
        }

        putValue(key, value);

        notifyListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        if (values.containsKey(key)) {
            notifyListeners(key, getValue(key), "remove");
            values.remove(key);
        }
    }

    @Override
    public V get(K key) {
        var value = getValue(key);
        notifyListeners(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.put(listener, true);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void deleteRandomValue() {
        var randomKey = values.keySet().stream().findAny();
        randomKey.ifPresent(values::remove);
    }

    private void deleteDeadReferences() {
        values.entrySet().removeIf(x -> x.getValue().get() == null);
    }

    private void notifyListeners(K key, V value, String action) {
        listeners.keySet().forEach(listener -> listener.notify(key, value, action));
    }

    private void putValue(K key, V value) {
        values.put(key, new SoftReference<>(value));
    }

    private V getValue(K key) {
        return values.get(key).get();
    }
}
