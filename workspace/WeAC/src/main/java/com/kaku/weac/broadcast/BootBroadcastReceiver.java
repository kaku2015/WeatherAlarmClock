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
