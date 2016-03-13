/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.common;

/**
 * 闹钟状态类
 *
 * @author 咖枯
 * @version 1.0 2015/07
 */
public class WeacStatus {

    /**
     * 启动的AlarmClockOnTimeActivity个数
     */
    public static int sActivityNumber = 0;

    /**
     * 上一次闹钟响起时间
     */
    public static long sLastStartTime = 0;

    /**
     * 上一次响起级别（1：闹钟，2：小睡，0：无）
     */
    public static int sStrikerLevel = 0;
}
