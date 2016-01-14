/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.activities.LocalAlbumActivity;
import com.kaku.weac.adapter.ThemeAdapter;
import com.kaku.weac.bean.Theme;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.LruMemoryCache;
import com.kaku.weac.util.MyUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 主题fragment
 *
 * @author 咖枯
 * @version 1.0 2015
 */
public class ThemeFragment extends BaseFragment implements View.OnClickListener {

    /**
     * Log tag ：ThemeFragment
     */
    private static final String LOG_TAG = "ThemeFragment";

    /**
     * 壁纸资源的集合
     */
    private List<Theme> mList;

    /**
     * 保存主题壁纸的适配器
     */
    private ThemeAdapter mAdapter;

    /**
     * 壁纸名
     */
    private String mWallpaperName;

    /**
     * 当前壁纸图片名称
     */
    private String mCurrentWallpaper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化主题壁纸适配器
        initAdapter();
        mCurrentWallpaper = mWallpaperName;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_theme, container, false);
        final ViewGroup background = (ViewGroup) view.findViewById(R.id.background);
        MyUtil.setBackgroundBlur(background, getActivity());

        ImageView backBtn = (ImageView) view.findViewById(R.id.back_btn);
        TextView customDefineBtn = (TextView) view.findViewById(R.id.custom_define_btn);
        backBtn.setOnClickListener(this);
        customDefineBtn.setOnClickListener(this);

        // 显示主题壁纸的GridView
        GridView gridView = (GridView) view.findViewById(R.id.gv_change_theme);
        gridView.setAdapter(this.mAdapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Theme theme = mList.get(position);
                String resName = theme.getResName();

                if (mCurrentWallpaper.equals(resName)) {
                    return;
                }
                mCurrentWallpaper = resName;
                // 更新当前选中壁纸的名称
                mAdapter.updateSelection(resName);
                // 更新适配器刷新GridView显示
                mAdapter.notifyDataSetChanged();

                // 与壁纸相关的存取信息
                SharedPreferences share = getActivity().getSharedPreferences(
                        WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit = share.edit();
                // 保存当前壁纸名称
                edit.putString(WeacConstants.WALLPAPER_NAME, resName);
                edit.apply();

                MyUtil.setBackgroundBlur(background, getActivity());

                // 返回并进行壁纸更新
                getActivity().setResult(Activity.RESULT_OK, getActivity().getIntent());
            }

        });
        return view;
    }

    /**
     * 读取资源文件取得图片列表，并进行主题壁纸适配器的初始化
     */
    private void initAdapter() {
        // 与主题壁纸相关的存取信息
        SharedPreferences share = getActivity().getSharedPreferences(
                WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
        // 取得使用中壁纸的位置
        mWallpaperName = share.getString(WeacConstants.WALLPAPER_NAME,
                getString(R.string.default_wallpaper_name));
        mList = new ArrayList<>();
        // 资源文件集合
        Field[] fields = R.drawable.class.getDeclaredFields();
        // 遍历资源文件
        for (Field field : fields) {
            String name = field.getName();
            // 取得文件名以"wallpaper_"开始的图片
            if (name.startsWith("wallpaper_")) {
                try {
                    Theme theme = new Theme();
                    theme.setResName(name);
                    theme.setResId(field.getInt(R.drawable.class));
                    this.mList.add(theme);
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    LogUtil.e(LOG_TAG, "initAdapter(): " + e.toString());
                }
            }
        }
        // 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        LogUtil.i(LOG_TAG, "java虚拟机默认情况下能从系统挖到的最大内存: " + maxMemory + "KB");
        // 使用最大可用内存值的1/8作为缓存的大小。
        int cacheSize = maxMemory / 8;
        // LruCache通过构造函数传入缓存值，以KB为单位
        LruMemoryCache memoryCache = new LruMemoryCache(cacheSize);
        LogUtil.i(LOG_TAG, "缓存空间大小: " + memoryCache.maxSize() / 1024 + "MB");

        WindowManager manager = (WindowManager) getActivity().getSystemService(
                Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        // 图片显示的宽度
        int reqWidth = size.x / 3;
        // 图片显示的高度
        int reqHeight = size.y / 4;

        // 创建主题壁纸适配器
        this.mAdapter = new ThemeAdapter(getActivity(), mList,
                mWallpaperName, memoryCache, reqWidth, reqHeight);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                getActivity().finish();
                break;
            case R.id.custom_define_btn:
                if (MyUtil.isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(getActivity(), LocalAlbumActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoomin, 0);
                break;
        }
    }
}
