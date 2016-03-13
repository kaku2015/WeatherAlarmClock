/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.fragment;


import android.support.v4.app.Fragment;

import com.kaku.weac.LeakCanaryApplication;
import com.squareup.leakcanary.RefWatcher;


/**
 * Fragment管理类
 *
 * @author 咖枯
 * @version 1.0 2015/11/25
 */
public class BaseFragment extends Fragment {
    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = LeakCanaryApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
