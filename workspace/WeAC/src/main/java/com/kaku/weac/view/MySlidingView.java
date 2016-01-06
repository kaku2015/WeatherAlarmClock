/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * 滑动关闭闹钟
 *
 * @author 咖枯
 * @version 1.0 2015/01/05
 */
public class MySlidingView extends RelativeLayout {

    private Context mContext;
    private Scroller mScroller;

    /**
     * 屏幕宽度
     */
    private int mScreenWidth = 0;

    /**
     * 按下时X轴的位置
     */
    private int mLastDownX = 0;

    /**
     * 按下时X轴的位置
     */
    private float mTouchDownX = 0;

    /**
     * 标识是否解锁
     */
    private boolean mCloseFlag = false;

    private SlidingTipListener mSlidingTipListener;

    public void setSlidingTipListener(SlidingTipListener slidingTipListener) {
        mSlidingTipListener = slidingTipListener;
    }

    public MySlidingView(Context context) {
        super(context);
    }

    public MySlidingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MySlidingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mScroller = new Scroller(mContext);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                mTouchDownX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                intercepted = Math.abs(event.getX() - mTouchDownX) >= ViewConfiguration.get(
                        getContext()).getScaledTouchSlop();
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
        }
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastDownX = (int) event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                // 当前移动的x轴位置
                int currX = (int) event.getX();
                int deltaX = currX - mLastDownX;
//                int deltaX = x - mLastDownX;
                // 右滑有效
                if (deltaX > 0) {
                    scrollTo(-deltaX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                currX = (int) event.getX();
                deltaX = currX - mLastDownX;
                if (deltaX > 0) {
                    // 当滑动距离超过半屏时解锁
                    if (Math.abs(deltaX) > mScreenWidth / 2) {
                        smoothScrollTo(getScrollX(), -mScreenWidth, 500);
                        mCloseFlag = true;

                    } else { // 向右滑动未超过半个屏幕宽的时候 开启向左弹动动画
                        smoothScrollTo(getScrollX(), -getScrollX(), 500);
                    }
                } else {// 回滚到原位置
                    smoothScrollTo(getScrollX(), -getScrollX(), 500);
                }
                break;
        }
//        mLastDownX = x;
        return super.onTouchEvent(event);
    }

    /**
     * 滚动解锁
     *
     * @param startX   起始位置的X坐标点
     * @param delX     结束时的X坐标点
     * @param duration 动画时长
     */
    private void smoothScrollTo(int startX, int delX, int duration) {
        mScroller.startScroll(startX, 0, delX, 0, duration);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            // 更新界面
            postInvalidate();
        } else if (mCloseFlag) {
            mSlidingTipListener.onScrollFinish();
        }
    }

    /**
     * 滑动解锁Listener
     */
    public interface SlidingTipListener {
        void onScrollFinish();
    }
}
