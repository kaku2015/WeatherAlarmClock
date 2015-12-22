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
 * 计时器
 *
 * @author 咖枯
 * @version 1.0 2015/12/22
 */
public class MyTimer extends View {

    /**
     * Log tag ：MyTimer
     */
    private static final String LOG_TAG = "MyTimer";

    /**
     * 受否已经初始化
     */
    private boolean mIsInitialized = false;

    /**
     * 受否倒计时已经开始
     */
    private boolean mIsStarted = false;

    /**
     * 是否拖动按钮获取事件
     */
    private boolean mIsInDragButton;

    /**
     * 设置的倒计时时间
     */
    private Calendar mTimeStart;

    /**
     * 倒计时剩余时间
     */
    private Calendar mTimeRemain;

    /**
     * 控件宽
     */
    private float mViewWidth;

    /**
     * 控件高
     */
    private float mViewHeight;

    /**
     * 表盘半径
     */
    private float mCircleRadiusWatcher;

    /**
     * 拖动按钮触摸半径
     */
    private float mCircleRadiusDragButtonTouch;

    /**
     * 当前角度
     */
    private float mCurrentDegree;

    /**
     * 表盘中心x坐标
     */
    private float mCenterX;

    /**
     * 表盘中心y坐标
     */
    private float mCenterY;

    /**
     * 表盘外圈宽度
     */
    private float mStrokeWidth;

    /**
     * 显示的剩余时间
     */
    private String mDisplayRemainTime;

    /**
     * 拖动按钮位置
     */
    private float[] mDragButtonPosition;

    /**
     * 表盘背景画笔
     */
    private Paint mPaintCircleBackground;

    /**
     * 拖动按钮画笔
     */
    private Paint mPaintDragButton;

    /**
     * 弧形画笔
     */
    private Paint mPaintArc;

    /**
     * 显示剩余时间画笔
     */
    private Paint mPaintRemainTime;

    /**
     * 辉光效果画笔
     */
    private Paint mPaintGlowEffect;

    /**
     * 剩余时间画笔
     */
    private int mColorRemainTime;

    /**
     * 控件默认宽度
     */
    private static final int DEFAULT_VIEW_WIDTH = 720;

    /**
     * 剩余时间变化回调
     */
    private OnTimeChangeListener mRemainTimeChangeListener;

    /**
     * 选择时间变化回调
     */
    private OnMinChangListener mTimeChangListener;


    /**
     * 初始化完成回调
     */
    private OnInitialFinishListener mInitialFinishListener;

    /**
     * 功能模式：默认为计时
     */
    private Model mModel = Model.Timer;

    /**
     * 矩形范围
     */
    private Rect mRect;

    private int mRemainMinute = 0;

    public MyTimer(Context context) {
        super(context);
    }

    public MyTimer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTimer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //初始化
        if (!mIsInitialized) {
            initialize(canvas);
            mIsInitialized = true;
        }

        //角度决定圆弧长度和数字，每次重绘前先更新角度
        if (mIsStarted) {
            updateDegree();
        }


        //draw background circle
        //画背景的圆圈
        canvas.drawCircle(mCenterX, mCenterY, mCircleRadiusWatcher, mPaintCircleBackground);

        //draw arc
        //画弧形
        RectF rectFMinute = new RectF(mCenterX - mCircleRadiusWatcher, mCenterY - mCircleRadiusWatcher
                , mCenterX + mCircleRadiusWatcher, mCenterY + mCircleRadiusWatcher);

        canvas.drawArc(rectFMinute, -90, mCurrentDegree, false, mPaintArc);


        //draw glow effect
        //画辉光效果
        mPaintDragButton.setColor(mColorRemainTime);
        canvas.drawCircle(mDragButtonPosition[0], mDragButtonPosition[1], mStrokeWidth / 2, mPaintDragButton);
        mPaintGlowEffect.setColor(mColorRemainTime);
        canvas.drawCircle(mDragButtonPosition[0], mDragButtonPosition[1], mStrokeWidth, mPaintGlowEffect);

        //draw letter "H""M""S",point(0,0) of text area is on the bottom-left of this area！
        //画"H""M""S"这三个字母，文字区域的(0,0)在左下角！
//        getDisplayNumber();
//        Rect rect = new Rect();
//
//        mPaintRemainTime.setTextSize(60 * mDensity);
//        mPaintRemainTime.setColor(mColorRemainTime);
        getDisplayNumber();
//        mPaintRemainTime.getTextBounds(remainTime, 0, remainTime.length(), rect);
        canvas.drawText(mDisplayRemainTime, mCenterX - mRect.width() / 2,
                mCenterY + mRect.height() / 2, mPaintRemainTime);
//        mPaintRemainTime.setTextSize(12 * mDensity);
//        canvas.drawText("M", mCenterX + 16 * mDensity, mCenterY + 8 * mDensity, mPaintRemainTime);
    }


    /**
     * 初始化
     *
     * @param canvas canvas
     */
    private void initialize(Canvas canvas) {
        mColorRemainTime = Color.WHITE;

        mTimeRemain = Calendar.getInstance();
        mTimeStart = Calendar.getInstance();
        mTimeStart.clear();
        mTimeRemain.clear();

        mViewWidth = canvas.getWidth();
        mViewHeight = canvas.getHeight();

        float density = getResources().getDisplayMetrics().density;
        mStrokeWidth = 12 * density;
        mCircleRadiusDragButtonTouch = 30 * density;

        mCircleRadiusWatcher = mViewWidth / 3;
        mCurrentDegree = 0;
        mCenterX = mViewWidth / 2;
        mCenterY = mViewHeight / 2;

        // 默认拖动按钮位置
        mDragButtonPosition = new float[]{mCenterX, mCenterY - mCircleRadiusWatcher};

        mPaintCircleBackground = new Paint();
        mPaintDragButton = new Paint();
        mPaintArc = new Paint();
        mPaintRemainTime = new Paint();
        mPaintGlowEffect = new Paint();

        // 表盘外圈颜色
        int colorWatcher = getResources().getColor(R.color.white_trans10);
        mPaintCircleBackground.setColor(colorWatcher);
        mPaintCircleBackground.setStrokeWidth(mStrokeWidth);
        mPaintCircleBackground.setStyle(Paint.Style.STROKE);
        mPaintCircleBackground.setAntiAlias(true);
        mPaintDragButton.setStyle(Paint.Style.FILL);
        mPaintDragButton.setAntiAlias(true);
        mPaintArc.setColor(mColorRemainTime);
        mPaintArc.setStrokeWidth(mStrokeWidth);
        mPaintArc.setStyle(Paint.Style.STROKE);
        mPaintArc.setAntiAlias(true);

        mPaintRemainTime.setStyle(Paint.Style.FILL);
        mPaintRemainTime.setAntiAlias(true);
        mRect = new Rect();
        float densityText = getResources().getDisplayMetrics().scaledDensity;
        mPaintRemainTime.setTextSize(60 * densityText);
        mPaintRemainTime.setColor(mColorRemainTime);
        mPaintRemainTime.setAntiAlias(true);
        mPaintRemainTime.getTextBounds("00:00", 0, "00:00".length(), mRect);

        //用于绘制圆弧尽头的辉光效果,辉光区域就是dragButton的区域
        mPaintGlowEffect.setMaskFilter(new BlurMaskFilter(2 * mStrokeWidth / 3, BlurMaskFilter.Blur.NORMAL));
        mPaintGlowEffect.setAntiAlias(true);
        mPaintGlowEffect.setStyle(Paint.Style.FILL);

        //完成初始化回调
        if (mInitialFinishListener != null) {
            mInitialFinishListener.onInitialFinishListener();
        }
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
                if (mCircleRadiusDragButtonTouch > Math.sqrt(Math.pow(event.getX() - mDragButtonPosition[0], 2)
                        + Math.pow(event.getY() - mDragButtonPosition[1], 2))) {
                    //在dragButtonMinute中
                    mIsInDragButton = true;
                } else {
                    //不在
                    mIsInDragButton = false;
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;

                }
                break;

            //update coordination of dragButton
            //更新dragButton的位置
            case MotionEvent.ACTION_MOVE:
                if (!mIsStarted) {
                    if (mIsInDragButton) {
                        mCurrentDegree = getDegree(event.getX(), event.getY(), mCenterX, mCenterY);
                        updateTime(2);
                        updateDragButtonPosition();
                        invalidate();

                        if (mRemainMinute != mTimeRemain.get(Calendar.MINUTE)) {
                            mRemainMinute = mTimeRemain.get(Calendar.MINUTE);
                            if (mTimeChangListener != null) {
                                mTimeChangListener.onMinChange(mRemainMinute);
                            }
                        }
                        break;
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(false);
                        return false;

                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                mIsInDragButton = false;
                break;
        }

        return true;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getDimension(DEFAULT_VIEW_WIDTH, widthMeasureSpec);
        int height = getDimension(width, heightMeasureSpec);

        mViewWidth = width;
        mViewHeight = height;

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

    /**
     * 根据用户在屏幕划过的轨迹更新角度
     *
     * @param eventX  eventX
     * @param eventY  eventY
     * @param centerX centerX
     * @param centerY centerY
     * @return 角度
     */
    private float getDegree(float eventX, float eventY, float centerX, float centerY) {

        //    http://stackoverflow.com/questions/7926816/calculate-angle-of-touched-point-and-rotate-it-in-android
        // x轴边
        double tx = eventX - centerX;
        // y轴边
        double ty = eventY - centerY;
        // 开正平方根,求出滑动点到圆心的距离（斜边）
        double t_length = Math.sqrt(tx * tx + ty * ty);
        // 根据反余弦求出弧度
        double radians = Math.acos(ty / t_length);
        // y的坐标轴是反的所以需要用 180-角度
        float degree = 180 - (float) Math.toDegrees(radians);

        // 当转到负坐标轴一侧
        if (centerX > eventX) {
            degree = 180 + (float) Math.toDegrees(radians);
        }

        return degree;
    }

    private void getDisplayNumber() {
        mDisplayRemainTime = MyUtil.addZero(mTimeRemain.get(Calendar.MINUTE)) + ":" +
                MyUtil.addZero(mTimeRemain.get(Calendar.SECOND));
    }

    /**
     * 更新角度，角度由剩余时间决定
     */
    private void updateDegree() {
        mCurrentDegree = (float) ((mTimeRemain.get(Calendar.MINUTE) * 60 +
                mTimeRemain.get(Calendar.SECOND)) / (60.0 * 60)) * 360;
        updateDragButtonPosition();

    }

    /**
     * 更新拖动按钮中心点
     */
    private void updateDragButtonPosition() {
        // 根据勾股定理已知斜边、正弦余弦，求对应的边
        mDragButtonPosition[0] = (float) (mCenterX + mCircleRadiusWatcher * Math.sin(Math.toRadians(mCurrentDegree)));
        mDragButtonPosition[1] = (float) (mCenterY - mCircleRadiusWatcher * Math.cos(Math.toRadians(mCurrentDegree)));
    }


    //get the time from currentDegree and store it in mTimeStart and mTimeRemain
    //从当前的角度获取时间，保存到timeStart和timeRemain
    private void updateTime(int flag) {

        switch (flag) {
            case 0:

                mTimeStart.set(Calendar.MINUTE, (int) Math.floor(60 * mCurrentDegree / 360));
                mTimeRemain.set(Calendar.MINUTE, (int) Math.floor(60 * mCurrentDegree / 360));

                mTimeStart.set(Calendar.SECOND, 0);
                mTimeRemain.set(Calendar.SECOND, 0);
                break;
            case 2:
                mTimeStart.set(Calendar.MINUTE, (int) Math.floor(60 * mCurrentDegree / 360));
                mTimeRemain.set(Calendar.MINUTE, (int) Math.floor(60 * mCurrentDegree / 360));

                mTimeStart.set(Calendar.SECOND, 0);
                mTimeRemain.set(Calendar.SECOND, 0);
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

                    mTimeRemain.add(Calendar.MILLISECOND, -1000);

                    if (mRemainTimeChangeListener != null) {
                        mRemainTimeChangeListener.onTimeChange(mTimeStart.getTimeInMillis(), mTimeRemain.getTimeInMillis());
                    }

                    invalidate();

                    break;
                //countdown stop
                //时间为空，停止倒计时，提示用户
                case 2:
                    mIsStarted = false;
                    timerTask.cancel();
                    break;

                //StopWatch running
                case 11:
                    mTimeRemain.add(Calendar.MILLISECOND, 1000);
                    if (mRemainTimeChangeListener != null) {
                        mRemainTimeChangeListener.onTimeChange(mTimeStart.getTimeInMillis(), mTimeRemain.getTimeInMillis());
                    }
                    invalidate();
                    break;
                //StopWatch stop
                //到达MAX TIME
                case 12:
                    mIsStarted = false;
                    timerTask.cancel();
                    break;
            }
        }
    };

    Timer timer = new Timer(true);
    TimerTask timerTask;


    public boolean start() {
        if (mModel == Model.Timer) {
            if (!isTimeEmpty() && !mIsStarted) {

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
                mIsStarted = true;

                if (mRemainTimeChangeListener != null) {
                    mRemainTimeChangeListener.onTimerStart(mTimeStart.getTimeInMillis());
                }
            }
        } /*else if (mModel == Model.StopWatch) {
            if (!isMaxTime() && !mIsStarted) {
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
                mIsStarted = true;

                if (mRemainTimeChangeListener != null) {
                    mRemainTimeChangeListener.onTimerStart(mTimeStart.getTimeInMillis());
                }
            }
        }*/
        return mIsStarted;
    }

    private boolean isTimeEmpty() {
        if (mTimeRemain.get(Calendar.HOUR_OF_DAY) != 0
                || mTimeRemain.get(Calendar.MINUTE) != 0
                || mTimeRemain.get(Calendar.SECOND) != 0
                || mTimeRemain.get(Calendar.MILLISECOND) != 0) {
            return false;
        } else {
            return true;
        }
    }


    public long stop() {
        timerTask.cancel();
        mIsStarted = false;

        if (mRemainTimeChangeListener != null) {
            mRemainTimeChangeListener.onTimeStop(mTimeStart.getTimeInMillis(), mTimeRemain.getTimeInMillis());
        }

        return mTimeStart.getTimeInMillis() - mTimeRemain.getTimeInMillis();
    }


    public Calendar getTimeStart() {
        return mTimeStart;
    }


    public Calendar getTimeRemaid() {
        return mTimeRemain;
    }


    public long getTimePass() {
        return mTimeStart.getTimeInMillis() - mTimeRemain.getTimeInMillis();
    }


    public void setOnTimeChangeListener(OnTimeChangeListener listener) {
        if (listener != null) {
            mRemainTimeChangeListener = listener;
        }
    }

    public void setTimeChangListener(OnMinChangListener timeChangListener) {
        this.mTimeChangListener = timeChangListener;
    }

    public void reset() {
        //先停止计时
        stop();
        //初始化calendar
        mIsInitialized = false;
        invalidate();
    }

    public boolean isMaxTime() {
        if (mTimeRemain.get(Calendar.HOUR_OF_DAY) == 5
                && mTimeRemain.get(Calendar.MINUTE) == 59
                && mTimeRemain.get(Calendar.SECOND) == 59) {
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
        void onInitialFinishListener();
    }

    public void setModel(Model model) {
        this.mModel = model;
    }

    /**
     * set default time
     *
     * @param h max 5
     * @param m max 59
     * @param s max 59
     */
    public void setStartTime(final int h, final int m, final int s) throws NumberFormatException {
        mInitialFinishListener = new OnInitialFinishListener() {
            @Override
            public void onInitialFinishListener() {
                if (h > 5 || m > 59 || s > 69 || h < 0 || m < 0 | s < 0) {
                    throw new NumberFormatException("hour must in [0-5], minute and second must in [0-59]");
                }
                mTimeRemain.set(Calendar.HOUR_OF_DAY, h);
                mTimeRemain.set(Calendar.MINUTE, m);
                mTimeRemain.set(Calendar.SECOND, s);
                mTimeStart.set(Calendar.HOUR_OF_DAY, h);
                mTimeStart.set(Calendar.MINUTE, m);
                mTimeStart.set(Calendar.SECOND, s);
                updateDegree();
                invalidate();
            }
        };
    }

}
