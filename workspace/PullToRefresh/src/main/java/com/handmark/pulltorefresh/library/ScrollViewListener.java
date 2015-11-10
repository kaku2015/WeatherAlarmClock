package com.handmark.pulltorefresh.library;


import android.widget.ScrollView;

/**
 * ScrollView滚动监听回调接口
 *
 * @author 咖枯
 * @version 1.0 2015/11/03
 */
public interface ScrollViewListener {
    /**
     * @param scrollView scrollView
     * @param x          当前滚动的水平位置
     * @param y          当前滚动的垂直位置
     * @param oldx       前一次滚动的水平位置
     * @param oldy       前一次滚动的垂直位置
     */
    void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy);
}
