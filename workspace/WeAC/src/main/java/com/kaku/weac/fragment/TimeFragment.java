/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kaku.weac.R;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.view.Model;
import com.kaku.weac.view.MyTimer;

import java.util.Calendar;

/**
 * 计时fragment
 *
 * @author 咖枯
 * @version 1.0 2015/05
 */
public class TimeFragment extends BaseFragment implements View.OnClickListener,
        MyTimer.OnTimeChangeListener, MyTimer.OnMinChangListener {
    /**
     * Log tag ：TimeFragment
     */
    private static final String LOG_TAG = "TimeFragment";
    MyTimer timer;
    Button btn_start, btn_stop, btn_reset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_time, container, false);

        timer = (MyTimer) view.findViewById(R.id.timer);
        timer.setOnTimeChangeListener(this);
        timer.setTimeChangListener(this);
        timer.setModel(Model.Timer);
        timer.setStartTime(0, 0, 0);
        btn_start = (Button) view.findViewById(R.id.btn_start);
        btn_stop = (Button) view.findViewById(R.id.btn_stop);
        btn_reset = (Button) view.findViewById(R.id.btn_reset);
        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_reset.setOnClickListener(this);

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
}
