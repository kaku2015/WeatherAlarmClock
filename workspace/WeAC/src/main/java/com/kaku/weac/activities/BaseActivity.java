/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.kaku.weac.LeakCanaryApplication;
import com.kaku.weac.R;
import com.kaku.weac.common.TransitionModel;
import com.kaku.weac.util.LogUtil;
import com.squareup.leakcanary.RefWatcher;

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LogUtil.i(LOG_TAG, getClass().getSimpleName());
    }

    /**
     * startActivityForResult with bundle
     *
     * @param c           target class name
     * @param requestCode requestCode
     * @param bundle      bundle
     */
    protected void myStartActivityForResult(Class<?> c, int requestCode, Bundle bundle,
                                            TransitionModel transitionModel) {
        Intent intent = new Intent(this, c);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        myOverridePendingTransition(transitionModel);
    }

    private void myOverridePendingTransition(TransitionModel transitionModel) {
        switch (transitionModel) {
            case SCALE_FADE:
                overridePendingTransition(R.anim.zoomin, 0);
                break;
            case BOTTOM:
                overridePendingTransition(R.anim.move_in_bottom, 0);
                break;
            case DEFAULT:
            default:
                break;
/*            case LEFT:
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case RIGHT:
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
                break;
            case TOP:
                overridePendingTransition(R.anim.top_in, R.anim.top_out);
                break;
            case SCALE:
                overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                break;
            case FADE:
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;*/
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = LeakCanaryApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }
}
