package com.kaku.weac.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;

import com.coolerfall.daemon.Daemon;
import com.kaku.weac.bean.AlarmClock;
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
        Daemon.run(this, DaemonService.class, 0);

        startService(new Intent(this, NotificationCenter.class));

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
                    MyUtil.startAlarmClock(this, alarmClock);
                }

            }
            cursor.close();
        }
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
        LogUtil.d(LOG_TAG, "onStartCommand");
        /* do something here */
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }
}
