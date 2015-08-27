package com.kaku.weac.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 闹钟实例
 * 
 * @author 咖枯
 * @version 1.0 2015/06
 * 
 */
public class AlarmClock implements Parcelable {

	/**
	 * 闹钟启动code
	 */
	private int mAlarmClockCode;

	/**
	 * 小时
	 */
	private int mHour;

	/**
	 * 分钟
	 */
	private int mMinute;

	/**
	 * 重复
	 */
	private String mRepeat;

	/**
	 * 周期
	 */
	private String mWeeks;

	/**
	 * 标签
	 */
	private String mTag;

	/**
	 * 铃声名
	 */
	private String mRingName;

	/**
	 * 铃声地址
	 */
	private String mRingUrl;

	/**
	 * 铃声选择标记界面
	 */
	private int mRingPager;

	/**
	 * 音量
	 */
	private int mVolume;

	/**
	 * 振动
	 */
	private boolean mVibrate;

	/**
	 * 小睡
	 */
	private boolean mNap;

	/**
	 * 小睡间隔
	 */
	private int mNapInterval;

	/**
	 * 小睡次数
	 */
	private int mNapTimes;

	/**
	 * 天气提示
	 */
	private boolean mWeaPrompt;

	/**
	 * 开关
	 */
	private boolean mOnOff;

	public AlarmClock() {
		super();
	}

	/**
	 * 闹钟实例构造方法
	 * 
	 * @param alarmClockCode
	 *            闹钟启动code
	 * @param hour
	 *            小时
	 * @param minute
	 *            分钟
	 * @param repeat
	 *            重复
	 * @param weeks
	 *            周期
	 * @param tag
	 *            标签
	 * @param ringName
	 *            铃声名
	 * @param ringUrl
	 *            铃声地址
	 * @param ringPager
	 *            铃声界面
	 * @param volume
	 *            音量
	 * @param vibrate
	 *            振动
	 * @param nap
	 *            小睡
	 * @param napInterval
	 *            小睡间隔
	 * @param napTimes
	 *            小睡次数
	 * @param weaPrompt
	 *            天气提示
	 * @param onOff
	 *            开关
	 */
	public AlarmClock(int alarmClockCode, int hour, int minute, String repeat,
			String weeks, String tag, String ringName, String ringUrl,
			int ringPager, int volume, boolean vibrate, boolean nap,
			int napInterval, int napTimes, boolean weaPrompt, boolean onOff) {
		super();
		mAlarmClockCode = alarmClockCode;
		mHour = hour;
		mMinute = minute;
		mRepeat = repeat;
		mWeeks = weeks;
		mTag = tag;
		mRingName = ringName;
		mRingUrl = ringUrl;
		mRingPager = ringPager;
		mVolume = volume;
		mVibrate = vibrate;
		mNap = nap;
		mNapInterval = napInterval;
		mNapTimes = napTimes;
		mWeaPrompt = weaPrompt;
		mOnOff = onOff;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(mAlarmClockCode);
		out.writeInt(mHour);
		out.writeInt(mMinute);
		out.writeString(mRepeat);
		out.writeString(mWeeks);
		out.writeString(mTag);
		out.writeString(mRingName);
		out.writeString(mRingUrl);
		out.writeInt(mRingPager);
		out.writeInt(mVolume);
		out.writeByte((byte) (mVibrate ? 1 : 0));
		out.writeByte((byte) (mNap ? 1 : 0));
		out.writeInt(mNapInterval);
		out.writeInt(mNapTimes);
		out.writeByte((byte) (mWeaPrompt ? 1 : 0));
		out.writeByte((byte) (mOnOff ? 1 : 0));
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public AlarmClock(Parcel in) {
		mAlarmClockCode = in.readInt();
		mHour = in.readInt();
		mMinute = in.readInt();
		mRepeat = in.readString();
		mWeeks = in.readString();
		mTag = in.readString();
		mRingName = in.readString();
		mRingUrl = in.readString();
		mRingPager = in.readInt();
		mVolume = in.readInt();
		mVibrate = in.readByte() != 0;
		mNap = in.readByte() != 0;
		mNapInterval = in.readInt();
		mNapTimes = in.readInt();
		mWeaPrompt = in.readByte() != 0;
		mOnOff = in.readByte() != 0;
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

	public int getNapInterval() {
		return mNapInterval;
	}

	public void setNapInterval(int napInterval) {
		mNapInterval = napInterval;
	}

	public int getNapTimes() {
		return mNapTimes;
	}

	public void setNapTimes(int napTimes) {
		mNapTimes = napTimes;
	}

	public int getAlarmClockCode() {
		return mAlarmClockCode;
	}

	public void setAlarmClockCode(int alarmClockCode) {
		mAlarmClockCode = alarmClockCode;
	}

	public int getHour() {
		return mHour;
	}

	public void setHour(int hour) {
		mHour = hour;
	}

	public int getMinute() {
		return mMinute;
	}

	public void setMinute(int minute) {
		mMinute = minute;
	}

	public String getRepeat() {
		return mRepeat;
	}

	public void setRepeat(String repeat) {
		mRepeat = repeat;
	}

	public String getWeeks() {
		return mWeeks;
	}

	public void setWeeks(String weeks) {
		mWeeks = weeks;
	}

	public String getTag() {
		return mTag;
	}

	public void setTag(String tag) {
		mTag = tag;
	}

	public String getRingName() {
		return mRingName;
	}

	public void setRingName(String ringName) {
		mRingName = ringName;
	}

	public String getRingUrl() {
		return mRingUrl;
	}

	public int getRingPager() {
		return mRingPager;
	}

	public void setRingPager(int ringPager) {
		mRingPager = ringPager;
	}

	public void setRingUrl(String ringUrl) {
		mRingUrl = ringUrl;
	}

	public int getVolume() {
		return mVolume;
	}

	public void setVolume(int volume) {
		mVolume = volume;
	}

	public boolean isVibrate() {
		return mVibrate;
	}

	public void setVibrate(boolean vibrate) {
		mVibrate = vibrate;
	}

	public boolean isNap() {
		return mNap;
	}

	public void setNap(boolean nap) {
		mNap = nap;
	}

	public boolean isWeaPrompt() {
		return mWeaPrompt;
	}

	public void setWeaPrompt(boolean weaPrompt) {
		mWeaPrompt = weaPrompt;
	}

	public boolean isOnOff() {
		return mOnOff;
	}

	public void setOnOff(boolean onOff) {
		mOnOff = onOff;
	}

}
