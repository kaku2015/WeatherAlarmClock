package com.kaku.weac.Listener;
/*
 * © 2016 咖枯. All Rights Reserved.
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