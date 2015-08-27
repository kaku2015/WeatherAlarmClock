package com.kaku.weac.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

//TODO 未使用
/**
 * 活动管理器类
 * 
 * @author 咖枯
 * @version 1.0 2015/07/22
 */
public class ActivityCollector {
	/**
	 * 保存Activity实例的List
	 */
	public static List<Activity> sActivities = new ArrayList<Activity>();

	/**
	 * 添加Activity
	 * 
	 * @param activity
	 */
	public static void addActivity(Activity activity) {
		sActivities.add(activity);
	}

	/**
	 * 移除Activity
	 * 
	 * @param activity
	 */
	public static void removeActivity(Activity activity) {
		sActivities.remove(activity);
	}

	/**
	 * 退出程序
	 */
	public static void finishAll() {
		for (Activity activity : sActivities) {
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
	}
}