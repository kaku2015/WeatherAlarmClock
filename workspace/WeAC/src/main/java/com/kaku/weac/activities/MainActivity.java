package com.kaku.weac.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.fragment.AlarmClockFragment;
import com.kaku.weac.fragment.MoreFragment;
import com.kaku.weac.fragment.TimeFragment;
import com.kaku.weac.fragment.WeaFragment;
import com.kaku.weac.service.GuardMasterService;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 天气闹钟主Activity
 *
 * @author 咖枯
 * @version 1.0 2015/04/12
 */
public class MainActivity extends FragmentActivity implements OnClickListener {

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

    // @Override
    // public void onLowMemory() {
    // super.onLowMemory();
    // // TODO
    // LogUtil.w(LOG_TAG, "onLowMemory");
    //
    // // clearMemory();
    // // Process.killProcess(Process.myPid());
    // }
    //
    // @Override
    // public void onTrimMemory(int level) {
    // super.onTrimMemory(level);
    // // TODO
    // LogUtil.w(LOG_TAG, "onTrimMemory");
    //
    // // clearMemory();
    //
    // // ActivityManager activityManager = (ActivityManager)
    // // getSystemService(Context.ACTIVITY_SERVICE);
    // // List<ActivityManager.RunningAppProcessInfo> list = activityManager
    // // .getRunningAppProcesses();
    // // if (list != null) {
    // // for (int i = 0; i < list.size(); i++) {
    // // ActivityManager.RunningAppProcessInfo apinfo = list.get(i);
    // // String[] pkgList = apinfo.pkgList;
    // // if (apinfo.importance >
    // // ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE) {
    // // for (int j = 0; j < pkgList.length; j++) {
    // // activityManager.killBackgroundProcesses(pkgList[j]);
    // // }
    // // }
    // // }
    // // }
    //
    // // Process.killProcess(Process.myPid());
    // }
    //
    // // public void clearMemory() {
    // // ActivityManager localActivityManager =
    // // (ActivityManager)this.getSystemService("activity");
    // // Iterator localIterator =
    // // localActivityManager.getRunningAppProcesses().iterator();
    // // PackageManager localPackageManager = this.getPackageManager();
    // // ActivityManager.MemoryInfo localMemoryInfo = new
    // // ActivityManager.MemoryInfo();
    // // localActivityManager.getMemoryInfo(localMemoryInfo);
    // // long l1 = localMemoryInfo.availMem / 1048576L;
    // // long l2 = 100L;
    // // if (l2 >= getTotalMemory()) {
    // // if (getTotalMemory() <= 0L)
    // // l2 = 100L;
    // // } else
    // // Log.e("SUNRIX", "MyLowMemoryThreshold is: " + l1 + "/" + l2);
    // // while (true)
    // // {
    // // // if ((l1 >= l2) || (!localIterator.hasNext()))
    // // // {
    // // // return;
    // // // // l2 = 3L * getTotalMemory() / 4L;
    // // // // break;
    // // // }
    // // ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo =
    // // (ActivityManager.RunningAppProcessInfo)localIterator.next();
    // // try
    // // {
    // //
    // localPackageManager.getApplicationLabel(localPackageManager.getApplicationInfo(localRunningAppProcessInfo.processName,
    // // 128));
    // // String str = localRunningAppProcessInfo.processName;
    // // if (str.equals(this.getPackageName()))
    // // continue;
    // // localActivityManager.killBackgroundProcesses(str);
    // // }
    // // catch (Exception localException)
    // // {
    // // break;
    // // }
    // // }
    // // }
    // // public long getTotalMemory()
    // // {
    // // try
    // // {
    // // BufferedReader localBufferedReader = new BufferedReader(new
    // // FileReader("/proc/meminfo"), 8192);
    // // long l1 = 1024 *
    // //
    // Integer.valueOf(localBufferedReader.readLine().split("\\s+")[1]).intValue();
    // // localBufferedReader.close();
    // // long l2 = l1 / 1048576L;
    // // return l2;
    // // }
    // // catch (IOException localIOException)
    // // {
    // // }
    // // return -1L;
    // // }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, GuardMasterService.class);
        startService(intent);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        // 设置主题壁纸
        setThemeWallpaper();
        mFm = getSupportFragmentManager();
        // Tab选中文字颜色
        mSelectColor = getResources().getColor(R.color.blue_tab);
        // Tab未选中文字颜色
        mUnSelectColor = getResources().getColor(R.color.gray_tab);
        // 初始化布局元素
        initViews();
        // 启动程序后选中Tab为闹钟
        setTabSelection(0);
    }

    /**
     * 设置主题壁纸
     */
    private void setThemeWallpaper() {
        ViewGroup vg = (ViewGroup) findViewById(R.id.llyt_activity_main);
        vg.setBackgroundResource(MyUtil.getWallPaper(this));
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
        WeaFragment mWeaFragment = new WeaFragment();
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

        // 当不是天气界面并且已经开始延迟刷新天气线程
        if (index != 1 && WeaFragment.sHandler != null && WeaFragment.sIsPostDelayed) {
            // 取消线程
            WeaFragment.sHandler.removeCallbacks(WeaFragment.sRun);
            WeaFragment.sIsPostDelayed = false;
            LogUtil.i(LOG_TAG, "已移除刷新天气线程");
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
        Drawable drawable = getResources().getDrawable(iconId);
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

}
