/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.kaku.weac.Listener.HttpCallbackListener;
import com.kaku.weac.R;
import com.kaku.weac.adapter.CityAdapter;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.db.WeatherDBOperate;
import com.kaku.weac.model.City;
import com.kaku.weac.model.Country;
import com.kaku.weac.model.Province;
import com.kaku.weac.util.CityUtil;
import com.kaku.weac.util.HttpUtil;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 添加城市activity
 *
 * @author 咖枯
 * @version 1.0 2015/11/02
 */
public class AddCityActivity extends BaseActivity implements View.OnClickListener {

    /**
     * Log tag ：AddCityActivity
     */
    private static final String LOG_TAG = "AddCityActivity";

    /**
     * 热门城市标志
     */
    private static final int LEVEL_HOT_CITY = 0;

    /**
     * 省标志
     */
    private static final int LEVEL_PROVINCE = 1;

    /**
     * 市标志
     */
    private static final int LEVEL_CITY = 2;

    /**
     * 县标记
     */
    private static final int LEVEL_COUNTY = 3;

    /**
     * 更多城市和返回按钮的TextView
     */
    private TextView mMoreCityAndReturnBtnTv;

    /**
     * 添加城市列表
     */
    private List<String> mAddCityList;

    /**
     * 添加城市列表适配器
     */
    private CityAdapter mAddCityAdapter;

    /**
     * 省列表
     */
    private List<Province> mProvinceList;

    /**
     * 市列表
     */
    private List<City> mCityList;

    /**
     * 县列表
     */
    private List<Country> mCountryList;

    /**
     * 选中的省份
     */
    private Province mSelectedProvince;

    /**
     * 选中的市
     */
    private City mSelectedCity;

    /**
     * 当前选中的级别
     */
    private int mCurrentLevel;

    /**
     * 城市列表标题
     */
    private TextView mGvTitle;

    /**
     * 进度对话框
     */
    private ProgressDialog mProgressDialog;

    /**
     * 百度定位服务
     */
    private LocationClient mLocationClient;

    /**
     * 百度定位监听
     */
    private BDLocationListener mBDLocationListener;

    /**
     * 请求MyDialogActivity
     */
    private static final int REQUEST_MY_DIALOG = 1;

    /**
     * 显示热门城市组件LinearLayout
     */
    private LinearLayout mHotCityllyt;

    /**
     * 清除按钮
     */
    private ImageView mClearBtn;

    /**
     * 当没有匹配到检索的城市时显示的提示内容
     */
    private TextView mNoMatchedCityTv;

    /**
     * 检索城市匹配的列表
     */
    private ListView mSearchCityLv;

    /**
     * 检索城市匹配的列表城市
     */
    private List<SpannableString> mSearchCityList;

    /**
     * 检索城市匹配的列表适配器
     */
    private ArrayAdapter<SpannableString> mSearchCityAdapter;

    /**
     * 输入城市名编辑框
     */
    private EditText mSearchCityEtv;

    /**
     * 全国城市
     */
    private String[] mCountries;

    /**
     * 全国城市代号
     */
    private String[] mWeatherCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        LinearLayout backGround = (LinearLayout) findViewById(R.id.city_manage_background);
        // 设置页面高斯模糊背景
        MyUtil.setBackgroundBlur(backGround, this);
        initViews();
        // 初始化查询热门城市
        queryHotCities();
    }

    private void initViews() {
        // 返回按钮
        ImageView returnBtn = (ImageView) findViewById(R.id.action_return);
        returnBtn.setOnClickListener(this);

        // 更多城市和返回按钮
        LinearLayout moreCityAndReturnBtn = (LinearLayout) findViewById(R.id.more_city_and_return_btn);
        moreCityAndReturnBtn.setOnClickListener(this);
        mMoreCityAndReturnBtnTv = (TextView) findViewById(R.id.more_city_and_return_btn_tv);

        // 编辑框
        mSearchCityEtv = (EditText) findViewById(R.id.search_city_edit_tv);
        mSearchCityEtv.addTextChangedListener(new TextWatcherImpl());

        // 城市视图列表
        mAddCityList = new ArrayList<>();
        mAddCityAdapter = new CityAdapter(this, mAddCityList);

        // 城市列表GridView
        mGvTitle = (TextView) findViewById(R.id.gv_add_city_title);
        GridView addCityGridView = (GridView) findViewById(R.id.gv_add_city);
        addCityGridView.setAdapter(mAddCityAdapter);
        addCityGridView.setOnItemClickListener(new AddCityOnItemClickListener());

        // 清除按钮
        mClearBtn = (ImageView) findViewById(R.id.clear_btn);
        mClearBtn.setOnClickListener(this);

        mCountries = getResources().getStringArray(R.array.city_china);
        mWeatherCodes = getResources().getStringArray(R.array.city_china_weather_code);

        // 热门城市视图
        mHotCityllyt = (LinearLayout) findViewById(R.id.city_contents);
        // 无匹配提示
        mNoMatchedCityTv = (TextView) findViewById(R.id.no_matched_city_tv);
        // 查找城市
        mSearchCityLv = (ListView) findViewById(R.id.lv_search_city);
        mSearchCityList = new ArrayList<>();
        mSearchCityAdapter = new ArrayAdapter<>(
                this, R.layout.lv_search_city, mSearchCityList);
        mSearchCityLv.setAdapter(mSearchCityAdapter);
        mSearchCityLv.setOnItemClickListener(new SearchCityOnItemClickListener());
    }

    class TextWatcherImpl implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 输入的城市名
            String cityName = s.toString();
            // 输入内容不为空
            if (!TextUtils.isEmpty(cityName)) {
                // 隐藏热门城市视图
                mHotCityllyt.setVisibility(View.GONE);
                // 显示清除按钮
                mClearBtn.setVisibility(View.VISIBLE);

                mSearchCityList.clear();
                for (String county : mCountries) {
                    if (county.contains(cityName)) {
                        SpannableString spanString = new SpannableString(county);
                        // 构造一个改变字体颜色的Span
                        ForegroundColorSpan span = new ForegroundColorSpan(getResources().
                                getColor(R.color.white_trans90));
                        //将这个Span应用于指定范围的字体
                        spanString.setSpan(span,
                                county.indexOf(cityName), county.indexOf(cityName) + cityName.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        mSearchCityList.add(spanString);
                    }
                }

                // 匹配的城市不为空
                if (mSearchCityList.size() != 0) {
                    mSearchCityAdapter.notifyDataSetChanged();
                    // 显示查找城市列表
                    mSearchCityLv.setVisibility(View.VISIBLE);
                    // 隐藏无匹配城市的提示
                    mNoMatchedCityTv.setVisibility(View.GONE);
                    // 无匹配的城市
                } else {
                    // 隐藏查找城市列表
                    mSearchCityLv.setVisibility(View.GONE);
                    // 显示无匹配城市的提示
                    mNoMatchedCityTv.setVisibility(View.VISIBLE);
                }
                // 输入内容为空
            } else {
                // 清空
//                mSearchCityList.clear();
//                mSearchCityAdapter.notifyDataSetChanged();
                // 显示城市视图
                mHotCityllyt.setVisibility(View.VISIBLE);
                // 隐藏清除按钮
                mClearBtn.setVisibility(View.GONE);
                // 隐藏查找城市列表
                mSearchCityLv.setVisibility(View.GONE);
                // 隐藏无匹配城市的提示
                mNoMatchedCityTv.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    class AddCityOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 当前选择的城市等级
            switch (mCurrentLevel) {
                case LEVEL_HOT_CITY:
                    if (!MyUtil.isNetworkAvailable(AddCityActivity.this)) {
                        ToastUtil.showShortToast(AddCityActivity.this, getString(R.string.internet_error));
                        return;
                    }

                    String cityName = mAddCityAdapter.getItem(position);
                    // 当尚未添加此城市
                    if (isCityNoAdd(cityName)) {
                        addCity(cityName);
                    } else {
                        ToastUtil.showShortToast(AddCityActivity.this, getString(
                                R.string.city_already_added, cityName));
                    }
                    break;
                // 省
                case LEVEL_PROVINCE:
                    // 当前选中的省
                    mSelectedProvince = mProvinceList.get(position);
                    // 查询市
                    queryCities();
                    break;
                // 市
                case LEVEL_CITY:
                    // 当前选中的市
                    mSelectedCity = mCityList.get(position);
                    // 查询县
                    queryCounties();
                    break;
                // 县
                case LEVEL_COUNTY:
                    if (!MyUtil.isNetworkAvailable(AddCityActivity.this)) {
                        ToastUtil.showShortToast(AddCityActivity.this, getString(R.string.internet_error));
                        return;
                    }

                    // 当前选中的县
                    Country selectedCountry = mCountryList.get(position);
                    // 当尚未添加此城市
                    if (isCityNoAdd(selectedCountry.getCountryName())) {
                        Intent intent = getIntent();
                        intent.putExtra(WeacConstants.COUNTRY_CODE, selectedCountry.getCountryCode());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        ToastUtil.showShortToast(AddCityActivity.this, getString(
                                R.string.city_already_added, selectedCountry.getCountryName()));
                    }
                    break;
            }
        }
    }

    class SearchCityOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (!MyUtil.isNetworkAvailable(AddCityActivity.this)) {
                ToastUtil.showShortToast(AddCityActivity.this, getString(R.string.internet_error));
                return;
            }

            SpannableString item = mSearchCityAdapter.getItem(position);
            String city = item.toString().split("-")[0];
            LogUtil.d(LOG_TAG, "city：" + city);

            // 当尚未添加此城市
            if (isCityNoAdd(city)) {
                String weatherCode = "";
                int length = mCountries.length;
                // 取得与城市对应的天气代号
                for (int i = 0; i < length; i++) {
                    if (mCountries[i].split("-")[0].equals(city)) {
                        weatherCode = mWeatherCodes[i];
                    }
                }

                Intent intent = getIntent();
                intent.putExtra(WeacConstants.WEATHER_CODE, weatherCode);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                ToastUtil.showShortToast(AddCityActivity.this, getString(
                        R.string.city_already_added, city));
            }
        }
    }

    /**
     * 初始化定位管理监听
     */
    private void initLocationManager() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(getApplicationContext());
        }
        if (mBDLocationListener == null) {
            mBDLocationListener = new MyLocationListener();
        }
    }

    /**
     * 添加城市
     *
     * @param cityName 城市名
     */
    private void addCity(String cityName) {
        switch (cityName) {
            case "定位":
                // 开始定位
                startLocation();
                break;
            case "北京":
                finish("010101");
                break;
            case "天津":
                finish("030101");
                break;
            case "上海":
                finish("020101");
                break;
            case "广州":
                finish("280101");
                break;
            case "深圳":
                finish("280601");
                break;
            case "重庆":
                finish("040101");
                break;
            case "福州":
                finish("230101");
                break;
            case "西安":
                finish("110101");
                break;
            case "南宁":
                finish("300101");
                break;
            case "昆明":
                finish("290101");
                break;
            case "济南":
                finish("120101");
                break;
            case "武汉":
                finish("200101");
                break;
            case "海口":
                finish("310101");
                break;
            case "三亚":
                finish("310301");
                break;
            case "长春":
                finish("060101");
                break;
            case "合肥":
                finish("220101");
                break;
            case "郑州":
                finish("180101");
                break;
            case "太原":
                finish("100101");
                break;
            case "南昌":
                finish("240101");
                break;
            case "拉萨":
                finish("140101");
                break;
            case "西宁":
                finish("150101");
                break;
            case "石家庄":
                finish("090101");
                break;
            case "哈尔滨":
                finish("050101");
                break;
            case "青岛":
                finish("120201");
                break;
            case "无锡":
                finish("190201");
                break;
            case "厦门":
                finish("230201");
                break;
            case "长沙":
                finish("250101");
                break;
            case "杭州":
                finish("210101");
                break;
            case "香港":
                finish("320101");
                break;
            case "澳门":
                finish("330101");
                break;
            case "台北":
                finish("340101");
                break;
        }
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        if (!MyUtil.isNetworkAvailable(this)) {
            ToastUtil.showShortToast(this, getString(R.string.internet_error));
            return;
        }

        int number = WeatherDBOperate.getInstance().queryCity(getString(R.string.auto_location));
        // 当已添加自动定位
        if (number == 1) {
            ToastUtil.showShortToast(AddCityActivity.this, getString(R.string.location_already_added));
            return;
        }

        // 初始化定位管理，监听
        initLocationManager();
        mLocationClient.registerLocationListener(mBDLocationListener);    //注册监听函数
        initLocation();

        showProgressDialog(getString(R.string.now_locating));

        mLocationClient.start();
        mLocationClient.requestLocation();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.disableCache(true);// 禁止启用缓存定位\
        mLocationClient.setLocOption(option);

//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
//        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        int span = 1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
    }

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            closeProgressDialog();
            mLocationClient.stop();
            mLocationClient.unRegisterLocationListener(mBDLocationListener);
            if (location == null) {
                ToastUtil.showShortToast(AddCityActivity.this, getString(R.string.location_fail));
                return;
            }
            LogUtil.d(LOG_TAG, "纬度：" + location.getLatitude() + ",经度：" + location.getLongitude());

            String address = location.getAddrStr();
            // 定位成功
            if (161 == location.getLocType() && address != null) {
                String cityName = MyUtil.formatCity(address);
                if (cityName != null) {
                    LogUtil.d(LOG_TAG, "城市名：" + cityName);
                    Intent intent = getIntent();
                    intent.putExtra(WeacConstants.CITY_NAME, cityName);
                    setResult(Activity.RESULT_OK, intent);
                    AddCityActivity.this.finish();
                } else {
                    ToastUtil.showLongToast(AddCityActivity.this, getString(R.string.can_not_find_current_location));
                }
                // 定位失败
            } else {
                LogUtil.d(LOG_TAG, "error code: " + location.getLocType());
                Intent intent = new Intent(AddCityActivity.this, MyDialogActivity.class);
                intent.putExtra(WeacConstants.TITLE, getString(R.string.prompt));
                intent.putExtra(WeacConstants.DETAIL, getString(R.string.location_fail));
                intent.putExtra(WeacConstants.SURE_TEXT, getString(R.string.retry));
                startActivityForResult(intent, REQUEST_MY_DIALOG);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_MY_DIALOG) {
            startLocation();
        }
    }

    /**
     * 检查城市是否还没有添加到城市管理表
     *
     * @param cityName 城市名
     * @return 是否没有添加过
     */
    private boolean isCityNoAdd(String cityName) {
        int number = WeatherDBOperate.getInstance().queryCity(cityName);
        return number == 0;
    }

    /**
     * 结束activity，返回结果到添加城市activity
     *
     * @param cityCode 城市代码
     */
    private void finish(String cityCode) {
        Intent intent = getIntent();
        intent.putExtra(WeacConstants.COUNTRY_CODE, cityCode);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * 显示热门城市
     */
    private void queryHotCities() {
        mAddCityList.clear();
        String[] city = getResources().getStringArray(R.array.city_hot);
        Collections.addAll(mAddCityList, city);
        mAddCityAdapter.notifyDataSetChanged();
        mGvTitle.setText(R.string.hot_city);
        mCurrentLevel = LEVEL_HOT_CITY;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回按钮
            case R.id.action_return:
                finish();
                break;
            // 更多城市和返回按钮
            case R.id.more_city_and_return_btn:
                dispatchBackAction(0);
                break;
            // 清除按钮
            case R.id.clear_btn:
                mSearchCityEtv.setText("");
                break;
        }
    }

    /**
     * 分发返回按钮事件
     *
     * @param type 案件类型：0,更多城市/返回按钮;1,返回键
     */
    private void dispatchBackAction(int type) {
        // 当前城市等级
        switch (mCurrentLevel) {
            // 热门城市
            case LEVEL_HOT_CITY:
                switch (type) {
                    case 0:
                        // 查询省
                        queryProvinces();
                        // 设置为返回按钮
                        mMoreCityAndReturnBtnTv.setText(getString(R.string.back));
                        break;
                    case 1:
                        finish();
                        break;
                }
                break;
            // 省
            case LEVEL_PROVINCE:
                // 查询热门城市
                queryHotCities();
                // 设置为更多城市按钮
                mMoreCityAndReturnBtnTv.setText(getString(R.string.more_city));
                break;
            // 市
            case LEVEL_CITY:
                // 查询省
                queryProvinces();
                break;
            // 县
            case LEVEL_COUNTY:
                // 查询市
                queryCities();
                break;
        }
    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryProvinces() {
        mProvinceList = WeatherDBOperate.getInstance().loadProvinces();
        if (mProvinceList.size() > 0) {
            mAddCityList.clear();
            for (Province province : mProvinceList) {
                mAddCityList.add(province.getProvinceName());
            }
            mAddCityAdapter.notifyDataSetChanged();
            mGvTitle.setText(R.string.china);
            mCurrentLevel = LEVEL_PROVINCE;
        } else {
            queryFromServer(null, WeacConstants.PROVINCE);
        }
    }

    /**
     * 查询全国所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCities() {
        mCityList = WeatherDBOperate.getInstance().loadCities(mSelectedProvince.getId());
        if (mCityList.size() > 0) {
            mAddCityList.clear();
            for (City city : mCityList) {
                mAddCityList.add(city.getCityName());
            }
            mAddCityAdapter.notifyDataSetChanged();
            mGvTitle.setText(mSelectedProvince.getProvinceName());
            mCurrentLevel = LEVEL_CITY;
        } else {
            queryFromServer(mSelectedProvince.getProvinceCode(), WeacConstants.CITY);
        }
    }

    /**
     * 查询全国所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCounties() {
        mCountryList = WeatherDBOperate.getInstance().loadCounties(mSelectedCity.getId());
        if (mCountryList.size() > 0) {
            mAddCityList.clear();
            for (Country country : mCountryList) {
                mAddCityList.add(country.getCountryName());
            }
            mAddCityAdapter.notifyDataSetChanged();
            mGvTitle.setText(mSelectedCity.getCityName());
            mCurrentLevel = LEVEL_COUNTY;
        } else {
            queryFromServer(mSelectedCity.getCityCode(), WeacConstants.COUNTRY);
        }
    }

    /**
     * 根据传入的代号和类型从服务器上查询省市县数据
     *
     * @param code 城市代号
     * @param type 城市类型
     */
    private void queryFromServer(final String code, final String type) {
        if (!MyUtil.isNetworkAvailable(this)) {
            ToastUtil.showShortToast(this, getString(R.string.internet_error));
            return;
        }

        // 查询地址
        String address;
        if (!TextUtils.isEmpty(code)) {
            address = getString(R.string.address_city, code);
        } else {
            address = getString(R.string.address_city, "");
        }
        // 显示查询进度对话框
        showProgressDialog(getString(R.string.now_loading));
        // FIXME：回调try catch
        HttpUtil.sendHttpRequest(address, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                // 城市类型
                switch (type) {
                    // 省
                    case WeacConstants.PROVINCE:
                        // 解析并存储省级数据
                        result = CityUtil.handleProvincesResponse(
                                WeatherDBOperate.getInstance(), response);
                        break;
                    // 市
                    case WeacConstants.CITY:
                        // 解析并存储市级数据
                        result = CityUtil.handleCitiesResponse(
                                WeatherDBOperate.getInstance(), response, mSelectedProvince.getId());
                        break;
                    // 县
                    case WeacConstants.COUNTRY:
                        // 解析并存储县级数据
                        result = CityUtil.handleCountriesResponse(
                                WeatherDBOperate.getInstance(), response, mSelectedCity.getId());
                        break;
                }
                // 解析并存储成功
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            switch (type) {
                                case WeacConstants.PROVINCE:
                                    queryProvinces();
                                    break;
                                case WeacConstants.CITY:
                                    queryCities();
                                    break;
                                case WeacConstants.COUNTRY:
                                    queryCounties();
                                    break;
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        ToastUtil.showShortToast(AddCityActivity.this, getString(R.string.internet_fail));
                    }
                });
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
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


    @Override
    public void onBackPressed() {
        dispatchBackAction(1);

    }
}