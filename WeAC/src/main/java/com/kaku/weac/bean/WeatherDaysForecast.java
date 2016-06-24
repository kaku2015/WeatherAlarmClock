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

import java.io.Serializable;

/**
 * 天气多天预报信息
 *
 * @author 咖枯
 * @version 1.0 2015/09/18
 */
public class WeatherDaysForecast implements Serializable {

    private static final long serialVersionUID = -3415028129243262810L;
    /**
     * 日期
     */
    private String mDate;

    /**
     * 高温
     */
    private String mHigh;

    /**
     * 低温
     */
    private String mLow;

    /**
     * 白天天气类型
     */
    private String mTypeDay;

    /**
     * 晚上天气类型
     */
    private String mTypeNight;

    /**
     * 白天风向
     */
    private String mWindDirectionDay;

    /**
     * 晚上风向
     */
    private String mWindDirectionNight;

    /**
     * 白天风力
     */
    private String mWindPowerDay;

    /**
     * 晚上风力
     */
    private String mWindPowerNight;

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
