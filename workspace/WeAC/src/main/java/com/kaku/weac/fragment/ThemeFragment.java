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

import com.kaku.weac.R;
import com.kaku.weac.adapter.ThemeAdapter;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.LruMemoryCache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 主题fragment
 *
 * @author 咖枯
 * @version 1.0 2015
 */
public class ThemeFragment extends BaseFragment {

    /**
     * Log tag ：ThemeFragment
     */
    private static final String LOG_TAG = "ThemeFragment";

    /**
     * 传递的主题壁纸名
     */
    private static final String EXTRA_WALLPAPER = "extra_theme_wallpaper_name";

    /**
     * 保存主题壁纸资源id的集合
     */
    private List<Integer> mList;

    /**
     * 保存主题壁纸的适配器
     */
    private ThemeAdapter mAdapter;

    /**
     * 壁纸位置
     */
    private int mWallpaperPosition;

    /**
     * 当前壁纸图片位置
     */
    private int mCurrentPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化主题壁纸适配器
        initAdapter();
        mCurrentPosition = mWallpaperPosition;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_theme, container, false);
        // 显示主题壁纸的GridView
        GridView gridView = (GridView) view.findViewById(R.id.gv_change_theme);
        gridView.setAdapter(this.mAdapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (mCurrentPosition == position) {
                    return;
                }
                mCurrentPosition = position;
                // 与壁纸相关的存取信息
                SharedPreferences share = getActivity().getSharedPreferences(
                        WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit = share.edit();
                // 更新当前选中壁纸的位置
                mAdapter.updateSelection(position);
                // 更新适配器刷新GridView显示
                mAdapter.notifyDataSetChanged();
                // 取得选中的壁纸ID
                int imgId = mList.get(position);
                // 保存当前壁纸ID
                edit.putInt(WeacConstants.WALLPAPER_ID, imgId);
                // 保存当前壁纸位置
                edit.putInt(WeacConstants.WALLPAPER_POSITION, position);
                edit.apply();
                Intent i = new Intent();
                // 设置壁纸信息
                i.putExtra(EXTRA_WALLPAPER, imgId);
                // 返回结果信息到MoreFragment并进行壁纸更新
                getActivity().setResult(Activity.RESULT_OK, i);
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
        mWallpaperPosition = share.getInt(WeacConstants.WALLPAPER_POSITION, 0);
        mList = new ArrayList<>();
        // 资源文件集合
        Field[] fields = R.drawable.class.getDeclaredFields();
        // 遍历资源文件
        for (Field field : fields) {
            // 取得文件名以"bg_"开始的图片
            if (field.getName().startsWith("wallpaper_")) {
                try {
                    // 添加图片到列表
                    this.mList.add(field.getInt(R.drawable.class));
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    e.printStackTrace();
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
        int reqHeight = size.y / 3;

        // 创建主题壁纸适配器
        this.mAdapter = new ThemeAdapter(getActivity(), mList,
                mWallpaperPosition, memoryCache, reqWidth, reqHeight);
    }
}
