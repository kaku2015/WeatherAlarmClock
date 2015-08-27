package com.kaku.weac.util;

import com.kaku.weac.R;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

	private static Handler sHandler = new Handler();

	private static Runnable sRun = new Runnable() {
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
	 * @param context
	 * @param msg
	 *            需要显示的信息
	 */
	public static void showShortToast(Context context, String msg) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.toast, new LinearLayout(context),
				false);
		TextView tv = (TextView) view.findViewById(R.id.toast_tv);
		tv.setText(msg);
		// 删除指定的Runnable对象，使线程对象停止运行。
		sHandler.removeCallbacks(sRun);
		// 只有mToast==null时才重新创建，否则只需更改提示文字
		if (sToast == null) {
			sToast = new Toast(context);
			sToast.setDuration(Toast.LENGTH_SHORT);
			sToast.setGravity(Gravity.CENTER, 0, 320);
			sToast.setView(view);
		} else {
			sToast.setView(view);
		}
		// 延迟1.5秒隐藏toast
		sHandler.postDelayed(sRun, 1500);
		sToast.show();

	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param msg
	 *            需要显示的信息
	 */
	public static void showLongToast(Context context, String msg) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.toast, new LinearLayout(context),
				false);
		TextView tv = (TextView) view.findViewById(R.id.toast_tv);
		tv.setText(msg);
		// 删除指定的Runnable对象，使线程对象停止运行。
		sHandler.removeCallbacks(sRun);
		// 只有mToast==null时才重新创建，否则只需更改提示文字
		if (sToast == null) {
			sToast = new Toast(context);
			sToast.setDuration(Toast.LENGTH_LONG);
			sToast.setGravity(Gravity.CENTER, 0, 320);
			sToast.setView(view);
		} else {
			sToast.setView(view);
		}
		// 延迟3秒隐藏toast
		sHandler.postDelayed(sRun, 3000);
		sToast.show();

	}
}
