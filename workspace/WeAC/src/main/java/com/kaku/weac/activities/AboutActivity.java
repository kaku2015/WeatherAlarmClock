/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;


/**
 * 关于Activity
 *
 * @author 咖枯
 * @version 1.0 2016/2/15
 */
public class AboutActivity extends BaseActivity {
    private static final String LOG_TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        assignViews();
    }

    private void assignViews() {
        ViewGroup background = (ViewGroup) findViewById(R.id.background);
        MyUtil.setBackground(background, this);

        ImageView actionBack = (ImageView) findViewById(R.id.action_back);
        actionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 设置版本号
        setVersion();
        // 设置标语
        setSlogan();
    }

    private void setSlogan() {
        try {
            Typeface fontFace = Typeface.createFromAsset(getAssets(), "fonts/weac_slogan.ttf");
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
