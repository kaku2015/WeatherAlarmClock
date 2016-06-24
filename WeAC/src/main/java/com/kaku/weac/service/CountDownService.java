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
package com.kaku.weac.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author 咖枯
 * @version 1.1.1 2016/5/28
 */
public class CountDownService extends Service {
    private TimerTask mTimerTask;
    private TimerUpdateListener mTimerUpdateListener;

    private CountDownService.TimerBinder mTimerBinder = new TimerBinder();

    public class TimerBinder extends Binder {
        public void cancel() {
            if (mTimerTask != null) {
                mTimerTask.cancel();
                stopSelf();
            }
        }

        public void setTimerUpdateListener(TimerUpdateListener timerUpdateListener) {
            mTimerUpdateListener = timerUpdateListener;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mTimerBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startCountDown();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public interface TimerUpdateListener {
        void OnUpdateTime();
    }

    public void startCountDown() {
        mTimerTask = new TimerTask() {

            @Override
            public void run() {
                if (mTimerUpdateListener != null) {
                    mTimerUpdateListener.OnUpdateTime();
                }
            }
        };

        new Timer(true).schedule(mTimerTask, 1000, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimerTask != null) {
            mTimerTask.cancel();
        }
    }
}
