/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Xml;

import com.kaku.weac.R;
import com.kaku.weac.bean.WeatherDaysForecast;
import com.kaku.weac.bean.WeatherInfo;
import com.kaku.weac.bean.WeatherLifeIndex;
import com.kaku.weac.common.WeacConstants;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 天气工具类
 *
 * @author 咖枯
 * @version 1.0 2015/8/29
 */
public class WeatherUtil {

    /**
     * Log tag ：WeatherUtil
     */
    private static final String LOG_TAG = "WeatherUtil";

    /**
     * 解析天气信息XML
     *
     * @param inputStream 输入流
     * @return 天气信息
     */
    public static WeatherInfo handleWeatherResponse(InputStream inputStream) {
        // 天气信息
        WeatherInfo weatherInfo = new WeatherInfo();
        // 多天预报信息集合
        List<WeatherDaysForecast> weatherDaysForecasts = new ArrayList<>();
        // 生活指数信息集合
        List<WeatherLifeIndex> weatherLifeIndexes = new ArrayList<>();
        // 是否为多天天气
        boolean isDaysForecast = false;
        // 是否为白天
        boolean isDay = false;
        // 多天预报信息
        WeatherDaysForecast weatherDaysForecast = null;
        // 生活指数信息
        WeatherLifeIndex weatherLifeIndex = null;
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        switch (parser.getName()) {
                            // 城市
                            case "city":
                                weatherInfo.setCity(parser.nextText());
                                break;
                            // 更新时间
                            case "updatetime":
                                weatherInfo.setUpdateTime(parser.nextText());
                                break;
                            // 温度
                            case "wendu":
                                weatherInfo.setTemperature(parser.nextText());
                                break;
                            // 风力
                            case "fengli":
                                // 不是多天预报
                                if (!isDaysForecast)
                                    weatherInfo.setWindPower(parser.nextText());
                                else {
                                    if (weatherDaysForecast != null) {
                                        // 白天
                                        if (isDay) {
                                            weatherDaysForecast.setWindPowerDay(parser.nextText());
                                        } else {
                                            weatherDaysForecast.setWindPowerNight(parser.nextText());
                                        }
                                    }
                                }
                                break;
                            // 湿度
                            case "shidu":
                                weatherInfo.setHumidity(parser.nextText());
                                break;
                            // 风向
                            case "fengxiang":
                                if (!isDaysForecast)
                                    weatherInfo.setWindDirection(parser.nextText());
                                else {
                                    if (weatherDaysForecast != null) {
                                        if (isDay) {
                                            weatherDaysForecast.setWindDirectionDay(parser.nextText());
                                        } else {
                                            weatherDaysForecast.setWindDirectionNight(parser.nextText());
                                        }
                                    }
                                }
                                break;
                            // 日出
                            case "sunrise_1":
                                weatherInfo.setSunrise(parser.nextText());
                                break;
                            // 日落
                            case "sunset_1":
                                weatherInfo.setSunset(parser.nextText());
                                break;
                            // 大气环境
                            case "aqi":
                                weatherInfo.setAQI(parser.nextText());
                                break;
                            // 空气质量
                            case "quality":
                                weatherInfo.setQuality(parser.nextText());
                                break;
                            // 警报类型
                            case "alarmType":
                                weatherInfo.setAlarmType(parser.nextText());
                                break;
                            // 警报等级
                            case "alarmDegree":
                                weatherInfo.setAlarmDegree(parser.nextText());
                                break;
                            // 警报详细
                            case "alarm_details":
                                weatherInfo.setAlarmDetail(parser.nextText());
                                break;
                            // 多天预报
                            case "forecast":
                                isDaysForecast = true;
                                break;
                            // 多天预报，昨天
                            case "yesterday":
                                isDaysForecast = true;
                                weatherDaysForecast = new WeatherDaysForecast();
                                break;
                            // 日期
                            case "date_1":
                                if (weatherDaysForecast != null)
                                    weatherDaysForecast.setDate(parser.nextText());
                                break;
                            // 高温
                            case "high_1":
                                if (weatherDaysForecast != null)
                                    weatherDaysForecast.setHigh(parser.nextText());
                                break;
                            // 低温
                            case "low_1":
                                if (weatherDaysForecast != null)
                                    weatherDaysForecast.setLow(parser.nextText());
                                break;
                            // 白天
                            case "day_1":
                                isDay = true;
                                break;
                            // 夜间
                            case "night_1":
                                isDay = false;
                                break;
                            // 天气类型
                            case "type_1":
                                if (weatherDaysForecast != null) {
                                    if (isDay) {
                                        weatherDaysForecast.setTypeDay(parser.nextText());
                                    } else {
                                        weatherDaysForecast.setTypeNight(parser.nextText());
                                    }
                                }
                                break;
                            // 风向
                            case "fx_1":
                                if (weatherDaysForecast != null) {
                                    if (isDay) {
                                        weatherDaysForecast.setWindDirectionDay(parser.nextText());
                                    } else {
                                        weatherDaysForecast.setWindDirectionNight(parser.nextText());
                                    }
                                }
                                break;
                            // 风力
                            case "fl_1":
                                if (weatherDaysForecast != null) {
                                    if (isDay) {
                                        weatherDaysForecast.setWindPowerDay(parser.nextText());
                                    } else {
                                        weatherDaysForecast.setWindPowerNight(parser.nextText());
                                    }
                                }
                                break;
                            // 多天天气
                            case "weather":
                                weatherDaysForecast = new WeatherDaysForecast();
                                break;
                            // 日期
                            case "date":
                                if (weatherDaysForecast != null)
                                    weatherDaysForecast.setDate(parser.nextText());
                                break;
                            // 高温
                            case "high":
                                if (weatherDaysForecast != null)
                                    weatherDaysForecast.setHigh(parser.nextText());
                                break;
                            // 低温
                            case "low":
                                if (weatherDaysForecast != null)
                                    weatherDaysForecast.setLow(parser.nextText());
                                break;
                            // 白天
                            case "day":
                                isDay = true;
                                break;
                            // 夜间
                            case "night":
                                isDay = false;
                                break;
                            // 天气类型
                            case "type":
                                if (weatherDaysForecast != null) {
                                    if (isDay) {
                                        weatherDaysForecast.setTypeDay(parser.nextText());
                                    } else {
                                        weatherDaysForecast.setTypeNight(parser.nextText());
                                    }
                                }
                                break;
                            // 生活指数
                            case "zhishu":
                                weatherLifeIndex = new WeatherLifeIndex();
                                break;
                            // 指数名
                            case "name":
                                if (weatherLifeIndex != null) {
                                    weatherLifeIndex.setIndexName(parser.nextText());
                                }
                                break;
                            // 指数值
                            case "value":
                                if (weatherLifeIndex != null) {
                                    weatherLifeIndex.setIndexValue(parser.nextText());
                                }
                                break;
                            // 指数详细
                            case "detail":
                                if (weatherLifeIndex != null) {
                                    weatherLifeIndex.setIndexDetail(parser.nextText());
                                }
                                break;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        switch (parser.getName()) {
                            // 多天，昨天
                            case "yesterday":
                                // 多天，天气
                            case "weather":
                                weatherDaysForecasts.add(weatherDaysForecast);
                                weatherDaysForecast = null;
                                break;
                            // 指数
                            case "zhishu":
                                weatherLifeIndexes.add(weatherLifeIndex);
                                weatherLifeIndex = null;
                                break;
                        }

                        break;
                }
                eventType = parser.next();
            }

        } catch (Exception e) {
            LogUtil.e(LOG_TAG, e.toString());
        }
        weatherInfo.setWeatherDaysForecast(weatherDaysForecasts);
        weatherInfo.setWeatherLifeIndex(weatherLifeIndexes);
        return weatherInfo;
    }

    /**
     * 将天气信息类转换成Base64编码，并保存转换后的字符串
     *
     * @param weatherInfo 天气信息类
     * @param context     context
     */
    public static void saveWeatherInfo(WeatherInfo weatherInfo, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(WeacConstants.BASE64,
                Activity.MODE_PRIVATE);
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(weatherInfo);
            // 将字节流编码成base64的字符串
            String weatherInfoBase64 = Base64.encodeToString(baos
                    .toByteArray(), 1);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(weatherInfo.getCity(), weatherInfoBase64);
            // 保存天气更新时间
            editor.putLong(context.getString(R.string.city_weather_update_time,
                    weatherInfo.getCity()), System.currentTimeMillis());
            editor.apply();
        } catch (IOException e) {
            LogUtil.e(LOG_TAG, e.toString());
        }
    }

    /**
     * 取得保存的Base64编码天气信息，并解码
     *
     * @param context context
     * @param city    需要取得天气信息的城市名
     * @return 天气信息类
     */
    public static WeatherInfo readWeatherInfo(Context context, String city) {
        WeatherInfo weatherInfo = null;
        SharedPreferences preferences = context.getSharedPreferences(WeacConstants.BASE64,
                Activity.MODE_PRIVATE);
        String weatherInfoBase64 = preferences.getString(city, "");
        //读取字节
        byte[] base64 = Base64.decode(weatherInfoBase64.getBytes(), 1);
        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            weatherInfo = (WeatherInfo) bis.readObject();
        } catch (Exception e) {
            LogUtil.e(LOG_TAG, e.toString());
        }
        return weatherInfo;
    }
}
