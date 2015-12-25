/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.kaku.weac.util.LogUtil;

public class NotificationCenter extends Service {

    /**
     * Log tag ：NotificationCenter
     */
    private static final String LOG_TAG = "NotificationCenter";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(LOG_TAG, "onCreate");
        // Notification notification = new Notification();
        // startForeground(-1, notification);

        // NotificationManager localNotificationManager = (NotificationManager)
        // getSystemService("notification");
        // Intent localIntent2 = new Intent(this, MainActivity.class);
        // localIntent2.setFlags(268435456);
        // localIntent2.setFlags(67108864);
        // PendingIntent localPendingIntent3 = PendingIntent.getActivity(this,
        // 1,
        // localIntent2, 268435456);
        // Notification localNotification = new Notification(2130837542, null,
        // 0L);
        // localNotification.setLatestEventInfo(this, "111 ", "08:12",
        // localPendingIntent3);
        // localNotification.flags = (0x2 | localNotification.flags);
        // localNotification.flags = (0x8 | localNotification.flags);
        // localNotificationManager.notify(1, localNotification);
        // localNotificationManager.cancel(1);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(LOG_TAG, "onStartCommand");
        // ActivityManager am = (ActivityManager)
        // getSystemService(ACTIVITY_SERVICE);
        // List<ActivityManager.RunningTaskInfo> taskInfo =
        // am.getRunningTasks(1);
        // if (taskInfo.size() == 0) {
        // Intent i = getPackageManager().getLaunchIntentForPackage(
        // "com.kaku.weac");
        // startActivity(i);
        // }

        // IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        // BootBroadcastReceiver receiver = new BootBroadcastReceiver();
        // registerReceiver(receiver, filter);

        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(LOG_TAG, "onDestroy");
        startService(new Intent(this, DaemonService.class));
    }

    // @Override
    // public void onTrimMemory(int level) {
    // super.onTrimMemory(level);
    // LogUtil.w(LOG_TAG, "onTrimMemory");
    //
    // // clearMemory();
    // // Process.killProcess(Process.myPid());
    // }
    //
    // @Override
    // public void onLowMemory() {
    // super.onLowMemory();
    // LogUtil.w(LOG_TAG, "onLowMemory");
    //
    // // clearMemory();
    // // Process.killProcess(Process.myPid());
    // }
    //
    // // public void clearMemory() {
    // // ActivityManager localActivityManager = (ActivityManager) this
    // // .getSystemService("activity");
    // // Iterator localIterator = localActivityManager.getRunningAppProcesses()
    // // .iterator();
    // // PackageManager localPackageManager = this.getPackageManager();
    // // ActivityManager.MemoryInfo localMemoryInfo = new
    // // ActivityManager.MemoryInfo();
    // // localActivityManager.getMemoryInfo(localMemoryInfo);
    // // long l1 = localMemoryInfo.availMem / 1048576L;
    // // long l2 = 100L;
    // // if (l2 >= getTotalMemory()) {
    // // if (getTotalMemory() <= 0L)
    // // l2 = 100L;
    // // } else
    // // LogUtil.e(LOG_TAG, "MyLowMemoryThreshold is: " + l1 + "/" + l2);
    // // while (true) {
    // // // if ((l1 >= l2) || (!localIterator.hasNext())) {
    // // // return;
    // // // // l2 = 3L * getTotalMemory() / 4L;
    // // // // break;
    // // // }
    // // ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo =
    // // (ActivityManager.RunningAppProcessInfo) localIterator
    // // .next();
    // // try {
    // // localPackageManager.getApplicationLabel(localPackageManager
    // // .getApplicationInfo(
    // // localRunningAppProcessInfo.processName, 128));
    // // String str = localRunningAppProcessInfo.processName;
    // // if (str.equals(this.getPackageName()))
    // // continue;
    // // localActivityManager.killBackgroundProcesses(str);
    // // } catch (Exception localException) {
    // // break;
    // // }
    // // }
    // // }
    // //
    // // public long getTotalMemory() {
    // // try {
    // // BufferedReader localBufferedReader = new BufferedReader(
    // // new FileReader("/proc/meminfo"), 8192);
    // // long l1 = 1024 * Integer.valueOf(
    // // localBufferedReader.readLine().split("\\s+")[1]).intValue();
    // // localBufferedReader.close();
    // // long l2 = l1 / 1048576L;
    // // return l2;
    // // } catch (IOException localIOException) {
    // // }
    // // return -1L;
    // // }
}
