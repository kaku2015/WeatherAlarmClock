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
package com.kaku.weac.common;

/**
 * 闹钟状态类
 *
 * @author 咖枯
 * @version 1.0 2015/07
 */
public class WeacStatus {

    /**
     * 启动的AlarmClockOnTimeActivity个数
     */
    public static int sActivityNumber = 0;

    /**
     * 上一次闹钟响起时间
     */
    public static long sLastStartTime = 0;

    /**
     * 上一次响起级别（1：闹钟，2：小睡，0：无）
     */
    public static int sStrikerLevel = 0;
}
