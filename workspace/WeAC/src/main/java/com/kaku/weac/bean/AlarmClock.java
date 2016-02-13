/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * 闹钟实例
 *
 * @author 咖枯
 * @version 1.0 2015/06
 */
public class AlarmClock extends DataSupport implements Parcelable {

    /**
     * 闹钟id
     */
    private int id;

    /**
     * 闹钟启动code
     */
    private int alarmClockCode;

    /**
     * 小时
     */
    private int hour;

    /**
     * 分钟
     */
    private int minute;

    /**
     * 重复
     */
    private String repeat;

    /**
     * 周期
     */
    private String weeks;

    /**
     * 标签
     */
    private String tag;

    /**
     * 铃声名
     */
    private String ringName;

    /**
     * 铃声地址
     */
    private String ringUrl;

    /**
     * 铃声选择标记界面
     */
    private int ringPager;

    /**
     * 音量
     */
    private int volume;

    /**
     * 振动
     */
    private boolean vibrate;

    /**
     * 小睡
     */
    private boolean nap;

    /**
     * 小睡间隔
     */
    private int napInterval;

    /**
     * 小睡次数
     */
    private int napTimes;

    /**
     * 天气提示
     */
    private boolean weaPrompt;

    /**
     * 开关
     */
    private boolean onOff;

    public AlarmClock() {
        super();
    }

    /**
     * 闹钟实例构造方法
     *
     * @param alarmClockCode 闹钟启动code
     * @param hour           小时
     * @param minute         分钟
     * @param repeat         重复
     * @param weeks          周期
     * @param tag            标签
     * @param ringName       铃声名
     * @param ringUrl        铃声地址
     * @param ringPager      铃声界面
     * @param volume         音量
     * @param vibrate        振动
     * @param nap            小睡
     * @param napInterval    小睡间隔
     * @param napTimes       小睡次数
     * @param weaPrompt      天气提示
     * @param onOff          开关
     */
    public AlarmClock(int alarmClockCode, int hour, int minute, String repeat,
                      String weeks, String tag, String ringName, String ringUrl,
                      int ringPager, int volume, boolean vibrate, boolean nap,
                      int napInterval, int napTimes, boolean weaPrompt, boolean onOff) {
        super();
        this.alarmClockCode = alarmClockCode;
        this.hour = hour;
        this.minute = minute;
        this.repeat = repeat;
        this.weeks = weeks;
        this.tag = tag;
        this.ringName = ringName;
        this.ringUrl = ringUrl;
        this.ringPager = ringPager;
        this.volume = volume;
        this.vibrate = vibrate;
        this.nap = nap;
        this.napInterval = napInterval;
        this.napTimes = napTimes;
        this.weaPrompt = weaPrompt;
        this.onOff = onOff;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeInt(alarmClockCode);
        out.writeInt(hour);
        out.writeInt(minute);
        out.writeString(repeat);
        out.writeString(weeks);
        out.writeString(tag);
        out.writeString(ringName);
        out.writeString(ringUrl);
        out.writeInt(ringPager);
        out.writeInt(volume);
        out.writeByte((byte) (vibrate ? 1 : 0));
        out.writeByte((byte) (nap ? 1 : 0));
        out.writeInt(napInterval);
        out.writeInt(napTimes);
        out.writeByte((byte) (weaPrompt ? 1 : 0));
        out.writeByte((byte) (onOff ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private AlarmClock(Parcel in) {
        id = in.readInt();
        alarmClockCode = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        repeat = in.readString();
        weeks = in.readString();
        tag = in.readString();
        ringName = in.readString();
        ringUrl = in.readString();
        ringPager = in.readInt();
        volume = in.readInt();
        vibrate = in.readByte() != 0;
        nap = in.readByte() != 0;
        napInterval = in.readInt();
        napTimes = in.readInt();
        weaPrompt = in.readByte() != 0;
        onOff = in.readByte() != 0;
    }

    public static final Parcelable.Creator<AlarmClock> CREATOR = new Creator<AlarmClock>() {

        @Override
        public AlarmClock createFromParcel(Parcel in) {
            return new AlarmClock(in);
        }

        @Override
        public AlarmClock[] newArray(int size) {

            return new AlarmClock[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNapInterval() {
        return napInterval;
    }

    public void setNapInterval(int napInterval) {
        this.napInterval = napInterval;
    }

    public int getNapTimes() {
        return napTimes;
    }

    public void setNapTimes(int napTimes) {
        this.napTimes = napTimes;
    }

    public int getAlarmClockCode() {
        return alarmClockCode;
    }

    public void setAlarmClockCode(int alarmClockCode) {
        this.alarmClockCode = alarmClockCode;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRingName() {
        return ringName;
    }

    public void setRingName(String ringName) {
        this.ringName = ringName;
    }

    public String getRingUrl() {
        return ringUrl;
    }

    public int getRingPager() {
        return ringPager;
    }

    public void setRingPager(int ringPager) {
        this.ringPager = ringPager;
    }

    public void setRingUrl(String ringUrl) {
        this.ringUrl = ringUrl;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public boolean isNap() {
        return nap;
    }

    public void setNap(boolean nap) {
        this.nap = nap;
    }

    public boolean isWeaPrompt() {
        return weaPrompt;
    }

    public void setWeaPrompt(boolean weaPrompt) {
        this.weaPrompt = weaPrompt;
    }

    public boolean isOnOff() {
        return onOff;
    }

    public void setOnOff(boolean onOff) {
        this.onOff = onOff;
    }
}
