package com.kaku.weac.activities;

import android.app.Activity;
import android.os.Bundle;

import com.kaku.weac.util.LogUtil;

//TODO 未使用

/**
 * Activity管理类
 *
 * @author 咖枯
 * @version 1.0 2015
 */
public class BaseActivity extends Activity {

    /**
     * Log tag ：BaseActivity
     */
    private static final String LOG_TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(LOG_TAG, getClass().getSimpleName());
    }
}
