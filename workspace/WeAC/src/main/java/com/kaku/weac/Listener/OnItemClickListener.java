package com.kaku.weac.Listener;
/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */

import android.view.View;

/**
 * @author 咖枯
 * @version 1.0 2016/2/13
 */
public interface OnItemClickListener {
    void onItemClick(View view, int position);

    void onItemLongClick(View view, int position);
}