/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.os.Bundle;

/**
 * 禁用滑动后退BaseActivity
 *
 * @author 咖枯
 * @version 1.0 2015/2/7
 */
public class NoSwipeBackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
    }

}
