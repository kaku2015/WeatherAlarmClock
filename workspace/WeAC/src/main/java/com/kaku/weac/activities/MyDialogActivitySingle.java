/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.common.WeacConstants;

/**
 * 自定义Dialog类(只有确定按钮)：需要传递三个参数，类型均为 String
 * 1，标题；2，描述；3，确定按钮文字（可以为空，默认【确定】）
 *
 * @author 咖枯
 * @version 1.0 2015/01/29
 */
public class MyDialogActivitySingle extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dialog_single);
        setFinishOnTouchOutside(false);
        // 设置Dialog全屏显示
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        initViews();
    }

    private void initViews() {
        // 确定按钮
        Button sureBtn = (Button) findViewById(R.id.dialog_sure_btn);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        Intent intent = getIntent();
        String title = intent.getStringExtra(WeacConstants.TITLE);
        String detail = intent.getStringExtra(WeacConstants.DETAIL);
        String sureText = intent.getStringExtra(WeacConstants.SURE_TEXT);

        // 标题
        TextView titleTv = (TextView) findViewById(R.id.dialog_title);
        // 详情
        TextView detailTv = (TextView) findViewById(R.id.dialog_detail);

        titleTv.setText(title);
        detailTv.setText(detail);
        if (sureText != null) {
            sureBtn.setText(sureText);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
