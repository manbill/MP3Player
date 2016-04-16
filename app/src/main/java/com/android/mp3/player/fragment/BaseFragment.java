package com.android.mp3.player.fragment;

import android.os.Bundle;

import com.android.lib.tools.ui.fragment.ToolsFragment;
import com.android.mp3.player.manager.NewsManager;

/**
 * Created by liutiantian on 2016-04-16.
 */
public abstract class BaseFragment extends ToolsFragment implements NewsManager.NewsReceiver {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NewsManager.getInstance().registerNewsReceiver(this);
    }

    public void onDestroy() {
        super.onDestroy();
        NewsManager.getInstance().unregisterNewsReceiver(this);
    }
}
