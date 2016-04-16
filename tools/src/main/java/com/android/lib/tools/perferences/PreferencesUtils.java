package com.android.lib.tools.perferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wb-liutiantian.h on 2016/3/11.
 */
public class PreferencesUtils {
    private static PreferencesUtils mInstance;

    public static PreferencesUtils getInstance() {
        if (mInstance == null) {
            synchronized (PreferencesUtils.class) {
                if (mInstance == null) {
                    mInstance = new PreferencesUtils();
                }
            }
        }
        return mInstance;
    }

    private PreferencesUtils() {

    }

    public int readIntFromPreferences(Context context, String name, String key, int defValue) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        if (preferences != null) {
            return preferences.getInt(key, defValue);
        }
        return defValue;
    }

    public boolean writeIntToPreferences(Context context, String name, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        if (preferences != null) {
            preferences.edit().putInt(key, value).apply();
            return true;
        }
        return false;
    }

    public long readLongFromPreferences(Context context, String name, String key, long defValue) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        if (preferences != null) {
            return preferences.getLong(key, defValue);
        }
        return defValue;
    }

    public boolean writeLongToPreferences(Context context, String name, String key, long value) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        if (preferences != null) {
            preferences.edit().putLong(key, value).apply();
            return true;
        }
        return false;
    }

    public boolean readBooleanFromPreferences(Context context, String name, String key, boolean defValue) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        if (preferences != null) {
            return preferences.getBoolean(key, defValue);
        }
        return defValue;
    }

    public boolean writeBooleanToPreferences(Context context, String name, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        if (preferences != null) {
            preferences.edit().putBoolean(key, value).apply();
            return true;
        }
        return false;
    }

    public String readStringFromPreferences(Context context, String name, String key, String defValue) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        if (preferences != null) {
            return preferences.getString(key, defValue);
        }
        return defValue;
    }

    public boolean writeStringToPreferences(Context context, String name, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        if (preferences != null) {
            preferences.edit().putString(key, value).apply();
            return true;
        }
        return false;
    }

    public boolean removeFromPreferences(Context context, String name, String key) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        if (preferences != null) {
            preferences.edit().remove(key).apply();
            return true;
        }
        return false;
    }
}
