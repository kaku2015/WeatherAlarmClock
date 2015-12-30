package com.kaku.weac.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.ToastUtil;

public class TimerService extends Service {
    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("TimerService","计时时间到");
        ToastUtil.showShortToast(this, "计时时间到");
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
}
