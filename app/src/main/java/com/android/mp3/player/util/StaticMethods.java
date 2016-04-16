package com.android.mp3.player.util;

/**
 * Created by liutiantian on 2016-04-16.
 */
public class StaticMethods {
    private static StaticMethods mInstance;

    public static StaticMethods getInstance() {
        if (mInstance == null) {
            synchronized (StaticMethods.class) {
                if (mInstance == null) {
                    mInstance = new StaticMethods();
                }
            }
        }
        return mInstance;
    }

    private StaticMethods() {

    }
}
