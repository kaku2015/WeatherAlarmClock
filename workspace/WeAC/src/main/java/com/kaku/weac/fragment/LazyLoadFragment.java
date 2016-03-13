/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.fragment;

/**
 * 延迟加载
 *
 * @author 咖枯
 * @version 1.0 2015/10/16
 */
public abstract class LazyLoadFragment extends BaseFragment {
    /**
     * Fragment当前状态是否可见
     */
    protected boolean mIsVisible;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();
}
