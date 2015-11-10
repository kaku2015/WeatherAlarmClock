package com.kaku.weac.util;

import com.kaku.weac.bean.WeatherInfo;

/**
 * Http访问返回回调接口
 *
 * @author 咖枯
 * @version 1.0 2015/8/29
 */
public interface HttpCallbackListener {

    /**
     * 加载完成
     *
     * @param response http返回信息
     */
    void onFinish(String response);

    /**
     * 加载失败
     *
     * @param e 错误信息
     */
    void onError(Exception e);
}
