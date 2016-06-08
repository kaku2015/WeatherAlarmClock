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

import com.kaku.weac.bean.Event.AlarmClockUpdateEvent;
import com.kaku.weac.util.OttoAppConfig;

/**
 * 单次闹钟响起，通过此BroadcastReceiver来实现多进程通信，更新闹钟开关
 *
 * @author 咖枯
 * @version 1.0 2016/2/13
 */
public class AlarmClockProcessReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        OttoAppConfig.getInstance().post(new AlarmClockUpdateEvent());
    }
}
