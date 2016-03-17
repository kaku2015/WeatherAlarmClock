/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.activities.LocalAlbumActivity;
import com.kaku.weac.adapter.ThemeAdapter;
import com.kaku.weac.bean.Event.WallpaperEvent;
import com.kaku.weac.bean.Theme;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.OttoAppConfig;
import com.squareup.otto.Subscribe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

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

    private ViewGroup mBackground;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OttoAppConfig.getInstance().register(this);
        // 初始化主题壁纸适配器
        initAdapter();
        mCurrentWallpaper = mWallpaperName;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_theme, container, false);
        mBackground = (ViewGroup) view.findViewById(R.id.background);
        MyUtil.setBackgroundBlur(mBackground, getActivity());

        ImageView backBtn = (ImageView) view.findViewById(R.id.action_back);
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

                // 保存壁纸信息
                MyUtil.saveWallpaper(getActivity(), WeacConstants.WALLPAPER_NAME, resName);

                // 发送更新应用自带壁纸事件
                // use @Subscribe then post when activity initialize
                OttoAppConfig.getInstance().post(new WallpaperEvent(true));

            }

        });

        OverScrollDecoratorHelper.setUpOverScroll(gridView);
        return view;
    }

    @Subscribe
    public void onWallpaperUpdate(WallpaperEvent wallpaperEvent) {
        if (mBackground != null) {
            MyUtil.setBackgroundBlur(mBackground, getActivity());
            // 不是app自带壁纸
            if (mAdapter != null && !wallpaperEvent.isAppWallpaper()) {
                mAdapter.updateSelection("");
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 读取资源文件取得图片列表，并进行主题壁纸适配器的初始化
     */
    private void initAdapter() {
        // 与主题壁纸相关的存取信息
        SharedPreferences share = getActivity().getSharedPreferences(
                WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
        String wallpaperPath = share.getString(WeacConstants.WALLPAPER_PATH, null);
        // 当为自定义主题壁纸，不显示壁纸标记
        if (wallpaperPath != null) {
            mWallpaperName = "";
        } else {
            // 取得使用中壁纸的位置
            mWallpaperName = share.getString(WeacConstants.WALLPAPER_NAME,
                    WeacConstants.DEFAULT_WALLPAPER_NAME);
        }
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
        // 创建主题壁纸适配器
        this.mAdapter = new ThemeAdapter(getActivity(), mList, mWallpaperName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_back:
                getActivity().finish();
                break;
            case R.id.custom_define_btn:
                if (MyUtil.isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(getActivity(), LocalAlbumActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoomin, 0);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OttoAppConfig.getInstance().unregister(this);
    }
}
