package com.handmark.pulltorefresh.library;


/**
 * ScrollView滚动监听接口
 *
 * @author 咖枯
 * @version 1.0 2015/11/03
 */
public interface ScrollViewListener {
    void onScrollChanged(PullToRefreshScrollView scrollView, int x, int y, int oldx, int oldy);
}
