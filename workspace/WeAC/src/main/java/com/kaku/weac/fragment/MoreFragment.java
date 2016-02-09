/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.OttoAppConfig;
import com.kaku.weac.util.ToastUtil;
import com.kaku.weac.view.CircleProgress;
import com.kaku.weac.zxing.activity.CaptureActivity;
import com.squareup.otto.Subscribe;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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
    private TextView mUsedMemoryTv;
    private CircleProgress mCleanUpCP;
    private ActivityManager mActivityManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OttoAppConfig.getInstance().register(this);
        mActivityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
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
        mUsedMemoryTv = (TextView) view.findViewById(R.id.used_memory_tv);
        ViewGroup clearUpBtn = (ViewGroup) view.findViewById(R.id.clean_up);
        mCleanUpCP = (CircleProgress) view.findViewById(R.id.circle_progress);

        themeBtn.setOnClickListener(this);
        scanQRcodeBtn.setOnClickListener(this);
        generateCodeBtn.setOnClickListener(this);
        clearMemoryBtn.setOnClickListener(this);
        clearUpBtn.setOnClickListener(this);
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
                mUsedMemoryTv.setText(DataCleanManager.getTotalCacheSize(getActivity()));
                break;
            case R.id.clean_up:
                new CleanUpAsyncTask().execute();
                break;
        }
    }

    class CleanUpAsyncTask extends AsyncTask<Void, Integer, String> {
        // 进程数
        private int processCount = 0;

        @Override
        protected String doInBackground(Void... params) {
            PackageManager packageManager = getActivity().getPackageManager();
            ApplicationInfo appInfo = null;
            ActivityManager.RunningAppProcessInfo appProcessInfo;
            List<ActivityManager.RunningAppProcessInfo> infoList = mActivityManager.getRunningAppProcesses();
            if (infoList != null) {

                long beforeMem = getAvailableMemory();
                Log.d(LOG_TAG, "-----------before memory info : " + beforeMem);
                // 进程数
                int length = infoList.size();
                for (int i = 0; i < length; i++) {
                    appProcessInfo = infoList.get(i);
                    Log.d(LOG_TAG, "process name : " + appProcessInfo.processName);
                    //importance 该进程的重要程度  分为几个级别，数值越低就越重要。
                    Log.d(LOG_TAG, "importance : " + appProcessInfo.importance);

                    try {
                        appInfo = packageManager.getApplicationInfo(appProcessInfo.processName, 0);

                    } catch (PackageManager.NameNotFoundException e) {
                        LogUtil.i(LOG_TAG, "clearMemory:>> " + e.toString());
                        // :服务的命名
                        if (appProcessInfo.processName.contains(":")) {
                            appInfo = getApplicationInfo(appProcessInfo.processName.split(":")[0], packageManager);

                        }
                    }
                    if (appInfo == null) {
                        continue;
                    }
                    // 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
                    // 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行
                    // 过滤系统应用
                    // a&b: “a”的值是129，转换成二进制就是10000001，而“b”的值是128，转换成二进制就是10000000。
                    // 根据与运算符的运算规律，只有两个位都是1，结果才是1，可以知道结果就是10000000，即128。
                    if ((appProcessInfo.importance > ActivityManager.RunningAppProcessInfo.
                            IMPORTANCE_VISIBLE) && (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                        String[] pkgList = appProcessInfo.pkgList;
                        for (String aPkgList : pkgList) {//pkgList 得到该进程下运行的包名
                            Log.d(LOG_TAG, "It will be killed, package name : " + aPkgList);
                            mActivityManager.killBackgroundProcesses(aPkgList);
                            processCount++;

                            // 更新内存百分比
                            publishProgress(getUsedPercentValue());
                        }
                    }
                }

                long afterMem = getAvailableMemory();
                Log.d(LOG_TAG, "----------- after memory info : " + afterMem);
                return MyUtil.formatFileSize(afterMem - beforeMem);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... memoryPercent) {
            super.onProgressUpdate(memoryPercent);
            setCleanUpProgress(memoryPercent[0]);
        }

        @Override
        protected void onPostExecute(String cleanedMemory) {
            super.onPostExecute(cleanedMemory);
            if (cleanedMemory != null) {
                ToastUtil.showLongToast(getActivity(), getString(R.string.clean_up_result,
                        processCount, cleanedMemory));
            }
        }
    }

    public ApplicationInfo getApplicationInfo(String processName, PackageManager packageManager) {
        if (processName == null) {
            return null;
        }
        List<ApplicationInfo> appList = packageManager
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo appInfo : appList) {
            if (processName.equals(appInfo.processName)) {
                return appInfo;
            }
        }
        return null;
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

    /**
     * 计算已使用内存的百分比
     */
    private int getUsedPercentValue() {
        int totalMemorySize = getTotalMemory();
        long availableSize = getAvailableMemory() / 1024;
        if (totalMemorySize != 0) {
            return (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);
        }
        return 0;
    }

    /**
     * 获得系统总内存大小,返回数据以KB为单位。
     */
    private int getTotalMemory() {
        // /proc/meminfo系统内存信息文件
        String path = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr, 2048);
            // 读取meminfo第一行，系统总内存大小
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            // \D匹配一个非数字字符。等价于[^0-9]。
            return Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
        } catch (IOException e) {
            LogUtil.e(LOG_TAG, "getUsedPercentValue: " + e.toString());
        }
        return 0;
    }

    /**
     * 获取当前可用内存，返回数据以字节为单位。
     *
     * @return 当前可用内存。
     */
    private long getAvailableMemory() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        mActivityManager.getMemoryInfo(mi);
        return mi.availMem;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mUsedMemoryTv.setText(DataCleanManager.getTotalCacheSize(getActivity()));
        setCleanUpProgress(getUsedPercentValue());
    }

    private void setCleanUpProgress(int percent) {
        mCleanUpCP.setProgress(percent);

        int color;
        if (percent < 70) {
            color = R.color.progress_1;
        } else if (percent < 90) {
            color = R.color.progress_2;
        } else {
            color = R.color.progress_3;
        }
        //noinspection deprecation
        mCleanUpCP.setFinishedColor(getActivity().getResources().getColor(color));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OttoAppConfig.getInstance().unregister(this);
    }
}
