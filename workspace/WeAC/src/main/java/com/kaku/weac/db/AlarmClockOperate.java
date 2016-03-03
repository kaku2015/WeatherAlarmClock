/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.db;

import android.content.ContentValues;

import com.kaku.weac.bean.AlarmClock;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

/**
 * 闹钟db操作类
 *
 * @author 咖枯
 * @version 1.0 2016/02/12
 */
public class AlarmClockOperate {
    private static AlarmClockOperate mAlarmClockDBOperate;

    private AlarmClockOperate() {
        Connector.getDatabase();
    }

    public synchronized static AlarmClockOperate getInstance() {
        if (mAlarmClockDBOperate == null) {
            mAlarmClockDBOperate = new AlarmClockOperate();
        }
        return mAlarmClockDBOperate;
    }

    public boolean saveAlarmClock(AlarmClock AlarmClock) {
        return AlarmClock != null && AlarmClock.saveFast();
    }

    public void updateAlarmClock(AlarmClock alarmClock) {
        if (alarmClock != null) {
            // alarmClock.update(alarmClock.getId()); 此方法不能修改默认值
            // 如果想要将某一列的数据修改成默认值的话，还需要借助setToDefault()方法。
            // 不适合做闹钟更新，故采用ContentValues方法更新
            // http://blog.csdn.net/guolin_blog/article/details/40083685
            ContentValues cv = setContentValues(alarmClock);
            DataSupport.update(AlarmClock.class, cv, alarmClock.getId());
        }
    }

    /**
     * 更新AlarmClock表的开关信息
     *
     * @param onOff 开关
     * @param id    对应的id
     */
    public void updateAlarmClock(boolean onOff, int id) {
        ContentValues cv = new ContentValues();
        cv.put(WeacDBMetaDataLitePal.AC_ON_OFF, onOff);
        DataSupport.update(AlarmClock.class, cv, id);
    }

    public List<AlarmClock> loadAlarmClocks() {
        List<AlarmClock> alarmClockList;
        alarmClockList = DataSupport.order("hour,minute asc").find(AlarmClock.class);
        return alarmClockList;
    }

    public void deleteAlarmClock(AlarmClock alarmClock) {
        if (alarmClock != null) {
            alarmClock.delete();
        }
    }

    /**
     * 设置列全部更新的ContentValues
     *
     * @param ac AlarmClock实例
     * @return ContentValues
     */
    private ContentValues setContentValues(AlarmClock ac) {
        ContentValues cv = new ContentValues();
        cv.put(WeacDBMetaDataLitePal.AC_HOUR, ac.getHour());
        cv.put(WeacDBMetaDataLitePal.AC_MINUTE, ac.getMinute());
        cv.put(WeacDBMetaDataLitePal.AC_REPEAT, ac.getRepeat());
        cv.put(WeacDBMetaDataLitePal.AC_WEEKS, ac.getWeeks());
        cv.put(WeacDBMetaDataLitePal.AC_TAG, ac.getTag());
        cv.put(WeacDBMetaDataLitePal.AC_RING_NAME, ac.getRingName());
        cv.put(WeacDBMetaDataLitePal.AC_RING_URL, ac.getRingUrl());
        cv.put(WeacDBMetaDataLitePal.AC_RING_PAGER, ac.getRingPager());
        cv.put(WeacDBMetaDataLitePal.AC_VOLUME, ac.getVolume());
        cv.put(WeacDBMetaDataLitePal.AC_VIBRATE, ac.isVibrate());
        cv.put(WeacDBMetaDataLitePal.AC_NAP, ac.isNap());
        cv.put(WeacDBMetaDataLitePal.AC_NAP_INTERVAL, ac.getNapInterval());
        cv.put(WeacDBMetaDataLitePal.AC_NAP_TIMES, ac.getNapTimes());
        cv.put(WeacDBMetaDataLitePal.AC_WEA_PROMPT, ac.isWeaPrompt());
        cv.put(WeacDBMetaDataLitePal.AC_ON_OFF, ac.isOnOff());
        return cv;
    }
}
