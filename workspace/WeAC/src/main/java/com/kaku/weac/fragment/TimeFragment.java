/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kaku.weac.R;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.view.Model;
import com.kaku.weac.view.MyTimer;

import java.util.Calendar;

/**
 * 计时fragment
 *
 * @author 咖枯
 * @version 1.0 2015/12/27
 */
public class TimeFragment extends BaseFragment implements View.OnClickListener,
        MyTimer.OnTimeChangeListener, MyTimer.OnMinChangListener {
    /**
     * Log tag ：TimeFragment
     */
    private static final String LOG_TAG = "TimeFragment";

    /**
     * 计时器
     */
    MyTimer timer;

    /**
     * 开始按钮
     */
    Button mStartBtn;

    /**
     * 停止按钮
     */
    Button mStopBtn;

    /**
     * 重置按钮
     */
    Button mResetBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_time, container, false);

        timer = (MyTimer) view.findViewById(R.id.timer);
        timer.setOnTimeChangeListener(this);
        timer.setTimeChangListener(this);
        timer.setModel(Model.Timer);
        timer.setStartTime(0, 0, 0, false);

        SharedPreferences preferences = getContext().getSharedPreferences(
                WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
        // 倒计时时间
        long countdown = preferences.getLong(WeacConstants.COUNTDOWN_TIME, 0);
        if (countdown != 0) {
            // 剩余时间
            long remainTime;
            boolean isStop = preferences.getBoolean(WeacConstants.IS_STOP, false);
            // 暂停状态
            if (isStop) {
                remainTime = countdown;
                // 正在计时状态
            } else {
                long now = System.currentTimeMillis();
                remainTime = countdown - now;
            }
            // 当剩余时间大于0
            if (remainTime > 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(remainTime);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                timer.setStartTime(0, minute, second, isStop);
            } else {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong(WeacConstants.COUNTDOWN_TIME, 0);
                editor.apply();
                // TODO: 响铃？
            }
        }

        mStartBtn = (Button) view.findViewById(R.id.btn_start);
        mStopBtn = (Button) view.findViewById(R.id.btn_stop);
        mResetBtn = (Button) view.findViewById(R.id.btn_reset);
        mStartBtn.setOnClickListener(this);
        mStopBtn.setOnClickListener(this);
        mResetBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                timer.start();
                break;
            case R.id.btn_stop:
                timer.stop();
                break;
            case R.id.btn_reset:
                timer.reset();
                break;
        }
    }

    @Override
    public void onTimerStart(long timeStart) {
        LogUtil.d(LOG_TAG, "onTimerStart " + timeStart);
    }

    @Override
    public void onTimeChange(long timeStart, long timeRemain) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeRemain);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        LogUtil.d(LOG_TAG, "onTimeChange timeStart " + timeStart);
        LogUtil.d(LOG_TAG, "onTimeChange timeRemain " + timeRemain + "\\\n剩余" + minute + " 分" + second + " 秒");
    }

    @Override
    public void onTimeStop(long timeStart, long timeRemain) {
        LogUtil.d(LOG_TAG, "onTimeStop timeRemain " + timeStart);
        LogUtil.d(LOG_TAG, "onTimeStop timeRemain " + timeRemain);
    }

    @Override
    public void onMinChange(int minute) {
        LogUtil.d(LOG_TAG, "minute change to " + minute);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancelTimer();
    }
}
