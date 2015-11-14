package com.kaku.weac.bean;

/**
 * 城市管理
 *
 * @author 咖枯
 * @version 1.0 2015/10/22
 */
public class CityManage {
    /**
     * 城市名
     */
    private String mCityName;

    /**
     * 高温
     */
    private String mTempHigh;

    /**
     * 低温
     */
    private String mTempLow;

    /**
     * 天气类型图片
     */
    private int mImageId;

    /**
     * 天气类型
     */
    private String mWeatherType;

    /**
     * 天气代码
     */
    private String mWeatherCode;

    public String getWeatherCode() {
        return mWeatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        mWeatherCode = weatherCode;
    }

    public CityManage() {
    }

    public CityManage(String cityName, int imageId, String tempHigh,
                      String tempLow, String weatherType) {
        mCityName = cityName;
        mImageId = imageId;
        mTempHigh = tempHigh;
        mTempLow = tempLow;
        mWeatherType = weatherType;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }

    public int getImageId() {
        return mImageId;
    }

    public void setImageId(int imageId) {
        mImageId = imageId;
    }

    public String getTempHigh() {
        return mTempHigh;
    }

    public void setTempHigh(String tempHigh) {
        mTempHigh = tempHigh;
    }

    public String getTempLow() {
        return mTempLow;
    }

    public void setTempLow(String tempLow) {
        mTempLow = tempLow;
    }

    public String getWeatherType() {
        return mWeatherType;
    }

    public void setWeatherType(String weatherType) {
        mWeatherType = weatherType;
    }
}
