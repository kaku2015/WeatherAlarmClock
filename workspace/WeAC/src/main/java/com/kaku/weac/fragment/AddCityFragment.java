package com.kaku.weac.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.adapter.CityAdapter;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.db.WeatherDBOperate;
import com.kaku.weac.model.City;
import com.kaku.weac.model.Country;
import com.kaku.weac.model.Province;
import com.kaku.weac.util.CityUtil;
import com.kaku.weac.util.HttpCallbackListener;
import com.kaku.weac.util.HttpUtil;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 添加城市fragment
 *
 * @author 咖枯
 * @version 1.0 2015/11/02
 */
public class AddCityFragment extends Fragment implements View.OnClickListener {

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
     * 返回按钮
     */
    private ImageView mReturnBtn;

    /**
     * 更多城市和返回按钮
     */
    private LinearLayout mMoreCityAndReturnBtn;

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
     * 城市列表GridView
     */
    private GridView mAddCityGridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddCityList = new ArrayList<>();
        mAddCityAdapter = new CityAdapter(getActivity(), mAddCityList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_add_city, container, false);
        final LinearLayout backGround = (LinearLayout) view.findViewById(R.id.city_manage_background);
        // 设置页面背景
        backGround.setBackground(MyUtil.getWallPaperDrawable(getActivity()));

        mReturnBtn = (ImageView) view.findViewById(R.id.action_return);
        mReturnBtn.setOnClickListener(this);

        mMoreCityAndReturnBtn = (LinearLayout) view.findViewById(R.id.more_city_and_return_btn);
        mMoreCityAndReturnBtn.setOnClickListener(this);
        mMoreCityAndReturnBtnTv = (TextView) view.findViewById(R.id.more_city_and_return_btn_tv);

        mGvTitle = (TextView) view.findViewById(R.id.gv_add_city_title);
        mAddCityGridView = (GridView) view.findViewById(R.id.gv_add_city);
        mAddCityGridView.setAdapter(mAddCityAdapter);
        mAddCityGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 当前选择的城市等级
                switch (mCurrentLevel) {
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
                }
            }
        });
        // 初始化查询热门城市
        queryHotCities();
        return view;
    }

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
                getActivity().finish();
                break;
            // 更多城市和返回按钮
            case R.id.more_city_and_return_btn:
                // 当前城市等级
                switch (mCurrentLevel) {
                    // 热门城市
                    case LEVEL_HOT_CITY:
                        // 查询省
                        queryProvinces();
                        // 设置为返回按钮
                        mMoreCityAndReturnBtnTv.setText(getString(R.string.back));
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
        String address;
        if (!TextUtils.isEmpty(code)) {
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        } else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
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
                    getActivity().runOnUiThread(new Runnable() {
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
                }
            }

            @Override
            public void onError(Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        ToastUtil.showLongToast(getActivity(), getString(R.string.Internet_fail));
                    }
                });
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.now_loading));
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
