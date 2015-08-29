package com.kaku.weac.util;

/**
 * Http访问返回接口
 *
 * @author 咖枯
 * @version 1.0 2015/8/29
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
