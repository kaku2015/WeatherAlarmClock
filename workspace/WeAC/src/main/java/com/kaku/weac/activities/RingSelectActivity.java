/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.support.v4.app.Fragment;

import com.kaku.weac.fragment.RingSelectFragment;

/**
 * 铃声选择activity
 *
 * @author 咖枯
 * @version 1.0 2015
 */
public class RingSelectActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RingSelectFragment();
    }

}