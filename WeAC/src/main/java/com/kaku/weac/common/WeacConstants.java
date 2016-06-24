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
 * 公共常量类
 *
 * @author 咖枯
 * @version 1.0 2015
 */
public class WeacConstants {

    /**
     * SharedPreferences属性信息文件
     */
    public static final String EXTRA_WEAC_SHARE = "extra_weac_shared_preferences_file";

    /**
     * 保存的壁纸key
     */
    public static final String WALLPAPER_NAME = "wallpaper_name";

    /**
     * 保存的壁纸value
     */
    public static final String DEFAULT_WALLPAPER_NAME = "wallpaper_0";

    /**
     * 保存的壁纸地址
     */
    public static final String WALLPAPER_PATH = "wallpaper_path";

    /**
     * 默认闹铃小时
     */
    public static final String DEFAULT_ALARM_HOUR = "default_alarm_hour";

    /**
     * 默认闹铃分钟
     */
    public static final String DEFAULT_ALARM_MINUTE = "default_alarm_minute";

    /**
     * 保存的闹钟铃声音量
     */
    public static final String AlARM_VOLUME = "alarm_volume";

    /**
     * 铃声名
     */
    public static final String RING_NAME = "ring_name";

    /**
     * 保存的AlarmClock单例
     */
    public static final String ALARM_CLOCK = "alarm_clock";

    /**
     * 铃声地址
     */
    public static final String RING_URL = "ring_url";

    /**
     * 铃声选择界面位置
     */
    public static final String RING_PAGER = "ring_pager_position";

    /**
     * 请求的铃声选择类型：0，闹钟；1，计时器
     */
    public static final String RING_REQUEST_TYPE = "ring_request_type";

    /**
     * 保存计时器的铃声名
     */
    public static final String RING_NAME_TIMER = "ring_name_timer";

    /**
     * 保存计时器的铃声地址
     */
    public static final String RING_URL_TIMER = "ring_url_timer";

    /**
     * 保存计时器的铃声选择界面位置
     */
    public static final String RING_PAGER_TIMER = "ring_pager_position_timer";

    /**
     * 默认铃声Url标记
     */
    public static final String DEFAULT_RING_URL = "default_ring_url";

    /**
     * 无铃声Url标记
     */

    public static final String NO_RING_URL = "no_ring_url";

    /**
     * 小睡重复次数
     */
    public static final String NAP_TIMES = "nap_times";

    /**
     * 小睡已执行次数
     */
    public static final String NAP_RAN_TIMES = "nap_ran_times";

    /**
     * 小睡间隔
     */
    public static final String NAP_INTERVAL = "nap_interval";

    /**
     * 位置
     */
    public static final String POSITION = "position";

    /**
     * 类型(1：重命名，2：删除，3：批量删除，4：详情）
     */
    public static final String TYPE = "type";

    /**
     * 新文件Url
     */
    public static final String NEW_URL = "url_new";

    /**
     * 最大录音时常10分钟
     */
    public static final int MAX_RECORD_LENGTH = 1000 * 60 * 10;

    /**
     * 标题
     */
    public static final String TITLE = "title";

    /**
     * 详情
     */
    public static final String DETAIL = "detail";

    /**
     * 取消按钮文字
     */
    public static final String CANCEL_TEXT = "cancel_text";

    /**
     * 确定按钮文字
     */
    public static final String SURE_TEXT = "sure_text";

    /**
     * 时间
     */
    public static final String TIME = "time";

    /**
     * base64编码
     */
    public static final String BASE64 = "base64";

    /**
     * 传递的城市名
     */
    public static final String CITY_NAME = "city_name";

    /**
     * 传递的天气代号
     */
    public static final String WEATHER_CODE = "weather_code";

    /**
     * 天气代号
     */
    public static final String DEFAULT_WEATHER_CODE = "default_weather_code";

    /**
     * 默认城市名
     */
    public static final String DEFAULT_CITY_NAME = "default_city_name";

    /**
     * 保存城市管理的默认城市
     */
    public static final String DEFAULT_CITY = "default_city";

    /**
     * 倒计时时间
     */
    public static final String COUNTDOWN_TIME = "countdown_time";

    /**
     * 倒计时是否为停止状态
     */
    public static final String IS_STOP = "is_stop";

    /**
     * 录音文件存放地址
     */
    public static final String RECORD_SAVE_PATH = "/WeaAlarmClock/audio/record";

    /**
     * 自定义壁纸存放地址
     */
    public static final String DIY_WALLPAPER_PATH = "/wallpaper/theme.jpg";

    /**
     * 自定义二维码logo存放地址
     */
    public static final String DIY_QRCODE_LOGO_PATH = "/qrcode/logo.jpg";

    /**
     * 自定义二维码图片存放地址
     */
    public static final String QRCODE_PATH = "/WeaAlarmClock/picture/qrcode";

    /**
     * 访问本地相册类型:0，默认；1，扫码；2，造码
     */
    public static final String REQUEST_LOCAL_ALBUM_TYPE = "request_local_album_type";

    /**
     * 图片地址
     */
    public static final String IMAGE_URL = "image_url";

    /**
     * 保存的自定义二维码的logo path
     */
    public static final String QRCODE_LOGO_PATH = "qrcode_logo_path";

    /**
     * 保存的二维码前景色
     */
    public static final String FORE_COLOR = "foreground_color";

    /**
     * 保存的二维码背景色
     */
    public static final String BACK_COLOR = "background_color";

    /**
     * 记录是否提示摇一摇恢复闹钟操作方法
     */
    public static final String SHAKE_RETRIEVE_AC = "shake_retrieve_ac";

}
