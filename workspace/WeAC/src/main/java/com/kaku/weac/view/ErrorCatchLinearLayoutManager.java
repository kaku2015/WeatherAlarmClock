package com.kaku.weac.view;
/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kaku.weac.util.LogUtil;

/**
 * @author 咖枯
 * @version 1.0 2016/2/14
 */
public class ErrorCatchLinearLayoutManager extends LinearLayoutManager {
    private static final String LOG_TAG = "ErrorCatchLinearLayoutManager";

    public ErrorCatchLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            LogUtil.e(LOG_TAG, "RecyclerView 错误：" + e.toString());
        }
    }
}