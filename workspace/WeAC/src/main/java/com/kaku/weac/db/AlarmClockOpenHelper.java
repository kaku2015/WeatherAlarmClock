/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库操作类
 *
 * @author 咖枯
 * @version 1.0 2015/06/13
 */
class AlarmClockOpenHelper extends SQLiteOpenHelper {

    /**
     * 数据库操作类构造方法
     *
     * @param context context
     */
    public AlarmClockOpenHelper(Context context) {
        super(context, WeacDBMetaData.DATA_BASE_NAME, null,
                WeacDBMetaData.DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + WeacDBMetaData.TABLE_NAME + "("
                + WeacDBMetaData.AC_ID + " integer primary key,"
                + WeacDBMetaData.AC_HOUR + " integer,"
                + WeacDBMetaData.AC_MINUTE + " integer,"
                + WeacDBMetaData.AC_REPEAT + " text," + WeacDBMetaData.AC_WEEKS
                + " text," + WeacDBMetaData.AC_TAG + " text,"
                + WeacDBMetaData.AC_RING_NAME + " text,"
                + WeacDBMetaData.AC_RING_URL + " text,"
                + WeacDBMetaData.AC_RING_PAGER + " integer,"
                + WeacDBMetaData.AC_VOLUME + " integer,"
                + WeacDBMetaData.AC_VIBRATE + " integer,"
                + WeacDBMetaData.AC_NAP + " integer,"
                + WeacDBMetaData.AC_NAP_INTERVAL + " integer,"
                + WeacDBMetaData.AC_NAP_TIMES + " integer,"
                + WeacDBMetaData.AC_WEA_PROMPT + " integer,"
                + WeacDBMetaData.AC_ON_OFF + " integer" + ")";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
