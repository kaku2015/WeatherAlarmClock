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
package com.kaku.weac.db;

import com.kaku.weac.bean.CityManage;

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

    private WeatherDBOperate() {
        Connector.getDatabase();
    }

    public synchronized static WeatherDBOperate getInstance() {
        if (mWeatherDBOperate == null) {
            mWeatherDBOperate = new WeatherDBOperate();
        }
        return mWeatherDBOperate;
    }

    /**
     * 将城市管理实例存储到数据库
     *
     * @param cityManage 城市管理实例
     * @return 是否存储成功
     */
    public boolean saveCityManage(CityManage cityManage) {
        return cityManage != null && cityManage.saveFast();
    }

    /**
     * 更新城市管理实例
     *
     * @param cityManage 城市管理实例
     */
    public void updateCityManage(CityManage cityManage) {
        if (cityManage != null) {
            cityManage.update(cityManage.getId());
        }
    }

    /**
     * 从WeaFragment界面更新城市管理实例
     *
     * @param cityManage 城市管理实例
     * @param cityName   城市名
     */
    public void updateCityManage(CityManage cityManage, String cityName) {
        if (cityManage != null) {
            cityManage.updateAll("cityName = ?", cityName);
        }
    }

    /**
     * 从数据库读取城市管理信息
     *
     * @return 城市管理信息
     */
    public List<CityManage> loadCityManages() {
        List<CityManage> cityManageList;
        cityManageList = DataSupport.findAll(CityManage.class);
        return cityManageList;

    }

    /**
     * 将城市管理实例从数据库中删除
     *
     * @param cityManage 城市管理实例
     */
    public void deleteCityManage(CityManage cityManage) {
        if (cityManage != null) {
            cityManage.delete();
        }
    }

    /**
     * 查询城市是否已存在城市管理列表
     *
     * @param cityName 城市名
     * @return 件数
     */
    public int queryCityManage(String cityName) {
        return DataSupport.where("cityName = ?", cityName).count(CityManage.class);
    }

    /**
     * 查询定位城市是否已存在城市管理列表
     *
     * @param locationCity 定位城市名
     * @return 件数
     */
    public int queryCityManageLocationCity(String locationCity) {
        return DataSupport.where("locationCity = ?", locationCity).count(CityManage.class);
    }

    /**
     * 查询城市管理列表城市个数
     *
     * @return 件数
     */
    public int queryCityManage() {
        return DataSupport.count(CityManage.class);
    }
}
