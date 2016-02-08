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
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.activities.GenerateCodeActivity;
import com.kaku.weac.activities.ThemeActivity;
import com.kaku.weac.bean.Event.WallpaperEvent;
import com.kaku.weac.util.DataCleanManager;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.OttoAppConfig;
import com.kaku.weac.zxing.activity.CaptureActivity;
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
    private TextView mUsedMemory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OttoAppConfig.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_more, container, false);
        assignViews(view);
        return view;
    }

    private void assignViews(View view) {
        ViewGroup themeBtn = (ViewGroup) view.findViewById(R.id.theme);
        ViewGroup scanQRcodeBtn = (ViewGroup) view.findViewById(R.id.scan_scan);
        ViewGroup generateCodeBtn = (ViewGroup) view.findViewById(R.id.generate_code);
        ViewGroup clearMemoryBtn = (ViewGroup) view.findViewById(R.id.clear_memory);
        mUsedMemory = (TextView) view.findViewById(R.id.used_memory_tv);

        themeBtn.setOnClickListener(this);
        scanQRcodeBtn.setOnClickListener(this);
        generateCodeBtn.setOnClickListener(this);
        clearMemoryBtn.setOnClickListener(this);
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
        if (MyUtil.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            // 主题
            case R.id.theme:
                Intent intent = new Intent(getActivity(), ThemeActivity.class);
                // 启动主题界面
                startActivity(intent);
                break;
            // 扫码
            case R.id.scan_scan:
                Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
                startActivity(openCameraIntent);
                break;
            // 造码
            case R.id.generate_code:
                // 打开扫描界面扫描条形码或二维码
                Intent generateCodeIntent = new Intent(getActivity(), GenerateCodeActivity.class);
                startActivity(generateCodeIntent);
                break;
            case R.id.clear_memory:
                DataCleanManager.clearAllCache(getActivity());
                mUsedMemory.setText(DataCleanManager.getTotalCacheSize(getActivity()));
                break;
        }
    }

/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {

        }
    }*/

    @Override
    public void onResume() {
        super.onResume();
        mUsedMemory.setText(DataCleanManager.getTotalCacheSize(getActivity()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OttoAppConfig.getInstance().unregister(this);
    }
}
