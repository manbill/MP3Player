package com.android.lib.tools.ui.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

/**
 * Created by wb-liutiantian.h on 2016/3/15.
 */
public class ToolsFragment extends Fragment {

    public ToolsFragment() {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentCache.getInstance().registerFragment(getClass(), this);
    }

    public void onDestroy() {
        super.onDestroy();
        FragmentCache.getInstance().unregisterFragment(this);
    }

    public void showFragment(int id, Fragment fragment) {
        showFragment(id, fragment, 0, 0);
    }

    public void showFragment(int id, Fragment fragment, int enter, int exit) {
        showFragment(id, fragment, enter, exit, 0, 0);
    }

    public void showFragment(int id, Fragment fragment, int enter, int exit, int popEnter, int popExit) {
        showFragment(id, getFragmentManager(), fragment, enter, exit, popEnter, popExit);
    }

    public void showFragment(int id, FragmentManager manager, Fragment fragment) {
        showFragment(id, manager, fragment, 0, 0);
    }

    public void showFragment(int id, FragmentManager manager, Fragment fragment, int enter, int exit) {
        showFragment(id, manager, fragment, enter, exit, 0, 0);
    }

    public void showFragment(int id, FragmentManager manager, Fragment fragment, int enter, int exit, int popEnter, int popExit) {
        if (fragment != null) {
            Fragment oldFragment = manager.findFragmentById(id);
            if (oldFragment == null || oldFragment != fragment) {
                manager.beginTransaction().setCustomAnimations(enter, exit, popEnter, popExit).replace(id, fragment).commitAllowingStateLoss();
            }
        }
    }
}
