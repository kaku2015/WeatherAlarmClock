/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.ToastUtil;
import com.kaku.weac.zxing.activity.CaptureActivity;

/**
 * 显示扫描结果
 *
 * @author 咖枯
 * @version 1.0 2016/1/24
 */
public class DisplayScanResultActivity extends BaseActivity implements View.OnClickListener {
    private TextView mScanResultContentTv;

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
        actionBack.setOnClickListener(this);

        mScanResultContentTv = (TextView) findViewById(R.id.scan_result_content_tv);
        String scanResult = getIntent().getStringExtra(CaptureActivity.SCAN_RESULT);
        mScanResultContentTv.setText(scanResult);

        Button copyBtn = (Button) findViewById(R.id.copy_btn);
        copyBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_back:
                finish();
                break;
            case R.id.copy_btn:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(
                        Context.CLIPBOARD_SERVICE);
                // 将文本复制到剪贴板
                clipboardManager.setPrimaryClip(ClipData.newPlainText("data",
                        mScanResultContentTv.getText().toString()));
                ToastUtil.showShortToast(this, getString(R.string.text_already_copied));
                break;
        }
    }
}
