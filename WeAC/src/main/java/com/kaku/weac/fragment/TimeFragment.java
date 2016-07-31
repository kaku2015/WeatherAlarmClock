/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.kaku.weac.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kaku.weac.Listener.OnVisibleListener;
import com.kaku.weac.R;
import com.kaku.weac.activities.RingSelectActivity;
import com.kaku.weac.activities.TimerOnTimeActivity;
import com.kaku.weac.bean.Event.TimerOnTimeEvent;
import com.kaku.weac.bean.Event.TimerStartEvent;
import com.kaku.weac.bean.model.TimeModel;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.service.CountDownService;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.OttoAppConfig;
import com.kaku.weac.view.MyTimer;
import com.squareup.otto.Subscribe;

import java.util.Calendar;

/**
 * 计时fragment
 *
 * @author 咖枯
 * @version 1.0 2015/12/27
 */
public class TimeFragment extends LazyLoadFragment implements View.OnClickListener,
        MyTimer.OnTimeChangeListener, MyTimer.OnMinChangListener, CountDownService.TimerUpdateListener {
    /**
     * Log tag ：TimeFragment
     */
    private static final String LOG_TAG = "TimeFragment";

    /**
     * 计时器
     */
    private MyTimer mTimer;

    /**
     * 开始按钮
     */
    private TextView mStartBtn;

    /**
     * 开始按钮2
     */
    private TextView mStartBtn2;

    /**
     * 停止按钮
     */
    private TextView mStopBtn;

    /**
     * 快捷按钮
     */
    private TextView mQuickBtn;

    /**
     * 初始计时按钮布局
     */
    private ViewGroup mStartLLyt;

    /**
     * 开启计时后按钮的布局
     */
    private ViewGroup mStartLLyt2;

    /**
     * 快捷便签PopupWindow
     */
    private PopupWindow mPopupWindow;

    /**
     * 是否已经初始化快捷便签选项布局
     */
    private boolean isQuickTimerOptionsInitialized;

    /**
     * 标志位，标志已经初始化完成
     */
    private boolean mIsPrepared;

    private OnVisibleListener mOnVisibleListener;

    /**
     * 绑定倒计时service
     */
    private boolean mIsBind;

    @Override
    protected void lazyLoad() {
        if (!mIsPrepared && mIsVisible) {
            if (mOnVisibleListener != null) {
                mOnVisibleListener.onVisible();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OttoAppConfig.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fm_time, container, false);

        mOnVisibleListener = new OnVisibleListener() {
            @Override
            public void onVisible() {
                showTimeLayout(view);
                assignViews(view);
            }
        };

        return view;
    }

    private void assignViews(View view) {
        mStartLLyt = (ViewGroup) view.findViewById(R.id.btn_start_llyt);
        mStartLLyt2 = (ViewGroup) view.findViewById(R.id.btn_start_llyt2);

        mStartBtn = (TextView) view.findViewById(R.id.btn_start);
        mStartBtn2 = (TextView) view.findViewById(R.id.btn_start2);
        mStopBtn = (TextView) view.findViewById(R.id.btn_stop);
        // 重置按钮
        TextView resetBtn = (TextView) view.findViewById(R.id.btn_reset);
        mQuickBtn = (TextView) view.findViewById(R.id.btn_quick);
        // 铃声按钮
        TextView ringBtn = (TextView) view.findViewById(R.id.btn_ring);

        mStartBtn2.setOnClickListener(TimeFragment.this);
        mStopBtn.setOnClickListener(TimeFragment.this);
        resetBtn.setOnClickListener(TimeFragment.this);
        mQuickBtn.setOnClickListener(TimeFragment.this);
        ringBtn.setOnClickListener(TimeFragment.this);

        mTimer = (MyTimer) view.findViewById(R.id.timer);
        mTimer.setOnTimeChangeListener(TimeFragment.this);
        mTimer.setTimeChangListener(TimeFragment.this);
        mTimer.setModel(TimeModel.Timer);
//        mTimer.setStartTime(0, 0, 0, true, false);
        setTimer();

        mIsPrepared = true;
    }

    private void showTimeLayout(View view) {
        // 计时布局
        ViewStub viewStub = (ViewStub) view.findViewById(R.id.viewstub_time);
        viewStub.inflate();

        // 加载中进度框
        ViewGroup progressBar = (ViewGroup) view.findViewById(R.id.progress_bar_llyt);
        progressBar.setVisibility(View.GONE);
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
                mTimer.setIsStarted(true);
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
                mTimer.setStartTime(0, minute, second, isStop, false);
                setStratLlyt2Visible();
            } else {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong(WeacConstants.COUNTDOWN_TIME, 0);
                editor.apply();

                initTimer();
            }
        } else {
            initTimer();
        }
    }

    /**
     * 初始化timer
     */
    private void initTimer() {
        setStartBtnNoClickable();
        mTimer.showAnimation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 开始
            case R.id.btn_start:
                mTimer.setIsInDragButton(false);
                startCountDown();
                setStratLlyt2Visible();
                setStopVisible();
                break;
            // 快捷便签
            case R.id.btn_quick:
                displayQuickOptions();
                break;
            // 开始2
            case R.id.btn_start2:
                startCountDown();
                setStopVisible();
                break;
            // 暂停
            case R.id.btn_stop:
                stopAlarmClockTimer();
                mTimer.stop();
                stopCountDown();
                setStart2Visible();
                break;
            // 重置
            case R.id.btn_reset:
                stopAlarmClockTimer();
                mTimer.reset();
                stopCountDown();
                setStratLlytVisible();
                break;
            // 铃声选择
            case R.id.btn_ring:
                processRingSelect();
                break;
            // 午睡
            case R.id.btn_siesta:
                processQuickTimer(0, 45, 0);
                break;
            // 面膜
            case R.id.btn_facial_mask:
                processQuickTimer(0, 15, 0);
                break;
            // 泡面
            case R.id.btn_instant_noodles:
                processQuickTimer(0, 3, 0);
                break;
            // 跑步
            case R.id.btn_run:
                processQuickTimer(0, 30, 0);
                break;
        }
    }

    @Subscribe
    public void OnTimerStart(TimerStartEvent event) {
        startCountDownService();
    }


    @Subscribe
    public void onTimerOnTime(TimerOnTimeEvent event) {
        if (mTimer != null) {
            mTimer.clearRemainTime();
        }
    }

    @Override
    public void OnUpdateTime() {
        try {
            if (mTimer != null && !getActivity().isFinishing()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTimer.updateDisplayTime();
                    }
                });
            }
        } catch (Exception e) {
            LogUtil.e(LOG_TAG, "OnUpdateTime(): e.toString()");
        }
    }

    private void stopCountDown() {
        try {
            if (mIsBind) {
                getActivity().unbindService(mConnection);
                mIsBind = false;
            }
        } catch (Exception e) {
            LogUtil.e(LOG_TAG, "stopCountDown(): " + e.toString());
        }
    }

    private void startCountDown() {
        if (mTimer.start()) {
            startCountDownService();
        }
    }

    private void startCountDownService() {
        if (!mIsBind) {
            Intent intent = new Intent(getActivity(), CountDownService.class);
            mIsBind = getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.d(LOG_TAG, ": onServiceConnected");
            CountDownService.TimerBinder binder = (CountDownService.TimerBinder) service;
            binder.setTimerUpdateListener(TimeFragment.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d(LOG_TAG, ": onServiceDisconnected");
        }
    };

    private void processQuickTimer(int h, int m, int s) {
        mTimer.setStartTime(h, m, s, false, true);
        mPopupWindow.dismiss();
        setStratLlyt2Visible();
        setStopVisible();
    }

    private void processRingSelect() {
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
        startActivity(i);
    }

    private void displayQuickOptions() {
        if (!isQuickTimerOptionsInitialized) {
            // There are of course instances where you can truly justify a null parent during inflation,
            // but they are few. One such instance occurs when you are inflating a custom layout to be attached to an AlertDialog.
            @SuppressLint("InflateParams")
            View convertView = LayoutInflater.from(getActivity()).inflate(R.layout.ppv_timer, null);

            mPopupWindow = new PopupWindow(convertView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setOutsideTouchable(true);
            // 不设置,说明PopUpWindow不能获得焦点，点击back键不会消失
            mPopupWindow.setFocusable(true);
            // 需要顺利让PopUpWindow dismiss（即点击PopUpWindow之外的地方此或者back键PopUpWindow会消失）；
            // PopUpWindow的背景不能为空。必须在PopUpWindow.showAsDropDown(v)；
            // 或者其它的显示PopUpWindow方法之前设置它的背景不为空
            //noinspection deprecation
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black_trans50)));
            // 相对某个控件的位置（正左下方）；showAtLocation(View parent, int gravity, int x, int y)：相对于父控件的位置
            mPopupWindow.showAsDropDown(mQuickBtn, 0, -Math.round(220 * getResources().getDisplayMetrics().density));

            // 午睡
            TextView siestaBtn = (TextView) convertView.findViewById(R.id.btn_siesta);
            // 面膜
            TextView facialMask = (TextView) convertView.findViewById(R.id.btn_facial_mask);
            // 泡面
            TextView instantNoodles = (TextView) convertView.findViewById(R.id.btn_instant_noodles);
            // 跑步
            TextView run = (TextView) convertView.findViewById(R.id.btn_run);

            siestaBtn.setOnClickListener(this);
            facialMask.setOnClickListener(this);
            instantNoodles.setOnClickListener(this);
            run.setOnClickListener(this);
            isQuickTimerOptionsInitialized = true;
        } else {
            mPopupWindow.showAsDropDown(mQuickBtn, 0, -Math.round(220 * getResources().getDisplayMetrics().density));
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

    /**
     * 设置计时前开始按钮可以点击
     */
    private void setStartBtnClickable() {
        mStartBtn.setAlpha(1);
        //noinspection deprecation
//        mStartBtn.setBackground(getResources().getDrawable(R.drawable.bg_timer_button));
        mStartBtn.setOnClickListener(this);
    }

    /**
     * 设置计时前开始按钮不可点击
     */
    @SuppressWarnings("deprecation")
    private void setStartBtnNoClickable() {
        mStartBtn.setAlpha(0.2f);
        mStartBtn.setBackgroundDrawable(null);
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
        LogUtil.d(LOG_TAG, "onTimerStart 距离计时结束：" + timeRemain);
        MyUtil.startAlarmTimer(getContext(), timeRemain);
    }

    @Override
    public void onTimeStop(long timeStart, long timeRemain) {
        stopCountDown();
        setStratLlytVisible();
    }

    @Override
    public void onMinChange(int minute) {
        LogUtil.d(LOG_TAG, "minute change to " + minute);

        if (minute == 0) {
            setStartBtnNoClickable();
        } else {
            // 开始按钮为不可用状态
            if (mStartBtn.getAlpha() == 0.2f) {
                setStartBtnClickable();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d(LOG_TAG, "onResume");
        if (mTimer != null) {
            setTimer();
            mTimer.setShowAnimation(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d(LOG_TAG, "onStop");
        stopCountDown();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopCountDown();
        OttoAppConfig.getInstance().unregister(this);
    }
}
