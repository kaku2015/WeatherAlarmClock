/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.fragment.AlarmClockFragment;
import com.kaku.weac.fragment.MoreFragment;
import com.kaku.weac.fragment.TimeFragment;
import com.kaku.weac.fragment.WeaFragment;
import com.kaku.weac.service.DaemonService;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 天气闹钟主Activity
 *
 * @author 咖枯
 * @version 1.0 2015/04/12
 */
public class MainActivity extends BaseActivity implements OnClickListener {

    /**
     * Log tag ：MainActivity
     */
    private static final String LOG_TAG = "MainActivity";

    /**
     * 闹钟Tab控件
     */
    private TextView tv_alarm_clock;

    /**
     * 天气Tab控件
     */
    private TextView tv_wea;

    /**
     * 计时Tab控件
     */
    private TextView tv_time;

    /**
     * 更多Tab控件
     */
    private TextView tv_more;

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager mFm;

    /**
     * Tab未选中文字颜色
     */
    private int mUnSelectColor;

    /**
     * Tab选中时文字颜色
     */
    private int mSelectColor;

    /**
     * 滑动菜单视图
     */
    private ViewPager mViewPager;

    /**
     * Tab页面集合
     */
    private List<Fragment> mFragmentList;

    /**
     * 当前Tab的Index
     */
    private int mCurrentIndex = -1;

    /**
     * 展示天气的Fragment
     */
    private WeaFragment mWeaFragment;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 配置友盟相关
        configureUmeng();
        // 禁止滑动后退
        setSwipeBackEnable(false);
        startService(new Intent(this, DaemonService.class));
        setContentView(R.layout.activity_main);
        // 设置主题壁纸
        setThemeWallpaper();

        mFm = getSupportFragmentManager();
        // Tab选中文字颜色
        mSelectColor = getResources().getColor(R.color.white);
        // Tab未选中文字颜色
        mUnSelectColor = getResources().getColor(R.color.white_trans50);
        // 初始化布局元素
        initViews();
        // 启动程序后选中Tab为闹钟
        setTabSelection(0);
    }

    /**
     * 配置友盟设置
     */
    private void configureUmeng() {
        // 使用友盟集成测试模式
        MobclickAgent.setDebugMode(true);
        // 因为以下这些设置是静态的参数，如果在应用中不止一次调用了检测更新的方法，而每次的设置都不一样，
        // 请在每次检测更新的函数之前先恢复默认设置再设置参数，避免在其他地方设置的参数影响到这次更新。
        UmengUpdateAgent.setDefault();
        UmengUpdateAgent.update(this);

        // 当开发者回复用户反馈后，提醒用户
        new FeedbackAgent(this).sync();
    }

    /**
     * 设置主题壁纸
     */
    private void setThemeWallpaper() {
        ViewGroup vg = (ViewGroup) findViewById(R.id.llyt_activity_main);
        MyUtil.setBackground(vg, this);
    }

    // XXX
    // @Override
    // public void onBackPressed() {
    // // super.onBackPressed();
    // // Intent localIntent = new Intent("android.intent.action.MAIN");
    // // localIntent.addCategory("android.intent.category.HOME");
    // // localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    // // MainActivity.this.startActivity(localIntent);
    // // System.exit(0);
    // Process.killProcess(Process.myPid());
    // }

    /**
     * 获取布局元素，并设置事件
     */
    private void initViews() {
        // 取得Tab布局
        // 闹钟Tab界面布局
        ViewGroup tab_alarm_clock = (ViewGroup) findViewById(R.id.tab_alarm_clock);
        // 天气Tab界面布局
        ViewGroup tab_wea = (ViewGroup) findViewById(R.id.tab_wea);
        // 计时Tab界面布局
        ViewGroup tab_time = (ViewGroup) findViewById(R.id.tab_time);
        // 更多Tab界面布局
        ViewGroup tab_more = (ViewGroup) findViewById(R.id.tab_more);

        // 取得Tab控件
        tv_alarm_clock = (TextView) findViewById(R.id.tv_alarm_clock);
        tv_wea = (TextView) findViewById(R.id.tv_wea);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_more = (TextView) findViewById(R.id.tv_more);

        // 设置Tab点击事件
        tab_alarm_clock.setOnClickListener(this);
        tab_wea.setOnClickListener(this);
        tab_time.setOnClickListener(this);
        tab_more.setOnClickListener(this);

        // 设置Tab页面集合
        mFragmentList = new ArrayList<>();
        // 展示闹钟的Fragment
        AlarmClockFragment mAlarmClockFragment = new AlarmClockFragment();
        // 展示天气的Fragment
        mWeaFragment = new WeaFragment();
        // 展示计时的Fragment
        TimeFragment mTimeFragment = new TimeFragment();
        // 展示更多的Fragment
        MoreFragment mMoreFragment = new MoreFragment();
        mFragmentList.add(mAlarmClockFragment);
        mFragmentList.add(mWeaFragment);
        mFragmentList.add(mTimeFragment);
        mFragmentList.add(mMoreFragment);

        // 设置ViewPager
        mViewPager = (ViewPager) findViewById(R.id.fragment_container);
        mViewPager.setAdapter(new MyFragmentPagerAdapter(mFm));
        // 设置一边加载的page数
        mViewPager.setOffscreenPageLimit(3);
        // TODO：切换渐变
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {
                setTabSelection(index);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    /**
     * ViewPager适配器
     */
    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

    }

    @Override
    public void onClick(View v) {
        // 判断选中的Tab
        switch (v.getId()) {
            // 当选中闹钟Tab时
            case R.id.tab_alarm_clock:
                // 切换闹钟视图
                setTabSelection(0);
                break;
            // 当选中天气Tab时
            case R.id.tab_wea:
                // 切换天气视图
                setTabSelection(1);
                break;
            // 当选中计时Tab时
            case R.id.tab_time:
                // 切换计时视图
                setTabSelection(2);
                break;
            // 当选中更多Tab时
            case R.id.tab_more:
                // 切换更多视图
                setTabSelection(3);
                break;
            default:
                break;
        }
    }

    /**
     * 设置选中的Tab
     *
     * @param index 每个tab对应的下标。0表示闹钟，1表示天气，2表示计时，3表示更多。
     */
    private void setTabSelection(int index) {
        // 当重复选中相同Tab时不进行任何处理
        if (mCurrentIndex == index) {
            return;
        }

        if (index != 1) {
            // 当不是天气界面并且已经开始延迟刷新天气线程
            if (mWeaFragment.mHandler != null && mWeaFragment.mIsPostDelayed) {
                // 取消线程
                mWeaFragment.mHandler.removeCallbacks(mWeaFragment.mRun);
                mWeaFragment.mIsPostDelayed = false;
                LogUtil.i(LOG_TAG, "已移除刷新天气线程");
            }
            if (mWeaFragment.mPullRefreshScrollView != null) {
                // 当正在刷新
                if (mWeaFragment.mPullRefreshScrollView.isRefreshing()) {
                    // 停止刷新
                    mWeaFragment.mPullRefreshScrollView.onRefreshComplete();
                    LogUtil.i(LOG_TAG, "已停止刷新天气动画");
                }
            }
            // 停止刷新动画
            if (mWeaFragment.mRefreshBtn != null) {
                mWeaFragment.mRefreshBtn.clearAnimation();
            }
        }

        // 设置当前Tab的Index值为传入的Index值
        mCurrentIndex = index;
        // 改变ViewPager视图
        mViewPager.setCurrentItem(index, false);
        // 清除掉上次的选中状态
        clearSelection();
        // 判断传入的Index
        switch (index) {
            // 闹钟
            case 0:
                // 改变闹钟控件的图片和文字颜色
                setTextView(R.drawable.ic_alarm_clock_select, tv_alarm_clock,
                        mSelectColor);
                break;
            // 天气
            case 1:
                // 改变天气控件的图片和文字颜色
                setTextView(R.drawable.ic_weather_select, tv_wea, mSelectColor);
                break;
            // 计时
            case 2:
                // 改变计时控件的图片和文字颜色
                setTextView(R.drawable.ic_time_select, tv_time, mSelectColor);
                break;
            // 更多
            case 3:
                // 改变更多控件的图片和文字颜色
                setTextView(R.drawable.ic_more_select, tv_more, mSelectColor);
                break;
        }

    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        // 设置闹钟Tab为未选中状态
        setTextView(R.drawable.ic_alarm_clock_unselect, tv_alarm_clock,
                mUnSelectColor);
        // 设置天气Tab为未选中状态
        setTextView(R.drawable.ic_weather_unselect, tv_wea, mUnSelectColor);
        // 设置计时Tab为未选中状态
        setTextView(R.drawable.ic_time_unselect, tv_time, mUnSelectColor);
        // 设置更多Tab为未选中状态
        setTextView(R.drawable.ic_more_unselect, tv_more, mUnSelectColor);
    }

    /**
     * 设置Tab布局
     *
     * @param iconId   Tab图标
     * @param textView Tab文字
     * @param color    Tab文字颜色
     */
    private void setTextView(int iconId, TextView textView, int color) {
        @SuppressWarnings("deprecation") Drawable drawable = getResources().getDrawable(iconId);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            // 设置图标
            textView.setCompoundDrawables(null, drawable, null, null);
        }
        // 设置文字颜色
        textView.setTextColor(color);
    }

    @Override
    protected void onDestroy() {
        LogUtil.d(LOG_TAG, "onDestroy()");
//        Process.killProcess(Process.myPid());
        super.onDestroy();
    }

    private long mExitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.showShortToast(this, getString(R.string.press_again_exit));
                mExitTime = System.currentTimeMillis();
            } else {
                MobclickAgent.onKillProcess(this);
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
