/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.support.v4.app.Fragment;

import com.kaku.weac.fragment.NapEditFragment;

/**
 * 小睡修改activity
 *
 * @author 咖枯
 * @version 1.0 2015/07
 */
public class NapEditActivity extends NoSwipeBackFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new NapEditFragment();
    }

}