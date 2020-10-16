package ru.mherarsh.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
    //Надо реализовать эти методы
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);
    private final Set<HwListener<K, V>> listeners = Collections.newSetFromMap(new WeakHashMap<>());
    private final Map<K, V> values = new WeakHashMap<>();

    @Override
    public void put(K key, V value) {
        values.put(key, value);
        notifyListeners(key, value, CacheActions.PUT);
    }

    @Override
    public void remove(K key) {
        var value = values.remove(key);
        if (value != null) {
            notifyListeners(key, value, CacheActions.REMOVE);
        }
    }

    @Override
    public V get(K key) {
        return values.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, CacheActions action) {
        listeners.forEach(listener -> {
            try {
                listener.notify(key, value, action.name());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }
}
