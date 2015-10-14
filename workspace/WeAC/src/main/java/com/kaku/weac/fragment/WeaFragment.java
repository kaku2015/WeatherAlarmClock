package com.kaku.weac.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.os.Handler;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.kaku.weac.R;
import com.kaku.weac.activities.LifeIndexDetailActivity;
import com.kaku.weac.bean.WeatherDaysForecast;
import com.kaku.weac.bean.WeatherInfo;
import com.kaku.weac.bean.WeatherLifeIndex;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.util.HttpCallbackListener;
import com.kaku.weac.util.LogUtil;
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
public class WeaFragment extends Fragment implements View.OnClickListener {
    /**
     * Log tag ：WeaFragment
     */
    private static final String LOG_TAG = "WeaFragment";

    /**
     * 城市名
     */
    private TextView mCityNameTv;

    /**
     * 警报
     */
    private TextView mAlarmTv;

    /**
     * 更新时间
     */
    private TextView mUpdateTimeTv;


    /**
     * 温度1
     */
    private ImageView mTemperature1Iv;

    /**
     * 温度2
     */
    private ImageView mTemperature2Iv;

    /**
     * 天气类型
     */
    private TextView mWeatherTypeTv;


    /**
     * 大气环境
     */
    private TextView mAqiTv;

    /**
     * 湿度
     */
    private TextView mHumidityTv;

    /**
     * 风向、风力
     */
    private TextView mWindTv;


    /**
     * 今天天气类型图片
     */
    private ImageView mWeatherTypeIvToday;

    /**
     * 今天高温
     */
    private TextView mTempHighTvToday;

    /**
     * 今天低温
     */
    private TextView mTempLowTvToday;

    /**
     * 今天天气类型文字
     */
    private TextView mWeatherTypeTvToday;


    /**
     * 明天天气类型图片
     */
    private ImageView mWeatherTypeIvTomorrow;

    /**
     * 明天高温
     */
    private TextView mTempHighTvTomorrow;

    /**
     * 明天低温
     */
    private TextView mTempLowTvTomorrow;

    /**
     * 明天天气类型文字
     */
    private TextView mWeatherTypeTvTomorrow;


    /**
     * 后天天气类型图片
     */
    private ImageView mWeatherTypeIvDayAfterTomorrow;

    /**
     * 后天高温
     */
    private TextView mTempHighTvDayAfterTomorrow;

    /**
     * 后天低温
     */
    private TextView mTempLowTvDayAfterTomorrow;

    /**
     * 后天天气类型文字
     */
    private TextView mWeatherTypeTvDayAfterTomorrow;


    /**
     * 多天预报标题1
     */
    private TextView mDaysForecastTvWeek1;

    /**
     * 多天预报标题2
     */
    private TextView mDaysForecastTvWeek2;

    /**
     * 多天预报标题3
     */
    private TextView mDaysForecastTvWeek3;

    /**
     * 多天预报标题4
     */
    private TextView mDaysForecastTvWeek4;

    /**
     * 多天预报标题5
     */
    private TextView mDaysForecastTvWeek5;

    /**
     * 多天预报标题6
     */
    private TextView mDaysForecastTvWeek6;


    /**
     * 多天预报日期1
     */
    private TextView mDaysForecastTvDay1;

    /**
     * 多天预报日期2
     */
    private TextView mDaysForecastTvDay2;

    /**
     * 多天预报日期3
     */
    private TextView mDaysForecastTvDay3;

    /**
     * 多天预报日期4
     */
    private TextView mDaysForecastTvDay4;

    /**
     * 多天预报日期5
     */
    private TextView mDaysForecastTvDay5;

    /**
     * 多天预报日期6
     */
    private TextView mDaysForecastTvDay6;


    /**
     * 多天预报白天天气类型图片1
     */
    private ImageView mDaysForecastWeaTypeDayIv1;

    /**
     * 多天预报白天天气类型图片2
     */
    private ImageView mDaysForecastWeaTypeDayIv2;

    /**
     * 多天预报白天天气类型图片3
     */
    private ImageView mDaysForecastWeaTypeDayIv3;

    /**
     * 多天预报白天天气类型图片4
     */
    private ImageView mDaysForecastWeaTypeDayIv4;

    /**
     * 多天预报白天天气类型图片5
     */
    private ImageView mDaysForecastWeaTypeDayIv5;

    /**
     * 多天预报白天天气类型图片6
     */
    private ImageView mDaysForecastWeaTypeDayIv6;


    /**
     * 多天预报白天天气类型文字1
     */
    private TextView mDaysForecastWeaTypeDayTv1;

    /**
     * 多天预报白天天气类型文字2
     */
    private TextView mDaysForecastWeaTypeDayTv2;

    /**
     * 多天预报白天天气类型文字3
     */
    private TextView mDaysForecastWeaTypeDayTv3;

    /**
     * 多天预报白天天气类型文字4
     */
    private TextView mDaysForecastWeaTypeDayTv4;

    /**
     * 多天预报白天天气类型文字5
     */
    private TextView mDaysForecastWeaTypeDayTv5;

    /**
     * 多天预报白天天气类型文字6
     */
    private TextView mDaysForecastWeaTypeDayTv6;


    /**
     * 白天温度曲线
     */
    private LineChartView mCharDay;

    /**
     * 夜间温度曲线
     */
    private LineChartView mCharNight;


    /**
     * 多天预报夜间天气类型图片1
     */
    private ImageView mDaysForecastWeaTypeNightIv1;

    /**
     * 多天预报夜间天气类型图片2
     */
    private ImageView mDaysForecastWeaTypeNightIv2;

    /**
     * 多天预报夜间天气类型图片3
     */
    private ImageView mDaysForecastWeaTypeNightIv3;

    /**
     * 多天预报夜间天气类型图片4
     */
    private ImageView mDaysForecastWeaTypeNightIv4;

    /**
     * 多天预报夜间天气类型图片5
     */
    private ImageView mDaysForecastWeaTypeNightIv5;

    /**
     * 多天预报夜间天气类型图片6
     */
    private ImageView mDaysForecastWeaTypeNightIv6;


    /**
     * 多天预报夜间天气类型文字1
     */
    private TextView mDaysForecastWeaTypeNightTv1;

    /**
     * 多天预报夜间天气类型文字2
     */
    private TextView mDaysForecastWeaTypeNightTv2;

    /**
     * 多天预报夜间天气类型文字3
     */
    private TextView mDaysForecastWeaTypeNightTv3;

    /**
     * 多天预报夜间天气类型文字4
     */
    private TextView mDaysForecastWeaTypeNightTv4;

    /**
     * 多天预报夜间天气类型文字5
     */
    private TextView mDaysForecastWeaTypeNightTv5;

    /**
     * 多天预报夜间天气类型文字6
     */
    private TextView mDaysForecastWeaTypeNightTv6;


    /**
     * 多天预报风向1
     */
    private TextView mDaysForecastWindDirectionTv1;

    /**
     * 多天预报风向2
     */
    private TextView mDaysForecastWindDirectionTv2;

    /**
     * 多天预报风向3
     */
    private TextView mDaysForecastWindDirectionTv3;

    /**
     * 多天预报风向4
     */
    private TextView mDaysForecastWindDirectionTv4;

    /**
     * 多天预报风向5
     */
    private TextView mDaysForecastWindDirectionTv5;

    /**
     * 多天预报风向6
     */
    private TextView mDaysForecastWindDirectionTv6;


    /**
     * 多天预报风力1
     */
    private TextView mDaysForecastWindPowerTv1;

    /**
     * 多天预报风力2
     */
    private TextView mDaysForecastWindPowerTv2;

    /**
     * 多天预报风力3
     */
    private TextView mDaysForecastWindPowerTv3;

    /**
     * 多天预报风力4
     */
    private TextView mDaysForecastWindPowerTv4;

    /**
     * 多天预报风力5
     */
    private TextView mDaysForecastWindPowerTv5;

    /**
     * 多天预报风力6
     */
    private TextView mDaysForecastWindPowerTv6;


    /**
     * 雨伞指数TextView
     */
    private TextView mLifeIndexUmbrellaTv;

    /**
     * 紫外线指数TextView
     */
    private TextView mLifeIndexUltravioletRaysTv;

    /**
     * 穿衣指数TextView
     */
    private TextView mLifeIndexDressTv;

    /**
     * 感冒指数TextView
     */
    private TextView mLifeIndexColdTv;

    /**
     * 晨练指数TextView
     */
    private TextView mLifeIndexMorningExerciseTv;

    /**
     * 运动指数TextView
     */
    private TextView mLifeIndexSportTv;

    /**
     * 洗车指数TextView
     */
    private TextView mLifeIndexCarWashTv;

    /**
     * 晾晒指数TextView
     */
    private TextView mLifeIndexAirCureTv;


    /**
     * 雨伞指数详细
     */
    private String mLifeIndexUmbrellaDetail;

    /**
     * 紫外线指数详细
     */
    private String mLifeIndexUltravioletRaysDetail;

    /**
     * 穿衣指数详细
     */
    private String mLifeIndexDressDetail;

    /**
     * 感冒指数详细
     */
    private String mLifeIndexColdDetail;

    /**
     * 晨练指数详细
     */
    private String mLifeIndexMorningExerciseDetail;

    /**
     * 运动指数详细
     */
    private String mLifeIndexSportDetail;

    /**
     * 洗车指数详细
     */
    private String mLifeIndexCarWashDetail;

    /**
     * 晾晒指数详细
     */
    private String mLifeIndexAirCureDetail;


    /**
     * 天气信息类
     */
    private WeatherInfo mWeatherInfo;

    /**
     * 生活指数信息
     */
    List<WeatherLifeIndex> mWeatherLifeIndexes;

    /**
     * 下拉刷新ScrollView
     */
    PullToRefreshScrollView mPullRefreshScrollView;

    /**
     * 刷新按钮
     */
    ImageView mRefreshBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fm_wea, container, false);
        init(view);

        mRefreshBtn = (ImageView) view.findViewById(R.id.action_refresh);
        mRefreshBtn.setOnClickListener(this);

        // 设置下拉刷新
        setPullToRefresh();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 刷新天气，还未获取到顶部下拉刷新的高度，适当的延时
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullRefreshScrollView.setRefreshing();
            }
        }, 1000);
    }

    /**
     * 设置下拉刷新
     */

    private void setPullToRefresh() {
        mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel("下拉更新");
        mPullRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel(
                "正在更新...");
        mPullRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel("松手可更新");
//        mPullRefreshScrollView.getLoadingLayoutProxy().
//                setLastUpdatedLabel("更新成功");
        mPullRefreshScrollView
                .setOnRefreshListener(new OnRefreshListener<ScrollView>() {

                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ScrollView> refreshView) {
                        new GetDataTask().execute();
                    }
                });

    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            refreshWeather();
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 刷新按钮
            case R.id.action_refresh:
                Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
                // 匀速
                LinearInterpolator lin = new LinearInterpolator();
                // 设置速率
                operatingAnim.setInterpolator(lin);
                mRefreshBtn.startAnimation(operatingAnim);
                // 刷新天气
                refreshWeather();
                break;
            // 雨伞指数
            case R.id.wea_life_index_rlyt_umbrella:
                skipToDetailInterface("雨伞指数详情", mLifeIndexUmbrellaDetail);
                break;
            // 紫外线指数
            case R.id.wea_life_index_rlyt_ultraviolet_rays:
                skipToDetailInterface("紫外线指数详情", mLifeIndexUltravioletRaysDetail);
                break;
            // 穿衣指数
            case R.id.wea_life_index_rlyt_dress:
                skipToDetailInterface("穿衣指数详情", mLifeIndexDressDetail);
                break;
            // 感冒指数
            case R.id.wea_life_index_rlyt_cold:
                skipToDetailInterface("感冒指数详情", mLifeIndexColdDetail);
                break;
            // 晨练指数
            case R.id.wea_life_index_rlyt_morning_exercise:
                skipToDetailInterface("晨练指数详情", mLifeIndexMorningExerciseDetail);
                break;
            // 运动指数
            case R.id.wea_life_index_rlyt_sport:
                skipToDetailInterface("运动指数详情", mLifeIndexSportDetail);
                break;
            // 洗车指数
            case R.id.wea_life_index_rlyt_carwash:
                skipToDetailInterface("洗车指数详情", mLifeIndexCarWashDetail);
                break;
            // 晾晒指数
            case R.id.wea_life_index_rlyt_air_cure:
                skipToDetailInterface("晾晒指数详情", mLifeIndexAirCureDetail);
                break;
        }

    }

    /**
     * 跳转到详情界面
     *
     * @param title  标题
     * @param detail 详情
     */
    private void skipToDetailInterface(String title, String detail) {
        Intent intent = new Intent(getActivity(), LifeIndexDetailActivity.class);
        intent.putExtra(WeacConstants.TITLE, title);
        intent.putExtra(WeacConstants.DETAIL, detail);
        startActivity(intent);
    }

    /**
     * 刷新天气
     */
    private void refreshWeather() {
        WeatherUtil.sendHttpRequest("http://wthrcdn.etouch.cn/WeatherApi?citykey=101030100",
                new HttpCallbackListener() {
                    @Override
                    public void onFinish(WeatherInfo weatherInfo) {
                        mWeatherInfo = weatherInfo;
                        getActivity().runOnUiThread(new SetWeatherInfoRunnable());
                    }

                    @Override
                    public void onError(final Exception e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mPullRefreshScrollView.getLoadingLayoutProxy().
//                                        setLastUpdatedLabel("更新失败");
                                // 取消刷新旋转动画
                                mRefreshBtn.clearAnimation();
                                mPullRefreshScrollView.onRefreshComplete();
                                LogUtil.e(LOG_TAG, "读取失败：" + e.toString());
                            }
                        });
                    }
                }

        );
    }

    /**
     * 设置天气信息
     */
    private class SetWeatherInfoRunnable implements Runnable {

        @Override
        public void run() {
            // 取消刷新旋转动画
            mRefreshBtn.clearAnimation();
            // 下拉刷新完成
            mPullRefreshScrollView.onRefreshComplete();
            // 多天预报信息
            List<WeatherDaysForecast> weatherDaysForecasts = mWeatherInfo.getWeatherDaysForecast();
            // 生活指数信息
            mWeatherLifeIndexes = mWeatherInfo.getWeatherLifeIndex();

            // 设置城市名
            mCityNameTv.setText(mWeatherInfo.getCity());
            // 设置预警信息
            if (mWeatherInfo.getAlarmType() != null) {
                mAlarmTv.setVisibility(View.VISIBLE);
                mAlarmTv.setText(mWeatherInfo.getAlarmType() + "预警");
                mAlarmTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skipToDetailInterface(mWeatherInfo.getAlarmType() +
                                mWeatherInfo.getAlarmDegree() + "预警", mWeatherInfo.getAlarmDetail());
                    }
                });
            } else {
                mAlarmTv.setVisibility(View.GONE);
            }
            // 设置更新时间
            mUpdateTimeTv.setText(mWeatherInfo.getUpdateTime() + "发布");

            // 设置温度
            String temp = mWeatherInfo.getTemperature();
            int temp1 = Integer.parseInt(temp.substring(0, 1));
            int temp2 = Integer.parseInt(temp.substring(1));
            setTemperatureImage(temp1, mTemperature1Iv);
            setTemperatureImage(temp2, mTemperature2Iv);

            // 今天天气信息
            WeatherDaysForecast weather2 = weatherDaysForecasts.get(1);
            // 明天天气信息
            WeatherDaysForecast weather3 = weatherDaysForecasts.get(2);
            // 后天天气信息
            WeatherDaysForecast weather4 = weatherDaysForecasts.get(3);
            // 现在小时
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            // 设置天气类型
            if (hour < 18) {
                // 白天天气
                mWeatherTypeTv.setText(weather2.getTypeDay());
            } else {
                // 夜间天气
                mWeatherTypeTv.setText(weather2.getTypeNight());
            }

            // 设置大气环境
            mAqiTv.setText(mWeatherInfo.getQuality() + " " + mWeatherInfo.getAQI());
            // 设置湿度
            mHumidityTv.setText("湿度 " + mWeatherInfo.getHumidity());
            // 设置风向、风力
            mWindTv.setText(mWeatherInfo.getWindDirection() + " "
                    + mWeatherInfo.getWindPower());

            int weatherId;

            // 设置今天天气信息
            // 当前为凌晨
            if (hour >= 0 && hour < 6) {
                weatherId = getWeatherTypeImageID(weather2.getTypeDay(), false);
                // 当前为白天时
            } else if (hour >= 6 && hour < 18) {
                weatherId = getWeatherTypeImageID(weather2.getTypeDay(), true);
                // 当前为夜间
            } else {
                weatherId = getWeatherTypeImageID(weather2.getTypeNight(), false);
            }
            mWeatherTypeIvToday.setImageResource(weatherId);
            mTempHighTvToday.setText(weather2.getHigh().substring(3));
            mTempLowTvToday.setText(weather2.getLow().substring(3));
            mWeatherTypeTvToday.setText(getWeatherType
                    (weather2.getTypeDay(), weather2.getTypeNight()));

            // 设置明天天气信息
            weatherId = getWeatherTypeImageID(weather3.getTypeDay(), true);
            mWeatherTypeIvTomorrow.setImageResource(weatherId);
            mTempHighTvTomorrow.setText(weather3.getHigh().substring(3));
            mTempLowTvTomorrow.setText(weather3.getLow().substring(3));
            mWeatherTypeTvTomorrow.setText(getWeatherType
                    (weather3.getTypeDay(), weather3.getTypeNight()));

            // 设置后天天气信息
            weatherId = getWeatherTypeImageID(weather4.getTypeDay(), true);
            mWeatherTypeIvDayAfterTomorrow.setImageResource(weatherId);
            mTempHighTvDayAfterTomorrow.setText(weather4.getHigh().substring(3));
            mTempLowTvDayAfterTomorrow.setText(weather4.getLow().substring(3));
            mWeatherTypeTvDayAfterTomorrow.setText(getWeatherType
                    (weather4.getTypeDay(), weather4.getTypeNight()));

            // 设置多天天气预报

            // 昨天天气信息
            WeatherDaysForecast weather1 = weatherDaysForecasts.get(0);
            // 第五天天天气信息
            WeatherDaysForecast weather5 = weatherDaysForecasts.get(4);
            // 第六天天气信息
            WeatherDaysForecast weather6 = weatherDaysForecasts.get(5);

            // 日期和星期标题 【索引0：日期;索引1：星期】
            String[] day1 = getDay(weather1.getDate());
            String[] day2 = getDay(weather2.getDate());
            String[] day3 = getDay(weather3.getDate());
            String[] day4 = getDay(weather4.getDate());
            String[] day5 = getDay(weather5.getDate());
            String[] day6 = getDay(weather6.getDate());

            // 设置标题星期
            mDaysForecastTvWeek1.setText("昨天");
            mDaysForecastTvWeek2.setText("今天");
            mDaysForecastTvWeek3.setText(getWeek(day3[1]));
            mDaysForecastTvWeek4.setText(getWeek(day4[1]));
            mDaysForecastTvWeek5.setText(getWeek(day5[1]));
            mDaysForecastTvWeek6.setText(getWeek(day6[1]));

            // 当前月份
            String month = MyUtil.addZero(calendar.get(Calendar.MONTH) + 1);

            // 日
            String day01 = day1[0].split("日")[0];
            String day02 = day2[0].split("日")[0];
            String day03 = day3[0].split("日")[0];
            String day04 = day4[0].split("日")[0];
            String day05 = day5[0].split("日")[0];
            String day06 = day6[0].split("日")[0];

            // 设置日期
            mDaysForecastTvDay1.setText(month + "/" + day01);
            mDaysForecastTvDay2.setText(month + "/" + day02);
            mDaysForecastTvDay3.setText(month + "/" + day03);
            mDaysForecastTvDay4.setText(month + "/" + day04);
            mDaysForecastTvDay5.setText(month + "/" + day05);
            mDaysForecastTvDay6.setText(month + "/" + day06);

            // 取得白天天气类型图片id
            int weatherDayId1 = getWeatherTypeImageID(weather1.getTypeDay(), true);
            int weatherDayId2 = getWeatherTypeImageID(weather2.getTypeDay(), true);
            int weatherDayId3 = getWeatherTypeImageID(weather3.getTypeDay(), true);
            int weatherDayId4 = getWeatherTypeImageID(weather4.getTypeDay(), true);
            int weatherDayId5 = getWeatherTypeImageID(weather5.getTypeDay(), true);
            int weatherDayId6 = getWeatherTypeImageID(weather6.getTypeDay(), true);

            //设置白天天气类型图片
            mDaysForecastWeaTypeDayIv1.setImageResource(weatherDayId1);
            mDaysForecastWeaTypeDayIv2.setImageResource(weatherDayId2);
            mDaysForecastWeaTypeDayIv3.setImageResource(weatherDayId3);
            mDaysForecastWeaTypeDayIv4.setImageResource(weatherDayId4);
            mDaysForecastWeaTypeDayIv5.setImageResource(weatherDayId5);
            mDaysForecastWeaTypeDayIv6.setImageResource(weatherDayId6);

            // 设置白天天气类型文字
            mDaysForecastWeaTypeDayTv1.setText(weather1.getTypeDay());
            mDaysForecastWeaTypeDayTv2.setText(weather2.getTypeDay());
            mDaysForecastWeaTypeDayTv3.setText(weather3.getTypeDay());
            mDaysForecastWeaTypeDayTv4.setText(weather4.getTypeDay());
            mDaysForecastWeaTypeDayTv5.setText(weather5.getTypeDay());
            mDaysForecastWeaTypeDayTv6.setText(weather6.getTypeDay());

            // 设置白天温度曲线
            mCharDay.setTemp(new int[]{getTemp(weather1.getHigh()),
                    getTemp(weather2.getHigh()), getTemp(weather3.getHigh()),
                    getTemp(weather4.getHigh()), getTemp(weather5.getHigh()),
                    getTemp(weather6.getHigh())});
            // 设置文字距离坐标距离
            mCharDay.setTextSpace(10);
            //noinspection deprecation
            int colorDay = getResources().getColor(R.color.yellow_hot);
            mCharDay.setLineColor(colorDay);
            mCharDay.setPointColor(colorDay);
            // 重新绘制
            mCharDay.invalidate();

            // 设置夜间温度曲线
            mCharNight.setTemp(new int[]{getTemp(weather1.getLow()),
                    getTemp(weather2.getLow()), getTemp(weather3.getLow()),
                    getTemp(weather4.getLow()), getTemp(weather5.getLow()),
                    getTemp(weather6.getLow())});
            mCharNight.setTextSpace(-10);
            //noinspection deprecation
            int colorNight = getResources().getColor(R.color.blue_ice);
            mCharNight.setLineColor(colorNight);
            mCharNight.setPointColor(colorNight);
            mCharNight.invalidate();

            // 设置夜间天气类型文字
            mDaysForecastWeaTypeNightTv1.setText(weather1.getTypeNight());
            mDaysForecastWeaTypeNightTv2.setText(weather2.getTypeNight());
            mDaysForecastWeaTypeNightTv3.setText(weather3.getTypeNight());
            mDaysForecastWeaTypeNightTv4.setText(weather4.getTypeNight());
            mDaysForecastWeaTypeNightTv5.setText(weather5.getTypeNight());
            mDaysForecastWeaTypeNightTv6.setText(weather6.getTypeNight());

            // 取得夜间天气类型图片id
            int weatherNightId1 = getWeatherTypeImageID(weather1.getTypeNight(), false);
            int weatherNightId2 = getWeatherTypeImageID(weather2.getTypeNight(), false);
            int weatherNightId3 = getWeatherTypeImageID(weather3.getTypeNight(), false);
            int weatherNightId4 = getWeatherTypeImageID(weather4.getTypeNight(), false);
            int weatherNightId5 = getWeatherTypeImageID(weather5.getTypeNight(), false);
            int weatherNightId6 = getWeatherTypeImageID(weather6.getTypeNight(), false);

            //设置夜间天气类型图片
            mDaysForecastWeaTypeNightIv1.setImageResource(weatherNightId1);
            mDaysForecastWeaTypeNightIv2.setImageResource(weatherNightId2);
            mDaysForecastWeaTypeNightIv3.setImageResource(weatherNightId3);
            mDaysForecastWeaTypeNightIv4.setImageResource(weatherNightId4);
            mDaysForecastWeaTypeNightIv5.setImageResource(weatherNightId5);
            mDaysForecastWeaTypeNightIv6.setImageResource(weatherNightId6);

            // 设置风向
            mDaysForecastWindDirectionTv1.setText(weather1.getWindDirectionDay());
            mDaysForecastWindDirectionTv2.setText(weather2.getWindDirectionDay());
            mDaysForecastWindDirectionTv3.setText(weather3.getWindDirectionDay());
            mDaysForecastWindDirectionTv4.setText(weather4.getWindDirectionDay());
            mDaysForecastWindDirectionTv5.setText(weather5.getWindDirectionDay());
            mDaysForecastWindDirectionTv6.setText(weather6.getWindDirectionDay());

            // 设置风力
            mDaysForecastWindPowerTv1.setText(weather1.getWindPowerDay());
            mDaysForecastWindPowerTv2.setText(weather2.getWindPowerDay());
            mDaysForecastWindPowerTv3.setText(weather3.getWindPowerDay());
            mDaysForecastWindPowerTv4.setText(weather4.getWindPowerDay());
            mDaysForecastWindPowerTv5.setText(weather5.getWindPowerDay());
            mDaysForecastWindPowerTv6.setText(weather6.getWindPowerDay());

            // 设置生活指数
            for (WeatherLifeIndex index : mWeatherLifeIndexes) {
                setLifeIndex(index);
            }

        }

        /**
         * 设置生活指数
         *
         * @param index 生活指数信息
         */
        private void setLifeIndex(WeatherLifeIndex index) {
            switch (index.getIndexName()) {
                case "雨伞指数":
                    mLifeIndexUmbrellaTv.setText(index.getIndexValue());
                    mLifeIndexUmbrellaDetail = index.getIndexDetail();
                    break;
                case "紫外线强度":
                    mLifeIndexUltravioletRaysTv.setText(index.getIndexValue());
                    mLifeIndexUltravioletRaysDetail = index.getIndexDetail();
                    break;
                case "穿衣指数":
                    mLifeIndexDressTv.setText(index.getIndexValue());
                    mLifeIndexDressDetail = index.getIndexDetail();
                    break;
                case "感冒指数":
                    mLifeIndexColdTv.setText(index.getIndexValue());
                    mLifeIndexColdDetail = index.getIndexDetail();
                    break;
                case "晨练指数":
                    mLifeIndexMorningExerciseTv.setText(index.getIndexValue());
                    mLifeIndexMorningExerciseDetail = index.getIndexDetail();
                    break;
                case "运动指数":
                    mLifeIndexSportTv.setText(index.getIndexValue());
                    mLifeIndexSportDetail = index.getIndexDetail();
                    break;
                case "洗车指数":
                    mLifeIndexCarWashTv.setText(index.getIndexValue());
                    mLifeIndexCarWashDetail = index.getIndexDetail();
                    break;
                case "晾晒指数":
                    mLifeIndexAirCureTv.setText(index.getIndexValue());
                    mLifeIndexAirCureDetail = index.getIndexDetail();
                    break;

            }
        }

        /**
         * 取得温度
         *
         * @param temp 温度信息
         * @return 温度
         */
        private int getTemp(String temp) {
            String temperature;
            if (!temp.contains("-")) {
                if (temp.length() == 6) {
                    temperature = temp.substring(3, 5);
                } else {
                    temperature = temp.substring(3, 4);
                }
            } else {
                if (temp.length() == 7) {
                    temperature = temp.substring(3, 6);
                } else {
                    temperature = temp.substring(3, 5);
                }
            }
            return Integer.parseInt(temperature);
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
         *
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
                case "星期天":
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
         *
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
         *
         * @param temp1     温度
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
         *
         * @param type  天气类型
         * @param isDay 是否为白天
         * @return 天气类型图片id
         */
        private int getWeatherTypeImageID(String type, boolean isDay) {
            int weatherId;
            switch (type) {
                case "晴":
                    if (isDay) {
                        weatherId = R.drawable.ic_weather_sunny_day;
                    } else {
                        weatherId = R.drawable.ic_weather_sunny_night;
                    }
                    break;
                case "多云":
                    if (isDay) {
                        weatherId = R.drawable.ic_weather_cloudy_day;
                    } else {
                        weatherId = R.drawable.ic_weather_cloudy_night;
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

        mTempLowTvToday = (TextView) view.findViewById(R.id.temp_low_today);
        mTempLowTvTomorrow = (TextView) view.findViewById(R.id.temp_low_tomorrow);
        mTempLowTvDayAfterTomorrow = (TextView) view.findViewById(R.id.temp_low_day_after_tomorrow);

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
        mLifeIndexUltravioletRaysTv = (TextView) view.findViewById(R.id.wea_life_index_tv_ultraviolet_rays);
        mLifeIndexDressTv = (TextView) view.findViewById(R.id.wea_life_tv_index_dress);
        mLifeIndexColdTv = (TextView) view.findViewById(R.id.wea_life_index_tv_cold);
        mLifeIndexMorningExerciseTv = (TextView) view.findViewById(R.id.wea_life_index_tv_morning_exercise);
        mLifeIndexSportTv = (TextView) view.findViewById(R.id.wea_life_index_tv_sport);
        mLifeIndexCarWashTv = (TextView) view.findViewById(R.id.wea_life_index_tv_car_wash);
        mLifeIndexAirCureTv = (TextView) view.findViewById(R.id.wea_life_index_tv_air_cure);

        // 雨伞指数控件
        RelativeLayout lifeIndexUmbrellaRlyt = (RelativeLayout) view.findViewById(R.id.wea_life_index_rlyt_umbrella);
        // 紫外线指数控件
        RelativeLayout lifeIndexUltravioletRaysRlyt = (RelativeLayout) view.findViewById(R.id.wea_life_index_rlyt_ultraviolet_rays);
        // 穿衣指数控件
        RelativeLayout lifeIndexDressRlyt = (RelativeLayout) view.findViewById(R.id.wea_life_index_rlyt_dress);
        // 感冒指数控件
        RelativeLayout lifeIndexColdRlyt = (RelativeLayout) view.findViewById(R.id.wea_life_index_rlyt_cold);
        // 晨练指数控件
        RelativeLayout lifeIndexMorningExerciseRlyt = (RelativeLayout) view.findViewById(R.id.wea_life_index_rlyt_morning_exercise);
        //  运动指数控件
        RelativeLayout lifeIndexSportRlyt = (RelativeLayout) view.findViewById(R.id.wea_life_index_rlyt_sport);
        // 洗车指数控件
        RelativeLayout lifeIndexCarWashRlyt = (RelativeLayout) view.findViewById(R.id.wea_life_index_rlyt_carwash);
        // 晾晒指数控件
        RelativeLayout lifeIndexAirCureRlyt = (RelativeLayout) view.findViewById(R.id.wea_life_index_rlyt_air_cure);

        lifeIndexUmbrellaRlyt.setOnClickListener(this);
        lifeIndexUltravioletRaysRlyt.setOnClickListener(this);
        lifeIndexDressRlyt.setOnClickListener(this);
        lifeIndexColdRlyt.setOnClickListener(this);
        lifeIndexMorningExerciseRlyt.setOnClickListener(this);
        lifeIndexSportRlyt.setOnClickListener(this);
        lifeIndexCarWashRlyt.setOnClickListener(this);
        lifeIndexAirCureRlyt.setOnClickListener(this);

        mPullRefreshScrollView = (PullToRefreshScrollView) view
                .findViewById(R.id.pull_refresh_scrollview);
    }

}
