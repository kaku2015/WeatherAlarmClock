package com.kaku.weac.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kaku.weac.R;
import com.kaku.weac.util.MyUtil;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by pheynix on 7/12/15.
 * Sorry for my English...
 * <p/>
 * Disable hardware accelerate if you need the glow effect,See: http://developer.android.com/guide/topics/graphics/hardware-accel.html
 * Disable hardware accelerate if you need the glow effect,See: http://developer.android.com/guide/topics/graphics/hardware-accel.html
 * Disable hardware accelerate if you need the glow effect,See: http://developer.android.com/guide/topics/graphics/hardware-accel.html
 * 如果需要辉光效果，请务必关闭硬件加速，参考： http://blog.chenming.info/blog/2012/09/18/android-hardware-accel/
 * 如果需要辉光效果，请务必关闭硬件加速，参考： http://blog.chenming.info/blog/2012/09/18/android-hardware-accel/
 * 如果需要辉光效果，请务必关闭硬件加速，参考： http://blog.chenming.info/blog/2012/09/18/android-hardware-accel/
 */
public class MyTimer extends View {
    //flags
    private boolean isInitialized = false;
    private boolean isStarted = false;// =true if countdown begin //倒计时开始的时候为true
    private boolean isInDragButton;
    private int whichDragButton;//1==hour 2==minute 3==second

    //store time,calculate result when countdown pause/stop
    //保存时间，倒计时结束时计算时间差
    private Calendar timeStart;
    private Calendar timeRemain;

    //dimension and coordinate
    //尺寸和坐标
    private float viewWidth;
    private float viewHeight;
    private float circleRadiusMinute;
    private float circleRadiusDragButton;
    private float currentDegreeMinute;
    private float centerXMinute;
    private float centerYMinute;
    private float strokeWidth;
    private String displayNumberMinute;

    private float[] dragButtonMinutePosition;

    private float[] defaultDragButtonMinutePosition;

    //paint
    private Paint paintCircleBackground;
    private Paint paintDragButton;
    private Paint paintMinute;
    private Paint paintNumber;
    private Paint paintGlowEffect;

    //color
    private int colorDefault;
    private int colorMinute;

    private static final int DEFAULT_VIEW_WIDTH = 720;

    private OnTimeChangeListener timeChangeListener;
    private OnMinChangListener minChangListener;
    private OnInitialFinishListener initialFinishListener;
    //add model by Swifty default timer
    private Model model = Model.Timer;
    private boolean maxTime;


    private float mDensity;

    private Rect mRect;

    //initialize every thing
    //初始化
    private void initialize(Canvas canvas) {
        colorDefault = getResources().getColor(R.color.white_trans10);
        colorMinute = Color.WHITE;

        timeRemain = Calendar.getInstance();
        timeStart = Calendar.getInstance();
        timeStart.clear();
        timeRemain.clear();

        viewWidth = canvas.getWidth();
        viewHeight = canvas.getHeight();

        mDensity = getResources().getDisplayMetrics().density;
        //use different dimension in high resolution device
        //保证高分辨率屏幕有比较好的显示效果
        strokeWidth = 15 * mDensity;
        circleRadiusDragButton = 30 * mDensity;

        circleRadiusMinute = viewWidth / 3;
        currentDegreeMinute = 0;
        displayNumberMinute = "0";
        centerXMinute = viewWidth / 2;
        centerYMinute = viewHeight / 2;

        defaultDragButtonMinutePosition = new float[]{centerXMinute, centerYMinute - circleRadiusMinute};
        dragButtonMinutePosition = defaultDragButtonMinutePosition;

        paintCircleBackground = new Paint();
        paintDragButton = new Paint();
        paintMinute = new Paint();
        paintNumber = new Paint();
        paintGlowEffect = new Paint();

        paintCircleBackground.setColor(colorDefault);
        paintCircleBackground.setStrokeWidth(strokeWidth);
        paintCircleBackground.setStyle(Paint.Style.STROKE);
        paintCircleBackground.setAntiAlias(true);
        paintDragButton.setStrokeWidth(5);
        paintDragButton.setStyle(Paint.Style.FILL);
        paintDragButton.setAntiAlias(true);
        paintMinute.setColor(colorMinute);
        paintMinute.setStrokeWidth(strokeWidth);
        paintMinute.setStyle(Paint.Style.STROKE);
        paintMinute.setAntiAlias(true);

        paintNumber.setStrokeWidth(2);
        paintNumber.setStyle(Paint.Style.FILL);
        paintNumber.setAntiAlias(true);
        mRect = new Rect();
        paintNumber.setTextSize(60 * mDensity);
        paintNumber.setColor(colorMinute);
        paintNumber.getTextBounds("00:00", 0, "00:00".length(), mRect);


        //draw glow effect on the end of arc,glow-effect == dragButton
        //用于绘制圆弧尽头的辉光效果,辉光区域就是dragButton的区域
        paintGlowEffect.setMaskFilter(new BlurMaskFilter(2 * strokeWidth / 3, BlurMaskFilter.Blur.NORMAL));
        paintGlowEffect.setStrokeWidth(strokeWidth);
        paintGlowEffect.setAntiAlias(true);
        paintGlowEffect.setStyle(Paint.Style.FILL);

        //完成初始化回调
        if (initialFinishListener != null) {
            initialFinishListener.onInitialFinishListener();
        }
    }

    public MyTimer(Context context) {
        super(context);
    }

    //use this view in .xml file will invoke this constructor
    //在.xml中使用此控件时调用此构造函数
    public MyTimer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTimer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //initialize dimension and coordinate just for once
        //初始化尺寸，只会调用一次
        if (!isInitialized) {
            initialize(canvas);
            isInitialized = true;
        }

        //arc and number depending on degree,update before drawing
        //角度决定圆弧长度和数字，每次重绘前先更新角度
        if (isStarted) {
            updateDegree();
        }


        //draw background circle
        //画背景的圆圈
        canvas.drawCircle(centerXMinute, centerYMinute, circleRadiusMinute, paintCircleBackground);

        //draw arc
        //画弧形
        RectF rectFMinute = new RectF(centerXMinute - circleRadiusMinute, centerYMinute - circleRadiusMinute
                , centerXMinute + circleRadiusMinute, centerYMinute + circleRadiusMinute);

        canvas.drawArc(rectFMinute, -90, currentDegreeMinute, false, paintMinute);


        //draw glow effect
        //画辉光效果
        paintDragButton.setColor(colorMinute);
        canvas.drawCircle(dragButtonMinutePosition[0], dragButtonMinutePosition[1], strokeWidth / 2, paintDragButton);
        paintGlowEffect.setColor(colorMinute);
        canvas.drawCircle(dragButtonMinutePosition[0], dragButtonMinutePosition[1], strokeWidth, paintGlowEffect);

        //draw letter "H""M""S",point(0,0) of text area is on the bottom-left of this area！
        //画"H""M""S"这三个字母，文字区域的(0,0)在左下角！
//        getDisplayNumber();
//        Rect rect = new Rect();
//
//        paintNumber.setTextSize(60 * mDensity);
//        paintNumber.setColor(colorMinute);
        String minute = MyUtil.addZero(timeRemain.get(Calendar.MINUTE));
        String second = MyUtil.addZero(timeRemain.get(Calendar.SECOND));
        String remainTime = minute + ":" + second;
//        paintNumber.getTextBounds(remainTime, 0, remainTime.length(), rect);
        canvas.drawText(remainTime, centerXMinute - mRect.width() / 2, centerYMinute + mRect.height() / 2, paintNumber);
//        paintNumber.setTextSize(12 * mDensity);
//        canvas.drawText("M", centerXMinute + 16 * mDensity, centerYMinute + 8 * mDensity, paintNumber);
    }


    //handle touch event
    //
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event == null) return false;
        getParent().requestDisallowInterceptTouchEvent(true);

        switch (event.getAction()) {
            //whether touch in the drag button or not
            //判断点击是否在dragButton内
            case MotionEvent.ACTION_DOWN:
                if (circleRadiusDragButton > Math.sqrt(Math.pow(event.getX() - dragButtonMinutePosition[0], 2)
                        + Math.pow(event.getY() - dragButtonMinutePosition[1], 2))) {
                    //在dragButtonMinute中
                    isInDragButton = true;
                    whichDragButton = 2;
                } else {
                    //不在
                    isInDragButton = false;
                    whichDragButton = 0;
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;

                }
                break;

            //update coordination of dragButton
            //更新dragButton的位置
            case MotionEvent.ACTION_MOVE:
                if (!isStarted) {
                    if (isInDragButton) {
                        switch (whichDragButton) {
                            case 2:
                                currentDegreeMinute = getDegree(event.getX(), event.getY(), centerXMinute, centerYMinute);
                                updateTime(2);
                                updateDragButtonPosition(2);
                                invalidate();
                                break;
                        }
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(false);
                        return false;

                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                isInDragButton = false;
                break;
        }

        return true;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getDimension(DEFAULT_VIEW_WIDTH, widthMeasureSpec);
        int height = getDimension(width, heightMeasureSpec);

        viewWidth = width;
        viewHeight = height;

        setMeasuredDimension(width, height);
    }

    private int getDimension(int defaultDimension, int measureSpec) {

        int result;

        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.EXACTLY:
                result = MeasureSpec.getSize(measureSpec);
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(defaultDimension, MeasureSpec.getSize(measureSpec));

                break;
            default:
                result = defaultDimension;
                break;
        }
        return result;
    }


    //update degree,depend on the path user dragging on the screen
    //根据用户在屏幕划过的轨迹更新角度
    private float getDegree(float eventX, float eventY, float centerX, float centerY) {

        //    http://stackoverflow.com/questions/7926816/calculate-angle-of-touched-point-and-rotate-it-in-android
        //    Math has defeated me once again.So sad...
        //    卧槽...
        double tx = eventX - centerX;
        double ty = eventY - centerY;
        double t_length = Math.sqrt(tx * tx + ty * ty);
        double a = Math.acos(ty / t_length);
        float degree = 180 - (float) Math.toDegrees(a);

        if (centerX > eventX) {
            degree = 180 + (float) Math.toDegrees(a);
        }

        return degree;
    }

    private void getDisplayNumber() {
        if (Integer.valueOf(displayNumberMinute) != timeRemain.get(Calendar.MINUTE)) {
            displayNumberMinute = timeRemain.get(Calendar.MINUTE) + "";
            if (minChangListener != null) {
                minChangListener.onMinChange(timeRemain.get(Calendar.MINUTE));
            }
        }

    }

    //degree depending on timeRemain
    //角度由剩余时间决定
    private void updateDegree() {

        currentDegreeMinute = (float) ((timeRemain.get(Calendar.MINUTE) * 60 + timeRemain.get(Calendar.SECOND)) / (60.0 * 60)) * 360;

        updateDragButtonPosition(0);

    }

    //update drag button position(glow effect area)
    //更新拖动按钮中心点（辉光效果区域）
    private void updateDragButtonPosition(int flag) {

        switch (flag) {
            case 0:

                dragButtonMinutePosition[0] = (float) (centerXMinute + circleRadiusMinute * Math.sin(Math.toRadians(currentDegreeMinute)));
                dragButtonMinutePosition[1] = (float) (centerYMinute - circleRadiusMinute * Math.cos(Math.toRadians(currentDegreeMinute)));

                break;
            case 2:
                dragButtonMinutePosition[0] = (float) (centerXMinute + circleRadiusMinute * Math.sin(Math.toRadians(currentDegreeMinute)));
                dragButtonMinutePosition[1] = (float) (centerYMinute - circleRadiusMinute * Math.cos(Math.toRadians(currentDegreeMinute)));
                break;
        }

    }


    //get the time from currentDegree and store it in timeStart and timeRemain
    //从当前的角度获取时间，保存到timeStart和timeRemain
    private void updateTime(int flag) {

        switch (flag) {
            case 0:

                timeStart.set(Calendar.MINUTE, (int) Math.floor(60 * currentDegreeMinute / 360));
                timeRemain.set(Calendar.MINUTE, (int) Math.floor(60 * currentDegreeMinute / 360));

                timeStart.set(Calendar.SECOND, 0);
                timeRemain.set(Calendar.SECOND, 0);
                break;
            case 2:
                timeStart.set(Calendar.MINUTE, (int) Math.floor(60 * currentDegreeMinute / 360));
                timeRemain.set(Calendar.MINUTE, (int) Math.floor(60 * currentDegreeMinute / 360));

                timeStart.set(Calendar.SECOND, 0);
                timeRemain.set(Calendar.SECOND, 0);
                break;
        }

    }


    //common Timer-TimerTask-Handler countdown solution
    //常见的Timer-TimerTask-Handler倒计时模式
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                //countdown running
                //可以倒计时
                case 1:

                    timeRemain.add(Calendar.MILLISECOND, -1000);

                    if (timeChangeListener != null) {
                        timeChangeListener.onTimeChange(timeStart.getTimeInMillis(), timeRemain.getTimeInMillis());
                    }

                    invalidate();

                    break;
                //countdown stop
                //时间为空，停止倒计时，提示用户
                case 2:
                    isStarted = false;
                    timerTask.cancel();
                    break;

                //StopWatch running
                case 11:
                    timeRemain.add(Calendar.MILLISECOND, 1000);
                    if (timeChangeListener != null) {
                        timeChangeListener.onTimeChange(timeStart.getTimeInMillis(), timeRemain.getTimeInMillis());
                    }
                    invalidate();
                    break;
                //StopWatch stop
                //到达MAX TIME
                case 12:
                    isStarted = false;
                    timerTask.cancel();
                    break;
            }
        }
    };

    Timer timer = new Timer(true);
    TimerTask timerTask;


    public boolean start() {
        if (model == Model.Timer) {
            if (!isTimeEmpty() && !isStarted) {

                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (!isTimeEmpty()) {
                            Message message = new Message();
                            message.what = 1;
                            mHandler.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = 2;
                            mHandler.sendMessage(message);
                        }

                    }
                };

                timer.schedule(timerTask, 1000, 1000);
                isStarted = true;

                if (timeChangeListener != null) {
                    timeChangeListener.onTimerStart(timeStart.getTimeInMillis());
                }
            }
        } else if (model == Model.StopWatch) {
            if (!isMaxTime() && !isStarted) {
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (!isMaxTime()) {
                            Message message = new Message();
                            message.what = 11;
                            mHandler.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = 12;
                            mHandler.sendMessage(message);
                        }
                    }
                };

                timer.schedule(timerTask, 1000, 1000);
                isStarted = true;

                if (timeChangeListener != null) {
                    timeChangeListener.onTimerStart(timeStart.getTimeInMillis());
                }
            }
        }
        return isStarted;
    }

    private boolean isTimeEmpty() {
        if (timeRemain.get(Calendar.HOUR_OF_DAY) != 0
                || timeRemain.get(Calendar.MINUTE) != 0
                || timeRemain.get(Calendar.SECOND) != 0
                || timeRemain.get(Calendar.MILLISECOND) != 0) {
            return false;
        } else {
            return true;
        }
    }


    public long stop() {
        timerTask.cancel();
        isStarted = false;

        if (timeChangeListener != null) {
            timeChangeListener.onTimeStop(timeStart.getTimeInMillis(), timeRemain.getTimeInMillis());
        }

        return timeStart.getTimeInMillis() - timeRemain.getTimeInMillis();
    }


    public Calendar getTimeStart() {
        return timeStart;
    }


    public Calendar getTimeRemaid() {
        return timeRemain;
    }


    public long getTimePass() {
        return timeStart.getTimeInMillis() - timeRemain.getTimeInMillis();
    }


    public void setOnTimeChangeListener(OnTimeChangeListener listener) {
        if (listener != null) {
            timeChangeListener = listener;
        }
    }

    public void setMinChangListener(OnMinChangListener minChangListener) {
        this.minChangListener = minChangListener;
    }

    public void reset() {
        //先停止计时
        stop();
        //初始化calendar
        isInitialized = false;
        invalidate();
    }

    public boolean isMaxTime() {
        if (timeRemain.get(Calendar.HOUR_OF_DAY) == 5
                && timeRemain.get(Calendar.MINUTE) == 59
                && timeRemain.get(Calendar.SECOND) == 59) {
            return true;
        } else {
            return false;
        }
    }


    //listener
    public interface OnTimeChangeListener {
        void onTimerStart(long timeStart);

        void onTimeChange(long timeStart, long timeRemain);

        void onTimeStop(long timeStart, long timeRemain);
    }

    public interface OnMinChangListener {
        void onMinChange(int minute);
    }

    public interface OnInitialFinishListener {
        public void onInitialFinishListener();
    }

    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * set default time
     *
     * @param h max 5
     * @param m max 59
     * @param s max 59
     */
    public void setStartTime(final int h, final int m, final int s) throws NumberFormatException {
        initialFinishListener = new OnInitialFinishListener() {
            @Override
            public void onInitialFinishListener() {
                if (h > 5 || m > 59 || s > 69 || h < 0 || m < 0 | s < 0) {
                    throw new NumberFormatException("hour must in [0-5], minute and second must in [0-59]");
                }
                timeRemain.set(Calendar.HOUR_OF_DAY, h);
                timeRemain.set(Calendar.MINUTE, m);
                timeRemain.set(Calendar.SECOND, s);
                timeStart.set(Calendar.HOUR_OF_DAY, h);
                timeStart.set(Calendar.MINUTE, m);
                timeStart.set(Calendar.SECOND, s);
                updateDegree();
                invalidate();
            }
        };
    }

}
