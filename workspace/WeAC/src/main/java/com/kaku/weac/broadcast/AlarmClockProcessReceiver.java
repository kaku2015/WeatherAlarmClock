/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kaku.weac.bean.Event.AlarmClockUpdateEvent;
import com.kaku.weac.util.OttoAppConfig;

/**
 * 单次闹钟响起，通过此BroadcastReceiver来实现多进程通信，更新闹钟开关
 *
 * @author 咖枯
 * @version 1.0 2016/2/13
 */
public class AlarmClockProcessReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        OttoAppConfig.getInstance().post(new AlarmClockUpdateEvent());
    }
}
