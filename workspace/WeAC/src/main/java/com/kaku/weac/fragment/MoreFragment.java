/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.kaku.weac.R;
import com.kaku.weac.activities.ThemeActivity;
import com.kaku.weac.bean.Event.WallpaperEvent;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.OttoAppConfig;
import com.squareup.otto.Subscribe;

/**
 * 更多fragment
 *
 * @author 咖枯
 * @version 1.0 2015
 */
public class MoreFragment extends BaseFragment implements OnClickListener {
    /**
     * Log tag ：MoreFragment
     */
    private static final String LOG_TAG = "MoreFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OttoAppConfig.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtil.i(LOG_TAG, "MoreFragmet:  onCreateView");

        View view = inflater.inflate(R.layout.fm_more, container, false);
        // 变更主题
        ViewGroup themeBtn = (ViewGroup) view.findViewById(R.id.theme);
        themeBtn.setOnClickListener(this);
        return view;
    }

    @Subscribe
    public void onWallpaperUpdate(WallpaperEvent wallpaperEvent) {
        ViewGroup vg = (ViewGroup) getActivity().findViewById(
                R.id.llyt_activity_main);
        // 更新壁纸
        MyUtil.setBackground(vg, getActivity());
    }

/*    private void initWallpaper() {
        ViewGroup vg = (ViewGroup) getActivity().findViewById(
                R.id.llyt_activity_main);
        // 更新壁纸
        MyUtil.setBackground(vg, getActivity());

        ViewPager pager = (ViewPager) getActivity().findViewById(R.id.fragment_container);
        PagerAdapter f = pager.getAdapter();
        WeaFragment weaFragment = (WeaFragment) f.instantiateItem(pager, 1);
        // 更新天气高斯模糊背景
        if (weaFragment.mBlurDrawable != null && weaFragment.mBackGround != null) {
            weaFragment.mBlurDrawable = MyUtil.getWallPaperBlurDrawable(getActivity());
            weaFragment.mBlurDrawable.setAlpha(weaFragment.mAlpha);
            weaFragment.mBackGround.setBackground(weaFragment.mBlurDrawable);
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.theme:
                if (MyUtil.isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(getActivity(), ThemeActivity.class);
                // 启动主题界面
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OttoAppConfig.getInstance().unregister(this);
    }
}
