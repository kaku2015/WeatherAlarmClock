/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kaku.weac.R;
import com.kaku.weac.activities.RingSelectActivity;
import com.kaku.weac.activities.TimerOnTimeActivity;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;
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
     * 铃声选择按钮的requestCode
     */
    private static final int REQUEST_RING_SELECT = 1;

    /**
     * 计时器
     */
    MyTimer timer;

    /**
     * 开始按钮
     */
    ImageView mStartBtn;

    /**
     * 开始按钮2
     */
    ImageView mStartBtn2;

    /**
     * 停止按钮
     */
    ImageView mStopBtn;

    /**
     * 重置按钮
     */
    ImageView mResetBtn;

    /**
     * 快捷按钮
     */
    ImageView mQuickBtn;

    /**
     * 铃声按钮
     */
    ImageView mRingBtn;

    /**
     * 初始计时按钮布局
     */
    ViewGroup mStartLLyt;

    /**
     * 开启计时后按钮的布局
     */
    ViewGroup mStartLLyt2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_time, container, false);

        mStartLLyt = (ViewGroup) view.findViewById(R.id.btn_start_llyt);
        mStartLLyt2 = (ViewGroup) view.findViewById(R.id.btn_start_llyt2);

        mStartBtn = (ImageView) view.findViewById(R.id.btn_start);
        mStartBtn2 = (ImageView) view.findViewById(R.id.btn_start2);
        mStopBtn = (ImageView) view.findViewById(R.id.btn_stop);
        mResetBtn = (ImageView) view.findViewById(R.id.btn_reset);
        mQuickBtn = (ImageView) view.findViewById(R.id.btn_quick);
        mRingBtn = (ImageView) view.findViewById(R.id.btn_ring);

        mStartBtn2.setOnClickListener(this);
        mStopBtn.setOnClickListener(this);
        mResetBtn.setOnClickListener(this);
        mQuickBtn.setOnClickListener(this);
        mRingBtn.setOnClickListener(this);

        timer = (MyTimer) view.findViewById(R.id.timer);
        timer.setOnTimeChangeListener(this);
        timer.setTimeChangListener(this);
        timer.setModel(Model.Timer);
        timer.setStartTime(0, 0, 0, true);
        setTimer();

        return view;
    }

    private void setTimer() {
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
                setStart2Visible();
                timer.setIsStarted(true);
                // 正在计时状态
            } else {
                long now = SystemClock.elapsedRealtime();
                remainTime = countdown - now;
            }
            // 当剩余时间大于0
            if (remainTime > 0 && (remainTime / 1000 / 60) < 60) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(remainTime);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                timer.setStartTime(0, minute, second, isStop);
                setStratLlyt2Visible();
            } else {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong(WeacConstants.COUNTDOWN_TIME, 0);
                editor.apply();
                setStartBtnNoClickable();
                // TODO: 响铃？
            }
        } else {
            setStartBtnNoClickable();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                timer.start();
                setStratLlyt2Visible();
                setStopVisible();
                break;
            case R.id.btn_start2:
                timer.start();
                setStopVisible();
                break;
            case R.id.btn_stop:
                stopAlarmClockTimer();
                timer.stop();
                setStart2Visible();
                break;
            case R.id.btn_reset:
                stopAlarmClockTimer();
                timer.reset();
                setStratLlytVisible();
                break;
            case R.id.btn_ring:
                if (MyUtil.isFastDoubleClick()) {
                    return;
                }

                SharedPreferences shares = getActivity().getSharedPreferences(
                        WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
                int ringPager = shares.getInt(WeacConstants.RING_PAGER_TIMER, 0);
                String ringUrl = shares.getString(WeacConstants.RING_URL_TIMER, WeacConstants.DEFAULT_RING_URL);
                String ringName = shares.getString(WeacConstants.RING_NAME_TIMER, getString(R.string.default_ring));

                Intent i = new Intent(getActivity(), RingSelectActivity.class);
                i.putExtra(WeacConstants.RING_NAME, ringName);
                i.putExtra(WeacConstants.RING_URL, ringUrl);
                i.putExtra(WeacConstants.RING_PAGER, ringPager);
                i.putExtra(WeacConstants.RING_REQUEST_TYPE, 1);
                startActivityForResult(i, REQUEST_RING_SELECT);
                break;
        }
    }

    /**
     * 停止倒计时广播
     */
    private void stopAlarmClockTimer() {
        Intent intent = new Intent(getContext(), TimerOnTimeActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getContext(),
                1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getContext()
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pi);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            // 铃声选择界面返回
            case REQUEST_RING_SELECT:
                // 铃声名
                String name = data.getStringExtra(WeacConstants.RING_NAME);
                // 铃声地址
                String url = data.getStringExtra(WeacConstants.RING_URL);
                // 铃声界面
                int ringPager = data.getIntExtra(WeacConstants.RING_PAGER, 0);
                break;
        }
    }

    /**
     * 设置计时前开始按钮可以点击
     */
    private void setStartBtnClickable() {
        mStartBtn.setImageAlpha(255);
        //noinspection deprecation
        mStartBtn.setBackground(getResources().getDrawable(R.drawable.bg_timer_button));
        mStartBtn.setOnClickListener(this);
    }

    /**
     * 设置计时前开始按钮不可点击
     */
    private void setStartBtnNoClickable() {
        mStartBtn.setImageAlpha(50);
        mStartBtn.setBackground(null);
        mStartBtn.setOnClickListener(null);
    }

    /**
     * 显示开始计时后的开始按钮
     */
    private void setStart2Visible() {
        mStartBtn2.setVisibility(View.VISIBLE);
        mStopBtn.setVisibility(View.GONE);
    }

    /**
     * 显示开始计时后的暂停按钮
     */
    private void setStopVisible() {
        mStartBtn2.setVisibility(View.GONE);
        mStopBtn.setVisibility(View.VISIBLE);
    }

    /**
     * 显示开始计时前的布局
     */
    private void setStratLlytVisible() {
        setStartBtnNoClickable();
        mStartLLyt.setVisibility(View.VISIBLE);
        mStartLLyt2.setVisibility(View.GONE);
    }

    /**
     * 显示开始计时后的布局
     */
    private void setStratLlyt2Visible() {
        mStartLLyt.setVisibility(View.GONE);
        mStartLLyt2.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTimerStart(long timeRemain) {
        LogUtil.d(LOG_TAG, "onTimerStart " + timeRemain);
        MyUtil.startAlarmTimer(getContext(), timeRemain);
    }

    @Override
    public void onTimeChange(long timeStart, long timeRemain) {
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(timeRemain);
//        int minute = c.get(Calendar.MINUTE);
//        int second = c.get(Calendar.SECOND);
//        LogUtil.d(LOG_TAG, "onTimeChange timeStart " + timeStart);
//        LogUtil.d(LOG_TAG, "onTimeChange timeRemain " + timeRemain + "\\\n剩余" + minute + " 分" + second + " 秒");
    }

    @Override
    public void onTimeStop(long timeStart, long timeRemain) {
//        LogUtil.d(LOG_TAG, "onTimeStop timeRemain " + timeStart);
//        LogUtil.d(LOG_TAG, "onTimeStop timeRemain " + timeRemain);
        setStratLlytVisible();
    }

    @Override
    public void onMinChange(int minute) {
        LogUtil.d(LOG_TAG, "minute change to " + minute);

        if (minute == 0) {
            setStartBtnNoClickable();
        } else {
            if (mStartBtn.getImageAlpha() == 50) {
                setStartBtnClickable();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancelTimer();
    }
}
