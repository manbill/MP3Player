package com.android.mp3.player.fragment.file.browser.view;

import com.android.mp3.player.fragment.BaseFragment;
import com.android.mp3.player.manager.NewsManager;

/**
 * Created by liutiantian on 2016-04-16.
 */
public class GridViewFragment extends BaseFragment {
    @Override
    public NewsManager.NewsResult onNewsArrival(String sMsg, Object oMsg) {
        return NewsManager.NewsResult.CONTINUE;
    }
}
