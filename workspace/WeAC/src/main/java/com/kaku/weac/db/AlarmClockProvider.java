package com.kaku.weac.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * 向CursorLoader提供Uri查询AlarmClock表
 *
 * @author 咖枯
 * @version 1.0 2015/06
 */
public class AlarmClockProvider extends ContentProvider {

    private static final UriMatcher sUriMathcer;

    static {
        sUriMathcer = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMathcer.addURI(WeacDBMetaData.AUTHORITY, WeacDBMetaData.TABLE_NAME,
                WeacDBMetaData.ALARM_CLOCK_DIR);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // 匹配查询表Uri
        if (sUriMathcer.match(uri) == WeacDBMetaData.ALARM_CLOCK_DIR) {
            AlarmClockOpenHelper helper = new AlarmClockOpenHelper(getContext());
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.query(WeacDBMetaData.TABLE_NAME, projection,
                    selection, selectionArgs, null, null, sortOrder);
            if (cursor != null) {
                // 注册Uri
                cursor.setNotificationUri(getContext().getContentResolver(),
                        WeacDBMetaData.CONTENT_URI);
            }
            return cursor;
        } else {
            return null;
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }

}
