/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    private String mScanResult;

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

        TextView scanResultContentTv = (TextView) findViewById(R.id.scan_result_content_tv);
        mScanResult = getIntent().getStringExtra(CaptureActivity.SCAN_RESULT);
        int type = getIntent().getIntExtra(CaptureActivity.SCAN_TYPE, 0);
        // 二维码
        if (type == 0) {
            scanResultContentTv.setText(mScanResult);
        } else { // 条形码
            scanResultContentTv.setText(getString(R.string.bar_code, mScanResult));
        }

        Button copyBtn = (Button) findViewById(R.id.copy_btn);
        copyBtn.setOnClickListener(this);
        Button searchBtn = (Button) findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(this);
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
                clipboardManager.setPrimaryClip(ClipData.newPlainText("data", mScanResult));
                ToastUtil.showShortToast(this, getString(R.string.text_already_copied));
                break;
            case R.id.search_btn:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri uri = Uri.parse("http://www.baidu.com/s?wd=" + mScanResult);
                intent.setData(uri);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
                break;
        }
    }
}
