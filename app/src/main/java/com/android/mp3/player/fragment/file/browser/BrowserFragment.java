package com.android.mp3.player.fragment.file.browser;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.lib.tools.perferences.PreferencesUtils;
import com.android.lib.tools.ui.fragment.FragmentCache;
import com.android.mp3.player.R;
import com.android.mp3.player.fragment.BaseFragment;
import com.android.mp3.player.fragment.file.browser.view.GridViewFragment;
import com.android.mp3.player.fragment.file.browser.view.ListViewFragment;
import com.android.mp3.player.manager.NewsManager;
import com.android.mp3.player.util.StaticFields;
import com.java.lib.oil.GlobalMethods;

/**
 * Created by liutiantian on 2016-04-16.
 */
public class BrowserFragment extends BaseFragment implements View.OnClickListener {

    private static final String KEY_CURRENT_VIEW = "current_view";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browser, container, false);

        initButton(view, R.id.list_confirm);
        initButton(view, R.id.list_back);
        initButton(view, R.id.list_switch_view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (view != null) {
            Fragment oldFragment = getChildFragmentManager().findFragmentById(R.id.browser_view_fragment);
            if (oldFragment == null) {
                String browserView = PreferencesUtils.getInstance().readStringFromPreferences(getActivity(), null, StaticFields.KEY.USER_CHOOSE_FILE_BROWSER, "ListViewFragment");
                if (savedInstanceState != null && savedInstanceState.getString(KEY_CURRENT_VIEW) != null) {
                    browserView = savedInstanceState.getString(KEY_CURRENT_VIEW);
                }
                if (GlobalMethods.getInstance().checkEqual(browserView, "ListViewFragment")) {
                    showFragment(R.id.browser_view_fragment, getChildFragmentManager(), FragmentCache.getInstance().getFragment(ListViewFragment.class));
                }
                else {
                    showFragment(R.id.browser_view_fragment, getChildFragmentManager(), FragmentCache.getInstance().getFragment(GridViewFragment.class));
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Fragment viewFragment = getChildFragmentManager().findFragmentById(R.id.browser_view_fragment);
        if (viewFragment == null || GlobalMethods.getInstance().checkEqual(viewFragment.getClass(), ListViewFragment.class)) {
            outState.putString(KEY_CURRENT_VIEW, "ListViewFragment");
        }
        else {
            outState.putString(KEY_CURRENT_VIEW, "GridViewFragment");
        }
    }

    private void initButton(View view, int id) {
        if (view != null) {
            Button button = (Button) view.findViewById(id);
            if (button != null) {
                button.setOnClickListener(this);
            }
        }
    }

    @Override
    public NewsManager.NewsResult onNewsArrival(String sMsg, Object oMsg) {
        return NewsManager.NewsResult.CONTINUE;
    }

    @Override
    public void onClick(View v) {
        if (getView() != null && v != null) {
            if (v.getId() == R.id.list_back) {
                NewsManager.getInstance().broadcastNews("browser_back");
            }
            else if (v.getId() == R.id.list_confirm) {
                NewsManager.getInstance().broadcastNews("browser_confirm");
            }
            else if (v.getId() == R.id.list_switch_view) {
                Fragment oldFragment = getChildFragmentManager().findFragmentById(R.id.browser_view_fragment);
                if (oldFragment != null) {
                    if (GlobalMethods.getInstance().checkEqual(oldFragment.getClass(), ListViewFragment.class)) {
                        PreferencesUtils.getInstance().writeStringToPreferences(getActivity(), null, StaticFields.KEY.USER_CHOOSE_FILE_BROWSER, "GridViewFragment");
                        showFragment(R.id.browser_view_fragment, getChildFragmentManager(), FragmentCache.getInstance().getFragment(GridViewFragment.class));
                    }
                    else {
                        PreferencesUtils.getInstance().writeStringToPreferences(getActivity(), null, StaticFields.KEY.USER_CHOOSE_FILE_BROWSER, "ListViewFragment");
                        showFragment(R.id.browser_view_fragment, getChildFragmentManager(), FragmentCache.getInstance().getFragment(ListViewFragment.class));
                    }
                }
            }
        }
    }
}
