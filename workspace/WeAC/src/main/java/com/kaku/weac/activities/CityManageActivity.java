/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kaku.weac.Listener.DBObserverListener;
import com.kaku.weac.Listener.HttpCallbackListener;
import com.kaku.weac.R;
import com.kaku.weac.adapter.CityManageAdapter;
import com.kaku.weac.bean.CityManage;
import com.kaku.weac.bean.WeatherDaysForecast;
import com.kaku.weac.bean.WeatherInfo;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.db.WeatherDBOperate;
import com.kaku.weac.fragment.WeaFragment;
import com.kaku.weac.util.HttpUtil;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.ToastUtil;
import com.kaku.weac.util.WeatherUtil;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 城市管理activity
 *
 * @author 咖枯
 * @version 1.0 2015/10/22
 */
public class CityManageActivity extends BaseActivity implements View.OnClickListener {

    /**
     * Log tag ：CityManageActivity
     */
    private static final String LOG_TAG = "CityManageActivity";

    /**
     * 城市管理的requestCode
     */
    private static final int REQUEST_CITY_MANAGE = 1;

    /**
     * 查询天气
     */
    private static final int QUERY_WEATHER = 1;

    /**
     * 查询县
     */
    private static final int QUERY_COUNTRY = 2;

    /**
     * 添加城市
     */
    private static final int ADD_CITY = -1;

    /**
     * 添加定位
     */
    private static final int ADD_LOCATION = -2;

    /**
     * 城市管理列表
     */
    private List<CityManage> mCityManageList;

    /**
     * 城市管理adapter
     */
    private CityManageAdapter mCityManageAdapter;

    /**
     * 天气代码
     */
    private String mWeatherCode;

    /**
     * 城市管理GridView
     */
    private GridView mGridView;

    /**
     * 操作栏编辑按钮
     */
    private ImageView mEditBtn;

    /**
     * 操作栏编辑完成按钮
     */
    private ImageView mAcceptBtn;

    /**
     * 刷新按钮
     */
    private ImageView mRefreshBtn;

    /**
     * 取消刷新按钮
     */
    private ImageView mCancelRefreshBtn;

    /**
     * 监听城市点击事件Listener
     */
    private AdapterView.OnItemClickListener mOnItemClickListener;

    /**
     * 城市管理实例
     */
    private CityManage mCityManage;

    /**
     * 是否正在刷新
     */
    private boolean mIsRefreshing;

    /**
     * 添加城市按钮点击时间
     */
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manage);
        LinearLayout backGround = (LinearLayout) findViewById(R.id.city_manage_background);
        // 设置页面高斯模糊背景
        MyUtil.setBackgroundBlur(backGround, this);
        // 设置adapter
        initAdapter();
        // 设置布局元素
        initViews();
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        mCityManageList = new ArrayList<>();
        mCityManageList = WeatherDBOperate.getInstance().loadCityManages();
        // 添加城市按钮用
        CityManage cityManage1 = new CityManage();
        mCityManageList.add(cityManage1);
        mCityManageAdapter = new CityManageAdapter(this, mCityManageList);
        // 城市列表为空的更新回调
        mCityManageAdapter.setDBObserverListener(new DBObserverListener() {
            @Override
            public void onDBDataChanged() {
                // 允许列表item点击
                mGridView.setOnItemClickListener(mOnItemClickListener);
                // 显示编辑按钮
                mEditBtn.setVisibility(View.VISIBLE);
                // 隐藏完成按钮
                mAcceptBtn.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 初始化布局元素
     */
    private void initViews() {
        mOnItemClickListener = new OnItemClickListenerImpl();
        mGridView = (GridView) findViewById(R.id.gv_city_manage);
        mGridView.setAdapter(mCityManageAdapter);
        mGridView.setOnItemClickListener(mOnItemClickListener);
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != (mCityManageList.size() - 1)) {
                    mIsRefreshing = false;
                    // 显示删除，完成按钮，隐藏修改按钮
                    displayDeleteAccept();
                }
                return true;
            }
        });

        // 返回按钮
        ImageView returnBtn = (ImageView) findViewById(R.id.action_return);
        returnBtn.setOnClickListener(this);
        // 编辑闹钟
        mEditBtn = (ImageView) findViewById(R.id.action_edit);
        mEditBtn.setOnClickListener(this);
        // 完成按钮
        mAcceptBtn = (ImageView) findViewById(R.id.action_accept);
        mAcceptBtn.setOnClickListener(this);
        // 刷新按钮
        mRefreshBtn = (ImageView) findViewById(R.id.action_refresh);
        mRefreshBtn.setOnClickListener(this);
        // 取消刷新按钮
        mCancelRefreshBtn = (ImageView) findViewById(R.id.action_refresh_cancel);
        mCancelRefreshBtn.setOnClickListener(this);
    }

    class OnItemClickListenerImpl implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mIsRefreshing = false;
            // 当为列表最后一项（添加城市）
            if (position == (mCityManageList.size() - 1)) {
                // 不响应重复点击
                if (isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(CityManageActivity.this, AddCityActivity.class);
                startActivityForResult(intent, REQUEST_CITY_MANAGE);
            } else {
                CityManage cityManage = mCityManageAdapter.getItem(position);
                String cityName = cityManage.getCityName();
                // 不是自动定位
                if (!cityName.equals(getString(R.string.auto_location))) {
                    saveDefaultCityInfoAndReturn(position, null);
                } else {
                    saveDefaultCityInfoAndReturn(position, cityManage.getLocationCity());
                }
            }
        }
    }

    /**
     * 设置默认城市信息并返回
     *
     * @param position     保存默认城市的位置
     * @param locationCity 定位城市名
     */
    private void saveDefaultCityInfoAndReturn(int position, String locationCity) {
        SharedPreferences share = getSharedPreferences(
                WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        CityManage cityManage = mCityManageAdapter.getItem(position);

        String weatherCode;
        String cityName;
        // 不是自定定位
        if (locationCity == null) {
            // 保存默认的城市名
            cityName = cityManage.getCityName();
            weatherCode = cityManage.getWeatherCode();
            // 定位城市
        } else {
            cityName = locationCity;
            weatherCode = getString(R.string.auto_location);
        }

        // 保存默认的城市名
        editor.putString(WeacConstants.DEFAULT_CITY_NAME, cityName);
        // 保存默认的天气代码
        editor.putString(WeacConstants.DEFAULT_WEATHER_CODE, weatherCode);
        editor.apply();

        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * 显示删除，完成按钮，隐藏修改按钮
     */
    private void displayDeleteAccept() {
        // 禁止gridView点击事件
        mGridView.setOnItemClickListener(null);
        mCityManageAdapter.setCityDeleteButton(true);
        mCityManageAdapter.notifyDataSetChanged();
        mEditBtn.setVisibility(View.GONE);
        mAcceptBtn.setVisibility(View.VISIBLE);

    }

    /**
     * 隐藏删除，完成按钮,显示修改按钮
     */
    private void hideDeleteAccept() {
        mGridView.setOnItemClickListener(mOnItemClickListener);
        mCityManageAdapter.setCityDeleteButton(false);
        mCityManageAdapter.notifyDataSetChanged();
        mAcceptBtn.setVisibility(View.GONE);
        mEditBtn.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回按钮
            case R.id.action_return:
                mIsRefreshing = false;
                finish();
                break;
            // 编辑按钮
            case R.id.action_edit:
                // 当列表内容为空时禁止响应编辑事件
                if (mGridView.getChildCount() == 1) {
                    return;
                }
                mIsRefreshing = false;
                // 显示删除，完成按钮，隐藏修改按钮
                displayDeleteAccept();
                break;
            // 完成按钮
            case R.id.action_accept:
                // 隐藏删除，完成按钮,显示修改按钮
                hideDeleteAccept();
                break;
            // 刷新按钮
            case R.id.action_refresh:
                // 列表为空（只有添加按钮）不刷新
                if (mCityManageList.size() == 1) {
                    return;
                }

                if (mEditBtn.getVisibility() == View.GONE) {
                    // 隐藏删除，完成按钮,显示修改按钮
                    hideDeleteAccept();
                }

                mRefreshBtn.setVisibility(View.INVISIBLE);
                mCancelRefreshBtn.setVisibility(View.VISIBLE);

                // 显示第一项的进度条
                mCityManageAdapter.displayProgressBar(0);
                mCityManageAdapter.notifyDataSetChanged();
                mIsRefreshing = true;

                String cityName;
                String address;
                CityManage cityManage = mCityManageList.get(0);
                // 不是自动定位
                if (cityManage.getWeatherCode() != null) {
                    cityName = cityManage.getCityName();
                    address = getString(R.string.address_weather, cityManage.getWeatherCode());
                } else {
                    cityName = cityManage.getLocationCity();
                    address = null;
                }

                queryFormServer(address, QUERY_WEATHER, 0, cityName);
                break;
            case R.id.action_refresh_cancel:
                mIsRefreshing = false;
                mRefreshBtn.setVisibility(View.VISIBLE);
                mCancelRefreshBtn.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 根据传入的代号和类型从服务器上查询天气代号、天气数据
     *
     * @param address  查询地址
     * @param type     查询类型:1,查询天气;2,查询县
     * @param position 更新位置:-1,添加城市; -2,添加定位; 其他，更新城市的位置
     * @param cityName 刷新城市名
     */
    private void queryFormServer(final String address, final int type, final int position, final String cityName) {

        HttpUtil.sendHttpRequest(address, cityName, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        try {
                            switch (type) {
                                // 查询天气
                                case 1:
                                    if (!response.contains("error")) {
                                        WeatherInfo weatherInfo = WeatherUtil.handleWeatherResponse(
                                                new ByteArrayInputStream(response.getBytes()));
                                        // 保存天气信息
                                        WeatherUtil.saveWeatherInfo(weatherInfo, CityManageActivity.this);
                                        runOnUiThread(new SetCityInfoRunnable(weatherInfo, position));
                                    } else {
                                        switch (position) {
                                            case -1:
                                                runOnUi(getString(R.string.no_city_info), position);
                                                break;
                                            case -2:
                                                runOnUi(getString(R.string.location_fail), position);
                                                break;
                                        }
                                    }

                                    break;
                                // 查询县
                                case 2:
                                    String[] array = response.split("\\|");
                                    if (array.length == 2) {
                                        mWeatherCode = array[1];
                                        // 查询天气代号
                                        queryFormServer(getString(R.string.address_weather, mWeatherCode),
                                                QUERY_WEATHER, -1, null);
                                    } else {
                                        runOnUi(getString(R.string.no_city_info), position);
                                    }
                                    break;
                            }
                        } catch (Exception e) {
                            LogUtil.e(LOG_TAG, "onFinish: " + e.toString());
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        try {
                            LogUtil.e(LOG_TAG, "onError(Exception e): " + e.toString());
                            // 添加城市
                            if (position == -1) {
                                runOnUi(getString(R.string.add_city_fail), position);
                            } else {
                                if (address != null) {
                                    runOnUi(String.format(getString(R.string.refresh_fail), cityName), position);
                                    // 自动定位
                                } else {
                                    runOnUi(String.format(getString(R.string.refresh_fail), getString(
                                            R.string.auto_location)), position);
                                }
                            }
                        } catch (Exception e1) {
                            LogUtil.e(LOG_TAG, "(Exception e1): " + e1.toString());
                        }
                    }
                }

        );
    }

    /**
     * 执行UI方法
     *
     * @param info 显示的错误信息
     */

    private void runOnUi(final String info, final int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 添加
                if (position == -1 || position == -2) {
                    // 移除临时添加的新城市item
                    mCityManageList.remove(mCityManageList.size() - 2);
                    mCityManageAdapter.notifyDataSetChanged();
                    // GridView设置点击事件
                    mGridView.setOnItemClickListener(mOnItemClickListener);
                } else {
                    // 隐藏进度条显示刷新按钮
                    hideProgressBarDisplayRefresh();
                }
                ToastUtil.showShortToast(CityManageActivity.this, info);
            }
        });
    }

    /**
     * 隐藏进度条显示刷新按钮
     */
    private void hideProgressBarDisplayRefresh() {
        mCityManageAdapter.displayProgressBar(-1);
        mCityManageAdapter.notifyDataSetChanged();

        mRefreshBtn.setVisibility(View.VISIBLE);
        mCancelRefreshBtn.setVisibility(View.GONE);
    }

    /**
     * 设置添加/更新城市列表信息Runnable
     */
    class SetCityInfoRunnable implements Runnable {

        private WeatherInfo mWeatherInfo;
        private int mPosition;

        public SetCityInfoRunnable(WeatherInfo weatherInfo, int position) {
            mWeatherInfo = weatherInfo;
            mPosition = position;
        }

        @Override
        public void run() {
            // 添加城市
            if (mPosition == -1) {
                processCityManage(mWeatherInfo.getCity(), mWeatherCode);
                // 添加定位
            } else if (-2 == mPosition) {
                mCityManage.setLocationCity(mWeatherInfo.getCity());
                processCityManage(getString(R.string.auto_location), null);
            } else {
                // 取得城市管理列表对应的城市管理item
                CityManage cityManage = mCityManageList.get(mPosition);
                // 更新城市管理列表item信息
                setCityManageInfo(cityManage, mWeatherInfo);

                // 修改城市管理item信息
                WeatherDBOperate.getInstance().updateCityManage(cityManage);

                // 当为列表最后一项或者不是正在刷新状态
                if (mPosition >= (mCityManageList.size() - 2) || !mIsRefreshing) {
                    // 隐藏进度条显示刷新按钮
                    hideProgressBarDisplayRefresh();
                    return;
                }
                // 下一项显示进度条
                mCityManageAdapter.displayProgressBar(mPosition + 1);
                mCityManageAdapter.notifyDataSetChanged();

                String cityName;
                String address;
                CityManage cityManage1 = mCityManageList.get(mPosition + 1);
                // 不是自动定位
                if (cityManage1.getWeatherCode() != null) {
                    cityName = cityManage1.getCityName();
                    address = getString(R.string.address_weather, cityManage1.getWeatherCode());
                } else {
                    cityName = cityManage1.getLocationCity();
                    address = null;
                }

                // 更新查询下一项
                queryFormServer(address, QUERY_WEATHER, mPosition + 1, cityName);
            }

        }

        private void processCityManage(String cityName, String weatherCode) {
            mCityManage.setCityName(cityName);
            mCityManage.setWeatherCode(weatherCode);
            // 设置城市管理列表item信息
            setCityManageInfo(mCityManage, mWeatherInfo);
            // 隐藏进度条
            mCityManageAdapter.displayProgressBar(-1);
            mCityManageAdapter.notifyDataSetChanged();

            // 存储城市管理item信息
            WeatherDBOperate.getInstance().saveCityManage(mCityManage);
            // 设置默认城市并且退出返回
            if (weatherCode != null) {
                saveDefaultCityInfoAndReturn(mCityManageList.size() - 2, null);
                // 添加定位
            } else {
                saveDefaultCityInfoAndReturn(mCityManageList.size() - 2, mWeatherInfo.getCity());
            }
        }

    }

    /**
     * 设置城市管理列表item信息
     *
     * @param cityManage  城市管理列表item
     * @param weatherInfo 天气信息
     */
    private void setCityManageInfo(CityManage cityManage, WeatherInfo weatherInfo) {
        if (weatherInfo.getWeatherDaysForecast().size() == 6) {
            WeatherDaysForecast weather;

            String time[] = weatherInfo.getUpdateTime().split(":");
            int hour = Integer.parseInt(time[0]);
            int minute = Integer.parseInt(time[1]);

            //更新时间从23：45开始到05：20以前的数据，后移一天填充
            if ((hour == 23 && minute >= 45) || (hour < 5) ||
                    ((hour == 5) && (minute < 20))) {
                weather = weatherInfo.getWeatherDaysForecast().get(2);
            } else {
                weather = weatherInfo.getWeatherDaysForecast().get(1);
            }

            cityManage.setTempHigh(weather.getHigh().substring(3));
            cityManage.setTempLow(weather.getLow().substring(3));

            // 天气类型图片id
            int weatherId;
            // 当前为凌晨
            if (hour >= 0 && hour < 6) {
                weatherId = WeaFragment.getWeatherTypeImageID(weather.getTypeDay(), false);
                // 当前为白天时
            } else if (hour >= 6 && hour < 18) {
                weatherId = WeaFragment.getWeatherTypeImageID(weather.getTypeDay(), true);
                // 当前为夜间
            } else {
                weatherId = WeaFragment.getWeatherTypeImageID(weather.getTypeNight(), false);
            }
            cityManage.setImageId(weatherId);

            // 白天和夜间类型相同
            if (weather.getTypeDay().equals(weather.getTypeNight())) {
                cityManage.setWeatherType(weather.getTypeDay());
            } else {
                cityManage.setWeatherType(String.format(getString(R.string.turn),
                        weather.getTypeDay(), weather.getTypeNight()));
            }
        } else {
            cityManage.setTempHigh(getString(R.string.dash));
            cityManage.setTempLow(getString(R.string.dash));
            cityManage.setImageId(R.drawable.ic_weather_no);
            cityManage.setWeatherType(getString(R.string.no));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CITY_MANAGE) {
            // GridView禁用点击事件
            mGridView.setOnItemClickListener(null);

            mCityManage = new CityManage();
            // 插在最后一项（添加按钮）之前
            mCityManageList.add(mCityManageList.size() - 1, mCityManage);
            // 显示progressBar
            mCityManageAdapter.displayProgressBar(mCityManageList.size() - 2);
            mCityManageAdapter.notifyDataSetChanged();

            String countryCode = data.getStringExtra(WeacConstants.COUNTRY_CODE);
            if (countryCode != null) {
                queryFormServer(getString(R.string.address_city, countryCode),
                        QUERY_COUNTRY, ADD_CITY, null);
                // 添加定位
            } else {
                String cityName = data.getStringExtra(WeacConstants.CITY_NAME);
                queryFormServer(null,
                        QUERY_WEATHER, ADD_LOCATION, cityName);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!mIsRefreshing) {
            super.onBackPressed();
        } else {
            mIsRefreshing = false;
        }
    }

    private boolean isFastDoubleClick() {
        long time = SystemClock.elapsedRealtime();
        // 初次点击响应事件
        if (mLastClickTime == 0) {
            mLastClickTime = time;
            return false;
        }
        long timeD = time - mLastClickTime;
        // 间隔x秒以内重复点击不多次响应
        if (timeD <= WeacConstants.QUICK_CLICK) {
            return true;
        } else {
            mLastClickTime = time;
            return false;
        }
    }
}