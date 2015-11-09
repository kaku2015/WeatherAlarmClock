package com.kaku.weac.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kaku.weac.R;
import com.kaku.weac.adapter.CityAdapter;
import com.kaku.weac.util.MyUtil;

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
     * 返回按钮
     */
    private ImageView mReturnBtn;

    /**
     * 更多城市按钮
     */
    private LinearLayout mMoreCityBtn;

    /**
     * 城市列表
     */
    private List<String> mCityList;

    /**
     * 城市列表适配器
     */
    private CityAdapter mCityAdapter;

    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCityList = new ArrayList<>();
        String[] city = getResources().getStringArray(R.array.city);
        Collections.addAll(mCityList, city);
        mCityAdapter = new CityAdapter(getActivity(), mCityList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_add_city, container, false);
        LinearLayout backGround = (LinearLayout) view.findViewById(R.id.city_manage_background);
        // 设置页面背景
        backGround.setBackground(MyUtil.getWallPaperDrawable(getActivity()));

        mReturnBtn = (ImageView) view.findViewById(R.id.action_return);
        mReturnBtn.setOnClickListener(this);

        mMoreCityBtn = (LinearLayout) view.findViewById(R.id.more_city_btn);
        mMoreCityBtn.setOnClickListener(this);

        GridView gridView = (GridView) view.findViewById(R.id.gv_add_city);
        gridView.setAdapter(mCityAdapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回按钮
            case R.id.action_return:
                getActivity().finish();
                break;
            case R.id.more_city_btn:
                break;
        }
    }
}
