/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;

/**
 * @author 咖枯
 * @version 1.0 2016/2/22
 */

public class SplashActivity extends Activity {
    private static final String LOG_TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, 0);
        setContentView(R.layout.activity_splash);
        MyUtil.setStatusBarTranslucent(this);
        assignViews();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void assignViews() {
        // 设置版本号
        setVersion();
        // 设置标语
        setSlogan();
        // 开启欢迎动画
        startAnimation();
    }

    private void startAnimation() {
        View splashIv = findViewById(R.id.splash_iv);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash);
        splashIv.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                MyUtil.startActivity(SplashActivity.this, MainActivity.class);
                overridePendingTransition(0, 0);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setSlogan() {
        try {
            AssetManager mgr = getAssets();
            Typeface fontFace = Typeface.createFromAsset(mgr, "fonts/weac_slogan.ttf");
            TextView SloganTv = (TextView) findViewById(R.id.weac_slogan_tv);
            SloganTv.setTypeface(fontFace);
        } catch (Exception e) {
            LogUtil.e(LOG_TAG, "Typeface.createFromAsset: " + e.toString());
        }
    }

    private void setVersion() {
        TextView versionTv = (TextView) findViewById(R.id.version_tv);
        versionTv.setText(getString(R.string.weac_version, MyUtil.getVersion(this)));
    }
}
