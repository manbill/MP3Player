package com.java.lib.oil;

/**
 * Created by liutiantian on 2016-03-08.
 */
public class GlobalMethods {
    private static GlobalMethods mInstance;

    public static GlobalMethods getInstance() {
        if (mInstance == null) {
            synchronized (GlobalMethods.class) {
                if (mInstance == null) {
                    mInstance = new GlobalMethods();
                }
            }
        }
        return mInstance;
    }

    private GlobalMethods() {

    }

    public <E, V> boolean checkEqual(E e, V v) {
        if (e == null && v == null) {
            return true;
        }
        else if (e == null || v == null) {
            return false;
        }
        return e.equals(v);
    }

    public <E, V> boolean checkSame(E e, V v) {
        if (e == null && v == null) {
            return true;
        }
        else if (e == null || v == null) {
            return false;
        }
        return e == v;
    }
}
