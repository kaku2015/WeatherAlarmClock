/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.ScrollViewListener;
import com.kaku.weac.Listener.HttpCallbackListener;
import com.kaku.weac.R;
import com.kaku.weac.activities.CityManageActivity;
import com.kaku.weac.activities.LifeIndexDetailActivity;
import com.kaku.weac.activities.MyDialogActivity;
import com.kaku.weac.activities.WeatherAlarmActivity;
import com.kaku.weac.bean.CityManage;
import com.kaku.weac.bean.WeatherDaysForecast;
import com.kaku.weac.bean.WeatherInfo;
import com.kaku.weac.bean.WeatherLifeIndex;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.db.WeatherDBOperate;
import com.kaku.weac.util.HttpUtil;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.ToastUtil;
import com.kaku.weac.util.WeatherUtil;
import com.kaku.weac.view.LineChartViewDouble;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 天气fragment
 *
 * @author 咖枯
 * @version 1.0 2015/9
 */
public class WeaFragment extends LazyLoadFragment implements View.OnClickListener {
    /**
     * Log tag ：WeaFragment
     */
    private static final String LOG_TAG = "WeaFragment";

    /**
     * 天气的requestCode
     */
    private static final int REQUEST_WEA = 1;

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
     * 温度3
     */
    private ImageView mTemperature3Iv;

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
     * 温度曲线
     */
    private LineChartViewDouble mCharView;


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
     * 下拉刷新ScrollView
     */
    public PullToRefreshScrollView mPullRefreshScrollView;

    /**
     * 刷新按钮
     */
    public ImageView mRefreshBtn;

    /**
     * 延迟刷新线程是否已经启动
     */
    public boolean mIsPostDelayed;

    /**
     * 延迟刷新Handler
     */
    public Handler mHandler;

    /**
     * 延迟刷新Runnable
     */
    public Runnable mRun;

    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;

    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;

    /**
     * 上次主动更新时间
     */
    private long mLastActiveUpdateTime;

    /**
     * 设置壁纸
     */
    LinearLayout mBackGround;

    /**
     * 模糊处理过的Drawable
     */
    Drawable mBlurDrawable;

    /**
     * 屏幕密度
     */
    private float mDensity;

    /**
     * 透明
     */
    int mAlpha = 0;

    /**
     * 当前天气预报城市名
     */
    private String mCityName;

    /**
     * 当前天气预报城市天气代码
     */
    private String mCityWeatherCode;


    /**
     * 百度定位服务
     */
    private LocationClient mLocationClient;

    /**
     * 百度定位监听
     */
    private BDLocationListener mBDLocationListener;

    /**
     * 进度对话框
     */
    private ProgressDialog mProgressDialog;

    /**
     * 请求MyDialogActivity
     */
    private static final int REQUEST_MY_DIALOG = 1;

    /**
     * 首次打开天气界面
     */
    private boolean mIsFirstUse = false;

    /**
     * 是否立刻刷新
     */
    private boolean mIsPromptRefresh = true;

    /**
     * 是否自动定位过
     */
    private boolean mIsLocated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtil.i(LOG_TAG, "onCreateView");

        final View view = inflater.inflate(R.layout.fm_wea, container, false);
        init(view);

        mCityName = getDefaultCityName();
        mCityWeatherCode = getDefaultWeatherCode();

        // 不是第一次加载天气界面
        if (mCityName != null) {
            // 初始化天气
            // FIXME: 2015/10/29
//        try {
            initWeather(WeatherUtil.readWeatherInfo(getActivity(), mCityName));
//        } catch (Exception e) {
//            LogUtil.e(LOG_TAG, e.toString());
//        }

            // 不是自动定位
            if (!mCityWeatherCode.equals(getString(R.string.auto_location))) {
                isPrepared = true;
                lazyLoad();
            } else {
                mIsFirstUse = false;
                mIsPromptRefresh = false;
                startLocation();
            }
        } else {
            // 首次进入天气界面，自动定位天气
            mIsFirstUse = true;
            // 不立刻刷新
            mIsPromptRefresh = false;
            // 自动定位
            startLocation();
        }
        return view;
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        if (!MyUtil.isNetworkAvailable(getActivity())) {
            ToastUtil.showShortToast(getActivity(), getString(R.string.internet_error));
            return;
        }

        // 初始化定位管理，监听
        initLocationManager();
        mLocationClient.registerLocationListener(mBDLocationListener);    //注册监听函数
        initLocation();
        mLocationClient.start();
        mLocationClient.requestLocation();
    }


    /**
     * 初始化定位管理监听
     */
    private void initLocationManager() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(getActivity().getApplicationContext());
        }
        if (mBDLocationListener == null) {
            mBDLocationListener = new MyLocationListener();
        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.disableCache(true);// 禁止启用缓存定位\
        mLocationClient.setLocOption(option);
    }

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            mLocationClient.stop();
            mLocationClient.unRegisterLocationListener(mBDLocationListener);
            if (location == null) {
                ToastUtil.showShortToast(getActivity(), getString(R.string.location_fail));
                return;
            }
            LogUtil.d(LOG_TAG, "纬度：" + location.getLatitude() + ",经度：" + location.getLongitude());

            String address = location.getAddrStr();
            // 定位成功
            if (161 == location.getLocType() && address != null) {
                String cityName = MyUtil.formatCity(address);
                if (cityName != null) {
                    LogUtil.d(LOG_TAG, "城市名：" + cityName);
                    mCityName = cityName;
                    mCityWeatherCode = getString(R.string.auto_location);

                    // 初次加载定位保存
                    if (mIsFirstUse) {
                        SharedPreferences share = getActivity().getSharedPreferences(
                                WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = share.edit();
                        // 保存默认的城市名
                        editor.putString(WeacConstants.DEFAULT_CITY_NAME, mCityName);
                        // 保存默认的天气代码
                        editor.putString(WeacConstants.DEFAULT_WEATHER_CODE, mCityWeatherCode);
                        editor.apply();
                    }

                    // 立刻更新
                    if (mIsPromptRefresh) {
                        LogUtil.d(LOG_TAG, "onReceiveLocation：refreshWeather()");
                        refreshWeather();
                    } else {
                        mIsPromptRefresh = true;
                        mIsLocated = true;
                        isPrepared = true;
                        LogUtil.d(LOG_TAG, "onReceiveLocation：lazyLoad()");
                        lazyLoad();
                    }
                } else {
                    ToastUtil.showShortToast(getActivity(), getString(R.string.can_not_find_current_location));
                }
                // 定位失败
            } else {
                LogUtil.d(LOG_TAG, "error code: " + location.getLocType());
                Intent intent = new Intent(getActivity(), MyDialogActivity.class);
                intent.putExtra(WeacConstants.TITLE, getString(R.string.prompt));
                intent.putExtra(WeacConstants.DETAIL, getString(R.string.location_fail));
                intent.putExtra(WeacConstants.SURE_TEXT, getString(R.string.retry));
                startActivityForResult(intent, REQUEST_MY_DIALOG);
            }

        }
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !mIsVisible || mHasLoadedOnce) {
            return;
        }
        // 自动下拉刷新
        pullToRefresh();
    }

    /**
     * 下拉刷新
     */
    private void pullToRefresh() {
        mHandler = new Handler();
        mRun = new Runnable() {
            @Override
            public void run() {
                try {
                    mIsPostDelayed = false;
                    if (!getActivity().isFinishing()) {
                        if (!hasActiveUpdated()) {
                            mPullRefreshScrollView.setRefreshing();
                        }
                        // 加载成功
//                        mHasLoadedOnce = true;
                    }
                } catch (Exception e) {
                    LogUtil.e(LOG_TAG, e.toString());
                }
            }
        };
        mHandler.postDelayed(mRun, 2000);
        mIsPostDelayed = true;
    }


    /**
     * 是否3秒内主动更新过
     *
     * @return 主动更新与否
     */
    private boolean hasActiveUpdated() {
        if (mLastActiveUpdateTime == 0) {
            return false;
        }
        long now = SystemClock.elapsedRealtime();
        long timeD = now - mLastActiveUpdateTime;
        // 间隔3秒内不再自动更新
        return timeD <= 3000;
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

                // 自动定位
                if (mCityWeatherCode.equals(getString(R.string.auto_location))) {
                    locationPromptRefresh();
                } else {
                    refreshWeather();
                }
                ////////////////////////
//                Intent intent = new Intent(getActivity(), AutoUpdateService.class);
//                getActivity().startService(intent);
/////////////////////////////////////////////////
                break;
            // 城市管理按钮
            case R.id.action_home:
                // 不响应重复点击
                if (MyUtil.isFastDoubleClick()) {
                    return;
                }

                // 当不是天气界面并且已经开始延迟刷新天气线程
                if (mHandler != null && mIsPostDelayed) {
                    // 取消线程
                    mHandler.removeCallbacks(mRun);
                    mIsPostDelayed = false;
                }
                // 当正在刷新
                if (mPullRefreshScrollView.isRefreshing()) {
                    // 停止刷新
                    mPullRefreshScrollView.onRefreshComplete();
                }
                // 停止刷新动画
                mRefreshBtn.clearAnimation();

                Intent intent1 = new Intent(getActivity(), CityManageActivity.class);
                String cityName;
                // 自动定位
                if (mCityWeatherCode.equals(getString(R.string.auto_location))) {
                    cityName = getString(R.string.auto_location);
                } else {
                    cityName = mCityName;
                }
                intent1.putExtra(WeacConstants.CITY_NAME, cityName);
                startActivityForResult(intent1, REQUEST_WEA);


                //////////////////////
//                Intent i = new Intent(getActivity(), AutoUpdateReceiver.class);
//                PendingIntent p = PendingIntent.getBroadcast(getActivity(), 1000,
//                        i, PendingIntent.FLAG_UPDATE_CURRENT);
//                AlarmManager am = (AlarmManager) getActivity()
//                        .getSystemService(Activity.ALARM_SERVICE);
//                am.cancel(p);
                //////////////////////////
                break;
            // 雨伞指数
            case R.id.wea_life_index_rlyt_umbrella:
                skipToDetailInterface(getString(R.string.umbrella_detail), mLifeIndexUmbrellaDetail);
                break;
            // 紫外线指数
            case R.id.wea_life_index_rlyt_ultraviolet_rays:
                skipToDetailInterface(getString(R.string.ultraviolet_Rays_detail), mLifeIndexUltravioletRaysDetail);
                break;
            // 穿衣指数
            case R.id.wea_life_index_rlyt_dress:
                skipToDetailInterface(getString(R.string.dress_detail), mLifeIndexDressDetail);
                break;
            // 感冒指数
            case R.id.wea_life_index_rlyt_cold:
                skipToDetailInterface(getString(R.string.cold_detail), mLifeIndexColdDetail);
                break;
            // 晨练指数
            case R.id.wea_life_index_rlyt_morning_exercise:
                skipToDetailInterface(getString(R.string.morning_exercise_detail), mLifeIndexMorningExerciseDetail);
                break;
            // 运动指数
            case R.id.wea_life_index_rlyt_sport:
                skipToDetailInterface(getString(R.string.sport_detail), mLifeIndexSportDetail);
                break;
            // 洗车指数
            case R.id.wea_life_index_rlyt_carwash:
                skipToDetailInterface(getString(R.string.car_wash_detail), mLifeIndexCarWashDetail);
                break;
            // 晾晒指数
            case R.id.wea_life_index_rlyt_air_cure:
                skipToDetailInterface(getString(R.string.air_cure_detail), mLifeIndexAirCureDetail);
                break;
        }

    }

    /**
     * 定位并立即刷新
     */
    private void locationPromptRefresh() {
        mIsFirstUse = false;
        mIsPromptRefresh = true;
        startLocation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_WEA) {
            String cityName = data.getStringExtra(WeacConstants.CITY_NAME);
            if (!TextUtils.isEmpty(cityName)) {
                mCityName = cityName;
                mCityWeatherCode = data.getStringExtra(WeacConstants.WEATHER_CODE);

                // 滚动到顶端
                mPullRefreshScrollView.getRefreshableView().scrollTo(0, 0);
                WeatherInfo weatherInfo = WeatherUtil.readWeatherInfo(getActivity(), mCityName);
                if (weatherInfo == null) {
                    return;
                }
                initWeather(weatherInfo);

                long now = System.currentTimeMillis();
                SharedPreferences share = getActivity().getSharedPreferences(
                        WeacConstants.BASE64, Activity.MODE_PRIVATE);
                // 最近一次天气更新时间
                long lastTime = share.getLong(getString(R.string.city_weather_update_time,
                        weatherInfo.getCity()), 0);
                long minuteD = (now - lastTime) / 1000 / 60;
                // 更新间隔大于10分钟自动下拉刷新
                if (minuteD > 10) {
                    // 自动定位
                    if (mCityWeatherCode.equals(getString(R.string.auto_location))) {
                        locationPromptRefresh();
                    } else {
                        mPullRefreshScrollView.setRefreshing();
                    }
                }
            }
        }
    }

    /**
     * 取得默认城市名
     *
     * @return 默认城市名
     */
    private String getDefaultCityName() {
        SharedPreferences share = getActivity().getSharedPreferences(
                WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
        return share.getString(WeacConstants.DEFAULT_CITY_NAME, null);
    }

    /**
     * 取得默认天气代号
     *
     * @return 默认天气代号
     */
    private String getDefaultWeatherCode() {
        SharedPreferences share = getActivity().getSharedPreferences(
                WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
        return share.getString(WeacConstants.DEFAULT_WEATHER_CODE, "101010100");
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
        // 判断网络是否可用
        if (!MyUtil.isNetworkAvailable(getActivity())) {
            stopRefresh();
            ToastUtil.showShortToast(getActivity(), getString(R.string.internet_error));
            return;
        }

        // FIXME：回调try catch
        String address;
        String cityName;
        // 自动定位
        if (mCityWeatherCode.equals(getString(R.string.auto_location))) {
            cityName = mCityName;
            address = null;
        } else {
            cityName = null;
            address = getString(R.string.address_weather, mCityWeatherCode);
        }
        HttpUtil.sendHttpRequest(address, cityName,
//        HttpUtil.sendHttpRequest("http://wthrcdn.etouch.cn/WeatherApi?city=天津",
                new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
//                        try {
                        WeatherInfo weatherInfo = WeatherUtil.handleWeatherResponse(
                                new ByteArrayInputStream(response.getBytes()));
                        // 保存天气信息
                        WeatherUtil.saveWeatherInfo(weatherInfo, getActivity());
                        getActivity().runOnUiThread(new SetWeatherInfoRunnable(weatherInfo));
//                        } catch (Exception e) {
//                            LogUtil.e(LOG_TAG, e.toString());
//                        }
                    }

                    @Override
                    public void onError(final Exception e) {
//                        try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                stopRefresh();
                                ToastUtil.showShortToast(getActivity(),
                                        getString(R.string.internet_fail));
                            }
                        });
//                        } catch (Exception e1) {
//                            LogUtil.e(LOG_TAG, e1.toString());
//                        }
                    }
                }
        );
    }

    /**
     * 停止正在刷新动画
     */
    private void stopRefresh() {
        // 停止正在刷新动画
        mPullRefreshScrollView.onRefreshComplete();
        // 取消刷新按钮的动画
        mRefreshBtn.clearAnimation();
        // 最近一次更细时间
        mLastActiveUpdateTime = SystemClock.elapsedRealtime();
    }

    /**
     * 设置天气信息
     */
    class SetWeatherInfoRunnable implements Runnable {

        private WeatherInfo mWeatherInfo;

        public SetWeatherInfoRunnable(WeatherInfo weatherInfo) {
            mWeatherInfo = weatherInfo;
        }

        @Override
        public void run() {
            try {
                stopRefresh();
                initWeather(mWeatherInfo);
            } catch (Exception e) {
                LogUtil.e(LOG_TAG, "SetWeatherInfoRunnable: " + e.toString());
            }
        }
    }

    /**
     * 初始化天气
     *
     * @param weatherInfo 天气信息类
     */
    private void initWeather(final WeatherInfo weatherInfo) {
        if (weatherInfo == null) {
            return;
        }
        // 多天预报信息
        List<WeatherDaysForecast> weatherDaysForecasts = weatherInfo.getWeatherDaysForecast();

        // 昨天天气信息（23：45开始到05：20以前的数据的日期和周）
        WeatherDaysForecast weather;
        // 昨天天气信息
        WeatherDaysForecast weather1;
        // 今天天气信息
        WeatherDaysForecast weather2;
        // 明天天气信息
        WeatherDaysForecast weather3;
        // 后天天气信息
        WeatherDaysForecast weather4;
        // 第五天天天气信息
        WeatherDaysForecast weather5;
        // 第六天天气信息
        WeatherDaysForecast weather6;

        String time[] = weatherInfo.getUpdateTime().split(":");
        int hour1 = Integer.parseInt(time[0]);
        int minute1 = Integer.parseInt(time[1]);
        //更新时间从23：45开始到05：20以前的数据，后移一天填充
        if ((hour1 == 23 && minute1 >= 45) || (hour1 < 5) ||
                ((hour1 == 5) && (minute1 < 20))) {
            if (weatherDaysForecasts.size() == 6) {
                weather = weatherDaysForecasts.get(0);
                weather1 = weatherDaysForecasts.get(1);
                weather2 = weatherDaysForecasts.get(2);
                weather3 = weatherDaysForecasts.get(3);
                weather4 = weatherDaysForecasts.get(4);
                weather5 = weatherDaysForecasts.get(5);
                weather6 = weatherDaysForecasts.get(5);
            } else {
                weather = null;
                weather1 = weatherDaysForecasts.get(0);
                weather2 = weatherDaysForecasts.get(1);
                weather3 = weatherDaysForecasts.get(2);
                weather4 = weatherDaysForecasts.get(3);
                weather5 = weatherDaysForecasts.get(4);
                weather6 = weatherDaysForecasts.get(4);

            }
        } else {
            if (weatherDaysForecasts.size() == 6) {
                weather = weatherDaysForecasts.get(0);
                weather1 = weatherDaysForecasts.get(0);
                weather2 = weatherDaysForecasts.get(1);
                weather3 = weatherDaysForecasts.get(2);
                weather4 = weatherDaysForecasts.get(3);
                weather5 = weatherDaysForecasts.get(4);
                weather6 = weatherDaysForecasts.get(5);
            } else {
                weather = null;
                weather1 = null;
                weather2 = weatherDaysForecasts.get(0);
                weather3 = weatherDaysForecasts.get(1);
                weather4 = weatherDaysForecasts.get(2);
                weather5 = weatherDaysForecasts.get(3);
                weather6 = weatherDaysForecasts.get(4);
            }
        }

        Calendar calendar = Calendar.getInstance();
        // 现在小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (weatherInfo.getCity() != null) {
            // 设置城市名
            mCityNameTv.setText(weatherInfo.getCity());
            // 不是自动定位
            if (!getString(R.string.auto_location).equals(mCityWeatherCode)) {
                mCityNameTv.setCompoundDrawables(null, null, null, null);
            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.ic_gps);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                            drawable.getMinimumHeight());
                    // 设置图标
                    mCityNameTv.setCompoundDrawables(drawable, null, null, null);
                }
            }
        } else {
            mCityNameTv.setText(getString(R.string.dash));
            mCityNameTv.setCompoundDrawables(null, null, null, null);
        }

        // 设置预警信息
        if (weatherInfo.getAlarmType() != null) {
            mAlarmTv.setVisibility(View.VISIBLE);
            mAlarmTv.setText(getString(R.string.alarm, weatherInfo.getAlarmType()));
            mAlarmTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 警报详情
                    String detail = weatherInfo.getAlarmDetail();
                    // 替换换行"\r\n"  \\\：转义字符
                    detail = detail.replaceAll("\\\\r\\\\n", "");
                    String format;
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                .parse(weatherInfo.getAlarmTime());
                        format = new SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault()).format(date);
                    } catch (ParseException e) {
                        LogUtil.e(LOG_TAG, "initWeather: " + e.toString());
                        format = weatherInfo.getAlarmTime();
                    }
                    String time = getString(R.string.release_time, format);

                    Intent intent = new Intent(getActivity(), WeatherAlarmActivity.class);
                    intent.putExtra(WeacConstants.TITLE, getString(R.string.alarm_title,
                            weatherInfo.getAlarmType(), weatherInfo.getAlarmDegree()));
                    intent.putExtra(WeacConstants.DETAIL, detail);
                    intent.putExtra(WeacConstants.TIME, time);
                    startActivity(intent);
                }
            });
        } else {
            mAlarmTv.setVisibility(View.GONE);
        }
        // 设置更新时间
        if (weatherInfo.getUpdateTime() != null) {
            long now = System.currentTimeMillis();
            SharedPreferences share = getActivity().getSharedPreferences(
                    WeacConstants.BASE64, Activity.MODE_PRIVATE);
            // 最近一次天气更新时间
            long lastTime = share.getLong(getString(R.string.city_weather_update_time,
                    weatherInfo.getCity()), 0);
            // 更新间隔时间（小时）
            long minuteD = (now - lastTime) / 1000 / 60 / 60;
            // 更新时间
            String updateTime;
            if (minuteD < 24) {
                updateTime = String.format(getString(R.string.update_time),
                        weatherInfo.getUpdateTime());
            } else if (minuteD >= 24 && minuteD < 48) {
                updateTime = String.format(getString(R.string.update_time2),
                        weatherInfo.getUpdateTime());
            } else {
                updateTime = getString(R.string.data_void);
            }
            mUpdateTimeTv.setText(updateTime);
            // 当不是数据过期
            if (!updateTime.equals(getString(R.string.data_void))) {
                mUpdateTimeTv.setTextColor(getResources().getColor(R.color.white_trans60));
            } else {
                mUpdateTimeTv.setTextColor(getResources().getColor(R.color.red));
            }
        } else {
            mUpdateTimeTv.setText(getString(R.string.dash));
            mUpdateTimeTv.setTextColor(getResources().getColor(R.color.white_trans60));
        }

        // 设置温度
        String temp = weatherInfo.getTemperature();
        mTemperature1Iv.setVisibility(View.VISIBLE);
        mTemperature2Iv.setVisibility(View.VISIBLE);
        mTemperature3Iv.setVisibility(View.VISIBLE);
        if (temp != null) {
            // 两位正数
            if (temp.length() == 2 && !temp.contains("-")) {
                int temp1 = Integer.parseInt(temp.substring(0, 1));
                setTemperatureImage(temp1, mTemperature1Iv);
                int temp2 = Integer.parseInt(temp.substring(1));
                setTemperatureImage(temp2, mTemperature2Iv);
                mTemperature3Iv.setVisibility(View.GONE);
                // 一位
            } else if (temp.length() == 1 && !temp.contains("-")) {
                int temp1 = Integer.parseInt(temp);
                setTemperatureImage(temp1, mTemperature1Iv);
                mTemperature2Iv.setVisibility(View.GONE);
                mTemperature3Iv.setVisibility(View.GONE);
                // 两位负数
            } else if (temp.length() == 2 && temp.contains("-")) {
                mTemperature1Iv.setImageResource(R.drawable.minus);
                int temp2 = Integer.parseInt(temp.substring(1));
                setTemperatureImage(temp2, mTemperature2Iv);
                mTemperature3Iv.setVisibility(View.GONE);
                // 三位负数
            } else if (temp.length() == 3 && temp.contains("-")) {
                mTemperature1Iv.setImageResource(R.drawable.minus);
                int temp2 = Integer.parseInt(temp.substring(1, 2));
                setTemperatureImage(temp2, mTemperature2Iv);
                int temp3 = Integer.parseInt(temp.substring(2));
                setTemperatureImage(temp3, mTemperature3Iv);
            } else {
                mTemperature1Iv.setImageResource(R.drawable.number_0);
                mTemperature2Iv.setImageResource(R.drawable.number_0);
                mTemperature3Iv.setImageResource(R.drawable.number_0);
            }
        } else {
            mTemperature1Iv.setImageResource(R.drawable.number_0);
            mTemperature2Iv.setImageResource(R.drawable.number_0);
            mTemperature3Iv.setImageResource(R.drawable.number_0);
        }


        // 设置天气类型
        if (hour < 18) {
            // 白天天气
            mWeatherTypeTv.setText(weather2.getTypeDay());
        } else {
            // 夜间天气
            mWeatherTypeTv.setText(weather2.getTypeNight());
        }


        if (weatherInfo.getQuality() != null && weatherInfo.getAQI() != null) {
            mAqiTv.setVisibility(View.VISIBLE);
            // 设置空气质量图片
            setImage(mAqiTv, getQualityImageId(weatherInfo.getQuality()));
            // 设置空气质量
            mAqiTv.setText(String.format(getString(R.string.aqi),
                    weatherInfo.getQuality(), weatherInfo.getAQI()));
        } else {
            mAqiTv.setVisibility(View.GONE);
        }

        if (weatherInfo.getHumidity() != null) {
            // 设置湿度图片
            setImage(mHumidityTv, getHumidityImageId(weatherInfo.getHumidity()));
            // 设置湿度
            mHumidityTv.setText(String.format(getString(R.string.humidity),
                    weatherInfo.getHumidity()));
        } else {
            setImage(mHumidityTv, R.drawable.ic_humidity20);
            mHumidityTv.setText(R.string.no);
        }

        if (weatherInfo.getWindDirection() != null && weatherInfo.getWindPower() != null) {
            // 设置风向图片
            setImage(mWindTv, getWindImageId(weatherInfo.getWindDirection()));
            // 设置风向、风力
            mWindTv.setText(String.format(getString(R.string.aqi)
                    , weatherInfo.getWindDirection(), weatherInfo.getWindPower()));
        } else {
            setImage(mWindTv, R.drawable.ic_wind_3);
            mWindTv.setText(R.string.no);
        }

        // 天气类型图片id
        int weatherId;

        // 设置今天天气信息
        // 当前为凌晨
        if (hour >= 0 && hour < 6) {
            weatherId = MyUtil.getWeatherTypeImageID(weather2.getTypeDay(), false);
            // 当前为白天时
        } else if (hour >= 6 && hour < 18) {
            weatherId = MyUtil.getWeatherTypeImageID(weather2.getTypeDay(), true);
            // 当前为夜间
        } else {
            weatherId = MyUtil.getWeatherTypeImageID(weather2.getTypeNight(), false);
        }
        mWeatherTypeIvToday.setImageResource(weatherId);
        mTempHighTvToday.setText(weather2.getHigh().substring(3));
        mTempLowTvToday.setText(weather2.getLow().substring(3));
        mWeatherTypeTvToday.setText(getWeatherType
                (weather2.getTypeDay(), weather2.getTypeNight()));

        // 设置明天天气信息
        weatherId = MyUtil.getWeatherTypeImageID(weather3.getTypeDay(), true);
        mWeatherTypeIvTomorrow.setImageResource(weatherId);
        mTempHighTvTomorrow.setText(weather3.getHigh().substring(3));
        mTempLowTvTomorrow.setText(weather3.getLow().substring(3));
        mWeatherTypeTvTomorrow.setText(getWeatherType
                (weather3.getTypeDay(), weather3.getTypeNight()));

        // 设置后天天气信息
        weatherId = MyUtil.getWeatherTypeImageID(weather4.getTypeDay(), true);
        mWeatherTypeIvDayAfterTomorrow.setImageResource(weatherId);
        mTempHighTvDayAfterTomorrow.setText(weather4.getHigh().substring(3));
        mTempLowTvDayAfterTomorrow.setText(weather4.getLow().substring(3));
        mWeatherTypeTvDayAfterTomorrow.setText(getWeatherType
                (weather4.getTypeDay(), weather4.getTypeNight()));

        // 设置多天天气预报

        // 日期和星期标题 【索引0：日期;索引1：星期】
        String[] day1;
        String[] day2;
        String[] day3;
        String[] day4;
        String[] day5;
        String[] day6;
        if ((hour1 == 23 && minute1 >= 45) || (hour1 < 5) || ((hour1 == 5) && (minute1 < 20))) {
            if (weather != null) {
                day1 = getDay(weather.getDate());
            } else {
                day1 = null;
            }

            assert weather1 != null;
            day2 = getDay(weather1.getDate());
            day3 = getDay(weather2.getDate());
            day4 = getDay(weather3.getDate());
            day5 = getDay(weather4.getDate());
            day6 = getDay(weather5.getDate());
        } else {
            if (weather1 != null) {
                day1 = getDay(weather1.getDate());
            } else {
                day1 = null;
            }

            day2 = getDay(weather2.getDate());
            day3 = getDay(weather3.getDate());
            day4 = getDay(weather4.getDate());
            day5 = getDay(weather5.getDate());
            day6 = getDay(weather6.getDate());
        }

        // 设置标题星期
        mDaysForecastTvWeek1.setText(getString(R.string.yesterday));
        mDaysForecastTvWeek2.setText(getString(R.string.today));
        mDaysForecastTvWeek3.setText(getWeek(day3[1]));
        mDaysForecastTvWeek4.setText(getWeek(day4[1]));
        mDaysForecastTvWeek5.setText(getWeek(day5[1]));
        mDaysForecastTvWeek6.setText(getWeek(day6[1]));

        // 月份
        calendar.add(Calendar.DATE, -1);
        String month1 = MyUtil.addZero(calendar.get(Calendar.MONTH) + 1);
        calendar.add(Calendar.DATE, 1);
        String month2 = MyUtil.addZero(calendar.get(Calendar.MONTH) + 1);
        calendar.add(Calendar.DATE, 1);
        String month3 = MyUtil.addZero(calendar.get(Calendar.MONTH) + 1);
        calendar.add(Calendar.DATE, 1);
        String month4 = MyUtil.addZero(calendar.get(Calendar.MONTH) + 1);
        calendar.add(Calendar.DATE, 1);
        String month5 = MyUtil.addZero(calendar.get(Calendar.MONTH) + 1);
        calendar.add(Calendar.DATE, 1);
        String month6 = MyUtil.addZero(calendar.get(Calendar.MONTH) + 1);

        // 日
        String day01;
        if (day1 != null) {
            day01 = day1[0].split("日")[0];
        } else {
            day01 = null;
        }

        String day02 = day2[0].split("日")[0];
        String day03 = day3[0].split("日")[0];
        String day04 = day4[0].split("日")[0];
        String day05 = day5[0].split("日")[0];
        String day06 = day6[0].split("日")[0];

        // 斜杠
        String date = getString(R.string.date);
        // 设置日期
        if (day01 != null) {
            mDaysForecastTvDay1.setText(String.format(date, month1, day01));
        } else {
            mDaysForecastTvDay1.setText(R.string.dash);
        }

        mDaysForecastTvDay2.setText(String.format(date, month2, day02));
        mDaysForecastTvDay3.setText(String.format(date, month3, day03));
        mDaysForecastTvDay4.setText(String.format(date, month4, day04));
        mDaysForecastTvDay5.setText(String.format(date, month5, day05));
        mDaysForecastTvDay6.setText(String.format(date, month6, day06));

        // 取得白天天气类型图片id
        int weatherDayId1;
        if (weather != null) {
            assert weather1 != null;
            weatherDayId1 = MyUtil.getWeatherTypeImageID(weather1.getTypeDay(), true);
        } else {
            weatherDayId1 = R.drawable.ic_weather_no;
        }
        int weatherDayId2 = MyUtil.getWeatherTypeImageID(weather2.getTypeDay(), true);
        int weatherDayId3 = MyUtil.getWeatherTypeImageID(weather3.getTypeDay(), true);
        int weatherDayId4 = MyUtil.getWeatherTypeImageID(weather4.getTypeDay(), true);
        int weatherDayId5 = MyUtil.getWeatherTypeImageID(weather5.getTypeDay(), true);
        int weatherDayId6 = MyUtil.getWeatherTypeImageID(weather6.getTypeDay(), true);

        //设置白天天气类型图片
        mDaysForecastWeaTypeDayIv1.setImageResource(weatherDayId1);
        mDaysForecastWeaTypeDayIv2.setImageResource(weatherDayId2);
        mDaysForecastWeaTypeDayIv3.setImageResource(weatherDayId3);
        mDaysForecastWeaTypeDayIv4.setImageResource(weatherDayId4);
        mDaysForecastWeaTypeDayIv5.setImageResource(weatherDayId5);
        mDaysForecastWeaTypeDayIv6.setImageResource(weatherDayId6);

        // 设置白天天气类型文字
        if (weather != null) {
            mDaysForecastWeaTypeDayTv1.setText(weather1.getTypeDay());
        } else {
            mDaysForecastWeaTypeDayTv1.setText(R.string.dash);
        }
        mDaysForecastWeaTypeDayTv2.setText(weather2.getTypeDay());
        mDaysForecastWeaTypeDayTv3.setText(weather3.getTypeDay());
        mDaysForecastWeaTypeDayTv4.setText(weather4.getTypeDay());
        mDaysForecastWeaTypeDayTv5.setText(weather5.getTypeDay());
        mDaysForecastWeaTypeDayTv6.setText(weather6.getTypeDay());

        // 设置白天温度曲线
        if (weather != null) {
            mCharView.setTempDay(new int[]{getTemp(weather1.getHigh()),
                    getTemp(weather2.getHigh()), getTemp(weather3.getHigh()),
                    getTemp(weather4.getHigh()), getTemp(weather5.getHigh()),
                    getTemp(weather6.getHigh())});
        } else {
            mCharView.setTempDay(new int[]{-1000,
                    getTemp(weather2.getHigh()), getTemp(weather3.getHigh()),
                    getTemp(weather4.getHigh()), getTemp(weather5.getHigh()),
                    getTemp(weather6.getHigh())});
        }
        // 设置夜间温度曲线
        if (weather != null) {
            mCharView.setTempNight(new int[]{getTemp(weather1.getLow()),
                    getTemp(weather2.getLow()), getTemp(weather3.getLow()),
                    getTemp(weather4.getLow()), getTemp(weather5.getLow()),
                    getTemp(weather6.getLow())});
        } else {
            mCharView.setTempNight(new int[]{-1000,
                    getTemp(weather2.getLow()), getTemp(weather3.getLow()),
                    getTemp(weather4.getLow()), getTemp(weather5.getLow()),
                    getTemp(weather6.getLow())});
        }
        mCharView.invalidate();

        // 设置夜间天气类型文字
        if (weather != null) {
            mDaysForecastWeaTypeNightTv1.setText(weather1.getTypeNight());
        } else {
            mDaysForecastWeaTypeNightTv1.setText(R.string.dash);
        }
        mDaysForecastWeaTypeNightTv2.setText(weather2.getTypeNight());
        mDaysForecastWeaTypeNightTv3.setText(weather3.getTypeNight());
        mDaysForecastWeaTypeNightTv4.setText(weather4.getTypeNight());
        mDaysForecastWeaTypeNightTv5.setText(weather5.getTypeNight());
        mDaysForecastWeaTypeNightTv6.setText(weather6.getTypeNight());

        // 取得夜间天气类型图片id
        int weatherNightId1;
        if (weather != null) {
            weatherNightId1 = MyUtil.getWeatherTypeImageID(weather1.getTypeNight(), false);
        } else {
            weatherNightId1 = R.drawable.ic_weather_no;
        }
        int weatherNightId2 = MyUtil.getWeatherTypeImageID(weather2.getTypeNight(), false);
        int weatherNightId3 = MyUtil.getWeatherTypeImageID(weather3.getTypeNight(), false);
        int weatherNightId4 = MyUtil.getWeatherTypeImageID(weather4.getTypeNight(), false);
        int weatherNightId5 = MyUtil.getWeatherTypeImageID(weather5.getTypeNight(), false);
        int weatherNightId6 = MyUtil.getWeatherTypeImageID(weather6.getTypeNight(), false);

        //设置夜间天气类型图片
        mDaysForecastWeaTypeNightIv1.setImageResource(weatherNightId1);
        mDaysForecastWeaTypeNightIv2.setImageResource(weatherNightId2);
        mDaysForecastWeaTypeNightIv3.setImageResource(weatherNightId3);
        mDaysForecastWeaTypeNightIv4.setImageResource(weatherNightId4);
        mDaysForecastWeaTypeNightIv5.setImageResource(weatherNightId5);
        mDaysForecastWeaTypeNightIv6.setImageResource(weatherNightId6);

        // 设置风向
        if (weather != null) {
            mDaysForecastWindDirectionTv1.setText(weather1.getWindDirectionDay());
        } else {
            mDaysForecastWindDirectionTv1.setText(R.string.dash);
        }
        mDaysForecastWindDirectionTv2.setText(weather2.getWindDirectionDay());
        mDaysForecastWindDirectionTv3.setText(weather3.getWindDirectionDay());
        mDaysForecastWindDirectionTv4.setText(weather4.getWindDirectionDay());
        mDaysForecastWindDirectionTv5.setText(weather5.getWindDirectionDay());
        mDaysForecastWindDirectionTv6.setText(weather6.getWindDirectionDay());

        // 设置风力
        if (weather != null) {
            mDaysForecastWindPowerTv1.setText(weather1.getWindPowerDay());
        } else {
            mDaysForecastWindPowerTv1.setText(R.string.dash);
        }
        mDaysForecastWindPowerTv2.setText(weather2.getWindPowerDay());
        mDaysForecastWindPowerTv3.setText(weather3.getWindPowerDay());
        mDaysForecastWindPowerTv4.setText(weather4.getWindPowerDay());
        mDaysForecastWindPowerTv5.setText(weather5.getWindPowerDay());
        mDaysForecastWindPowerTv6.setText(weather6.getWindPowerDay());


        // 生活指数信息
        List<WeatherLifeIndex> weatherLifeIndexes = weatherInfo.getWeatherLifeIndex();
        // 设置生活指数
        for (WeatherLifeIndex index : weatherLifeIndexes) {
            setLifeIndex(index);
        }

        String cityName;
        // 自动定位
        if (getString(R.string.auto_location).equals(mCityWeatherCode)) {
            cityName = mCityWeatherCode;
        } else {
            cityName = weatherInfo.getCity();
        }

        CityManage cityManage = new CityManage();
        cityManage.setImageId(weatherId);
        cityManage.setTempHigh(weather2.getHigh().substring(3));
        cityManage.setTempLow(weather2.getLow().substring(3));
        cityManage.setWeatherType(getWeatherType
                (weather2.getTypeDay(), weather2.getTypeNight()));

        // CityManage表中存在此城市时
        if (1 == WeatherDBOperate.getInstance().queryCity(cityName)) {
            // 修改城市管理item信息
            WeatherDBOperate.getInstance().updateCityManage(cityManage, cityName);
        }

        // 首次进入天气界面
        if (mIsFirstUse && mCityWeatherCode.equals(getString(R.string.auto_location))) {
            int number = WeatherDBOperate.getInstance().queryCity(mCityWeatherCode);
            if (number == 1) {
                return;
            }
            cityManage.setCityName(mCityWeatherCode);
            cityManage.setWeatherCode(mCityWeatherCode);
            cityManage.setLocationCity(mCityName);

            // 存储城市管理item信息
            boolean result = WeatherDBOperate.getInstance().saveCityManage(cityManage);
            // 存储成功
            if (result) {
                mIsFirstUse = false;
                SharedPreferences share = getActivity().getSharedPreferences(
                        WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = share.edit();
                // 保存城市管理的默认城市
                editor.putString(WeacConstants.DEFAULT_CITY, mCityWeatherCode);
                editor.apply();
            }
        }

    }

    /**
     * 取得风向图片id
     *
     * @param windDirection 风向
     * @return 风向图片id
     */
    private int getWindImageId(String windDirection) {
        int imgId;
        switch (windDirection) {
            case "南风":
                imgId = R.drawable.ic_wind_1;
                break;
            case "西南风":
                imgId = R.drawable.ic_wind_2;
                break;
            case "西风":
                imgId = R.drawable.ic_wind_3;
                break;
            case "西北风":
                imgId = R.drawable.ic_wind_4;
                break;
            case "北风":
                imgId = R.drawable.ic_wind_5;
                break;
            case "东北风":
                imgId = R.drawable.ic_wind_6;
                break;
            case "东风":
                imgId = R.drawable.ic_wind_7;
                break;
            case "东南风":
                imgId = R.drawable.ic_wind_8;
                break;
            default:
                imgId = R.drawable.ic_wind_3;
                break;
        }
        return imgId;
    }

    /**
     * 取得湿度图片id
     *
     * @param humidity 湿度
     * @return 湿度图片id
     */
    private int getHumidityImageId(String humidity) {
        int imgId;
        int num = Integer.parseInt(humidity.split("%")[0]);
        if (num == 0)
            imgId = R.drawable.ic_humidity0;
        else if (num <= 10)
            imgId = R.drawable.ic_humidity10;
        else if (num <= 20)
            imgId = R.drawable.ic_humidity20;
        else if (num <= 30)
            imgId = R.drawable.ic_humidity30;
        else if (num <= 40)
            imgId = R.drawable.ic_humidity40;
        else if (num <= 50)
            imgId = R.drawable.ic_humidity50;
        else if (num <= 60)
            imgId = R.drawable.ic_humidity60;
        else if (num <= 70)
            imgId = R.drawable.ic_humidity70;
        else if (num <= 80)
            imgId = R.drawable.ic_humidity80;
        else if (num < 100)
            imgId = R.drawable.ic_humidity90;
        else if (num == 100)
            imgId = R.drawable.ic_humidity100;
        else imgId = R.drawable.ic_humidity20;
        return imgId;
    }

    /**
     * 设置左侧图片
     *
     * @param tv      textView
     * @param imageId 图片id
     */
    private void setImage(TextView tv, int imageId) {
        Drawable drawable = getResources().getDrawable(imageId);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            // 设置图片
            tv.setCompoundDrawables(drawable, null, null, null);
        }
    }

    /**
     * 取得aqi图片id
     *
     * @param quality 大气质量
     * @return aqi图片id
     */
    private int getQualityImageId(String quality) {
        int imgId;
        switch (quality) {
            case "优":
                imgId = R.drawable.ic_quality_nice;
                break;
            case "良":
                imgId = R.drawable.ic_quality_good;
                break;
            case "轻度污染":
                imgId = R.drawable.ic_quality_little;
                break;
            case "中度污染":
                imgId = R.drawable.ic_quality_medium;
                break;
            case "重度污染":
                imgId = R.drawable.ic_quality_serious;
                break;
            case "严重污染":
                imgId = R.drawable.ic_quality_terrible;
                break;
            default:
                imgId = R.drawable.ic_quality_nice;
                break;
        }
        return imgId;
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
                week1 = getString(R.string.monday);
                break;
            case "星期二":
                week1 = getString(R.string.tuesday);
                break;
            case "星期三":
                week1 = getString(R.string.wednesday);
                break;
            case "星期四":
                week1 = getString(R.string.thursday);
                break;
            case "星期五":
                week1 = getString(R.string.friday);
                break;
            case "星期六":
                week1 = getString(R.string.saturday);
                break;
            case "星期天":
            case "星期日":
                week1 = getString(R.string.sunday);
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
            return String.format(getString(R.string.turn), type1, type2);
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
     * 初始化控件
     *
     * @param view view
     */
    private void init(View view) {
        mRefreshBtn = (ImageView) view.findViewById(R.id.action_refresh);
        mRefreshBtn.setOnClickListener(this);
        // HOME按钮
        ImageView homeBtn = (ImageView) view.findViewById(R.id.action_home);
        homeBtn.setOnClickListener(this);

        mCityNameTv = (TextView) view.findViewById(R.id.action_title);
        mAlarmTv = (TextView) view.findViewById(R.id.alarm);
        mUpdateTimeTv = (TextView) view.findViewById(R.id.update_time);

        mTemperature1Iv = (ImageView) view.findViewById(R.id.temperature1);
        mTemperature2Iv = (ImageView) view.findViewById(R.id.temperature2);
        mTemperature3Iv = (ImageView) view.findViewById(R.id.temperature3);
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

        mCharView = (LineChartViewDouble) view.findViewById(R.id.line_char);

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

        mDensity = getResources().getDisplayMetrics().density;
        mBlurDrawable = MyUtil.getWallPaperDrawable(getActivity());
        mBackGround = (LinearLayout) view.findViewById(R.id.wea_background);

        mPullRefreshScrollView = (PullToRefreshScrollView) view
                .findViewById(R.id.pull_refresh_scrollview);
        // 设置下拉刷新
        setPullToRefresh();
        mPullRefreshScrollView.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {
//                LogUtil.i(LOG_TAG, "x: " + x + "y: " + y + "oldx: " + oldx + "oldy: " + oldy);
                // scroll最大滚动距离（xxxh：2320）/密度（xxxh：3）/1.5  =  515
                mAlpha = Math.round(Math.round(y / mDensity / 1.5));
                if (mAlpha > 255) {
                    mAlpha = 255;
                } else if (mAlpha < 0) {
                    mAlpha = 0;
                }
                // 设置模糊处理后drawable的透明度
                mBlurDrawable.setAlpha(mAlpha);
                // 设置背景
                mBackGround.setBackground(mBlurDrawable);
            }
        });
    }

    /**
     * 设置下拉刷新
     */

    private void setPullToRefresh() {
        mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel(getString(R.string.pull_to_refresh));
        mPullRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel(
                getString(R.string.refreshing));
        mPullRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel(getString(R.string.leave_to_refresh));
        mPullRefreshScrollView
                .setOnRefreshListener(new OnRefreshListener<ScrollView>() {

                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ScrollView> refreshView) {
                        LogUtil.d(LOG_TAG, "  setPullToRefresh()");
                        // 不是从自动定位返回
                        if (!mIsLocated && mCityWeatherCode.equals(getString(R.string.auto_location))) {
                            LogUtil.d(LOG_TAG, "  startLocation()");
                            locationPromptRefresh();
                        } else {
                            LogUtil.d(LOG_TAG, "  refreshWeather()");
                            mIsLocated = false;
                            refreshWeather();
                        }
//                        new GetDataTask().execute();
                    }
                });

    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
//            if (!hasActiveUpdated) {
//                refreshWeather();
//            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAlpha = 0;
        if (mHandler != null) {
            mHandler.removeCallbacks(mRun);
        }
    }


    /**
     * 显示进度对话框
     */
    private void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
