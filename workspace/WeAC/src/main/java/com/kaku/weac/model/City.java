package com.kaku.weac.model;

import org.litepal.crud.DataSupport;

/**
 * 市
 *
 * @author 咖枯
 * @version 1.0 2015/11/02
 */
public class City extends DataSupport {
    private int id;
    private String cityName;
    private String cityCode;
    private int provinceId;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
