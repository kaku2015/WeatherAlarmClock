package com.kaku.weac.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.kaku.weac.R;
import com.kaku.weac.activities.TimerOnTimeActivity;
import com.kaku.weac.bean.AlarmClock;
import com.kaku.weac.broadcast.AlarmClockBroadcast;
import com.kaku.weac.common.WeacConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author 咖枯
 * @version 1.0 2015/07
 */
public class MyUtil {

    /**
     * Log tag ：MyUtil
     */
    private static final String LOG_TAG = "MyUtil";

    /**
     * 保存壁纸信息
     *
     * @param context  context
     * @param saveType 保存类型：WeacConstants.WALLPAPER_NAME;WeacConstants.WALLPAPER_PATH
     * @param value    value
     */
    public static void saveWallpaper(Context context, String saveType, String value) {
        SharedPreferences share = context.getSharedPreferences(
                WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = share.edit();
        switch (saveType) {
            case WeacConstants.WALLPAPER_NAME:
                edit.putString(WeacConstants.WALLPAPER_PATH, null);
                break;
            case WeacConstants.WALLPAPER_PATH:
                edit.putString(WeacConstants.WALLPAPER_NAME, null);
                break;
        }
        edit.putString(saveType, value);
        edit.apply();
    }

    /**
     * 设置壁纸
     *
     * @param vg       viewGroup
     * @param activity activity
     */
    public static void setBackground(ViewGroup vg, Activity activity) {
        // 取得主题背景配置信息
        SharedPreferences share = activity.getSharedPreferences(WeacConstants.EXTRA_WEAC_SHARE,
                Activity.MODE_PRIVATE);
        String value = share.getString(WeacConstants.WALLPAPER_PATH, null);
        // 默认壁纸为自定义
        if (value != null) {
            // 自定义壁纸
            Drawable drawable1 = Drawable.createFromPath(value);
            // 文件没有被删除
            if (drawable1 != null) {
                vg.setBackground(drawable1);
            } else {
                saveWallpaper(activity, WeacConstants.WALLPAPER_NAME, activity.getString(R
                        .string.default_wallpaper_name));
                setWallpaper(vg, activity, share);
            }
        } else {
            setWallpaper(vg, activity, share);

        }
        setStatusBarTranslucent(vg, activity);
    }

    public static void setStatusBarTranslucent(ViewGroup vg, Activity activity) {
        // 如果版本在4.4以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 状态栏高度
            int height = getStatusBarHeight(activity);
            if (height <= 0) {
                return;
            }
            // 设置距离顶部状态栏垂直距离
            vg.setPadding(0, height, 0, 0);
            // 状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 导航栏透明
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public static int getStatusBarHeight(Activity activity) {
        int height = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            height = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    private static void setWallpaper(ViewGroup vg, Activity activity, SharedPreferences share) {
        int resId = getResId(activity, share);
        vg.setBackgroundResource(resId);
    }

    private static int getResId(Context context, SharedPreferences share) {
        String value = share.getString(WeacConstants.WALLPAPER_NAME, context.getString(R
                .string.default_wallpaper_name));
//        int resId = context.getApplicationContext().getResources().getIdentifier(
//                value, "drawable", context.getPackageName());

        Class drawable = R.drawable.class;
        int resId;
        try {
            Field field = drawable.getField(value);
            resId = field.getInt(field.getName());
        } catch (Exception e) {
            resId = R.drawable.wallpaper_0;
            LogUtil.e(LOG_TAG, "setWallPaper(Context context): " + e.toString());
        }
        return resId;
    }

    /**
     * 设置模糊壁纸
     *
     * @param vg       viewGroup
     * @param activity activity
     */
    public static void setBackgroundBlur(ViewGroup vg, Activity activity) {
        vg.setBackground(getWallPaperBlurDrawable(activity));
        setStatusBarTranslucent(vg, activity);
    }

    /**
     * 取得模糊处理后的壁纸资源Drawable
     *
     * @param context context
     * @return 壁纸资源 Drawable
     */
    public static Drawable getWallPaperBlurDrawable(Context context) {
        Bitmap bitmap;

        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        // 取得主题背景配置信息
        SharedPreferences share = context.getSharedPreferences(WeacConstants.EXTRA_WEAC_SHARE,
                Activity.MODE_PRIVATE);
        String value = share.getString(WeacConstants.WALLPAPER_PATH, null);
        // 默认壁纸为自定义
        if (value != null) {
            try {
                BitmapFactory.decodeStream(new FileInputStream(new File(value)), null, options);
                // 设置图片模糊度为20
                options.inSampleSize = 20;
                options.inJustDecodeBounds = false;
                // 使用设置的inSampleSize值再次解析图片
                bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(value)),
                        null, options);
                bitmap = fastBlur(context, 0, value, bitmap, 20);
            } catch (FileNotFoundException e) {
                LogUtil.e(LOG_TAG, "getWallPaperBlurDrawable(Context context): " + e.toString());
                bitmap = setWallpaperBlur(context, options, share);
            }
        } else {
            bitmap = setWallpaperBlur(context, options, share);
        }

        // 返回经过毛玻璃模糊度20处理后的Bitmap
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    private static Bitmap setWallpaperBlur(Context context, BitmapFactory.Options options, SharedPreferences share) {
        int resId = getResId(context, share);
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        // 设置图片模糊度为20
        options.inSampleSize = 20;
        options.inJustDecodeBounds = false;
        // 使用设置的inSampleSize值再次解析图片
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        bitmap = fastBlur(context, resId, null, bitmap, 20);
        return bitmap;
    }

    /**
     * 毛玻璃效果
     *
     * @param context    context
     * @param resId      图片资源id
     * @param filePath   自定义壁纸path
     * @param sentBitmap bitmap
     * @param radius     模糊半径：指每个像素点周围模糊的半径，半径越大，
     *                   模糊程度约高，模糊效果越明显，同时，模糊计算耗费时间越长。
     * @return 模糊处理后的bitmap
     */
    private static Bitmap fastBlur(Context context, int resId, String filePath, Bitmap sentBitmap,
                                   int radius) {
        try {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            if (radius < 1) {
                return (null);
            }

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            int[] pix = new int[w * h];
//        Log.e("pix", w + " " + h + " " + pix.length);
            bitmap.getPixels(pix, 0, w, 0, 0, w, h);

            int wm = w - 1;
            int hm = h - 1;
            int wh = w * h;
            int div = radius + radius + 1;

            int r[] = new int[wh];
            int g[] = new int[wh];
            int b[] = new int[wh];
            int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
            int vmin[] = new int[Math.max(w, h)];

            int divsum = (div + 1) >> 1;
            divsum *= divsum;
            int temp = 256 * divsum;
            int dv[] = new int[temp];
            for (i = 0; i < temp; i++) {
                dv[i] = (i / divsum);
            }

            yw = yi = 0;

            int[][] stack = new int[div][3];
            int stackpointer;
            int stackstart;
            int[] sir;
            int rbs;
            int r1 = radius + 1;
            int routsum, goutsum, boutsum;
            int rinsum, ginsum, binsum;

            for (y = 0; y < h; y++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                for (i = -radius; i <= radius; i++) {
                    p = pix[yi + Math.min(wm, Math.max(i, 0))];
                    sir = stack[i + radius];
                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);
                    rbs = r1 - Math.abs(i);
                    rsum += sir[0] * rbs;
                    gsum += sir[1] * rbs;
                    bsum += sir[2] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                }
                stackpointer = radius;

                for (x = 0; x < w; x++) {

                    r[yi] = dv[rsum];
                    g[yi] = dv[gsum];
                    b[yi] = dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (y == 0) {
                        vmin[x] = Math.min(x + radius + 1, wm);
                    }
                    p = pix[yw + vmin[x]];

                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[(stackpointer) % div];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi++;
                }
                yw += w;
            }
            for (x = 0; x < w; x++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                yp = -radius * w;
                for (i = -radius; i <= radius; i++) {
                    yi = Math.max(0, yp) + x;

                    sir = stack[i + radius];

                    sir[0] = r[yi];
                    sir[1] = g[yi];
                    sir[2] = b[yi];

                    rbs = r1 - Math.abs(i);

                    rsum += r[yi] * rbs;
                    gsum += g[yi] * rbs;
                    bsum += b[yi] * rbs;

                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }

                    if (i < hm) {
                        yp += w;
                    }
                }
                yi = x;
                stackpointer = radius;
                for (y = 0; y < h; y++) {
                    // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                    pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) |
                            dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (x == 0) {
                        vmin[y] = Math.min(y + r1, hm) * w;
                    }
                    p = x + vmin[y];

                    sir[0] = r[p];
                    sir[1] = g[p];
                    sir[2] = b[p];

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi += w;
                }
            }

//        Log.e("pix", w + " " + h + " " + pix.length);
            bitmap.setPixels(pix, 0, w, 0, 0, w, h);
            return (bitmap);
        } catch (Exception e) {
            LogUtil.e("MyUtil", e.toString());
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                return blurBitmap(context, sentBitmap, radius);
            } else {
                // TODO : 天气模糊背景返回透明色
                if (filePath == null) {
                    return BitmapFactory.decodeResource(context.getResources(), resId);
                } else {
                    return BitmapFactory.decodeFile(filePath);
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static Bitmap blurBitmap(Context context, Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        final RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation
                        .MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius /* e.g. 3.f */);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);
        return bitmap;
    }

    /**
     * 开启闹钟
     *
     * @param context    context
     * @param alarmClock 闹钟实例
     */
    @TargetApi(19)
    public static void startAlarmClock(Context context, AlarmClock alarmClock) {
        Intent intent = new Intent(context, AlarmClockBroadcast.class);
        intent.putExtra(WeacConstants.ALARM_CLOCK, alarmClock);
        // FLAG_UPDATE_CURRENT：如果PendingIntent已经存在，保留它并且只替换它的extra数据。
        // FLAG_CANCEL_CURRENT：如果PendingIntent已经存在，那么当前的PendingIntent会取消掉，然后产生一个新的PendingIntent。
        PendingIntent pi = PendingIntent.getBroadcast(context,
                alarmClock.getAlarmClockCode(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        // 取得下次响铃时间
        long nextTime = calculateNextTime(alarmClock.getHour(),
                alarmClock.getMinute(), alarmClock.getWeeks());
        // 设置闹钟
        // 当前版本为19（4.4）或以上使用精准闹钟
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextTime, pi);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, nextTime, pi);
        }

    }

    /**
     * 取消闹钟
     *
     * @param context        context
     * @param alarmClockCode 闹钟启动code
     */
    public static void cancelAlarmClock(Context context, int alarmClockCode) {
        Intent intent = new Intent(context, AlarmClockBroadcast.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, alarmClockCode,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context
                .getSystemService(Activity.ALARM_SERVICE);
        am.cancel(pi);
    }

    /**
     * 开启倒计时
     *
     * @param context    context
     * @param timeRemain 剩余时间
     */
    public static void startAlarmTimer(Context context, long timeRemain) {
        Intent intent = new Intent(context, TimerOnTimeActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context,
                1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        long countdownTime = timeRemain + SystemClock.elapsedRealtime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, countdownTime, pi);
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, countdownTime, pi);
        }
    }

    /**
     * 取得下次响铃时间
     *
     * @param hour   小时
     * @param minute 分钟
     * @param weeks  周
     * @return 下次响铃时间
     */
    public static long calculateNextTime(int hour, int minute, String weeks) {
        // 当前系统时间
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 下次响铃时间
        long nextTime = calendar.getTimeInMillis();
        // 当单次响铃时
        if (weeks == null) {
            // 当设置时间大于系统时间时
            if (nextTime > now) {
                return nextTime;
            } else {
                // 设置的时间加一天
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                nextTime = calendar.getTimeInMillis();
                return nextTime;
            }
        } else {
            nextTime = 0;
            // 临时比较用响铃时间
            long tempTime;
            // 取得响铃重复周期
            final String[] weeksValue = weeks.split(",");
            for (String aWeeksValue : weeksValue) {
                int week = Integer.parseInt(aWeeksValue);
                // 设置重复的周
                calendar.set(Calendar.DAY_OF_WEEK, week);
                tempTime = calendar.getTimeInMillis();
                // 当设置时间小于等于当前系统时间时
                if (tempTime <= now) {
                    // 设置时间加7天
                    tempTime += AlarmManager.INTERVAL_DAY * 7;
                }

                if (nextTime == 0) {
                    nextTime = tempTime;
                } else {
                    // 比较取得最小时间为下次响铃时间
                    nextTime = Math.min(tempTime, nextTime);
                }

            }

            return nextTime;
        }
    }

    /**
     * 转换文件大小
     *
     * @param fileLength file
     * @return 格式化后的大小
     */
    public static String FormatFileSize(long fileLength) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        if (fileLength < 1024) {
            fileSizeString = df.format((double) fileLength) + "B";
        } else if (fileLength < 1048576) {
            fileSizeString = df.format((double) fileLength / 1024) + "KB";
        } else if (fileLength < 1073741824) {
            fileSizeString = df.format((double) fileLength / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileLength / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /***
     * 转换文件时长
     *
     * @param ms 毫秒数
     * @return mm:ss
     */
    public static String FormatFileDuration(int ms) {
        // 单位秒
        int ss = 1000;
        // 单位分
        int mm = ss * 60;

        // 剩余分钟
        int remainMinute = ms / mm;
        // 剩余秒
        int remainSecond = (ms - remainMinute * mm) / ss;

        return addZero(remainMinute) + ":"
                + addZero(remainSecond);

    }

    /**
     * 格式化时间
     *
     * @param hour   小时
     * @param minute 分钟
     * @return 格式化后的时间:[xx:xx]
     */
    public static String formatTime(int hour, int minute) {
        return addZero(hour) + ":" + addZero(minute);
    }

    /**
     * 时间补零
     *
     * @param time 需要补零的时间
     * @return 补零后的时间
     */
    public static String addZero(int time) {
        if (String.valueOf(time).length() == 1) {
            return "0" + time;
        }

        return String.valueOf(time);
    }

    /**
     * 振动单次100毫秒
     *
     * @param context context
     */
    public static void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }


    /**
     * 去掉文件扩展名
     *
     * @param fileName 文件名
     * @return 没有扩展名的文件名
     */
    public static String removeEx(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot > -1) && (dot < fileName.length())) {
                return fileName.substring(0, dot);
            }
        }
        return fileName;
    }

    /**
     * 检查当前网络是否可用
     *
     * @param context context
     * @return 是否连接到网络
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {

                    return true;
                }
            }
        }
        return false;
    }


    private static long mLastClickTime = 0;             // 按钮最后一次点击时间
    private static final int SPACE_TIME = 500;          // 空闲时间

    /**
     * 是否连续点击按钮多次
     *
     * @return 是否快速多次点击
     */
    public static boolean isFastDoubleClick() {
        long time = SystemClock.elapsedRealtime();
        if (time - mLastClickTime <= SPACE_TIME) {
            return true;
        } else {
            mLastClickTime = time;
            return false;
        }
    }

    /**
     * 取得对应的天气类型图片id
     *
     * @param type  天气类型
     * @param isDay 是否为白天
     * @return 天气类型图片id
     */
    public static int getWeatherTypeImageID(String type, boolean isDay) {
        if (type == null) {
            return R.drawable.ic_weather_no;
        }
        int weatherId;
        switch (type) {
            case "晴":
                if (isDay) {
                    weatherId = R.drawable.ic_weather_sunny_day;
                } else {
                    weatherId = R.drawable.ic_weather_sunny_night;
                }
                break;
            case "多云":
                if (isDay) {
                    weatherId = R.drawable.ic_weather_cloudy_day;
                } else {
                    weatherId = R.drawable.ic_weather_cloudy_night;
                }
                break;
            case "阴":
                weatherId = R.drawable.ic_weather_overcast;
                break;
            case "雷阵雨":
            case "雷阵雨伴有冰雹":
                weatherId = R.drawable.ic_weather_thunder_shower;
                break;
            case "雨夹雪":
            case "冻雨":
                weatherId = R.drawable.ic_weather_sleet;
                break;
            case "小雨":
            case "小到中雨":
            case "阵雨":
                weatherId = R.drawable.ic_weather_light_rain_or_shower;
                break;
            case "中雨":
            case "中到大雨":
                weatherId = R.drawable.ic_weather_moderate_rain;
                break;
            case "大雨":
            case "大到暴雨":
                weatherId = R.drawable.ic_weather_heavy_rain;
                break;
            case "暴雨":
            case "大暴雨":
            case "特大暴雨":
            case "暴雨到大暴雨":
            case "大暴雨到特大暴雨":
                weatherId = R.drawable.ic_weather_storm;
                break;
            case "阵雪":
            case "小雪":
            case "小到中雪":
                weatherId = R.drawable.ic_weather_light_snow;
                break;
            case "中雪":
            case "中到大雪":
                weatherId = R.drawable.ic_weather_moderate_snow;
                break;
            case "大雪":
            case "大到暴雪":
                weatherId = R.drawable.ic_weather_heavy_snow;
                break;
            case "暴雪":
                weatherId = R.drawable.ic_weather_snowstrom;
                break;
            case "雾":
            case "霾":
                weatherId = R.drawable.ic_weather_foggy;
                break;
            case "沙尘暴":
                weatherId = R.drawable.ic_weather_duststorm;
                break;
            case "强沙尘暴":
                weatherId = R.drawable.ic_weather_sandstorm;
                break;
            case "浮尘":
            case "扬沙":
                weatherId = R.drawable.ic_weather_sand_or_dust;
                break;
            default:
                if (type.contains("尘") || type.contains("沙")) {
                    weatherId = R.drawable.ic_weather_sand_or_dust;
                } else if (type.contains("雾") || type.contains("霾")) {
                    weatherId = R.drawable.ic_weather_foggy;
                } else if (type.contains("雨")) {
                    weatherId = R.drawable.ic_weather_sleet;
                } else if (type.contains("雪") || type.contains("冰雹")) {
                    weatherId = R.drawable.ic_weather_moderate_snow;
                } else {
                    weatherId = R.drawable.ic_weather_no;
                }
                break;
        }

        return weatherId;
    }

    /**
     * 取得天气类型描述
     *
     * @param type1 白天天气类型
     * @param type2 夜间天气类型
     * @return 天气类型
     */
    public static String getWeatherType(Context context, String type1, String type2) {
        // 白天和夜间类型相同
        if (type1.equals(type2)) {
            return type1;
        } else {
            return String.format(context.getString(R.string.turn), type1, type2);
        }
    }

    /**
     * 将地址信息转换为城市
     *
     * @param address 地址
     * @return 城市名称
     */
    public static String formatCity(String address) {
        String city = null;
        // TODO: 数据测试
        if (address.contains("自治州")) {
            if (address.contains("市")) {
                city = address.substring(address.indexOf("州") + 1, address.indexOf("市"));
            } else if (address.contains("县")) {
                city = address.substring(address.indexOf("州") + 1, address.indexOf("县"));
            } else if (address.contains("地区")) {
                city = address.substring(address.indexOf("州") + 1, address.indexOf("地区"));
            }

        } else if (address.contains("自治区")) {
            if (address.contains("地区") && address.contains("县")) {
                city = address.substring(address.indexOf("地区") + 2, address.indexOf("县"));
            } else if (address.contains("地区")) {
                city = address.substring(address.indexOf("区") + 1, address.indexOf("地区"));
            } else if (address.contains("市")) {
                city = address.substring(address.indexOf("区") + 1, address.indexOf("市"));
            } else if (address.contains("县")) {
                city = address.substring(address.indexOf("区") + 1, address.indexOf("县"));
            }

        } else if (address.contains("地区")) {
            if (address.contains("县")) {
                city = address.substring(address.indexOf("地区") + 2, address.indexOf("县"));
            }

        } else if (address.contains("香港")) {
            if (address.contains("九龙")) {
                city = "九龙";
            } else if (address.contains("新界")) {
                city = "新界";
            } else {
                city = "香港";
            }

        } else if (address.contains("澳门")) {
            if (address.contains("氹仔")) {
                city = "氹仔岛";
            } else if (address.contains("路环")) {
                city = "路环岛";
            } else {
                city = "澳门";
            }

        } else if (address.contains("台湾")) {
            if (address.contains("台北")) {
                city = "台北";
            } else if (address.contains("高雄")) {
                city = "高雄";
            } else if (address.contains("台中")) {
                city = "台中";
            }

        } else if (address.contains("省")) {
            if (address.contains("市") && address.contains("县")) {
                city = address.substring(address.lastIndexOf("市") + 1, address.indexOf("县"));
            } else if (!address.contains("市") && address.contains("县")) {
                city = address.substring(address.indexOf("省") + 1, address.indexOf("县"));
            } else if (!address.contains("市")) {
                int start = address.indexOf("市");
                int end = address.lastIndexOf("市");
                if (start == end) {
                    city = address.substring(address.indexOf("省") + 1, end);
                } else {
                    city = address.substring(start, end);
                }
            }

        } else if (address.contains("市")) {
/*            if (address.contains("区")) {
                city = address.substring(address.indexOf("市") + 1, address.indexOf("区"));
            } else if (address.contains("县")) {
                city = address.substring(address.indexOf("市") + 1, address.indexOf("县"));
            }*/
            if (address.contains("中国")) {
                city = address.substring(address.indexOf("国") + 1, address.indexOf("市"));
            } else {
                city = address.substring(0, address.indexOf("市"));
            }
        }

        return city;
    }

/*    *//**
     * 关键字高亮显示
     *
     * @param target 需要高亮的关键字
     * @param text   需要显示的文字
     * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
     *//*
    public static SpannableStringBuilder highlight(String text, String target) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span;

        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(text);
        while (m.find()) {
            span = new ForegroundColorSpan(Color.BLUE);// 需要重复！
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    // 调用
    // SpannableStringBuilder textString = TextUtilTools.highlight(item.getItemName(),
    KnowledgeActivity.searchKey);
    // vHolder.tv_itemName_search.setText(textString);

    public static boolean isLetterDigitOrChinese(String str) {
        String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]$";
        return str.matches(regex);
    }

    *//**
     * 判断字符串是否含有数字
     *//*

    public static boolean isContainNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (Character.isDigit(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    //
    public static boolean is_number(String number) {
        if (number == null) return false;
        return number.matches("[+-]?[1-9]+[0-9]*(\\.[0-9]+)?");
    }

    public static boolean is_alpha(String alpha) {
        if (alpha == null) return false;
        return alpha.matches("[a-zA-Z]+");
    }*/
/*    public static boolean isChineseChar(String str) {
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }*/

    /**
     * Returns specified directory(/mnt/sdcard/...).
     * directory will be created on SD card by defined path if card
     * is mounted. Else - Android defines files directory on device's
     * files（/data/data/<application package>/files） system.
     *
     * @param context context
     * @param path    file path (e.g.: "/AppDir/a.mp3", "/AppDir/files/images/a.jp")
     * @return File {@link File directory}
     */
    public static File getFileDirectory(Context context, String path) {
        File file = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            file = new File(Environment.getExternalStorageDirectory(), path);
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    file = null;
                }
            }
        }
        if (file == null) {
            // 使用内部缓存[MediaStore.EXTRA_OUTPUT ("output")]是无法正确写入裁切后的图片的。
            // 系统是用全局的ContentResolver来做这个过程的文件io操作，app内部的存储被忽略。（猜测）
            file = new File(context.getFilesDir(), path);
        }
        return file;
    }

    /**
     * Returns specified directory(/mnt/sdcard/Android/data/<application package>/files/...).
     * directory will be created on SD card by defined path if card
     * is mounted. Else - Android defines files directory on device's
     * files（/data/data/<application package>/files） system.
     *
     * @param context context
     * @param path    file  path (e.g.: "/music/a.mp3", "/pictures/a.jpg")
     * @return File {@link File directory}
     */
    public static File getExternalFileDirectory(Context context, String path) {
        File file = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            file = new File(context.getExternalFilesDir(null), path);
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    file = null;
                }
            }
        }
        if (file == null) {
            // 使用内部缓存[MediaStore.EXTRA_OUTPUT ("output")]是无法正确写入裁切后的图片的。
            // 系统是用全局的ContentResolver来做这个过程的文件io操作，app内部的存储被忽略。（猜测）
            file = new File(context.getFilesDir(), path);
        }
        return file;
    }

    /**
     * Returns directory absolutePath.
     *
     * @param context context
     * @param path    file path (e.g.: "/AppDir/a.mp3", "/AppDir/files/images/a.jpg")
     * @return absolutePath.
     */
    public static String getFilePath(Context context, String path) {
        return getExternalFileDirectory(context, path).getAbsolutePath();
    }

    /**
     * set intent options
     *
     * @param context  context
     * @param uri      image path uri
     * @param filePath save path (e.g.: "/AppDir/a.mp3", "/AppDir/files/images/a.jpg")
     * @return Intent
     */
    public static Intent getCropImageOptions(Context context, Uri uri, String filePath) {
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;

        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框比例
        intent.putExtra("aspectX", width);
        intent.putExtra("aspectY", height);
        // 保存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getExternalFileDirectory
                (context, filePath)));
        // 是否去除面部检测
        intent.putExtra("noFaceDetection", true);
        // 是否保留比例
        intent.putExtra("scale", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 裁剪区的宽高
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        // 是否将数据保留在Bitmap中返回
        intent.putExtra("return-data", false);
        return intent;
    }

    /**
     * 网址验证
     *
     * @param url 需要验证的内容
     */
    public static boolean checkWebSite(String url) {
        //http://www.163.com
//        String format = "^(http)\\://(\\w+\\.\\w+\\.\\w+|\\w+\\.\\w+)";
        //TODO: 正则表达式理解
        String format = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+" +
                "([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
        return startCheck(format, url);
    }

    /**
     * 网址路径（无协议部分）验证
     *
     * @param url 需要验证的路径
     */
    public static boolean checkWebSitePath(String url) {
        String format = "[\\w\\-_]+(\\.[\\w\\-_]+)+" +
                "([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
        return startCheck(format, url);
    }

    /**
     * 匹配正则表达式
     *
     * @param format 匹配格式
     * @param str    匹配内容
     * @return 是否匹配成功
     */
    private static boolean startCheck(String format, String str) {
        boolean tem;
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(str);

        tem = matcher.matches();
        return tem;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
