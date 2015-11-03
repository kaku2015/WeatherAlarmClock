package com.kaku.weac.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.kaku.weac.R;
import com.kaku.weac.activities.AddCityActivity;
import com.kaku.weac.adapter.CityManageAdapter;
import com.kaku.weac.bean.CityManage;
import com.kaku.weac.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市管理fragment
 *
 * @author 咖枯
 * @version 1.0 2015/10/22
 */
public class CityManageFragment extends Fragment {

    private List<CityManage> mCityManageList;
    private CityManageAdapter mCityManageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCityManageList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            CityManage cityManage = new CityManage("城市" + i, R.drawable.ic_weather_cloudy, "29", "20", "多云");
            mCityManageList.add(cityManage);
        }
        mCityManageAdapter = new CityManageAdapter(getActivity(), mCityManageList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_city_manage, container, false);
        LinearLayout backGround = (LinearLayout) view.findViewById(R.id.city_manage_background);
        // 设置页面背景
        backGround.setBackground(MyUtil.getWallPaperDrawable(getActivity()));

        GridView mGridView = (GridView) view.findViewById(R.id.gv_city_manage);
        mGridView.setAdapter(mCityManageAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == (mCityManageList.size() - 1)) {
                    Intent intent = new Intent(getActivity(), AddCityActivity.class);
                    startActivity(intent);
                }
            }
        });


        return view;
    }

}
