package com.android.mp3.player.manager;

import java.util.ArrayList;

/**
 * Created by liutiantian on 2016-04-16.
 */
public class NewsManager {
    private static NewsManager mInstance;

    public static NewsManager getInstance() {
        if (mInstance == null) {
            synchronized (NewsManager.class) {
                if (mInstance == null) {
                    mInstance = new NewsManager();
                }
            }
        }
        return mInstance;
    }

    private NewsManager() {
        mReceiverPool = new ArrayList<>();
    }

    private ArrayList<NewsReceiver> mReceiverPool;

    public enum NewsResult {
        CONTINUE,
        ABORT
    }

    public interface NewsReceiver {
        NewsResult onNewsArrival(String sMsg, Object oMsg);
    }

    public boolean registerNewsReceiver(NewsReceiver receiver) {
        if (receiver == null) {
            return false;
        }
        synchronized (NewsManager.class) {
            if (mReceiverPool.contains(receiver)) {
                return true;
            }
            return mReceiverPool.add(receiver);
        }
    }

    public boolean unregisterNewsReceiver(NewsReceiver receiver) {
        if (receiver == null) {
            return true;
        }
        synchronized (NewsManager.class) {
            if (mReceiverPool.contains(receiver)) {
                return mReceiverPool.remove(receiver);
            }
        }
        return true;
    }

    public void broadcastNews(String sMsg) {
        broadcastNews(sMsg, null);
    }

    public void broadcastNews(String sMsg, Object oMsg) {
        synchronized (NewsReceiver.class) {
            for (int i = 0; i < mReceiverPool.size(); ++i) {
                if (NewsResult.ABORT == mReceiverPool.get(i).onNewsArrival(sMsg, oMsg)) {
                    return;
                }
            }
        }
    }
}
