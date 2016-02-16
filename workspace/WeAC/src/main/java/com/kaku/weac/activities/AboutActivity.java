/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

        // 应用版本号
        String version;
        try {
            PackageManager packageManager = getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e(LOG_TAG, "assignViews: " + e.toString());
            version = getString(R.string.version);
        }

        TextView versionTv = (TextView) findViewById(R.id.version_tv);
        versionTv.setText(getString(R.string.weac_version, version));

        /*
                     * 必须事先在assets底下创建一fonts文件夹 并放入要使用的字体文件(.ttf)
                     * 并提供相对路径给creatFromAsset()来创建Typeface对象
                     */
/*        AssetManager mgr=getAssets();//得到AssetManager
        Typeface fontFace = Typeface.createFromAsset(mgr,"fonts/ttf.ttf");
        // 字体文件必须是true type font的格式(ttf)；
        // 当使用外部字体却又发现字体没有变化的时候(以 Droid Sans代替)，通常是因为
        // 这个字体android没有支持,而非你的程序发生了错误

        TextView text = (TextView) findViewById(R.id.words);
        text.setTypeface(fontFace);*/
    }
}
