/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.util;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaku.weac.R;

/**
 * 显示Toast提示信息
 *
 * @author 咖枯
 * @version 1.0 2015/08/08
 */
public class ToastUtil {

    /**
     * Log tag ：ToastUtil
     */
    private static final String LOG_TAG = "ToastUtil";

    private static Toast sToast;

    private static final Handler sHandler = new Handler();

    private static final Runnable sRun = new Runnable() {
        public void run() {
            try {
                sToast.cancel();
                // toast隐藏后，将其置为null
                sToast = null;
            } catch (Exception e) {
                LogUtil.e(LOG_TAG, "run方法出现错误：" + e.toString());
            }
        }
    };

    /**
     * 短时间显示Toast
     *
     * @param context context
     * @param msg     需要显示的信息
     */
    public static void showShortToast(Context context, String msg) {
        showToast(context.getApplicationContext(), msg, Toast.LENGTH_SHORT, 1500);

    }

    /**
     * 长时间显示Toast
     *
     * @param context context
     * @param msg     需要显示的信息
     */
    public static void showLongToast(Context context, String msg) {
        showToast(context.getApplicationContext(), msg, Toast.LENGTH_LONG, 3000);

    }

    /**
     * 显示toast
     *
     * @param appContext  appContext
     * @param msg         需要显示的信息
     * @param duration    显示时间
     * @param delayMillis 延迟关闭时间
     */
    private static void showToast(Context appContext, String msg, int duration, int delayMillis) {
        LayoutInflater inflater = (LayoutInflater) appContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast, new LinearLayout(appContext),
                false);
        TextView tv = (TextView) view.findViewById(R.id.toast_tv);
        tv.setText(msg);
        // 删除指定的Runnable对象，使线程对象停止运行。
        sHandler.removeCallbacks(sRun);
        // 只有mToast==null时才重新创建，否则只需更改提示文字
        if (sToast == null) {
            sToast = new Toast(appContext);
            sToast.setDuration(duration);
            int density = (int) appContext.getResources().getDisplayMetrics().density;
            sToast.setGravity(Gravity.CENTER, 0, 115 * density);
            sToast.setView(view);
        } else {
            sToast.setView(view);
        }
        // 延迟1.5秒隐藏toast
        sHandler.postDelayed(sRun, delayMillis);
        sToast.show();
    }
}
