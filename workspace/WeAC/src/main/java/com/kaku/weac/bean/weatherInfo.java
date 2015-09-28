package com.kaku.weac.bean;

import java.util.List;

/**
 * 天气信息
 *
 * @author 咖枯
 * @version 1.0 2015/09/18
 */
public class WeatherInfo {
    /**
     * 城市
     */
    String mCity;

    /**
     * 更新时间
     */
    String mUpdateTime;

    /**
     * 温度
     */
    String mTemperature;

    /***
     * 湿度
     */
    String mHumidity;

    /**
     * 风力
     */
    String mWindPower;

    /**
     * 风向
     */
    String mWindDirection;

    /**
     * 日出
     */
    String mSunrise;

    /**
     * 日落
     */
    String mSunset;

    /**
     * 空气指数
     */
    String mAQI;

    /**
     * 空气质量
     */
    String mQuality;

    /**
     * 警报类型
     */
    String mAlarmType;

    /**
     * 警报内容
     */
    String mAlarmDetail;

    /**
     * 多天预报信息
     */
    List<WeatherForecast> mWeatherForecast;

    /**
     * 生活指数
     */
    List<WeatherLifeIndex> mWeatherLifeIndex;

    public String getAlarmDetail() {
        return mAlarmDetail;
    }

    public void setAlarmDetail(String alarmDetail) {
        mAlarmDetail = alarmDetail;
    }

    public String getAlarmType() {
        return mAlarmType;
    }

    public void setAlarmType(String alarmType) {
        mAlarmType = alarmType;
    }

    public String getAQI() {
        return mAQI;
    }

    public void setAQI(String AQI) {
        mAQI = AQI;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getHumidity() {
        return mHumidity;
    }

    public void setHumidity(String humidity) {
        mHumidity = humidity;
    }

    public String getQuality() {
        return mQuality;
    }

    public void setQuality(String quality) {
        mQuality = quality;
    }

    public String getSunrise() {
        return mSunrise;
    }

    public void setSunrise(String sunrise) {
        mSunrise = sunrise;
    }

    public String getSunset() {
        return mSunset;
    }

    public void setSunset(String sunset) {
        mSunset = sunset;
    }

    public String getTemperature() {
        return mTemperature;
    }

    public void setTemperature(String temperature) {
        mTemperature = temperature;
    }

    public String getUpdateTime() {
        return mUpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        mUpdateTime = updateTime;
    }

    public List<WeatherForecast> getWeatherForecast() {
        return mWeatherForecast;
    }

    public void setWeatherForecast(List<WeatherForecast> weatherForecast) {
        mWeatherForecast = weatherForecast;
    }

    public List<WeatherLifeIndex> getWeatherLifeIndex() {
        return mWeatherLifeIndex;
    }

    public void setWeatherLifeIndex(List<WeatherLifeIndex> weatherLifeIndex) {
        mWeatherLifeIndex = weatherLifeIndex;
    }

    public String getWindDirection() {
        return mWindDirection;
    }

    public void setWindDirection(String windDirection) {
        mWindDirection = windDirection;
    }

    public String getWindPower() {
        return mWindPower;
    }

    public void setWindPower(String windPower) {
        mWindPower = windPower;
    }
}
