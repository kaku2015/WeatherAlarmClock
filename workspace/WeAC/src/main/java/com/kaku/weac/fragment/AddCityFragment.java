package com.kaku.weac.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kaku.weac.R;
import com.kaku.weac.util.MyUtil;

/**
 * 添加城市fragment
 *
 * @author 咖枯
 * @version 1.0 2015/11/02
 */
public class AddCityFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_add_city, container, false);
        LinearLayout backGround = (LinearLayout) view.findViewById(R.id.city_manage_background);
        // 设置页面背景
        backGround.setBackground(MyUtil.getWallPaperDrawable(getActivity()));

        return view;
    }

}
