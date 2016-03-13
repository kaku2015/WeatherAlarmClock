/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.activities;

import android.os.Bundle;

import com.kaku.weac.LeakCanaryApplication;
import com.kaku.weac.util.LogUtil;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Activity管理类
 *
 * @author 咖枯
 * @version 1.0 2015
 */
public class BaseActivity extends SwipeBackActivity {

    /**
     * Log tag ：BaseActivity
     */
    private static final String LOG_TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LogUtil.i(LOG_TAG, getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = LeakCanaryApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 友盟session的统计
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
