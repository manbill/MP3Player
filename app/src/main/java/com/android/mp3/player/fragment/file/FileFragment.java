package com.android.mp3.player.fragment.file;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.lib.tools.ui.fragment.FragmentCache;
import com.android.mp3.player.fragment.BaseFragment;
import com.android.mp3.player.fragment.file.browser.BrowserFragment;
import com.android.mp3.player.manager.NewsManager;
import com.android.mp3.player.R;
import com.java.lib.oil.GlobalMethods;

/**
 * Created by liutiantian on 2016-04-16.
 */
public class FileFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file, container, false);

        Button chooseButton = (Button) view.findViewById(R.id.choose_file);
        if (chooseButton != null) {
            chooseButton.setOnClickListener(this);
        }

        Button autoScanButton = (Button) view.findViewById(R.id.auto_scan_file);
        if (autoScanButton != null) {
            autoScanButton.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public NewsManager.NewsResult onNewsArrival(String sMsg, Object oMsg) {
        return NewsManager.NewsResult.CONTINUE;
    }

    @Override
    public void onClick(View v) {
        if (getView() != null && v != null) {
            if (v.getId() == R.id.choose_file) {
                Fragment chooserFragment = getChildFragmentManager().findFragmentById(R.id.file_browser);
                if (chooserFragment == null || (!GlobalMethods.getInstance().checkEqual(chooserFragment.getClass(), BrowserFragment.class))) {
                    showFragment(R.id.file_browser, getChildFragmentManager(), FragmentCache.getInstance().getFragment(BrowserFragment.class));
                }
            }
        }
    }
}
