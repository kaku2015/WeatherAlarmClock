package com.kaku.weac.util;

import android.text.TextUtils;

import com.kaku.weac.db.WeatherDBOperate;
import com.kaku.weac.model.City;
import com.kaku.weac.model.Country;
import com.kaku.weac.model.Province;

/**
 * 城市管理工具类
 *
 * @author 咖枯
 * @version 1.0 2015/11/02
 */
public class CityUtil {


    /**
     * 解析和处理服务器返回的省级数据
     *
     * @param weatherDBOperate 天气相关表的操作类
     * @param response         http返回的省级信息
     * @return 处理并存储省级信息成功与否
     */
    public static boolean handleProvincesResponse(
            WeatherDBOperate weatherDBOperate, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces.length > 0) {
                for (String p : allProvinces) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceName(array[0]);
                    province.setProvinceCode(array[1]);
                    weatherDBOperate.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     *
     * @param weatherDBOperate 天气相关表的操作类
     * @param response         http返回的市级信息
     * @param provinceId       省id
     * @return 处理并存储市级信息成功与否
     */
    public static boolean handleCitiesResponse(
            WeatherDBOperate weatherDBOperate, String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities.length > 0) {
                for (String c : allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityName(array[0]);
                    city.setCityCode(array[1]);
                    city.setProvinceId(provinceId);
                    weatherDBOperate.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     *
     * @param weatherDBOperate 天气相关表的操作类
     * @param response         http返回的县级信息
     * @param cityId           市id
     * @return 处理并存储县级信息成功与否
     */
    public static boolean handleCountriesResponse(
            WeatherDBOperate weatherDBOperate, String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCountries = response.split(",");
            if (allCountries.length > 0) {
                for (String c : allCountries) {
                    String[] array = c.split("\\|");
                    Country country = new Country();
                    country.setCountryName(array[0]);
                    country.setCountryCode(array[1]);
                    country.setCityId(cityId);
                    weatherDBOperate.saveCountry(country);
                }
                return true;
            }
        }
        return false;
    }

}
