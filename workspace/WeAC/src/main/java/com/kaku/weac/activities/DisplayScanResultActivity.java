/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.zxing.activity.CaptureActivity;

/**
 * 显示扫描结果
 *
 * @author 咖枯
 * @version 1.0 2016/1/24
 */
public class DisplayScanResultActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_scan_result);
        LinearLayout backGround = (LinearLayout) findViewById(R.id.background);
        MyUtil.setBackground(backGround, this);
        assignViews();
    }

    private void assignViews() {
        ImageView actionBack = (ImageView) findViewById(R.id.action_back);
        actionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 显示扫描内容TextView
        TextView scanResultContentTv = (TextView) findViewById(R.id.scan_result_content_tv);
        String scanResult = getIntent().getStringExtra(CaptureActivity.SCAN_RESULT);
        scanResultContentTv.setText(scanResult);
    }
}
