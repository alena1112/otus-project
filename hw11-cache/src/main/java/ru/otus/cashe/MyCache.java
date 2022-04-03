package ru.otus.cashe;


import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private final WeakHashMap<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        V foundValue = get(key);
        if (foundValue == null) {
            cache.put(key, value);
            listeners.forEach(l -> l.notify(key, value, "PUT"));
        } else {
            cache.replace(key, foundValue, value);
            listeners.forEach(l -> l.notify(key, value, "UPDATE"));
        }
    }

    @Override
    public void remove(K key) {
        V removedValue = cache.remove(key);
        if (removedValue != null) {
            listeners.forEach(l -> l.notify(key, removedValue, "REMOVE"));
        }
    }

    @Override
    public V get(K key) {
        V foundValue = cache.get(key);
        if (foundValue != null) {
            listeners.forEach(l -> l.notify(key, foundValue, "GET"));
        }
        return foundValue;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
}
