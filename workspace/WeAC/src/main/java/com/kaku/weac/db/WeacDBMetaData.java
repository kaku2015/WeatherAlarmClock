/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.db;

import android.net.Uri;

/**
 * 数据库weac的操作元数据信息
 *
 * @author 咖枯
 * @version 1.0 2015/06
 */
public class WeacDBMetaData {

    /**
     * 数据库名
     */
    public static final String DATA_BASE_NAME = "weac.db";

    /**
     * 数据库版本
     */
    public static final int DATA_BASE_VERSION = 1;

    /**
     * 表名
     */
    public static final String TABLE_NAME = "AlarmClock";

    /**
     * id
     */
    public static final String AC_ID = "id";

    /**
     * 小时
     */
    public static final String AC_HOUR = "hour";

    /**
     * 分钟
     */
    public static final String AC_MINUTE = "minute";

    /**
     * 周重复信息描述
     */
    public static final String AC_REPEAT = "repeat";

    /**
     * 周重复信息
     */
    public static final String AC_WEEKS = "weeks";

    /**
     * 标签描述信息
     */
    public static final String AC_TAG = "tag";

    /**
     * 铃声名
     */
    public static final String AC_RING_NAME = "ringName";

    /**
     * 铃声地址
     */
    public static final String AC_RING_URL = "ringUrl";

    /**
     * 铃声选择标记界面
     */
    public static final String AC_RING_PAGER = "ringPager";

    /**
     * 音量
     */
    public static final String AC_VOLUME = "volume";

    /**
     * 振动
     */
    public static final String AC_VIBRATE = "vibrate";

    /**
     * 小睡
     */
    public static final String AC_NAP = "nap";

    /**
     * 小睡间隔
     */
    public static final String AC_NAP_INTERVAL = "nap_interval";

    /**
     * 小睡次数
     */
    public static final String AC_NAP_TIMES = "nap_times";

    /**
     * 天气提示
     */
    public static final String AC_WEA_PROMPT = "weaPrompt";

    /**
     * 闹钟开关
     */
    public static final String AC_ON_OFF = "onOff";

    /**
     * 匹配查询AlarmClock表Uri的返回值
     */
    public static final int ALARM_CLOCK_DIR = 1;

    /**
     * 提供器权限，需要在AndroidManifest文件中指定
     */
    public static final String AUTHORITY = "com.kaku.weac";

    /**
     * 按小时，分钟排序
     */
    public static final String SORT_ORDER = "hour,minute asc";

    /**
     * 查询AlarmClock表的Uri
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/AlarmClock");

}
