package com.kaku.weac.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kaku.weac.bean.AlarmClock;

/**
 * 表AlarmClock操作类
 *
 * @author 咖枯
 * @version 1.0 2015/08/08 update
 */
public class TabAlarmClockOperate {

    private final Context mContext;
    private static TabAlarmClockOperate mTabAlarmClockOperate;
    private AlarmClockOpenHelper mHelper;

    /**
     * 表AlarmClock操作类构造方法
     *
     * @param context context
     */
    private TabAlarmClockOperate(Context context) {
        super();
        mContext = context;
        mHelper = new AlarmClockOpenHelper(mContext);
    }

    public synchronized static TabAlarmClockOperate getInstance(Context context) {
        if (mTabAlarmClockOperate == null) {
            mTabAlarmClockOperate = new TabAlarmClockOperate(context);
        }
        return mTabAlarmClockOperate;
    }

    /**
     * 插入AlarmClock表
     *
     * @param ac alarmClock实例
     */
    public void insert(AlarmClock ac) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues cv = setACContentValues(ac);
        // 插入AlarmClock表
        db.insert(WeacDBMetaData.TABLE_NAME, null, cv);
        db.close();
        // 通知Uri观察者数据库有更新
        mContext.getContentResolver().notifyChange(WeacDBMetaData.CONTENT_URI,
                null);
    }

    /**
     * 删除AlarmClock表
     *
     * @param id AlarmClock表的Id
     */
    public void delete(int id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String whereClause = "id = ?";
        String whereArgs[] = new String[]{String.valueOf(id)};
        // 删除对应Id的闹钟数据
        db.delete(WeacDBMetaData.TABLE_NAME, whereClause, whereArgs);
        db.close();

        mContext.getContentResolver().notifyChange(WeacDBMetaData.CONTENT_URI,
                null);
    }

    /**
     * 更新AlarmClock表修改信息
     *
     * @param ac AlarmClock实例
     */
    public void update(AlarmClock ac) {
        SQLiteDatabase mDb = mHelper.getWritableDatabase();
        ContentValues cv = setACContentValues(ac);

        String whereClause = "id = ?";
        String whereArgs[] = new String[]{String.valueOf(ac
                .getAlarmClockCode())};
        // 更新AlarmClock表
        mDb.update(WeacDBMetaData.TABLE_NAME, cv, whereClause, whereArgs);
        mDb.close();

        mContext.getContentResolver().notifyChange(WeacDBMetaData.CONTENT_URI,
                null);
    }

    /**
     * 更新AlarmClock表的开关信息
     *
     * @param onOff 开关
     * @param id    对应的id
     */
    public void update(int onOff, int id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(WeacDBMetaData.AC_ON_OFF, onOff);
        String whereClause = "id = ?";
        String whereArgs[] = new String[]{String.valueOf(id)};
        // 更新AlarmClock表
        db.update(WeacDBMetaData.TABLE_NAME, cv, whereClause, whereArgs);
        db.close();

        mContext.getContentResolver().notifyChange(WeacDBMetaData.CONTENT_URI,
                null);
    }

    /**
     * 设置列全部更新的ContentValues
     *
     * @param ac AlarmClock实例
     * @return ContentValues
     */
    private ContentValues setACContentValues(AlarmClock ac) {
        ContentValues cv = new ContentValues();
        cv.put(WeacDBMetaData.AC_HOUR, ac.getHour());
        cv.put(WeacDBMetaData.AC_MINUTE, ac.getMinute());
        cv.put(WeacDBMetaData.AC_REPEAT, ac.getRepeat());
        cv.put(WeacDBMetaData.AC_WEEKS, ac.getWeeks());
        cv.put(WeacDBMetaData.AC_TAG, ac.getTag());
        cv.put(WeacDBMetaData.AC_RING_NAME, ac.getRingName());
        cv.put(WeacDBMetaData.AC_RING_URL, ac.getRingUrl());
        cv.put(WeacDBMetaData.AC_RING_PAGER, ac.getRingPager());
        cv.put(WeacDBMetaData.AC_VOLUME, ac.getVolume());
        cv.put(WeacDBMetaData.AC_VIBRATE, ac.isVibrate() ? 1 : 0);
        cv.put(WeacDBMetaData.AC_NAP, ac.isNap() ? 1 : 0);
        cv.put(WeacDBMetaData.AC_NAP_INTERVAL, ac.getNapInterval());
        cv.put(WeacDBMetaData.AC_NAP_TIMES, ac.getNapTimes());
        cv.put(WeacDBMetaData.AC_WEA_PROMPT, ac.isWeaPrompt() ? 1 : 0);
        cv.put(WeacDBMetaData.AC_ON_OFF, ac.isOnOff() ? 1 : 0);
        return cv;
    }
}
