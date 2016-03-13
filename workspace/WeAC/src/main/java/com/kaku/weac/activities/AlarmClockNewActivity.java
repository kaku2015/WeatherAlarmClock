/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.activities;

import android.support.v4.app.Fragment;

import com.kaku.weac.R;
import com.kaku.weac.fragment.AlarmClockNewFragment;

/**
 * 新建闹钟activity
 *
 * @author 咖枯
 * @version 1.0 2015
 */
public class AlarmClockNewActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AlarmClockNewFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 按下返回键开启渐变缩小动画
        overridePendingTransition(0, R.anim.zoomout);
    }

}