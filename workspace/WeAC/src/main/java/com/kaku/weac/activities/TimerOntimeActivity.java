/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.kaku.weac.R;
import com.kaku.weac.util.AudioPlayer;

/**
 * 计时时间到activity
 *
 * @author 咖枯
 * @version 1.0 2015/12/31
 */
public class TimerOnTimeActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_on_time);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 状态栏透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // 画面出现在解锁屏幕上,显示,常亮
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initViews();
    }

    private void initViews() {
        // 知道了按钮
        Button rogerBtn = (Button) findViewById(R.id.roger_btn);
        rogerBtn.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 确定按钮
            case R.id.roger_btn:
                // 停止播放
                AudioPlayer.getInstance(this).stop();
                finish();
                overridePendingTransition(0, R.anim.zoomout);
                break;
        }

    }
}
