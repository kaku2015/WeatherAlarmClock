package com.kaku.weac.db;

import com.kaku.weac.model.City;
import com.kaku.weac.model.Country;
import com.kaku.weac.model.Province;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

/**
 * 天气db操作类
 *
 * @author 咖枯
 * @version 1.0 2015/11/02
 */
public class WeatherDBOperate {
    private static WeatherDBOperate mWeatherDBOperate;

    public WeatherDBOperate() {
        Connector.getDatabase();
    }

    public synchronized static WeatherDBOperate getInstance() {
        if (mWeatherDBOperate == null) {
            mWeatherDBOperate = new WeatherDBOperate();
        }
        return mWeatherDBOperate;
    }

    /**
     * 将省实例存储到数据库
     *
     * @param province 省实例
     */
    public void saveProvince(Province province) {
        if (province != null) {
            province.save();
        }
    }

    /**
     * 从数据库读取全国所有的省份信息
     *
     * @return 全国所有的省份信息
     */
    public List<Province> loadProvinces() {
        List<Province> provinceList;
        provinceList = DataSupport.findAll(Province.class);
        return provinceList;

    }

    /**
     * 将市实例存储到数据库
     *
     * @param city 市实例
     */
    public void saveCity(City city) {
        if (city != null) {
            city.save();
        }
    }

    /**
     * 从数据库读取某省下的市信息
     *
     * @param provinceId 省id
     * @return 市信息
     */
    public List<City> loadCities(int provinceId) {
        List<City> cityList;
        cityList = DataSupport.where("provinceId = ?", String.valueOf(provinceId)).find(City.class);
        return cityList;

    }

    /**
     * 将县实例存储到数据库
     *
     * @param country 县实例
     */
    public void saveCountry(Country country) {
        if (country != null) {
            country.save();
        }
    }

    /**
     * 从数据库读取某市下的县信息
     *
     * @param cityId 市id
     * @return 县信息
     */
    public List<Country> loadCounties(int cityId) {
        List<Country> countryList;
        countryList = DataSupport.where("cityId = ?", String.valueOf(cityId)).find(Country.class);
        return countryList;

    }

}
