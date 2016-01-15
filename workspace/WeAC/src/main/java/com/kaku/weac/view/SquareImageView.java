/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 高与宽相等的正方形ImageView
 *
 * @author 咖枯
 * @version 1.0 2016/1/15
 */
public class SquareImageView extends ImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
