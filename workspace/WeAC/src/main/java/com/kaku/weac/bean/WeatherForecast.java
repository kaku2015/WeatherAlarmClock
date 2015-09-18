package com.kaku.weac.bean;

/**
 * 天气多天预报信息
 *
 * @author 咖枯
 * @version 1.0 2015/09/18
 */
public class WeatherForecast {
    /**
     * 日期
     */
    String mDate;

    /**
     * 高温
     */
    String mHigh;

    /**
     * 低温
     */
    String mLow;

    /**
     * 白天天气类型
     */
    String mTypeDay;

    /**
     * 晚上天气类型
     */
    String mTypeNight;

    /**
     * 白天风向
     */
    String mWindDirectionDay;

    /**
     * 晚上风向
     */
    String mWindDirectionNight;

    /**
     * 白天风力
     */
    String mWindPowerDay;

    /**
     * 晚上风力
     */
    String mWindPowerNight;

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getHigh() {
        return mHigh;
    }

    public void setHigh(String high) {
        mHigh = high;
    }

    public String getLow() {
        return mLow;
    }

    public void setLow(String low) {
        mLow = low;
    }

    public String getTypeDay() {
        return mTypeDay;
    }

    public void setTypeDay(String typeDay) {
        mTypeDay = typeDay;
    }

    public String getTypeNight() {
        return mTypeNight;
    }

    public void setTypeNight(String typeNight) {
        mTypeNight = typeNight;
    }

    public String getWindDirectionDay() {
        return mWindDirectionDay;
    }

    public void setWindDirectionDay(String windDirectionDay) {
        mWindDirectionDay = windDirectionDay;
    }

    public String getWindDirectionNight() {
        return mWindDirectionNight;
    }

    public void setWindDirectionNight(String windDirectionNight) {
        mWindDirectionNight = windDirectionNight;
    }

    public String getWindPowerDay() {
        return mWindPowerDay;
    }

    public void setWindPowerDay(String windPowerDay) {
        mWindPowerDay = windPowerDay;
    }

    public String getWindPowerNight() {
        return mWindPowerNight;
    }

    public void setWindPowerNight(String windPowerNight) {
        mWindPowerNight = windPowerNight;
    }
}
