package com.kaku.weac.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.IBinder;

import com.kaku.weac.R;
import com.kaku.weac.activities.TimerOnTimeActivity;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.util.AudioPlayer;
import com.kaku.weac.util.LogUtil;

public class TimerService extends Service {
    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("TimerService", "计时时间到");
        playRing();
        skipToDetailInterface();
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 跳转到详情界面
     *
     */
    private void skipToDetailInterface() {
        Intent intent = new Intent(this, TimerOnTimeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 播放铃声
     */
    private void playRing() {
        AudioManager mAudioManager = (AudioManager) getSystemService(
                Context.AUDIO_SERVICE);

        int mCurrentVolume = mAudioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        // 设置铃声音量
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                6, AudioManager.ADJUST_SAME);

        SharedPreferences shares = getSharedPreferences(
                WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
        String ringUrl = shares.getString(WeacConstants.RING_URL_TIMER, WeacConstants.DEFAULT_RING_URL);

        // 默认铃声
        switch (ringUrl) {
            case WeacConstants.DEFAULT_RING_URL:
                AudioPlayer.getInstance(this).playRaw(R.raw.ring_weac_alarm_clock_default, true, false);
                // 无铃声
                break;
            case WeacConstants.NO_RING_URL:
                AudioPlayer.getInstance(this).stop();
                break;
            default:
                AudioPlayer.getInstance(this).play(ringUrl, true, false);
                break;
        }
    }
}
