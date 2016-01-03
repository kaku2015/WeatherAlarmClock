package com.kaku.weac.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.IBinder;
import android.os.SystemClock;

import com.coolerfall.daemon.Daemon;
import com.kaku.weac.bean.AlarmClock;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.db.WeacDBMetaData;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;

public class DaemonService extends Service {

    /**
     * Log tag ：DaemonService
     */
    private static final String LOG_TAG = "DaemonService";

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(LOG_TAG, "onCreate");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Daemon.run(DaemonService.this, DaemonService.class, Daemon.INTERVAL_ONE_MINUTE);

                startService(new Intent(DaemonService.this, NotificationCenter.class));

                // FIXME: 修改或改为共用代码
                Cursor cursor = getContentResolver().query(WeacDBMetaData.CONTENT_URI,
                        null, null, null, WeacDBMetaData.SORT_ORDER);
                if (cursor != null) {
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        int id = cursor.getInt(cursor.getColumnIndex(WeacDBMetaData.AC_ID));
                        int hour = cursor.getInt(cursor
                                .getColumnIndex(WeacDBMetaData.AC_HOUR));
                        int minute = cursor.getInt(cursor
                                .getColumnIndex(WeacDBMetaData.AC_MINUTE));
                        String weeks = cursor.getString(cursor
                                .getColumnIndex(WeacDBMetaData.AC_WEEKS));
                        String repeat = cursor.getString(cursor
                                .getColumnIndex(WeacDBMetaData.AC_REPEAT));
                        String tag = cursor.getString(cursor
                                .getColumnIndex(WeacDBMetaData.AC_TAG));
                        String ringName = cursor.getString(cursor
                                .getColumnIndex(WeacDBMetaData.AC_RING_NAME));
                        String ringUrl = cursor.getString(cursor
                                .getColumnIndex(WeacDBMetaData.AC_RING_URL));
                        int ringPager = cursor.getInt(cursor
                                .getColumnIndex(WeacDBMetaData.AC_RING_PAGER));
                        int volume = cursor.getInt(cursor
                                .getColumnIndex(WeacDBMetaData.AC_VOLUME));
                        boolean vibrate = cursor.getInt(cursor
                                .getColumnIndex(WeacDBMetaData.AC_VIBRATE)) == 1;
                        boolean nap = cursor.getInt(cursor
                                .getColumnIndex(WeacDBMetaData.AC_NAP)) == 1;
                        int napInterval = cursor.getInt(cursor
                                .getColumnIndex(WeacDBMetaData.AC_NAP_INTERVAL));
                        int napTimes = cursor.getInt(cursor
                                .getColumnIndex(WeacDBMetaData.AC_NAP_TIMES));
                        boolean weaPrompt = cursor.getInt(cursor
                                .getColumnIndex(WeacDBMetaData.AC_WEA_PROMPT)) == 1;
                        boolean onOff = cursor.getInt(cursor
                                .getColumnIndex(WeacDBMetaData.AC_ON_OFF)) == 1;
                        AlarmClock alarmClock = new AlarmClock(id, hour, minute, repeat,
                                weeks, tag, ringName, ringUrl, ringPager, volume, vibrate,
                                nap, napInterval, napTimes, weaPrompt, onOff);

                        if (onOff) {
                            MyUtil.startAlarmClock(DaemonService.this, alarmClock);
                        }

                    }
                    cursor.close();
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
