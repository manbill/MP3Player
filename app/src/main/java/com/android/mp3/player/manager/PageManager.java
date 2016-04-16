package com.android.mp3.player.manager;

import android.app.Fragment;
import android.os.Bundle;

import com.android.lib.tools.log.MyLog;
import com.java.lib.oil.GlobalMethods;
import com.java.lib.oil.json.JsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by liutiantian on 2016-04-16.
 */
public class PageManager {
    private static PageManager mInstance;

    public static PageManager getInstance() {
        if (mInstance == null) {
            synchronized (PageManager.class) {
                if (mInstance == null) {
                    mInstance = new PageManager();
                }
            }
        }
        return mInstance;
    }

    private static final String KEY_PAGE_HISTORY = "page_history";
    private static final String KEY_CUR_PAGE = "cur_page";

    public static final String NEWS_PAGE_CHANGE = "page_change";

    private Stack<Page> mPageHistory;
    private PageChangeExecutor mPageChangeExecutor;
    private Page currentPage;
    private Page changingPage;

    private PageManager() {
        mPageHistory = new Stack<>();
    }

    public boolean back() {
        if (mPageChangeExecutor == null || mPageHistory.isEmpty()) {
            return false;
        }

        if (this.currentPage != null) {
            this.currentPage.canBack = false;
        }

        Page page = mPageHistory.pop();
        while (page == null && !mPageHistory.isEmpty()) {
            page = mPageHistory.pop();
        }

        if (page != null) {
            return showPage(page);
        }

        return false;
    }

    public Page getCurrentPage() {
        if (this.currentPage != null) {
            return this.currentPage;
        }
        return mPageHistory.isEmpty() ? null : mPageHistory.peek();
    }

    public Page getChangingPage() {
        return this.changingPage;
    }

    public boolean showPage(Class<? extends Fragment> screenClass, boolean canBack) {
        if (screenClass == null) {
            return false;
        }
        return showPage(new Page(screenClass, canBack));
    }

    public boolean showPage(Page page) {
        if (page == null || page.getScreenClass() == null) {
            return false;
        }

        if (GlobalMethods.getInstance().checkEqual(this.currentPage, page)) {
            return true;
        }

        if (this.currentPage != null && this.currentPage.canBack()) {
            if (mPageChangeExecutor != null && mPageChangeExecutor.shouldPushToHistory(this.currentPage, page)) {
                mPageHistory.push(this.currentPage);
            }
        }

        this.changingPage = page;
        if (this.currentPage == null) {
            if (mPageChangeExecutor != null) {
                if (mPageChangeExecutor.changeToPage(page)) {
                    this.currentPage = page;
                    this.changingPage = null;
                    NewsManager.getInstance().broadcastNews(NEWS_PAGE_CHANGE);
                    return true;
                }
            }
        }
        else {
            if (mPageChangeExecutor != null) {
                if (!GlobalMethods.getInstance().checkEqual(page.getScreenClass(), this.currentPage.getScreenClass())) {
                    if (!mPageChangeExecutor.changeScreenFragment(page.getScreenClass())) {
                        return false;
                    }
                }

                this.currentPage = page;
                this.changingPage = null;
                NewsManager.getInstance().broadcastNews(NEWS_PAGE_CHANGE);

                return true;
            }
        }

        return false;
    }

    public void registerPageChangeExecutor(PageChangeExecutor executor) {
        mPageChangeExecutor = executor;
    }

    public void unregisterPageChangeExecutor() {
        mPageChangeExecutor = null;
    }

    @SuppressWarnings("unchecked")
    public void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.currentPage = (Page) savedInstanceState.getSerializable(KEY_CUR_PAGE);
            MyLog.i("PageManager", "restoreInstanceState", "show currentPage: " + this.currentPage);
            ArrayList<Page> history = (ArrayList<Page>) savedInstanceState.getSerializable(KEY_PAGE_HISTORY);
            MyLog.i("PageManager", "restoreInstanceState", "show history: " + history);
            if (history != null) {
                for (Page page : history) {
                    mPageHistory.push(page);
                }
            }
        }
    }

    public void saveInstanceState(Bundle outState) {
        if (outState != null) {
            MyLog.i("PageManager", "saveInstanceState", "show this.currentPage != null: " + (this.currentPage != null));
            if (this.currentPage != null) {
                outState.putSerializable(KEY_CUR_PAGE, this.currentPage);
            }
            MyLog.i("PageManager", "saveInstanceState", "show !mPageHistory.isEmpty(): " + !mPageHistory.isEmpty());
            if (!mPageHistory.isEmpty()) {
                outState.putSerializable(KEY_PAGE_HISTORY, new ArrayList<>(mPageHistory));
            }
        }
    }

    public static class Page implements Serializable {
        private Class<? extends Fragment> screenClass;
        private boolean canBack;

        public Page(Class<? extends Fragment> screenClass, boolean canBack) {
            this.screenClass = screenClass;
            this.canBack = canBack;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Page) {
                Page page = (Page) obj;
                if (!GlobalMethods.getInstance().checkEqual(getScreenClass(), page.getScreenClass())) {
                    return false;
                }
                if (!GlobalMethods.getInstance().checkEqual(canBack(), page.canBack())) {
                    return false;
                }
                return true;
            }
            return false;
        }

        public Class<? extends Fragment> getScreenClass() {
            return this.screenClass;
        }

        public boolean canBack() {
            return this.canBack;
        }

        @Override
        public String toString() {
            JsonBuilder builder = new JsonBuilder();
            builder.append("screenClass", getScreenClass().getCanonicalName());
            builder.append("canBack", canBack());
            return builder.build();
        }
    }

    public void destroy() {
        this.currentPage = null;
        mPageHistory.clear();
        unregisterPageChangeExecutor();
    }

    public interface PageChangeExecutor {
        boolean changeToPage(Page page);
        boolean changeScreenFragment(Class<? extends Fragment> screenClass);
        boolean shouldPushToHistory(Page oldPage, Page newPage);
    }
}
