package com.kaku.weac.util;

import android.util.Xml;

import com.kaku.weac.bean.WeatherForecast;
import com.kaku.weac.bean.WeatherLifeIndex;
import com.kaku.weac.bean.WeatherInfo;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    WeatherInfo weatherInfo = handleWeatherResponse(in);

//                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                    StringBuilder response = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        response.append(line);
//                    }
                    if (listener != null) {
                        listener.onFinish(weatherInfo);
                    }
                } catch (Exception e) {
                    LogUtil.e(LOG_TAG, e.toString());
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }

                }
            }
        }).start();
    }

    public static WeatherInfo handleWeatherResponse(InputStream inputStream) {
        WeatherInfo weatherInfo = new WeatherInfo();
        List<WeatherForecast> weatherForecasts = new ArrayList<>();
        List<WeatherLifeIndex> weatherLifeIndexes = new ArrayList<>();
        boolean isForcast = false;
        boolean isDay = false;
        WeatherForecast weatherForecast = null;
        WeatherLifeIndex weatherLifeIndex = null;
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        switch (parser.getName()) {
                            case "city":
                                weatherInfo.setCity(parser.nextText());
                                break;
                            case "updatetime":
                                weatherInfo.setUpdateTime(parser.nextText());
                                break;
                            case "wendu":
                                weatherInfo.setTemperature(parser.nextText());
                                break;
                            case "fengli":
                                if (!isForcast)
                                    weatherInfo.setWindPower(parser.nextText());
                                else {
                                    if (isDay) {
                                        if (weatherForecast != null)
                                            weatherForecast.setWindPowerDay(parser.nextText());
                                    } else {
                                        if (weatherForecast != null)
                                            weatherForecast.setWindPowerNight(parser.nextText());
                                    }
                                }
                                break;
                            case "shidu":
                                weatherInfo.setHumidity(parser.nextText());
                                break;
                            case "fengxiang":
                                if (!isForcast)
                                    weatherInfo.setWindDirection(parser.nextText());
                                else {
                                    if (isDay) {
                                        if (weatherForecast != null)
                                            weatherForecast.setWindDirectionDay(parser.nextText());
                                    } else {
                                        if (weatherForecast != null)
                                            weatherForecast.setWindDirectionNight(parser.nextText());
                                    }
                                }
                                break;
                            case "sunrise_1":
                                weatherInfo.setSunrise(parser.nextText());
                                break;
                            case "sunset_1":
                                weatherInfo.setSunset(parser.nextText());
                                break;
                            case "aqi":
                                weatherInfo.setAQI(parser.nextText());
                                break;
                            case "quality":
                                weatherInfo.setQuality(parser.nextText());
                                break;
                            case "alarmType":
                                weatherInfo.setAlarmType(parser.nextText());
                                break;
                            case "alarm_details":
                                weatherInfo.setAlarmDetail(parser.nextText());
                                break;
                            case "forecast":
                                isForcast = true;
                                break;
                            case "yesterday":
                                isForcast = true;
                                weatherForecast = new WeatherForecast();
                                break;
                            case "date_1":
                                if (weatherForecast != null)
                                    weatherForecast.setDate(parser.nextText());
                                break;
                            case "high_1":
                                if (weatherForecast != null)
                                    weatherForecast.setHigh(parser.nextText());
                                break;
                            case "low_1":
                                if (weatherForecast != null)
                                    weatherForecast.setLow(parser.nextText());
                                break;
                            case "day_1":
                                isDay = true;
                                break;
                            case "night_1":
                                isDay = false;
                                break;
                            case "type_1":
                                if (isDay) {
                                    if (weatherForecast != null)
                                        weatherForecast.setTypeDay(parser.nextText());
                                } else {
                                    if (weatherForecast != null)
                                        weatherForecast.setTypeNight(parser.nextText());
                                }
                                break;
                            case "fx_1":
                                if (isDay) {
                                    if (weatherForecast != null)
                                        weatherForecast.setWindDirectionDay(parser.nextText());
                                } else {
                                    if (weatherForecast != null)
                                        weatherForecast.setWindDirectionNight(parser.nextText());
                                }
                                break;
                            case "fl_1":
                                if (isDay) {
                                    if (weatherForecast != null)
                                        weatherForecast.setWindPowerDay(parser.nextText());
                                } else if (weatherForecast != null) {
                                    weatherForecast.setWindPowerNight(parser.nextText());
                                }
                                break;
                            case "weather":
                                weatherForecast = new WeatherForecast();
                                break;
                            case "date":
                                if (weatherForecast != null)
                                    weatherForecast.setDate(parser.nextText());
                                break;
                            case "high":
                                if (weatherForecast != null)
                                    weatherForecast.setHigh(parser.nextText());
                                break;
                            case "low":
                                if (weatherForecast != null)
                                    weatherForecast.setLow(parser.nextText());
                                break;
                            case "day":
                                isDay = true;
                                break;
                            case "night":
                                isDay = false;
                                break;
                            case "type":
                                if (isDay) {
                                    if (weatherForecast != null)
                                        weatherForecast.setTypeDay(parser.nextText());
                                } else {
                                    if (weatherForecast != null)
                                        weatherForecast.setTypeNight(parser.nextText());
                                }
                                break;
                            case "zhishu":
                                weatherLifeIndex = new WeatherLifeIndex();
                                break;
                            case "name":
                                if (weatherLifeIndex != null) {
                                    weatherLifeIndex.setIndexName(parser.nextText());
                                }
                                break;
                            case "value":
                                if (weatherLifeIndex != null) {
                                    weatherLifeIndex.setIndexValue(parser.nextText());
                                }
                                break;
                            case "detail":
                                if (weatherLifeIndex != null) {
                                    weatherLifeIndex.setIndexDetail(parser.nextText());
                                }
                                break;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        switch (parser.getName()) {
                            case "yesterday":
                            case "weather":
                                weatherForecasts.add(weatherForecast);
                                weatherForecast = null;
                                break;
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
        } finally {
            weatherInfo.setWeatherForecast(weatherForecasts);
            weatherInfo.setWeatherLifeIndex(weatherLifeIndexes);
            return weatherInfo;

        }
    }

}
