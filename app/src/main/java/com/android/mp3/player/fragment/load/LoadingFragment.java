package com.android.mp3.player.fragment.load;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mp3.player.fragment.BaseFragment;
import com.android.mp3.player.manager.NewsManager;
import com.android.mp3.player.R;

/**
 * Created by liutiantian on 2016-04-16.
 */
public class LoadingFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public NewsManager.NewsResult onNewsArrival(String sMsg, Object oMsg) {
        return NewsManager.NewsResult.CONTINUE;
    }
}
