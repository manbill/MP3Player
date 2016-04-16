package com.java.lib.oil.cache;

import com.java.lib.oil.SolidLengthDeque;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

public class DataCache<K, V> {
    private SolidLengthDeque<K> keys;
    private int maxCacheSize;
    private LinkedHashMap<K, SoftReference<V>> cache;

    public DataCache() {
        this(256);
    }

    public DataCache(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
        this.keys = new SolidLengthDeque<K>(this.maxCacheSize);
        this.cache = new LinkedHashMap<K, SoftReference<V>>(this.maxCacheSize);
    }

    public V put(K key, V value) {
        K removed = this.keys.addLastSafely(key);
        this.cache.put(key, new SoftReference<V>(value));
        if(removed != null) {
            return this.cache.get(removed).get();
        }
        return null;
    }

    public V remove(K key) {
        V value = this.cache.remove(key).get();
        this.keys.remove(key);
        return value;
    }

    public V get(K key) {
        SoftReference<V> soft = this.cache.get(key);
        if(soft != null) {
            return soft.get();
        }
        return null;
    }

    public int size() {
        return this.cache.size();
    }

    public boolean isEmpty() {
        return this.cache.isEmpty();
    }

    public boolean containsKey(K key) {
        return this.cache.containsKey(key);
    }

    public boolean containsValue(V value) {
        return this.cache.containsValue(value);
    }
}
