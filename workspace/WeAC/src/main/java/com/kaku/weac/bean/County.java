/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.bean;

import org.litepal.crud.DataSupport;

/**
 * 县
 *
 * @author 咖枯
 * @version 1.0 2015/11/02
 */
public class County extends DataSupport {
    private String countyName;
    private String weatherCode;

    public String getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

}
