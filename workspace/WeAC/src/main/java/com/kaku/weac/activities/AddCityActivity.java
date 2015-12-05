/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

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
     * 选中的县
     */
    private Country mSelectedCountry;

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
     * 定位管理
     */
    private LocationManager mLocationManager;

    /**
     * 定位监听
     */
    private MyLocationListener mMyLocationListener;

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

        mAddCityList = new ArrayList<>();
        mAddCityAdapter = new CityAdapter(this, mAddCityList);

        // 城市列表GridView
        mGvTitle = (TextView) findViewById(R.id.gv_add_city_title);
        GridView addCityGridView = (GridView) findViewById(R.id.gv_add_city);
        addCityGridView.setAdapter(mAddCityAdapter);
        addCityGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 当前选择的城市等级
                switch (mCurrentLevel) {
                    case LEVEL_HOT_CITY:
                        String cityName = mAddCityAdapter.getItem(position);
                        // 当尚未添加此城市
                        if (isCityNoAdd(cityName)) {
                            addCity(cityName);
                        } else {
                            ToastUtil.showLongToast(AddCityActivity.this, getString(R.string.city_already_added));
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
                        // 当前选中的县
                        mSelectedCountry = mCountryList.get(position);
                        // 当尚未添加此城市
                        if (isCityNoAdd(mSelectedCountry.getCountryName())) {
                            Intent intent = getIntent();
                            intent.putExtra(WeacConstants.COUNTRY_CODE, mSelectedCountry.getCountryCode());
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        } else {
                            ToastUtil.showLongToast(AddCityActivity.this, getString(R.string.city_already_added));
                        }
                        break;
                }
            }
        });
    }

    /**
     * 添加城市
     *
     * @param cityName 城市名
     */
    private void addCity(String cityName) {
        switch (cityName) {
            case "定位":
                int number = WeatherDBOperate.getInstance().queryCity(getString(R.string.auto_location));
                // 当已添加自动定位
                if (number == 1) {
                    ToastUtil.showLongToast(AddCityActivity.this, getString(R.string.location_already_added));
                    return;
                }

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ToastUtil.showShortToast(AddCityActivity.this, "请打开位置权限");
                    return;
                }

                // 初始化定位管理监听
                initLocation();

                String provider;
                List<String> providerList = mLocationManager.getProviders(true);
                if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
                    provider = LocationManager.NETWORK_PROVIDER;
                } else {
                    ToastUtil.showShortToast(AddCityActivity.this, "请打开网络定位");
                    return;
                }

                showProgressDialog(getString(R.string.now_locating));

                mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                // 注册请求定位
                // minDistance:最小距离位置更新  0代表不更新
                mLocationManager.requestLocationUpdates(provider, 0, 0, mMyLocationListener);
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
     * 初始化定位管理监听
     */
    private void initLocation() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        if (mMyLocationListener == null) {
            mMyLocationListener = new MyLocationListener();
        }
    }


    /**
     * 定位监听接口
     */
    class MyLocationListener implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {
            LogUtil.d(LOG_TAG, "经度：" + location.getLongitude() + "，纬度：" + location.getLatitude());
            // 注销监听接口
            mLocationManager.removeUpdates(mMyLocationListener);
            if (ActivityCompat.checkSelfPermission(AddCityActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    AddCityActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                closeProgressDialog();
                return;
            }

            new AnalyzeLocationAsyncTask().execute(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    }

    /**
     * 解析位置信息
     */
    class AnalyzeLocationAsyncTask extends AsyncTask<Location, Void, String> {

        @Override
        protected String doInBackground(Location... params) {
            String cityName = null;
            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(
                        "http://maps.google.cn/maps/api/geocode/json?latlng=" +
                                params[0].getLatitude() + "," +
                                params[0].getLongitude() +
//                                "41.0177553606,111.9529258679"
                                "&sensor=false&language=zh-CN").build();
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    // 取得results节点下的位置信息
                    JSONArray resultArray = jsonObject.getJSONArray("results");
                    if (resultArray.length() > 0) {
                        JSONObject subObject = resultArray.getJSONObject(0);
                        // 取得格式化后的位置信息
                        cityName = formatCity(subObject.getString("formatted_address"));
                    }
                }
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        ToastUtil.showShortToast(AddCityActivity.this, getString(R.string.location_fail));
                    }
                });
            }
            return cityName;
        }

        @Override
        protected void onPostExecute(String cityName) {
            super.onPostExecute(cityName);
            closeProgressDialog();
            if (cityName != null) {
                LogUtil.d(LOG_TAG, "城市名：" + cityName);
                Intent intent = getIntent();
                intent.putExtra(WeacConstants.CITY_NAME, cityName);
                setResult(Activity.RESULT_OK, intent);
                AddCityActivity.this.finish();
            } else {
                ToastUtil.showShortToast(AddCityActivity.this, getString(R.string.location_fail));
            }

        }
    }

    /**
     * 将地址信息转换为城市
     *
     * @param address 地址
     * @return 城市名称
     */
    private String formatCity(String address) {
        String city = null;

        // TODO: 数据测试
        if (address.contains("自治州")) {
            if (address.contains("市")) {
                city = address.substring(address.indexOf("州") + 1, address.indexOf("市"));
            } else if (address.contains("县")) {
                city = address.substring(address.indexOf("州") + 1, address.indexOf("县"));
            } else if (address.contains("地区")) {
                city = address.substring(address.indexOf("州") + 1, address.indexOf("地区"));
            }

        } else if (address.contains("自治区")) {
            if (address.contains("地区") && address.contains("县")) {
                city = address.substring(address.indexOf("地区") + 2, address.indexOf("县"));
            } else if (address.contains("地区")) {
                city = address.substring(address.indexOf("区") + 1, address.indexOf("地区"));
            } else if (address.contains("市")) {
                city = address.substring(address.indexOf("区") + 1, address.indexOf("市"));
            } else if (address.contains("县")) {
                city = address.substring(address.indexOf("区") + 1, address.indexOf("县"));
            }

        } else if (address.contains("地区")) {
            if (address.contains("县")) {
                city = address.substring(address.indexOf("地区") + 2, address.indexOf("县"));
            }

        } else if (address.contains("香港")) {
            if (address.contains("九龙")) {
                city = "九龙";
            } else if (address.contains("新界")) {
                city = "新界";
            } else {
                city = "香港";
            }

        } else if (address.contains("澳门")) {
            if (address.contains("氹仔")) {
                city = "氹仔岛";
            } else if (address.contains("路环")) {
                city = "路环岛";
            } else {
                city = "澳门";
            }

        } else if (address.contains("台湾")) {
            if (address.contains("台北")) {
                city = "台北";
            } else if (address.contains("高雄")) {
                city = "高雄";
            } else if (address.contains("台中")) {
                city = "台中";
            }

        } else if (address.contains("省")) {
            if (address.contains("市") && address.contains("县")) {
                city = address.substring(address.lastIndexOf("市") + 1, address.indexOf("县"));
            } else if (!address.contains("市") && address.contains("县")) {
                city = address.substring(address.indexOf("省") + 1, address.indexOf("县"));
            } else if (!address.contains("市")) {
                int start = address.indexOf("市");
                int end = address.lastIndexOf("市");
                if (start == end) {
                    city = address.substring(address.indexOf("省") + 1, end);
                } else {
                    city = address.substring(start, end);
                }
            }

        } else if (address.contains("市")) {
            if (address.contains("区")) {
                city = address.substring(address.indexOf("市") + 1, address.indexOf("区"));
            } else if (address.contains("县")) {
                city = address.substring(address.indexOf("市") + 1, address.indexOf("县"));
            }
        }

        return city;
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
        String[] city = getResources().getStringArray(R.array.city);
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
                        ToastUtil.showLongToast(AddCityActivity.this, getString(R.string.Internet_fail));
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