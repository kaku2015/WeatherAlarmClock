/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.fragment;

import android.support.v4.app.ListFragment;

import com.kaku.weac.LeakCanaryApplication;
import com.squareup.leakcanary.RefWatcher;

/**
 * ListFragment管理类
 *
 * @author 咖枯
 * @version 1.0 2015/11/25
 */
public class BaseListFragment extends ListFragment {
    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = LeakCanaryApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
