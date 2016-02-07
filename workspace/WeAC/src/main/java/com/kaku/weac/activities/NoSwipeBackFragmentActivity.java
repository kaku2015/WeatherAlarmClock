/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.os.Bundle;

/**
 * 禁用滑动后退SingleFragmentActivity
 *
 * @author 咖枯
 * @version 1.0 2015/2/7
 */
public abstract class NoSwipeBackFragmentActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
    }

}
