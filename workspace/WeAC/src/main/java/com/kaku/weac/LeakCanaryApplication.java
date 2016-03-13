/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.kaku.weac.util.LogUtil;
import com.karumi.dexter.Dexter;
import com.squareup.leakcanary.RefWatcher;

import org.litepal.LitePalApplication;

/**
 * 内存泄露检测
 *
 * @author 咖枯
 * @version 1.0 2015/11/25
 */
public class LeakCanaryApplication extends LitePalApplication {
    public static RefWatcher getRefWatcher(Context context) {
        LeakCanaryApplication application = (LeakCanaryApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    /**
     * release版本使用此方法
     */
    protected RefWatcher installLeakCanary() {
        return RefWatcher.DISABLED;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Dexter.initialize(this);

//        refWatcher = LeakCanary.install(this);
        refWatcher = installLeakCanary();
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LogUtil.v("=========", activity + "  onActivityCreated");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                LogUtil.v("=========", activity + "  onActivityStarted");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                LogUtil.v("=========", activity + "  onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                LogUtil.v("=========", activity + "  onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                LogUtil.v("=========", activity + "  onActivityStopped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                LogUtil.v("=========", activity + "  onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                LogUtil.v("=========", activity + "  onActivityDestroyed");
            }
        });
    }

}
