package com.java.lib.oil.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * this class hold a strong reference to the value, remember call clear when your application destroyed.
 */
public class SingleTanPool {
    private static SingleTanPool mInstance;

    public static SingleTanPool getInstance() {
        if (mInstance == null) {
            synchronized(SingleTanPool.class) {
                if(mInstance == null) {
                    mInstance = new SingleTanPool();
                }
            }
        }
        return mInstance;
    }

    private SingleTanPool() {
        this.pools = new HashMap<>();
    }

    private Map<String, Object> pools;

    @SuppressWarnings("unchecked")
    public <T> T get(String id, T defValue) {
        Object value = this.pools.get(id);
        if (value != null) {
            return (T) value;
        }
        return defValue;
    }

    public <T> void put(String id, T value) {
        if (value != null) {
            this.pools.put(id, value);
        }
    }

    public void clear(String id) {
        this.pools.remove(id);
    }

    public void clear() {
        this.pools.clear();
    }
}
