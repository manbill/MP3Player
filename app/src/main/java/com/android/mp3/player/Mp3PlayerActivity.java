package com.android.mp3.player;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.android.lib.tools.handler.MessageManager;
import com.android.lib.tools.ui.fragment.FragmentCache;
import com.android.mp3.player.manager.PageManager;
import com.android.mp3.player.process.StartProcess;
import com.java.lib.oil.GlobalMethods;

/**
 * Created by liutiantian on 2016-04-16.
 */
public class Mp3PlayerActivity extends Activity implements PageManager.PageChangeExecutor {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mp3_player);

        PageManager.getInstance().registerPageChangeExecutor(this);

        if (savedInstanceState == null) {
            StartProcess.getInstance(this).start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageManager.getInstance().destroy();
        PageManager.getInstance().unregisterPageChangeExecutor();
        FragmentCache.getInstance().destroy();
    }

    @Override
    public boolean changeToPage(PageManager.Page page) {
        if (page != null && page.getScreenClass() != null) {
            Fragment rootFragment = getFragmentManager().findFragmentById(R.id.root_fragment);
            if (rootFragment == null || !GlobalMethods.getInstance().checkEqual(rootFragment.getClass(), page.getScreenClass())) {
                getFragmentManager().beginTransaction().replace(R.id.root_fragment, FragmentCache.getInstance().getFragment(page.getScreenClass())).commitAllowingStateLoss();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean changeScreenFragment(Class<? extends Fragment> screenClass) {
        if (screenClass != null) {
            Fragment rootFragment = getFragmentManager().findFragmentById(R.id.root_fragment);
            if (rootFragment == null || !GlobalMethods.getInstance().checkEqual(rootFragment.getClass(), screenClass)) {
                getFragmentManager().beginTransaction().replace(R.id.root_fragment, FragmentCache.getInstance().getFragment(screenClass)).commitAllowingStateLoss();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldPushToHistory(PageManager.Page oldPage, PageManager.Page newPage) {
        return false;
    }
}
