/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
public class MyDialogActivitySingle extends BaseActivitySimple {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dialog_single);
        // true 扫码禁止相机的话，会无限重新显示对话框
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
        // 扫码禁止相机的话，会无限重新显示对话框
//        super.onBackPressed();
    }
}
