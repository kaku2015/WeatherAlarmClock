/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.activities;

import android.support.v4.app.Fragment;

import com.kaku.weac.fragment.AlarmClockOntimeFragment;

/**
 * 闹钟响起画面Activity
 *
 * @author 咖枯
 * @version 1.0 2015/06
 */
public class AlarmClockOntimeActivity extends SingleFragmentDialogActivity {

    @Override
    protected Fragment createFragment() {
        return new AlarmClockOntimeFragment();
    }

    @Override
    public void onBackPressed() {
        // 禁用back键
    }

}
