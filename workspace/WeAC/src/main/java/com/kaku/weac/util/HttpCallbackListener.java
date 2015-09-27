package com.kaku.weac.util;

import com.kaku.weac.bean.WeatherInfo;

/**
 * Http访问返回接口
 *
 * @author 咖枯
 * @version 1.0 2015/8/29
 */
public interface HttpCallbackListener {
    void onFinish(WeatherInfo weatherInfo);

    void onError(Exception e);
}
