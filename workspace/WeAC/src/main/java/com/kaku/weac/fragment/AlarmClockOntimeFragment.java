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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kaku.weac.Listener.HttpCallbackListener;
import com.kaku.weac.R;
import com.kaku.weac.activities.AlarmClockNapNotificationActivity;
import com.kaku.weac.bean.AlarmClock;
import com.kaku.weac.bean.WeatherDaysForecast;
import com.kaku.weac.bean.WeatherInfo;
import com.kaku.weac.bean.WeatherLifeIndex;
import com.kaku.weac.broadcast.AlarmClockBroadcast;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.common.WeacStatus;
import com.kaku.weac.util.AudioPlayer;
import com.kaku.weac.util.HttpUtil;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.WeatherUtil;
import com.kaku.weac.view.MySlidingView;

import java.io.ByteArrayInputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 闹钟响起画面Fragment
 *
 * @author 咖枯
 * @version 1.0 2015/06/25
 */
public class AlarmClockOntimeFragment extends BaseFragment implements
        OnClickListener {

    /**
     * Log tag ：AlarmClockOntimeFragment
     */
    private static final String LOG_TAG = "AlarmClockOntimeFragment";

    /**
     * 当前时间
     */
    private TextView mTimeTv;

    /**
     * 闹钟实例
     */
    private AlarmClock mAlarmClock;

    /**
     * 线程运行flag
     */
    private boolean mIsRun = true;

    /**
     * 线程标记
     */
    private static final int UPDATE_TIME = 1;

    /**
     * 通知消息管理
     */
    private NotificationManagerCompat mNotificationManager;

    /**
     * 小睡间隔
     */
    private int mNapInterval;

    /**
     * 小睡次数
     */
    private int mNapTimes;

    /**
     * 是否点击按钮
     */
    private boolean mIsOnclick = false;

    /**
     * 小睡已执行次数
     */
    private int mNapTimesRan;

    /**
     * 声音管理
     */
    private AudioManager mAudioManager;

    /**
     * 当前音量
     */
    private int mCurrentVolume;

    /**
     * 显示当前时间Handler
     */
    private ShowTimeHandler mShowTimeHandler;

    private ViewGroup mWeatherInfoGroup;
    private ProgressBar mWeatherPbar;
    private TextView mWeatherTypeTv;
    private TextView mUmbrellaTv;
    private String mCurrentTimeDisplay = "";

    /**
     * 显示当前时间
     */
    static class ShowTimeHandler extends Handler {
        private WeakReference<AlarmClockOntimeFragment> mWeakReference;

        public ShowTimeHandler(AlarmClockOntimeFragment alarmClockOntimeFragment) {
            mWeakReference = new WeakReference<>(alarmClockOntimeFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AlarmClockOntimeFragment alarmClockOntimeFragment = mWeakReference.get();

            switch (msg.what) {
                case UPDATE_TIME:
                    alarmClockOntimeFragment.mTimeTv.setText(msg.obj.toString());
                    alarmClockOntimeFragment.mCurrentTimeDisplay =
                            alarmClockOntimeFragment.mTimeTv.getText().toString();
                    break;
            }
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(LOG_TAG, getActivity().toString() + "：onCreate");

        // 启动的Activity个数加1
        WeacStatus.sActivityNumber++;

        // 画面出现在解锁屏幕上,显示,常亮
        getActivity().getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mAlarmClock = getActivity().getIntent()
        .getParcelableExtra(WeacConstants.ALARM_CLOCK);
        // 取得小睡间隔
        mNapInterval = mAlarmClock.getNapInterval();
        // 取得小睡次数
        mNapTimes = mAlarmClock.getNapTimes();
        // XXX:修正小睡数
        // mNapTimes = 1000;
        // 小睡已执行次数
        mNapTimesRan = getActivity().getIntent().getIntExtra(
                WeacConstants.NAP_RAN_TIMES, 0);
        // 播放铃声
        playRing();

        mNotificationManager = NotificationManagerCompat.from(getActivity());
        // 取消下拉列表通知消息
        mNotificationManager.cancel(mAlarmClock.getId());

        mShowTimeHandler = new ShowTimeHandler(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.d(LOG_TAG, getActivity().toString() + "：onCreateView");

        View view = inflater.inflate(R.layout.fm_alarm_clock_ontime, container,
                false);
        mTimeTv = (TextView) view.findViewById(R.id.ontime_time);
        // 显示现在时间
        mTimeTv.setText(new SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(new Date()));
        mCurrentTimeDisplay = mTimeTv.getText().toString();
        // 启动更新时间线程
        new Thread(new TimeUpdateThread()).start();

        // 标签
        TextView tagTv = (TextView) view.findViewById(R.id.ontime_tag);
        tagTv.setText(mAlarmClock.getTag());

        // 小睡按钮
        TextView napTv = (TextView) view.findViewById(R.id.ontime_nap);
        // 小睡开启状态
        if (mAlarmClock.isNap()) {
            // 当执行X次小睡后隐藏小睡按钮
            if (mNapTimesRan != mNapTimes) {
                // 设置小睡
                napTv.setText(String.format(
                        getString(R.string.touch_here_nap), mNapInterval));
                napTv.setOnClickListener(this);
            } else {
                napTv.setVisibility(View.GONE);
            }
        } else {
            napTv.setVisibility(View.GONE);
        }

        LogUtil.i(LOG_TAG, "小睡次数：" + mNapTimes);

        // 滑动提示
        TextView slidingTipIv = (TextView) view.findViewById(R.id.sliding_tip_tv);
        final AnimationDrawable animationDrawable = (AnimationDrawable) slidingTipIv.getCompoundDrawables()[0];
        // 直接启动动画，测试4.0模拟器没有动画效果
        slidingTipIv.post(new Runnable() {
            @Override
            public void run() {
                animationDrawable.start();
            }
        });

        MySlidingView mySlidingView = (MySlidingView) view.findViewById(R.id.my_sliding_view);
        mySlidingView.setSlidingTipListener(new MySlidingView.SlidingTipListener() {
            @Override
            public void onSlidFinish() {
                // 执行关闭操作
                finishActivity();
            }
        });

        // 天气提示
        if (mAlarmClock.isWeaPrompt()) {
            mWeatherInfoGroup = (ViewGroup) view.findViewById(R.id.weather_info_group);
            mWeatherPbar = (ProgressBar) view.findViewById(R.id.progress_bar);
            mWeatherTypeTv = (TextView) view.findViewById(R.id.weather_type_tv);
            mUmbrellaTv = (TextView) view.findViewById(R.id.umbrella_tv);
            // 初始化天气预报
            initWeather();
        }
        return view;
    }

    private void initWeather() {
        // 判断网络是否可用
        if (!MyUtil.isNetworkAvailable(getActivity())) {
            return;
        }

        SharedPreferences share = getActivity().getSharedPreferences(
                WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
        // 没有默认城市自动定位
        String weatherCode = share.getString(WeacConstants.DEFAULT_WEATHER_CODE,
                getString(R.string.auto_location));

        String cityName;
        String address;
        // 自动定位
        if (weatherCode.equals(getString(R.string.auto_location))) {
            cityName = share.getString(WeacConstants.DEFAULT_CITY_NAME, null);
            address = null;
        } else {
            cityName = null;
            address = getString(R.string.address_weather, weatherCode);
        }
        mWeatherPbar.setVisibility(View.VISIBLE);
        HttpUtil.sendHttpRequest(address, cityName,
                new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        try {
                            if (!response.contains("error")) {
                                WeatherInfo weatherInfo = WeatherUtil.handleWeatherResponse(
                                        new ByteArrayInputStream(response.getBytes()));
                                getActivity().runOnUiThread(new SetWeatherInfoRunnable(weatherInfo));
                                // 无法解析当前位置
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mWeatherPbar.setVisibility(View.GONE);
                                    }
                                });
                            }
                        } catch (Exception e) {
                            LogUtil.e(LOG_TAG, "initWeather(): " + e.toString());
                        }
                    }

                    @Override
                    public void onError(final Exception e) {
                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mWeatherPbar.setVisibility(View.GONE);
                                }
                            });
                        } catch (Exception e1) {
                            LogUtil.e(LOG_TAG, e1.toString());
                        }
                    }
                });
    }


    private class SetWeatherInfoRunnable implements Runnable {
        private WeatherInfo mWeatherInfo;

        public SetWeatherInfoRunnable(WeatherInfo weatherInfo) {
            mWeatherInfo = weatherInfo;
        }

        @Override
        public void run() {
            if (mWeatherInfo == null) {
                mWeatherPbar.setVisibility(View.GONE);
                return;
            }
            try {
                Calendar calendar = Calendar.getInstance();
                // 现在小时
                int hour = calendar.get(Calendar.HOUR_OF_DAY);

                // 设置城市名
                TextView cityName = (TextView) getActivity().findViewById(R.id.city_name_tv);
                cityName.setText(mWeatherInfo.getCity());

                // 多天预报信息
                List<WeatherDaysForecast> weatherDaysForecasts = mWeatherInfo.getWeatherDaysForecast();
/*                if (weatherDaysForecasts.size() < 6) {
                    mWeatherPbar.setVisibility(View.GONE);
                    return;
                }*/
                // 今天天气信息
                WeatherDaysForecast weather;
                String time[] = mWeatherInfo.getUpdateTime().split(":");
                int hour1 = Integer.parseInt(time[0]);
                int minute1 = Integer.parseInt(time[1]);
                //更新时间从23：45开始到05：20以前的数据，或者早上5点以前，后移一天填充
                if ((hour1 == 23 && minute1 >= 45) || (hour1 < 5) ||
                        ((hour1 == 5) && (minute1 < 20)) || hour <= 5) {
                    weather = weatherDaysForecasts.get(2);
                } else {
                    weather = weatherDaysForecasts.get(1);
                }

                // 天气类型图片id
                int weatherId;
                // 设置今天天气信息
                // 当前为凌晨
                if (hour >= 0 && hour < 6) {
                    weatherId = MyUtil.getWeatherTypeImageID(weather.getTypeDay(), false);
                    // 当前为白天时
                } else if (hour >= 6 && hour < 18) {
                    weatherId = MyUtil.getWeatherTypeImageID(weather.getTypeDay(), true);
                    // 当前为夜间
                } else {
                    weatherId = MyUtil.getWeatherTypeImageID(weather.getTypeNight(), false);
                }

                @SuppressWarnings("deprecation")
                Drawable drawable = getResources().getDrawable(weatherId);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                            drawable.getMinimumHeight());
                    // 设置图标
                    mWeatherTypeTv.setCompoundDrawables(drawable, null, null, null);
                }

                String type = MyUtil.getWeatherType
                        (getActivity(), weather.getTypeDay(), weather.getTypeNight());
                String tempHigh = weather.getHigh().replace("℃", "").substring(3);
                String tempLow = weather.getLow().replace("℃", "").substring(3);
                mWeatherTypeTv.setText(String.format(getString(R.string.weather_type), type, tempHigh, tempLow));

                // 生活指数信息
                List<WeatherLifeIndex> weatherLifeIndexes = mWeatherInfo.getWeatherLifeIndex();
                for (WeatherLifeIndex index : weatherLifeIndexes) {
                    if (index.getIndexName().equals("雨伞指数")) {
                        if (index.getIndexValue().equals("带伞")) {
                            mUmbrellaTv.setVisibility(View.VISIBLE);
                        }
                    }
                }

                mWeatherInfoGroup.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                LogUtil.e(LOG_TAG, e.toString());
            } finally {
                mWeatherPbar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        LogUtil.d(LOG_TAG, getActivity().toString() + "：onStop");
        // 当第二个闹钟响起时第一个闹钟需要进入小睡或关闭闹钟（启动此Activity时加上
        // 【Intent.FLAG_ACTIVITY_CLEAR_TOP】flag 会自动关闭当前Activity，只有
        // 【Intent.FLAG_ACTIVITY_NEW_TASK】 flag的话，
        // 只是暂停，当第二个Activity结束后后会重新恢复显示）

//        LogUtil.d(LOG_TAG, getActivity().toString() + "：activityNumber: "
//                + WeacStatus.sActivityNumber);

        // 当点击关闭或者小睡按钮或者画面关闭状态时或点击电源键闹钟响起会执行一次onStop()
        // 当点击按钮
        // if (mIsOnclick) {
        // // 点击按钮后，执行程序结束处理，故Activity数减1
        // WeacStatus.activityNumber--;
        // return;
        // }
        // // 第二个闹钟Activity启动
        // if (WeacStatus.activityNumber > 1) {
        // WeacStatus.activityNumber--;
        // // // 停止运行更新时间的线程
        // // mIsRun = false;
        // // // 小睡
        // // nap();
        //
        // }
    }

    @Override
    public void onDestroy() {
        LogUtil.d(LOG_TAG, getActivity().toString() + "：onDestroy");
        super.onDestroy();
        // 停止运行更新时间的线程
        mIsRun = false;

        // 当没有点击按钮，则当前响铃被新闹钟任务杀死，开启小睡
        if (!mIsOnclick) {
            // 小睡
            nap();
        }

        // 当前只有一个Activity
        if (WeacStatus.sActivityNumber <= 1) {
            // 停止播放
            AudioPlayer.getInstance(getActivity()).stop();
        }

        // 启动的Activity个数减一
        WeacStatus.sActivityNumber--;

        // If null, all callbacks and messages will be removed.
        if (mShowTimeHandler != null) {
            mShowTimeHandler.removeCallbacksAndMessages(null);
        }

        // 复原手机媒体音量
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                mCurrentVolume, AudioManager.ADJUST_SAME);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击小睡
            case R.id.ontime_nap:
                // 执行小睡操作
                onClickNapButton();
                break;
        }
    }

    /**
     * 执行结束当前Activity操作
     */
    private void finishActivity() {
        // 点击按钮标记
        mIsOnclick = true;

        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
    }

    /**
     * 当点击小睡按钮
     */
    private void onClickNapButton() {
        if (!(mNapTimesRan == mNapTimes)) {
            // 小睡
            nap();
        }
        // 执行关闭操作
        finishActivity();
    }

    /**
     * 小睡
     */
    @TargetApi(19)
    private void nap() {
        // 当小睡执行了X次
        if (mNapTimesRan == mNapTimes) {
            return;
        }
        // 小睡次数加1
        mNapTimesRan++;
        LogUtil.d(LOG_TAG, "已执行小睡次数：" + mNapTimesRan);

        // 设置小睡相关信息
        Intent intent = new Intent(getActivity(), AlarmClockBroadcast.class);
        intent.putExtra(WeacConstants.ALARM_CLOCK, mAlarmClock);
        intent.putExtra(WeacConstants.NAP_RAN_TIMES, mNapTimesRan);
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(),
                -mAlarmClock.getId(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity()
                .getSystemService(Activity.ALARM_SERVICE);
        // XXX
        // 下次响铃时间
        long nextTime = System.currentTimeMillis() + 1000 * 60 * mNapInterval;

        LogUtil.i(LOG_TAG, "小睡间隔:" + mNapInterval + "分钟");

        // 当前版本为19（4.4）或以上使用精准闹钟
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextTime, pi);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, nextTime, pi);
        }

        // 设置通知相关信息
        Intent it = new Intent(getActivity(),
                AlarmClockNapNotificationActivity.class);
        it.putExtra(WeacConstants.ALARM_CLOCK, mAlarmClock);
        // FLAG_UPDATE_CURRENT 点击通知有时不会跳转！！
        // FLAG_ONE_SHOT 清除列表只响应一个
        PendingIntent napCancel = PendingIntent.getActivity(getActivity(),
                mAlarmClock.getId(), it,
                PendingIntent.FLAG_CANCEL_CURRENT);
        // 下拉列表通知显示的时间
        CharSequence time = new SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(nextTime);

        // 通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
        // 设置PendingIntent
        Notification notification = builder.setContentIntent(napCancel)
                // 当清除下拉列表触发
                .setDeleteIntent(napCancel)
                // 设置下拉列表标题
                .setContentTitle(
                        String.format(getString(R.string.xx_naping),
                                mAlarmClock.getTag()))
                // 设置下拉列表显示内容
                .setContentText(String.format(getString(R.string.nap_to), time))
                // 设置状态栏显示的信息
                .setTicker(
                        String.format(getString(R.string.nap_time),
                                mNapInterval))
                // 设置状态栏（小图标）
                .setSmallIcon(R.drawable.ic_nap_notification)
                // 设置下拉列表（大图标）
                .setLargeIcon(
                        BitmapFactory.decodeResource(getResources(),
                                R.drawable.ic_launcher)).setAutoCancel(true)
                // 默认呼吸灯
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS | NotificationCompat.FLAG_SHOW_LIGHTS)
                .build();
/*        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;*/

        // 下拉列表显示小睡信息
        mNotificationManager.notify(mAlarmClock.getId(), notification);
    }

    /**
     * 播放铃声
     */
    private void playRing() {
        mAudioManager = (AudioManager) getActivity().getSystemService(
                Context.AUDIO_SERVICE);

        mCurrentVolume = mAudioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        // 设置铃声音量
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                mAlarmClock.getVolume(), AudioManager.ADJUST_SAME);

        // 默认铃声
        if (mAlarmClock.getRingUrl().equals(WeacConstants.DEFAULT_RING_URL)
                || TextUtils.isEmpty(mAlarmClock.getRingUrl())) {
            // 振动模式
            if (mAlarmClock.isVibrate()) {
                // 播放
                AudioPlayer.getInstance(getActivity()).playRaw(
                        R.raw.ring_weac_alarm_clock_default, true, true);
            } else {
                AudioPlayer.getInstance(getActivity()).playRaw(
                        R.raw.ring_weac_alarm_clock_default, true, false);
            }

            // 无铃声
        } else if (mAlarmClock.getRingUrl().equals(WeacConstants.NO_RING_URL)) {
            // 振动模式
            if (mAlarmClock.isVibrate()) {
                AudioPlayer.getInstance(getActivity()).stop();
                AudioPlayer.getInstance(getActivity()).vibrate();
            } else {
                AudioPlayer.getInstance(getActivity()).stop();
            }
        } else {
            // 振动模式
            if (mAlarmClock.isVibrate()) {
                AudioPlayer.getInstance(getActivity()).play(
                        mAlarmClock.getRingUrl(), true, true);
            } else {
                AudioPlayer.getInstance(getActivity()).play(
                        mAlarmClock.getRingUrl(), true, false);
            }
        }
    }

    /**
     * 显示时间的线程类
     */
    private class TimeUpdateThread implements Runnable {
        /**
         * 闹钟响铃时间
         */
        private int startedTime = 0;

        /**
         * 3分钟
         */
        private static final int TIME = 60 * 3;

        @Override
        public void run() {
            // Activity没有结束
            while (mIsRun) {
                LogUtil.d(LOG_TAG, "TimeUpdateThread(已启动时间): " + startedTime);

                try {
                    // 响铃XX分钟并且当前Activity没有被销毁进入小睡
                    if (startedTime == TIME) {
                        // 小睡开启状态
                        if (mAlarmClock.isNap()) {
                            if (!getActivity().isFinishing()) {
                                onClickNapButton();
                                return;
                            } else {
                                LogUtil.w(LOG_TAG,
                                        "准备进行自动小睡处理时，闹钟已经为Finishing状态");
                                return;
                            }
                        } else {
                            // 执行关闭操作
                            finishActivity();
                        }
                    }
                    Thread.sleep(1000);
                    startedTime++;
                    // 界面显示的时间
                    CharSequence currentTime = new SimpleDateFormat("HH:mm",
                            Locale.getDefault()).format(System
                            .currentTimeMillis());
                    if (mCurrentTimeDisplay.equals(currentTime)) {
                        continue;
                    }

                    Message msg = mShowTimeHandler.obtainMessage(UPDATE_TIME,
                            currentTime);
                    // 发送消息
                    mShowTimeHandler.sendMessage(msg);
                } catch (InterruptedException | NullPointerException e) {
                    LogUtil.e(LOG_TAG, "run方法出现错误：" + e.toString());
                }
            }

        }
    }

}
