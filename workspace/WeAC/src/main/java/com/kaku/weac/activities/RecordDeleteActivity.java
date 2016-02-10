/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.support.v4.app.Fragment;

import com.kaku.weac.fragment.RecordDeleteFragment;

/**
 * 录音删除activity
 *
 * @author 咖枯
 * @version 1.0 2015/08
 */
public class RecordDeleteActivity extends SingleFragmentDialogActivity {

    @Override
    protected Fragment createFragment() {
        return new RecordDeleteFragment();
    }

}