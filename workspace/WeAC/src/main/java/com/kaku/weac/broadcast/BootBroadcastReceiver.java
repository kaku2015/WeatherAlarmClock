/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.kaku.weac.bean.AlarmClock;
import com.kaku.weac.db.WeacDBMetaData;
import com.kaku.weac.service.NotificationCenter;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;

/**
 * 接收手机启动广播
 *
 * @author 咖枯
 * @version 1.0 2015
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.i("BootBroadcastReceiver", "onReceive ");
        Intent i = new Intent(context, NotificationCenter.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(i);

        // FIXME: 修改或改为共用代码
        Cursor cursor = context.getContentResolver().query(WeacDBMetaData.CONTENT_URI,
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
                    MyUtil.startAlarmClock(context, alarmClock);
                }

            }
            cursor.close();
        }
    }

}
