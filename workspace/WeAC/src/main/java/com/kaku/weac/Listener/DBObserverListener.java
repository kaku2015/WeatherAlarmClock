package com.kaku.weac.Listener;

/**
 * DB数据观察者接口
 *
 * @author 咖枯
 * @version 1.0 2015/11/19
 */
public interface DBObserverListener {
    /**
     * db数据有更新
     */
    void onDBDataChanged();
}
