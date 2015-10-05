package com.kaku.weac.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.bean.WeatherForecast;
import com.kaku.weac.bean.WeatherInfo;
import com.kaku.weac.bean.WeatherLifeIndex;
import com.kaku.weac.util.HttpCallbackListener;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.WeatherUtil;
import com.kaku.weac.view.LineChartView;

import java.util.Calendar;
import java.util.List;

/**
 * 天气fragment
 *
 * @author 咖枯
 * @version 1.0 2015/9
 */
public class WeaFragment extends Fragment {
    /**
     * Log tag ：WeaFragment
     */
    private static final String LOG_TAG = "WeaFragment";

    /**
     * 城市名
     */
    TextView mCityNameTv;

    /**
     * 警报
     */
    TextView mAlarmTv;

    /**
     * 更新时间
     */
    TextView mUpdateTimeTv;


    /**
     * 温度1
     */
    ImageView mTemperature1Iv;

    /**
     * 温度2
     */
    ImageView mTemperature2Iv;

    /**
     * 天气类型
     */
    TextView mWeatherTypeTv;


    /**
     * 大气环境
     */
    TextView mAqiTv;

    /**
     * 湿度
     */
    TextView mHumidityTv;

    /**
     * 风向、风力
     */
    TextView mWindTv;


    /**
     * 今天天气类型图片
     */
    ImageView mWeatherTypeIvToday;

    /**
     * 今天高温
     */
    TextView mTempHighTvToday;

    /**
     * 今天低温
     */
    TextView mTemplowTvToday;

    /**
     * 今天天气类型文字
     */
    TextView mWeatherTypeTvToday;


    /**
     * 明天天气类型图片
     */
    ImageView mWeatherTypeIvTomorrow;

    /**
     * 明天高温
     */
    TextView mTempHighTvTomorrow;

    /**
     * 明天低温
     */
    TextView mTemplowTvTomorrow;

    /**
     * 明天天气类型文字
     */
    TextView mWeatherTypeTvTomorrow;


    /**
     * 后天天气类型图片
     */
    ImageView mWeatherTypeIvDayAfterTomorrow;

    /**
     * 后天高温
     */
    TextView mTempHighTvDayAfterTomorrow;

    /**
     * 后天低温
     */
    TextView mTemplowTvDayAfterTomorrow;

    /**
     * 后天天气类型文字
     */
    TextView mWeatherTypeTvDayAfterTomorrow;


    /**
     * 多天预报标题1
     */
    TextView mDaysForecastTvWeek1;

    /**
     * 多天预报标题2
     */
    TextView mDaysForecastTvWeek2;

    /**
     * 多天预报标题3
     */
    TextView mDaysForecastTvWeek3;

    /**
     * 多天预报标题4
     */
    TextView mDaysForecastTvWeek4;

    /**
     * 多天预报标题5
     */
    TextView mDaysForecastTvWeek5;

    /**
     * 多天预报标题6
     */
    TextView mDaysForecastTvWeek6;


    /**
     * 多天预报日期1
     */
    TextView mDaysForecastTvDay1;

    /**
     * 多天预报日期2
     */
    TextView mDaysForecastTvDay2;

    /**
     * 多天预报日期3
     */
    TextView mDaysForecastTvDay3;

    /**
     * 多天预报日期4
     */
    TextView mDaysForecastTvDay4;

    /**
     * 多天预报日期5
     */
    TextView mDaysForecastTvDay5;

    /**
     * 多天预报日期6
     */
    TextView mDaysForecastTvDay6;


    /**
     * 多天预报白天天气类型图片1
     */
    ImageView mDaysForecastWeaTypeDayIv1;

    /**
     * 多天预报白天天气类型图片2
     */
    ImageView mDaysForecastWeaTypeDayIv2;

    /**
     * 多天预报白天天气类型图片3
     */
    ImageView mDaysForecastWeaTypeDayIv3;

    /**
     * 多天预报白天天气类型图片4
     */
    ImageView mDaysForecastWeaTypeDayIv4;

    /**
     * 多天预报白天天气类型图片5
     */
    ImageView mDaysForecastWeaTypeDayIv5;

    /**
     * 多天预报白天天气类型图片6
     */
    ImageView mDaysForecastWeaTypeDayIv6;


    /**
     * 多天预报白天天气类型文字1
     */
    TextView mDaysForecastWeaTypeDayTv1;

    /**
     * 多天预报白天天气类型文字2
     */
    TextView mDaysForecastWeaTypeDayTv2;

    /**
     * 多天预报白天天气类型文字3
     */
    TextView mDaysForecastWeaTypeDayTv3;

    /**
     * 多天预报白天天气类型文字4
     */
    TextView mDaysForecastWeaTypeDayTv4;

    /**
     * 多天预报白天天气类型文字5
     */
    TextView mDaysForecastWeaTypeDayTv5;

    /**
     * 多天预报白天天气类型文字6
     */
    TextView mDaysForecastWeaTypeDayTv6;


    /**
     * 白天温度曲线
     */
    LineChartView mCharDay;

    /**
     * 夜间温度曲线
     */
    LineChartView mCharNight;


    /**
     * 多天预报夜间天气类型图片1
     */
    ImageView mDaysForecastWeaTypeNightIv1;

    /**
     * 多天预报夜间天气类型图片2
     */
    ImageView mDaysForecastWeaTypeNightIv2;

    /**
     * 多天预报夜间天气类型图片3
     */
    ImageView mDaysForecastWeaTypeNightIv3;

    /**
     * 多天预报夜间天气类型图片4
     */
    ImageView mDaysForecastWeaTypeNightIv4;

    /**
     * 多天预报夜间天气类型图片5
     */
    ImageView mDaysForecastWeaTypeNightIv5;

    /**
     * 多天预报夜间天气类型图片6
     */
    ImageView mDaysForecastWeaTypeNightIv6;


    /**
     * 多天预报夜间天气类型文字1
     */
    TextView mDaysForecastWeaTypeNightTv1;

    /**
     * 多天预报夜间天气类型文字2
     */
    TextView mDaysForecastWeaTypeNightTv2;

    /**
     * 多天预报夜间天气类型文字3
     */
    TextView mDaysForecastWeaTypeNightTv3;

    /**
     * 多天预报夜间天气类型文字4
     */
    TextView mDaysForecastWeaTypeNightTv4;

    /**
     * 多天预报夜间天气类型文字5
     */
    TextView mDaysForecastWeaTypeNightTv5;

    /**
     * 多天预报夜间天气类型文字6
     */
    TextView mDaysForecastWeaTypeNightTv6;


    /**
     * 多天预报风向1
     */
    TextView mDaysForecastWindDirectionTv1;

    /**
     * 多天预报风向2
     */
    TextView mDaysForecastWindDirectionTv2;

    /**
     * 多天预报风向3
     */
    TextView mDaysForecastWindDirectionTv3;

    /**
     * 多天预报风向4
     */
    TextView mDaysForecastWindDirectionTv4;

    /**
     * 多天预报风向5
     */
    TextView mDaysForecastWindDirectionTv5;

    /**
     * 多天预报风向6
     */
    TextView mDaysForecastWindDirectionTv6;


    /**
     * 多天预报风力1
     */
    TextView mDaysForecastWindPowerTv1;

    /**
     * 多天预报风力2
     */
    TextView mDaysForecastWindPowerTv2;

    /**
     * 多天预报风力3
     */
    TextView mDaysForecastWindPowerTv3;

    /**
     * 多天预报风力4
     */
    TextView mDaysForecastWindPowerTv4;

    /**
     * 多天预报风力5
     */
    TextView mDaysForecastWindPowerTv5;

    /**
     * 多天预报风力6
     */
    TextView mDaysForecastWindPowerTv6;


    /**
     * 雨伞指数
     */
    TextView mLifeIndexUmbrellaTv;

    /**
     * 紫外线指数
     */
    TextView mLifeIndexulTravioletRaysTv;

    /**
     * 穿衣指数
     */
    TextView mLifeIndexDressTv;

    /**
     * 感冒指数
     */
    TextView mLifeIndexcoldTv;

    /**
     * 晨练指数
     */
    TextView mLifeIndexMorningExerciseTv;

    /**
     * 运动指数
     */
    TextView mLifeIndexSportTv;

    /**
     * 洗车指数
     */
    TextView mLifeIndexCarWashTv;

    /**
     * 钓鱼指数
     */
    TextView mLifeIndexFishTv;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fm_wea, container, false);

        init(view);

        /*LineChartView chartDay = (LineChartView) view.findViewById(R.id.line_char_day);
        chartDay.setTemp(new int[]{32, 31, 31, 30, 25, 26});
        chartDay.setTextSpace(10);
        int colorDay = getResources().getColor(R.color.yellow_hot);
        chartDay.setLineColor(colorDay);
        chartDay.setPointColor(colorDay);

        LineChartView chartNight = (LineChartView) view.findViewById(R.id.line_chart_night);
        chartNight.setTemp(new int[]{20, 25, 24, 25, 20, 20});
        chartNight.setTextSpace(-10);
        int colorNight = getResources().getColor(R.color.blue_ice);
        chartNight.setLineColor(colorNight);
        chartNight.setPointColor(colorNight);*/


        Button readButton = (Button) view.findViewById(R.id.read_wea);
        final TextView tv = (TextView) view.findViewById(R.id.wea_prompt_tv);
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherUtil.sendHttpRequest("http://wthrcdn.etouch.cn/WeatherApi?citykey=101030100",
                        new HttpCallbackListener() {
                            @Override
                            public void onFinish(final WeatherInfo weatherInfo) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 多天预报信息
                                        List<WeatherForecast> weatherForecasts = weatherInfo.getWeatherForecast();
                                        // 生活指数信息
                                        List<WeatherLifeIndex> weatherLifeIndexes = weatherInfo.getWeatherLifeIndex();

                                        // 设置城市名
                                        mCityNameTv.setText(weatherInfo.getCity());
                                        // 设置预警信息
                                        if (weatherInfo.getAlarmType() != null) {
                                            mAlarmTv.setVisibility(View.VISIBLE);
                                            mAlarmTv.setText(weatherInfo.getAlarmType() + "预警");
                                        }
                                        // 设置更新时间
                                        mUpdateTimeTv.setText(weatherInfo.getUpdateTime() + "发布");

                                        // 设置温度
                                        String temp = weatherInfo.getTemperature();
                                        int temp1 = Integer.parseInt(temp.substring(0, 1));
                                        int temp2 = Integer.parseInt(temp.substring(1));
                                        setTemperatureImage(temp1, mTemperature1Iv);
                                        setTemperatureImage(temp2, mTemperature2Iv);

                                        // 今天天气信息
                                        WeatherForecast weatherToday = weatherForecasts.get(1);
                                        Calendar calendar = Calendar.getInstance();
                                        // 现在小时
                                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                        // 设置天气类型
                                        if (hour <= 18) {
                                            // 白天天气
                                            mWeatherTypeTv.setText(weatherToday.getTypeDay());
                                        } else {
                                            // 夜间天气
                                            mWeatherTypeTv.setText(weatherToday.getTypeNight());
                                        }

                                        // 设置大气环境
                                        mAqiTv.setText(weatherInfo.getQuality() + " " + weatherInfo.getAQI());
                                        // 设置湿度
                                        mHumidityTv.setText("湿度 " + weatherInfo.getHumidity());
                                        // 设置风向、风力
                                        mWindTv.setText(weatherInfo.getWindDirection() + " "
                                                + weatherInfo.getWindPower());

                                        // 明天天气信息
                                        WeatherForecast weatherTomorrow = weatherForecasts.get(2);
                                        // 后天天气信息
                                        WeatherForecast weatherDayOfTomorrow = weatherForecasts.get(3);
                                        int weatherId;

                                        // 设置今天天气信息
                                        weatherId = getWeatherTypeImageID(weatherToday.getTypeDay(), hour, false);
                                        mWeatherTypeIvToday.setImageResource(weatherId);
                                        mTempHighTvToday.setText(weatherToday.getHigh().substring(3));
                                        mTemplowTvToday.setText(weatherToday.getLow().substring(3));
                                        mWeatherTypeTvToday.setText(getWeatherType
                                                (weatherToday.getTypeDay(), weatherToday.getTypeNight()));

                                        // 设置明天天气信息
                                        weatherId = getWeatherTypeImageID(weatherTomorrow.getTypeDay(), hour, false);
                                        mWeatherTypeIvTomorrow.setImageResource(weatherId);
                                        mTempHighTvTomorrow.setText(weatherTomorrow.getHigh().substring(3));
                                        mTemplowTvTomorrow.setText(weatherTomorrow.getLow().substring(3));
                                        mWeatherTypeTvTomorrow.setText(getWeatherType
                                                (weatherTomorrow.getTypeDay(), weatherTomorrow.getTypeNight()));

                                        // 设置后天天气信息
                                        weatherId = getWeatherTypeImageID(weatherDayOfTomorrow.getTypeDay(), hour, false);
                                        mWeatherTypeIvDayAfterTomorrow.setImageResource(weatherId);
                                        mTempHighTvDayAfterTomorrow.setText(weatherDayOfTomorrow.getHigh().substring(3));
                                        mTemplowTvDayAfterTomorrow.setText(weatherDayOfTomorrow.getLow().substring(3));
                                        mWeatherTypeTvDayAfterTomorrow.setText(getWeatherType
                                                (weatherDayOfTomorrow.getTypeDay(), weatherDayOfTomorrow.getTypeNight()));

                                        // 设置多天天气预报

                                        // 昨天天气信息
                                        WeatherForecast weatherYesterday = weatherForecasts.get(0);
                                        // 第五天天天气信息
                                        WeatherForecast weatherFifth = weatherForecasts.get(4);
                                        // 第六天天气信息
                                        WeatherForecast weatherSixth = weatherForecasts.get(5);

                                        // 日期和星期标题 【数组0：日期 数组1：星期】
                                        String[] day1 = getDay(weatherYesterday.getDate());
                                        String[] day2 = getDay(weatherToday.getDate());
                                        String[] day3 = getDay(weatherTomorrow.getDate());
                                        String[] day4 = getDay(weatherDayOfTomorrow.getDate());
                                        String[] day5 = getDay(weatherFifth.getDate());
                                        String[] day6 = getDay(weatherSixth.getDate());

                                        // 设置标题星期
                                        mDaysForecastTvWeek1.setText("昨天");
                                        mDaysForecastTvWeek2.setText("今天");
                                        mDaysForecastTvWeek3.setText(getWeek(day3[1]));
                                        mDaysForecastTvWeek4.setText(getWeek(day4[1]));
                                        mDaysForecastTvWeek5.setText(getWeek(day5[1]));
                                        mDaysForecastTvWeek6.setText(getWeek(day6[1]));

                                        //设置标题日期
                                        Calendar c = Calendar.getInstance();
                                        // 当前月份
                                        String month = MyUtil.addZero(c.get(Calendar.MONTH) + 1);

                                        // 日
                                        String day01 = MyUtil.addZero(
                                                Integer.parseInt(day1[0].split("日")[0]));
                                        String day02 = MyUtil.addZero(
                                                Integer.parseInt(day2[0].split("日")[0]));
                                        String day03 = MyUtil.addZero(
                                                Integer.parseInt(day3[0].split("日")[0]));
                                        String day04 = MyUtil.addZero(
                                                Integer.parseInt(day4[0].split("日")[0]));
                                        String day05 = MyUtil.addZero(
                                                Integer.parseInt(day5[0].split("日")[0]));
                                        String day06 = MyUtil.addZero(
                                                Integer.parseInt(day6[0].split("日")[0]));

                                        mDaysForecastTvDay1.setText(month + "/" + day01);
                                        mDaysForecastTvDay2.setText(month + "/" + day02);
                                        mDaysForecastTvDay3.setText(month + "/" + day03);
                                        mDaysForecastTvDay4.setText(month + "/" + day04);
                                        mDaysForecastTvDay5.setText(month + "/" + day05);
                                        mDaysForecastTvDay6.setText(month + "/" + day06);


//                                        tv.setText(weatherInfo);

                                        /*try {
                                            // 外部存储根路径
                                            String fileName = Environment.getExternalStorageDirectory()
                                                    .getAbsolutePath();
                                            // 录音文件路径
                                            fileName += "/WeaAlarmClock/wea2.txt";
                                            File file = new File(fileName);
                                            if (!file.exists()) {
                                                file.createNewFile();
                                            }

                                            FileWriter fw = new FileWriter(file.getAbsoluteFile());
                                            BufferedWriter bw = new BufferedWriter(fw);
                                            bw.write(weatherInfo);
                                            bw.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }*/
                                    }

                                    /**
                                     * 截取日期和星期
                                     *
                                     * @param date 日期信息
                                     * @return 包含日期和星期的数组
                                     */
                                    private String[] getDay(String date) {
                                        String[] date1 = new String[2];
                                        if (date.length() == 5) {
                                            date1[0] = date.substring(0, 2);
                                            date1[1] = date.substring(2);
                                        } else {
                                            date1[0] = date.substring(0, 3);
                                            date1[1] = date.substring(3);
                                        }
                                        return date1;
                                    }

                                    /**
                                     * 转换周的标题
                                     * @param week 需要转换的周标题
                                     * @return 周的标题
                                     */
                                    private String getWeek(String week) {
                                        String week1;
                                        switch (week) {
                                            case "星期一":
                                                week1 = "周一";
                                                break;
                                            case "星期二":
                                                week1 = "周二";
                                                break;
                                            case "星期三":
                                                week1 = "周三";
                                                break;
                                            case "星期四":
                                                week1 = "周四";
                                                break;
                                            case "星期五":
                                                week1 = "周五";
                                                break;
                                            case "星期六":
                                                week1 = "周六";
                                                break;
                                            case "星期日":
                                                week1 = "周日";
                                                break;
                                            default:
                                                week1 = week;
                                                break;
                                        }
                                        return week1;
                                    }

                                    /**
                                     * 取得天气类型描述
                                     * @param type1 白天天气类型
                                     * @param type2 夜间天气类型
                                     * @return 天气类型
                                     */
                                    private String getWeatherType(String type1, String type2) {
                                        // 白天和夜间类型相同
                                        if (type1.equals(type2)) {
                                            return type1;
                                        } else {
                                            return type1 + "转" + type2;
                                        }
                                    }


                                    /**
                                     * 设置温度图片
                                     * @param temp1 温度
                                     * @param imageView imageView控件
                                     */
                                    private void setTemperatureImage(int temp1, ImageView imageView) {
                                        switch (temp1) {
                                            case 0:
                                                imageView.setImageResource(R.drawable.number_0);
                                                break;
                                            case 1:
                                                imageView.setImageResource(R.drawable.number_1);
                                                break;
                                            case 2:
                                                imageView.setImageResource(R.drawable.number_2);
                                                break;
                                            case 3:
                                                imageView.setImageResource(R.drawable.number_3);
                                                break;
                                            case 4:
                                                imageView.setImageResource(R.drawable.number_4);
                                                break;
                                            case 5:
                                                imageView.setImageResource(R.drawable.number_5);
                                                break;
                                            case 6:
                                                imageView.setImageResource(R.drawable.number_6);
                                                break;
                                            case 7:
                                                imageView.setImageResource(R.drawable.number_7);
                                                break;
                                            case 8:
                                                imageView.setImageResource(R.drawable.number_8);
                                                break;
                                            case 9:
                                                imageView.setImageResource(R.drawable.number_9);
                                                break;
                                            default:
                                                imageView.setImageResource(R.drawable.number_0);
                                                break;
                                        }
                                    }

                                    /**
                                     * 取得对应的天气类型图片id
                                     * @param type 天气类型
                                     * @param hour 现在小时
                                     * @param isDaysForecast 是否为多天预报
                                     * @return 天气类型图片id
                                     */
                                    private int getWeatherTypeImageID(String type, int hour, boolean isDaysForecast) {
                                        int weatherId;
                                        // 是否为白天
                                        boolean isDay;
                                        isDay = hour >= 6 && hour < 18;

                                        switch (type) {
                                            case "晴":
                                                if (isDaysForecast) {
                                                    if (isDay) {
                                                        weatherId = R.drawable.ic_weather_sunny_day;
                                                    } else {
                                                        weatherId = R.drawable.ic_weather_sunny_night;
                                                    }
                                                } else {
                                                    weatherId = R.drawable.ic_weather_sunny_day;
                                                }
                                                break;
                                            case "多云":
                                                if (isDaysForecast) {
                                                    if (isDay) {
                                                        weatherId = R.drawable.ic_weather_cloudy_day;
                                                    } else {
                                                        weatherId = R.drawable.ic_weather_cloudy_night;
                                                    }
                                                } else {
                                                    weatherId = R.drawable.ic_weather_cloudy_day;
                                                }
                                                break;
                                            case "阴":
                                                weatherId = R.drawable.ic_weather_overcast;
                                                break;
                                            case "雷阵雨":
                                            case "雷阵雨伴有冰雹":
                                                weatherId = R.drawable.ic_weather_thunder_shower;
                                                break;
                                            case "雨夹雪":
                                            case "冻雨":
                                                weatherId = R.drawable.ic_weather_sleet;
                                                break;
                                            case "小雨":
                                            case "小到中雨":
                                            case "阵雨":
                                                weatherId = R.drawable.ic_weather_light_rain_or_shower;
                                                break;
                                            case "中雨":
                                            case "中到大雨":
                                                weatherId = R.drawable.ic_weather_moderate_rain;
                                                break;
                                            case "大雨":
                                            case "大到暴雨":
                                                weatherId = R.drawable.ic_weather_heavy_rain;
                                                break;
                                            case "暴雨":
                                            case "大暴雨":
                                            case "特大暴雨":
                                            case "暴雨到大暴雨":
                                            case "大暴雨到特大暴雨":
                                                weatherId = R.drawable.ic_weather_storm;
                                                break;
                                            case "阵雪":
                                            case "小雪":
                                            case "小到中雪":
                                                weatherId = R.drawable.ic_weather_light_snow;
                                                break;
                                            case "中雪":
                                            case "中到大雪":
                                                weatherId = R.drawable.ic_weather_moderate_snow;
                                                break;
                                            case "大雪":
                                            case "大到暴雪":
                                                weatherId = R.drawable.ic_weather_heavy_snow;
                                                break;
                                            case "暴雪":
                                                weatherId = R.drawable.ic_weather_snowstrom;
                                                break;
                                            case "雾":
                                            case "霾":
                                                weatherId = R.drawable.ic_weather_foggy;
                                                break;
                                            case "沙尘暴":
                                                weatherId = R.drawable.ic_weather_duststorm;
                                                break;
                                            case "强沙尘暴":
                                                weatherId = R.drawable.ic_weather_sandstorm;
                                                break;
                                            case "浮尘":
                                            case "扬沙":
                                                weatherId = R.drawable.ic_weather_sand_or_dust;
                                                break;
                                            default:
                                                if (type.contains("尘") || type.contains("沙")) {
                                                    weatherId = R.drawable.ic_weather_sand_or_dust;
                                                } else if (type.contains("雾") || type.contains("霾")) {
                                                    weatherId = R.drawable.ic_weather_foggy;
                                                } else if (type.contains("雨")) {
                                                    weatherId = R.drawable.ic_weather_sleet;
                                                } else if (type.contains("雪") || type.contains("冰雹")) {
                                                    weatherId = R.drawable.ic_weather_moderate_snow;
                                                } else {
                                                    weatherId = R.drawable.ic_weather_no;
                                                }
                                                break;

                                        }
                                        return weatherId;
                                    }
                                });
                            }

                            @Override
                            public void onError(final Exception e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv.setText("读取失败：" + e.toString());
                                    }
                                });
                            }
                        });
            }
        });

        return view;
    }

    /**
     * 初始化控件
     *
     * @param view view
     */
    private void init(View view) {
        mCityNameTv = (TextView) view.findViewById(R.id.action_title);
        mAlarmTv = (TextView) view.findViewById(R.id.alarm);
        mUpdateTimeTv = (TextView) view.findViewById(R.id.update_time);

        mTemperature1Iv = (ImageView) view.findViewById(R.id.temperature1);
        mTemperature2Iv = (ImageView) view.findViewById(R.id.temperature2);
        mWeatherTypeTv = (TextView) view.findViewById(R.id.weather_type);

        mAqiTv = (TextView) view.findViewById(R.id.aqi);
        mHumidityTv = (TextView) view.findViewById(R.id.humidity);
        mWindTv = (TextView) view.findViewById(R.id.wind);

        mWeatherTypeIvToday = (ImageView) view.findViewById(R.id.weather_type_iv_today);
        mWeatherTypeIvTomorrow = (ImageView) view.findViewById(R.id.weather_type_iv_tomorrow);
        mWeatherTypeIvDayAfterTomorrow = (ImageView) view.findViewById(R.id.weather_type_iv_day_after_tomorrow);

        mTempHighTvToday = (TextView) view.findViewById(R.id.temp_high_today);
        mTempHighTvTomorrow = (TextView) view.findViewById(R.id.temp_high_tomorrow);
        mTempHighTvDayAfterTomorrow = (TextView) view.findViewById(R.id.temp_high_day_after_tomorrow);

        mTemplowTvToday = (TextView) view.findViewById(R.id.temp_low_today);
        mTemplowTvTomorrow = (TextView) view.findViewById(R.id.temp_low_tomorrow);
        mTemplowTvDayAfterTomorrow = (TextView) view.findViewById(R.id.temp_low_day_after_tomorrow);

        mWeatherTypeTvToday = (TextView) view.findViewById(R.id.weather_type_tv_today);
        mWeatherTypeTvTomorrow = (TextView) view.findViewById(R.id.weather_type_tv_tomorrow);
        mWeatherTypeTvDayAfterTomorrow = (TextView) view.findViewById(R.id.weather_type_tv_day_after_tomorrow);

        mDaysForecastTvWeek1 = (TextView) view.findViewById(R.id.wea_days_forecast_week1);
        mDaysForecastTvWeek2 = (TextView) view.findViewById(R.id.wea_days_forecast_week2);
        mDaysForecastTvWeek3 = (TextView) view.findViewById(R.id.wea_days_forecast_week3);
        mDaysForecastTvWeek4 = (TextView) view.findViewById(R.id.wea_days_forecast_week4);
        mDaysForecastTvWeek5 = (TextView) view.findViewById(R.id.wea_days_forecast_week5);
        mDaysForecastTvWeek6 = (TextView) view.findViewById(R.id.wea_days_forecast_week6);

        mDaysForecastTvDay1 = (TextView) view.findViewById(R.id.wea_days_forecast_day1);
        mDaysForecastTvDay2 = (TextView) view.findViewById(R.id.wea_days_forecast_day2);
        mDaysForecastTvDay3 = (TextView) view.findViewById(R.id.wea_days_forecast_day3);
        mDaysForecastTvDay4 = (TextView) view.findViewById(R.id.wea_days_forecast_day4);
        mDaysForecastTvDay5 = (TextView) view.findViewById(R.id.wea_days_forecast_day5);
        mDaysForecastTvDay6 = (TextView) view.findViewById(R.id.wea_days_forecast_day6);

        mDaysForecastWeaTypeDayIv1 = (ImageView) view.findViewById(R.id.wea_days_forecast_weather_day_iv1);
        mDaysForecastWeaTypeDayIv2 = (ImageView) view.findViewById(R.id.wea_days_forecast_weather_day_iv2);
        mDaysForecastWeaTypeDayIv3 = (ImageView) view.findViewById(R.id.wea_days_forecast_weather_day_iv3);
        mDaysForecastWeaTypeDayIv4 = (ImageView) view.findViewById(R.id.wea_days_forecast_weather_day_iv4);
        mDaysForecastWeaTypeDayIv5 = (ImageView) view.findViewById(R.id.wea_days_forecast_weather_day_iv5);
        mDaysForecastWeaTypeDayIv6 = (ImageView) view.findViewById(R.id.wea_days_forecast_weather_day_iv6);

        mDaysForecastWeaTypeDayTv1 = (TextView) view.findViewById(R.id.wea_days_forecast_weather_day_tv1);
        mDaysForecastWeaTypeDayTv2 = (TextView) view.findViewById(R.id.wea_days_forecast_weather_day_tv2);
        mDaysForecastWeaTypeDayTv3 = (TextView) view.findViewById(R.id.wea_days_forecast_weather_day_tv3);
        mDaysForecastWeaTypeDayTv4 = (TextView) view.findViewById(R.id.wea_days_forecast_weather_day_tv4);
        mDaysForecastWeaTypeDayTv5 = (TextView) view.findViewById(R.id.wea_days_forecast_weather_day_tv5);
        mDaysForecastWeaTypeDayTv6 = (TextView) view.findViewById(R.id.wea_days_forecast_weather_day_tv6);

        mCharDay = (LineChartView) view.findViewById(R.id.line_char_day);
        mCharNight = (LineChartView) view.findViewById(R.id.line_chart_night);

        mDaysForecastWeaTypeNightIv1 = (ImageView) view.findViewById(R.id.wea_days_forecast_weather_night_iv1);
        mDaysForecastWeaTypeNightIv2 = (ImageView) view.findViewById(R.id.wea_days_forecast_weather_night_iv2);
        mDaysForecastWeaTypeNightIv3 = (ImageView) view.findViewById(R.id.wea_days_forecast_weather_night_iv3);
        mDaysForecastWeaTypeNightIv4 = (ImageView) view.findViewById(R.id.wea_days_forecast_weather_night_iv4);
        mDaysForecastWeaTypeNightIv5 = (ImageView) view.findViewById(R.id.wea_days_forecast_weather_night_iv5);
        mDaysForecastWeaTypeNightIv6 = (ImageView) view.findViewById(R.id.wea_days_forecast_weather_night_iv6);

        mDaysForecastWeaTypeNightTv1 = (TextView) view.findViewById(R.id.wea_days_forecast_weather_night_tv1);
        mDaysForecastWeaTypeNightTv2 = (TextView) view.findViewById(R.id.wea_days_forecast_weather_night_tv2);
        mDaysForecastWeaTypeNightTv3 = (TextView) view.findViewById(R.id.wea_days_forecast_weather_night_tv3);
        mDaysForecastWeaTypeNightTv4 = (TextView) view.findViewById(R.id.wea_days_forecast_weather_night_tv4);
        mDaysForecastWeaTypeNightTv5 = (TextView) view.findViewById(R.id.wea_days_forecast_weather_night_tv5);
        mDaysForecastWeaTypeNightTv6 = (TextView) view.findViewById(R.id.wea_days_forecast_weather_night_tv6);

        mDaysForecastWindDirectionTv1 = (TextView) view.findViewById(R.id.wea_days_forecast_wind_direction_tv1);
        mDaysForecastWindDirectionTv2 = (TextView) view.findViewById(R.id.wea_days_forecast_wind_direction_tv2);
        mDaysForecastWindDirectionTv3 = (TextView) view.findViewById(R.id.wea_days_forecast_wind_direction_tv3);
        mDaysForecastWindDirectionTv4 = (TextView) view.findViewById(R.id.wea_days_forecast_wind_direction_tv4);
        mDaysForecastWindDirectionTv5 = (TextView) view.findViewById(R.id.wea_days_forecast_wind_direction_tv5);
        mDaysForecastWindDirectionTv6 = (TextView) view.findViewById(R.id.wea_days_forecast_wind_direction_tv6);

        mDaysForecastWindPowerTv1 = (TextView) view.findViewById(R.id.wea_days_forecast_wind_power_tv1);
        mDaysForecastWindPowerTv2 = (TextView) view.findViewById(R.id.wea_days_forecast_wind_power_tv2);
        mDaysForecastWindPowerTv3 = (TextView) view.findViewById(R.id.wea_days_forecast_wind_power_tv3);
        mDaysForecastWindPowerTv4 = (TextView) view.findViewById(R.id.wea_days_forecast_wind_power_tv4);
        mDaysForecastWindPowerTv5 = (TextView) view.findViewById(R.id.wea_days_forecast_wind_power_tv5);
        mDaysForecastWindPowerTv6 = (TextView) view.findViewById(R.id.wea_days_forecast_wind_power_tv6);

        mLifeIndexUmbrellaTv = (TextView) view.findViewById(R.id.wea_life_index_tv_umbrella);
        mLifeIndexulTravioletRaysTv = (TextView) view.findViewById(R.id.wea_life_index_tv_ultraviolet_rays);
        mLifeIndexDressTv = (TextView) view.findViewById(R.id.wea_life_tv_index_dress);
        mLifeIndexcoldTv = (TextView) view.findViewById(R.id.wea_life_index_tv_cold);
        mLifeIndexMorningExerciseTv = (TextView) view.findViewById(R.id.wea_life_index_tv_morning_exercise);
        mLifeIndexSportTv = (TextView) view.findViewById(R.id.wea_life_index_tv_sport);
        mLifeIndexCarWashTv = (TextView) view.findViewById(R.id.wea_life_index_tv_car_wash);
        mLifeIndexFishTv = (TextView) view.findViewById(R.id.wea_life_index_tv_fish);
    }

}
