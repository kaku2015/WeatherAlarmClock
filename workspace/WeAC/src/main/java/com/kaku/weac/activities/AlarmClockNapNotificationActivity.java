/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.activities;

import android.os.Bundle;

import com.kaku.weac.bean.AlarmClock;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.util.MyUtil;

/**
 * 闹钟小睡通知Activity
 *
 * @author 咖枯
 * @version 1.0 2015
 */
public class AlarmClockNapNotificationActivity extends BaseActivitySimple {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlarmClock alarmClock = getIntent().getParcelableExtra(
                WeacConstants.ALARM_CLOCK);
        // 关闭小睡
        MyUtil.cancelAlarmClock(this, -alarmClock.getId());
        finish();
    }

}
