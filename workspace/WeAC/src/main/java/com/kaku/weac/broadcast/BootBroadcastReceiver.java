/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kaku.weac.service.DaemonService;
import com.kaku.weac.util.LogUtil;

/**
 * 接收手机启动广播
 *
 * @author 咖枯
 * @version 1.0 2015
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    /**
     * Log tag ：BootBroadcastReceiver
     */
    private static final String LOG_TAG = "BootBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d(LOG_TAG, "onReceive ");
        context.startService(new Intent(context, DaemonService.class));

    }

}
