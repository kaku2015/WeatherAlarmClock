/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.kaku.weac.bean;

import org.litepal.crud.DataSupport;

/**
 * 城市管理
 *
 * @author 咖枯
 * @version 1.0 2015/10/22
 */
public class CityManage extends DataSupport {
    private int id;

    private String locationCity;

    /**
     * 城市名
     */
    private String cityName;

    /**
     * 高温
     */
    private String tempHigh;

    /**
     * 低温
     */
    private String tempLow;

    /**
     * 天气类型
     */
    private String weatherType;

    /**
     * 天气类型白天
     */
    private String weatherTypeDay;

    /**
     * 天气类型夜间
     */
    private String weatherTypeNight;

    /**
     * 天气代码
     */
    private String weatherCode;


    public int getId() {
        return id;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTempHigh() {
        return tempHigh;
    }

    public void setTempHigh(String tempHigh) {
        this.tempHigh = tempHigh;
    }

    public String getTempLow() {
        return tempLow;
    }

    public void setTempLow(String tempLow) {
        this.tempLow = tempLow;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getWeatherTypeDay() {
        return weatherTypeDay;
    }

    public void setWeatherTypeDay(String weatherTypeDay) {
        this.weatherTypeDay = weatherTypeDay;
    }

    public String getWeatherTypeNight() {
        return weatherTypeNight;
    }

    public void setWeatherTypeNight(String weatherTypeNight) {
        this.weatherTypeNight = weatherTypeNight;
    }
}
