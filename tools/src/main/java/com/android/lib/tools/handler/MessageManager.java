package com.android.lib.tools.handler;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.os.Message;

/**
 * a class to manage Handlers.
 * @author liutiantian
 *
 */
public class MessageManager {
    private Map<Object, Handler> mHandlerPool;

    private static MessageManager mInstance;

    /**
     * get the static instance of this class
     * @return the static instance of this class
     */
    public static MessageManager getInstance() {
        if (mInstance == null) {
            synchronized (MessageManager.class) {
                if (mInstance == null) {
                    mInstance = new MessageManager();
                }
            }
        }
        return mInstance;
    }

    private MessageManager() {
        mHandlerPool = new HashMap<>(8);
    }

    /**
     * add a handler to the handler pool.<br>
     * if the name already existed, it will overwrite the old one.
     * @param name the identifier of the handler, null or empty will be set to default automatically.
     * @param handler a handler object
     */
    public void addHandler(Object name, Handler handler) {
        synchronized (MessageManager.class) {
            if (name == null) {
                mHandlerPool.put("default", handler);
                return;
            }
            mHandlerPool.put(name, handler);
        }
    }

    /**
     * check whether current handler pool has a handler has the {@param code} key.
     * @param name the handler key
     * @return true if current handler pool has the key, otherwise false.
     */
    public boolean containsHandler(Object name) {
        synchronized (MessageManager.class) {
            if (name == null) {
                name = "default";
            }
            return mHandlerPool.containsKey(name);
        }
    }

    /**
     * remove a handler from the handler pool.
     * @param name the handler name.
     */
    public void removeHandler(Object name) {
        synchronized (MessageManager.class) {
            if (name == null) {
                name = "default";
            }
            mHandlerPool.remove(name);
        }
    }

    /**
     * broadcast empty message to all the handlers in the current pool.
     * @param what the message.
     */
    public void broadcastEmptyMessage(int what) {
        synchronized (MessageManager.class) {
            for (Map.Entry<Object, Handler> entry : mHandlerPool.entrySet()) {
                if (entry.getValue() != null) {
                    entry.getValue().sendEmptyMessage(what);
                }
            }
        }
    }

    /**
     * broadcast message to all the handlers in the current pool.
     * @param what the message
     * @param obj the message obj attached
     */
    public void broadcastMessage(int what, Object obj) {
        synchronized (MessageManager.class) {
            for (Map.Entry<Object, Handler> entry : mHandlerPool.entrySet()) {
                if (entry.getValue() != null) {
                    entry.getValue().obtainMessage(what, obj).sendToTarget();
                }
            }
        }
    }

    /**
     * broadcast message to all the handlers in the current pool.
     * @param message the message.
     */
    public void broadcastMessage(Message message) {
        synchronized (MessageManager.class) {
            for (Map.Entry<Object, Handler> entry : mHandlerPool.entrySet()) {
                if (entry.getValue() != null) {
                    entry.getValue().sendMessage(message);
                }
            }
        }
    }

    /**
     * broadcast empty message to all the handlers in the current pool with a delay.
     * @param what the message.
     * @param delay time of delay.
     */
    public void broadcastEmptyMessageDelayed(int what, long delay) {
        synchronized (MessageManager.class) {
            for (Map.Entry<Object, Handler> entry : mHandlerPool.entrySet()) {
                if (entry.getValue() != null) {
                    entry.getValue().sendEmptyMessageDelayed(what, delay);
                }
            }
        }
    }

    /**
     * broadcast message to all the handlers in the current pool.
     * @param what the message
     * @param obj the message obj attached
     * @param delay the delay milliseconds delayed
     */
    public void broadcastMessageDelayed(int what, Object obj, long delay) {
        synchronized (MessageManager.class) {
            for (Map.Entry<Object, Handler> entry : mHandlerPool.entrySet()) {
                if (entry.getValue() != null) {
                    entry.getValue().sendMessageDelayed(entry.getValue().obtainMessage(what, obj), delay);
                }
            }
        }
    }

    /**
     * broadcast message to all the handlers in the current pool with a delay.
     * @param message the message.
     * @param delay time of delay.
     */
    public void broadcastMessageDelayed(Message message, long delay) {
        synchronized (MessageManager.class) {
            for (Map.Entry<Object, Handler> entry : mHandlerPool.entrySet()) {
                if (entry.getValue() != null) {
                    entry.getValue().sendMessageDelayed(message, delay);
                }
            }
        }
    }

    /**
     * remove message from all the handlers in the current handler pool.
     * @param what the message.
     */
    public void removeMessages(int what) {
        synchronized (MessageManager.class) {
            for (Map.Entry<Object, Handler> entry : mHandlerPool.entrySet()) {
                if (entry.getValue() != null) {
                    entry.getValue().removeMessages(what);
                }
            }
        }
    }

    /**
     * send empty message to the handler named name.
     * @param name the handler name, null or empty will redirect the message to the default handler.
     * @param what the message.
     * @return true if the message is send successfully, others false.
     */
    public boolean sendEmptyMessage(Object name, int what) {
        synchronized (MessageManager.class) {
            if (name == null) {
                name = "default";
            }

            Handler handler = mHandlerPool.get(name);
            if (handler != null) {
                handler.sendEmptyMessage(what);
                return true;
            }
            return false;
        }
    }

    /**
     * send message to handler named name.
     * @param name the handler name, null or empty will redirect the message to the default handler.
     * @param what the message.
     * @param obj the obj property of the sent message
     * @return true if the message is send successfully, others false.
     */
    public boolean sendMessage(Object name, int what, Object obj) {
        synchronized(MessageManager.class) {
            if(name == null) {
                name = "default";
            }
            Handler handler = mHandlerPool.get(name);
            if(handler != null) {
                handler.obtainMessage(what, obj).sendToTarget();
                return true;
            }
            return false;
        }
    }

    /**
     * send message to handler named name.
     * @param name the handler name, null or empty will redirect the message to the default handler.
     * @param message the message.
     * @return true if the message is send successfully, others false.
     */
    public boolean sendMessage(Object name, Message message) {
        synchronized (MessageManager.class) {
            if (name == null) {
                name = "default";
            }

            Handler handler = mHandlerPool.get(name);
            if (handler != null) {
                handler.sendMessage(message);
                return true;
            }
            return false;
        }
    }

    /**
     * send empty message to the handler named name with a delay.
     * @param name the handler name, null or empty will redirect the message to the default handler.
     * @param what the message.
     * @param delay time of delay.
     * @return true if the message is send successfully, others false.
     */
    public boolean sendEmptyMessageDelayed(Object name, int what, long delay) {
        synchronized (MessageManager.class) {
            if (name == null) {
                name = "default";
            }

            Handler handler = mHandlerPool.get(name);
            if (handler != null) {
                handler.sendEmptyMessageDelayed(what, delay);
                return true;
            }
            return false;
        }
    }

    /**
     * send message to the handler named name with a delay.
     * @param name the handler name, null or empty will redirect the message to the default handler.
     * @param what the message.
     * @param obj the obj property of the sent message
     * @param delay time of delay.
     * @return true if the message is send successfully, others false.
     */
    public boolean sendMessageDelayed(Object name, int what, Object obj, long delay) {
        synchronized(MessageManager.class) {
            if(name == null) {
                name = "default";
            }

            Handler handler = mHandlerPool.get(name);
            if(handler != null) {
                handler.sendMessageDelayed(handler.obtainMessage(what, obj), delay);
                return true;
            }
            return false;
        }
    }

    /**
     * send message to the handler named name with a delay.
     * @param name the handler name, null or empty will redirect the message to the default handler.
     * @param message the message.
     * @param delay time of delay.
     * @return true if the message is send successfully, others false.
     */
    public boolean sendMessageDelayed(Object name, Message message, long delay) {
        synchronized (MessageManager.class) {
            if (name == null) {
                name = "default";
            }

            Handler handler = mHandlerPool.get(name);
            if (handler != null) {
                handler.sendMessageDelayed(message, delay);
                return true;
            }
            return false;
        }
    }

    /**
     * remove message from the handler named name.
     * @param name the handler name.
     * @param what the message intended to be removed.
     */
    public void removeMessages(Object name, int what) {
        synchronized (MessageManager.class) {
            if (name == null) {
                name = "default";
            }

            Handler handler = mHandlerPool.get(name);
            if (handler != null) {
                handler.removeMessages(what);
            }
        }
    }

    /**
     * clear all the cache. this method is recommended to be called in the activity's onDestroy method.
     */
    public void destroy() {
        synchronized (MessageManager.class) {
            mHandlerPool.clear();
        }
    }

    /**
     * a simple Handler model.<br>
     * what is host object?<br>
     * because this handler was designed for nested class usage, the host is where the handler class nested.<br>
     * if you do not use a handler this way, please use pure Handler class or some other model.
     * @author liutiantian
     *
     * @param <T>
     */
    public abstract static class BaseHandler<T> extends Handler {
        /**
         * this is a soft reference to the initial host object, activity or view or something.<br>
         * it is a soft reference so that this handler object may not disturb the object's life circle.
         */
        private SoftReference<T> mHost;

        /**
         * @param host the initial host object, activity or view or something
         */
        public BaseHandler(T host) {
            if (host != null) {
                mHost = new SoftReference<>(host);
            }
        }

        /**
         * this method may return null if the inner host container is null or host is not valid.
         * @return Host object.
         */
        public T getHost() {
            if (mHost != null) {
                T host = mHost.get();
                if (validHost(host)) {
                    return host;
                }
                mHost = null;
            }
            return null;
        }

        /**
         * set host object to this handler.
         * @param host host object.
         */
        public void setHost(T host) {
            if (host != null) {
                mHost = new SoftReference<T>(host);
            }
        }

        /**
         * you must verify the host is still workable when call handleMessage called.
         * @param host the host object.
         * @return true if the host is still workable, others false.
         */
        protected abstract boolean validHost(T host);
    }
}
