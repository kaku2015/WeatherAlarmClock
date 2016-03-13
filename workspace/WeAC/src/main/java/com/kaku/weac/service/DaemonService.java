/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;

import com.coolerfall.daemon.Daemon;
import com.kaku.weac.bean.AlarmClock;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.db.AlarmClockOperate;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;

import java.util.List;

public class DaemonService extends Service {

    /**
     * Log tag ：DaemonService
     */
    private static final String LOG_TAG = "DaemonService";

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(LOG_TAG, "onCreate");

        // Notification notification = new Notification();
        // startForeground(-1, notification);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Daemon.run(DaemonService.this, DaemonService.class, Daemon.INTERVAL_ONE_MINUTE);

//                startService(new Intent(DaemonService.this, NotificationCenter.class));

                List<AlarmClock> list = AlarmClockOperate.getInstance().loadAlarmClocks();
                for (AlarmClock alarmClock : list) {
                    // 当闹钟为开时刷新开启闹钟
                    if (alarmClock.isOnOff()) {
                        MyUtil.startAlarmClock(DaemonService.this, alarmClock);
                    }
                }

                SharedPreferences preferences = getSharedPreferences(
                        WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
                // 倒计时时间
                long countdown = preferences.getLong(WeacConstants.COUNTDOWN_TIME, 0);
                boolean isStop = preferences.getBoolean(WeacConstants.IS_STOP, false);
                if (countdown != 0 && !isStop) {
                    long now = SystemClock.elapsedRealtime();
                    long remainTime = countdown - now;
                    if (remainTime > 0 && (remainTime / 1000 / 60) < 60) {
                        MyUtil.startAlarmTimer(DaemonService.this, remainTime);
                    }
                }
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(LOG_TAG, "onDestroy");
        startService(new Intent(this, DaemonService.class));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        LogUtil.d(LOG_TAG, "onStartCommand");
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }
}
