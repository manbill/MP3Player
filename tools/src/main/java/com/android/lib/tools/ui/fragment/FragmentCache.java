package com.android.lib.tools.ui.fragment;

import android.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wb-liutiantian.h on 2016/3/11.
 */
public class FragmentCache {
    private static FragmentCache mInstance;

    public static FragmentCache getInstance() {
        if (mInstance == null) {
            synchronized (FragmentCache.class) {
                if (mInstance == null) {
                    mInstance = new FragmentCache();
                }
            }
        }
        return mInstance;
    }

    private FragmentCache() {
        mFragmentCache = new HashMap<>();
    }

    private Map<Object, ToolsFragment> mFragmentCache;

    public void registerFragment(Class<? extends ToolsFragment> classObject, ToolsFragment fragment) {
        if (classObject == null || fragment == null) {
            return;
        }
        if (mFragmentCache.containsKey(classObject)) {
            return;
        }
        mFragmentCache.put(classObject, fragment);
    }

    public void unregisterFragment(ToolsFragment fragment) {
        if (fragment == null) {
            return;
        }
        if (mFragmentCache.containsValue(fragment)) {
            mFragmentCache.remove(fragment.getClass());
        }
    }

    /**
     * this method will return an empty or dirty Fragment.
     * @param key the key to identify the Fragment
     * @return the Fragment identified by the {@param key}
     */
    public Fragment getFragment(Object key) {
        if (key == null) {
            return null;
        }

        ToolsFragment fragment = mFragmentCache.get(key);
        if (fragment == null) {
            if (key instanceof Class) {
                fragment = getFragment((Class) key);
                if (fragment != null) {
                    mFragmentCache.put(key, fragment);
                }
            }
        }
        return fragment;
    }

    public ToolsFragment getFragment(Class<?> classObject) {
        if (classObject != null) {
            ToolsFragment fragment = mFragmentCache.get(classObject);
            if (fragment == null && Fragment.class.isAssignableFrom(classObject)) {
                try {
                    fragment = (ToolsFragment) classObject.newInstance();
                    if (fragment != null) {
                        mFragmentCache.put(classObject, fragment);
                    }
                }
                catch (InstantiationException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return fragment;
        }
        return null;
    }

    public boolean destroy(Object key) {
        if (key != null && mFragmentCache.containsKey(key)) {
            if (mFragmentCache.remove(key) != null) {
                return true;
            }
        }
        return false;
    }

    public void destroy() {
        mFragmentCache.clear();
    }
}
