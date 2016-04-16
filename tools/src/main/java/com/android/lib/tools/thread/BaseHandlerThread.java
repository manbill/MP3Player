package com.android.lib.tools.thread;

import android.content.Context;
import android.os.Handler;

/**
 * Created by wb-liutiantian.h on 2016/3/18.
 */
public class BaseHandlerThread extends BaseThread {
    private Handler handler;

    public BaseHandlerThread(Context context, Handler handler) {
        super(context);
        this.handler = handler;
    }

    public Handler getHandler() {
        return this.handler;
    }
}
