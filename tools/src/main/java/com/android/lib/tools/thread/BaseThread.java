package com.android.lib.tools.thread;

import android.content.Context;

/**
 * Created by wb-liutiantian.h on 2016/3/18.
 */
public class BaseThread extends Thread {
    private Context context;

    public BaseThread(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }
}
