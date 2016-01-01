package com.kaku.weac.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kaku.weac.R;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.util.MyUtil;

import java.util.Calendar;
import java.util.TimeZone;
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
     * 是否已经初始化
     */
    private boolean mIsInitialized = false;

    /**
     * 是否倒计时已经开始
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

    /**
     * 当前设置的分钟数
     */
    private int mRemainMinute = 0;

    /**
     * 弧形的参考矩形
     */
    private RectF mRectF;

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
        // 初始化
        if (!mIsInitialized) {
            initialize(canvas);
            mIsInitialized = true;
        }
        // 当不是拖动按钮改变时间
        if (!mIsInDragButton) {
            // 根据当前剩余时间更新精确角度、拖动按钮位置
            updateDegree();
        }
        // 画表盘背景的圆圈
        canvas.drawCircle(mCenterX, mCenterY, mCircleRadiusWatcher, mPaintCircleBackground);
        // 画弧形
        canvas.drawArc(mRectF, -90, mCurrentDegree, false, mPaintArc);
        // 画按钮
        canvas.drawCircle(mDragButtonPosition[0], mDragButtonPosition[1], mStrokeWidth / 2, mPaintDragButton);
        // 画按钮的辉光效果
        canvas.drawCircle(mDragButtonPosition[0], mDragButtonPosition[1], mStrokeWidth, mPaintGlowEffect);
        // 设置显示的剩余时间
        setDisplayNumber();
        // 画剩余时间
        canvas.drawText(mDisplayRemainTime, mCenterX - mRect.width() / 2,
                mCenterY + mRect.height() / 2, mPaintRemainTime);

//        LogUtil.d(LOG_TAG, "绘制中");
    }

    /**
     * 初始化
     *
     * @param canvas canvas
     */
    private void initialize(Canvas canvas) {

        mTimeRemain = Calendar.getInstance();
        mTimeStart = Calendar.getInstance();
        mTimeStart.clear();
        mTimeRemain.clear();
        // GMT（格林尼治标准时间）一般指世界时.中国时间（GST）与之相差-8小时
        TimeZone tz = TimeZone.getTimeZone("GMT");
        mTimeStart.setTimeZone(tz);
        mTimeRemain.setTimeZone(tz);

        float density = getResources().getDisplayMetrics().density;
        mViewWidth = canvas.getWidth();
        mViewHeight = canvas.getHeight();

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
        @SuppressWarnings("deprecation")
        int colorWatcher = getResources().getColor(R.color.white_trans10);
        mPaintCircleBackground.setColor(colorWatcher);
        mPaintCircleBackground.setStrokeWidth(mStrokeWidth);
        mPaintCircleBackground.setStyle(Paint.Style.STROKE);
        mPaintCircleBackground.setAntiAlias(true);

        // 剩余时间颜色
        int colorRemainTime = Color.WHITE;
        mPaintDragButton.setColor(colorRemainTime);
        mPaintDragButton.setStyle(Paint.Style.FILL);
        mPaintDragButton.setAntiAlias(true);

        mPaintArc.setColor(colorRemainTime);
        mPaintArc.setStrokeWidth(mStrokeWidth);
        mPaintArc.setStyle(Paint.Style.STROKE);
        mPaintArc.setAntiAlias(true);

        mPaintRemainTime.setStyle(Paint.Style.FILL);
        mPaintRemainTime.setAntiAlias(true);
        mRect = new Rect();
        float densityText = getResources().getDisplayMetrics().scaledDensity;
        mPaintRemainTime.setTextSize(60 * densityText);
        mPaintRemainTime.setColor(colorRemainTime);
        mPaintRemainTime.setAntiAlias(true);
        mPaintRemainTime.getTextBounds("00:00", 0, "00:00".length(), mRect);

        //用于绘制圆弧尽头的辉光效果,辉光区域就是dragButton的区域
        mPaintGlowEffect.setMaskFilter(new BlurMaskFilter(2 * mStrokeWidth / 3, BlurMaskFilter.Blur.NORMAL));
        mPaintGlowEffect.setAntiAlias(true);
        mPaintGlowEffect.setColor(colorRemainTime);
        mPaintGlowEffect.setStyle(Paint.Style.FILL);

        mRectF = new RectF(mCenterX - mCircleRadiusWatcher, mCenterY - mCircleRadiusWatcher
                , mCenterX + mCircleRadiusWatcher, mCenterY + mCircleRadiusWatcher);

        //完成初始化回调
        if (mInitialFinishListener != null) {
            mInitialFinishListener.onInitialFinishListener();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event == null || mIsStarted) {
            return false;
        }
        // 拦截父组件触摸事件
        getParent().requestDisallowInterceptTouchEvent(true);

        switch (event.getAction()) {
            //判断点击是否在拖动按钮内
            case MotionEvent.ACTION_DOWN:
                // Math.pow:返回的第一个参数的值提高到第二个参数的幂（第一个参数为底数，第二个参数为指数） x2 的结果为 x的2次幂
                // 当拖动按钮半径>=触摸点到拖动按钮圆心的距离
                if (mCircleRadiusDragButtonTouch >= Math.sqrt(Math.pow(event.getX() - mDragButtonPosition[0], 2)
                        + Math.pow(event.getY() - mDragButtonPosition[1], 2))) {
                    //在拖动按钮中
                    mIsInDragButton = true;
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
                break;

            //更新拖动按钮的位置
            case MotionEvent.ACTION_MOVE:
                // 没有开始倒计时
                if (!mIsStarted) {
                    // 在拖动按钮中
                    if (mIsInDragButton) {
                        mCurrentDegree = getDegree(event.getX(), event.getY());
                        // 更新显示时间
                        updateTime();
                        // 更新拖动按钮位置
                        updateDragButtonPosition();
                        invalidate();

                        //FiXME: 不需要删除回调接口
                        // 当分钟数改变
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
        // 控件默认宽度（屏幕宽度）
        int defaultViewWidth = (int) (360 * getResources().getDisplayMetrics().density);
        int width = getDimension(defaultViewWidth, widthMeasureSpec);
        int height = getDimension(width, heightMeasureSpec);

        mViewWidth = width;
        mViewHeight = height;

        setMeasuredDimension(width, height);
    }

    /**
     * 取得尺寸
     *
     * @param defaultDimension 默认尺寸
     * @param measureSpec      measureSpec
     * @return 尺寸
     */
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
     * @param eventX eventX
     * @param eventY eventY
     * @return 角度
     */
    private float getDegree(float eventX, float eventY) {
        // x轴边
        double tx = eventX - mCenterX;
        // y轴边
        double ty = eventY - mCenterY;
        // 开正平方根,求出滑动点到圆心的距离（斜边）
        double t_length = Math.sqrt(tx * tx + ty * ty);
        // 根据反余弦求出弧度
        double radians = Math.acos(ty / t_length);
        // y的坐标轴是反的所以需要用 180-角度 // Math.toDegrees： 根据角度转化为弧度
        float degree = 180 - (float) Math.toDegrees(radians);

        // 当转到负坐标轴一侧
        if (mCenterX > eventX) {
            degree = 180 + (float) Math.toDegrees(radians);
        }

        return degree;
    }

    /**
     * 设置显示的剩余时间
     */
    private void setDisplayNumber() {
        mDisplayRemainTime = MyUtil.formatTime(mTimeRemain.get(Calendar.MINUTE),
                mTimeRemain.get(Calendar.SECOND));
    }

    /**
     * 更新角度，角度由剩余时间决定
     * 度数 = （（分钟数 * 60 + 秒数） / 60分 * 60秒） * 360度 = （分钟数 * 60 + 秒数） * /10.0
     */
    private void updateDegree() {
        mCurrentDegree = (float) ((mTimeRemain.get(Calendar.MINUTE) * 60 + mTimeRemain.get(Calendar.SECOND)) / 10.0);
        updateDragButtonPosition();
    }

    /**
     * 更新拖动按钮位置
     */
    private void updateDragButtonPosition() {
        // 根据勾股定理已知斜边、正弦余弦，求对应的边 // Math.toRadians： 根据角度转化为弧度
        mDragButtonPosition[0] = (float) (mCenterX + mCircleRadiusWatcher * Math.sin(Math.toRadians(mCurrentDegree)));
        mDragButtonPosition[1] = (float) (mCenterY - mCircleRadiusWatcher * Math.cos(Math.toRadians(mCurrentDegree)));
    }


    /**
     * 从当前的角度获取时间，保存到timeStart和timeRemain
     * 度数 = 分钟数 * 60分 / 3600 * 360度，分钟数 = 度数 * 3600 / 360 * 60 = 度数 / 6
     */
    private void updateTime() {
        mTimeStart.set(Calendar.MINUTE, (int) (mCurrentDegree / 6));
        mTimeRemain.set(Calendar.MINUTE, (int) (mCurrentDegree / 6));
        mTimeStart.set(Calendar.SECOND, 0);
        mTimeRemain.set(Calendar.SECOND, 0);
    }


    /**
     * 计时Handler
     */
    private Handler mTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                // 正在倒计时
                case STARTING:
                    mTimeRemain.add(Calendar.MILLISECOND, -1000);

                    if (mRemainTimeChangeListener != null) {
                        mRemainTimeChangeListener.onTimeChange(mTimeStart.getTimeInMillis(),
                                mTimeRemain.getTimeInMillis());
                    }

                    invalidate();

                    break;
                // 停止倒计时
                case STOP:
                    mIsStarted = false;
                    mTimerTask.cancel();
                    saveRemainTime(0, false);

                    if (mRemainTimeChangeListener != null) {
                        mRemainTimeChangeListener.onTimeStop(mTimeStart.getTimeInMillis(),
                                mTimeRemain.getTimeInMillis());
                    }
                    break;

/*                //StopWatch running
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
                    mTimerTask.cancel();
                    break;*/
            }
        }
    };

    /**
     * 计时Task
     */
    private TimerTask mTimerTask;

    /**
     * 正在计时
     */
    private static final int STARTING = 1;

    /**
     * 停止计时
     */
    private static final int STOP = 2;

    public void setIsStarted(boolean isStarted) {
        mIsStarted = isStarted;
    }

    /**
     * 开始计时
     *
     * @return 是否开启成功
     */
    public boolean start() {
        // 倒计时
        if (mModel == Model.Timer) {
            if (!isTimeEmpty()) {
                setRemainTime(false);
                mTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        // 时间不为0，继续倒计时
                        if (!isTimeEmpty()) {
                            Message message = new Message();
                            message.what = STARTING;
                            mTimeHandler.sendMessage(message);
                            // 停止倒计时
                        } else {
                            Message message = new Message();
                            message.what = STOP;
                            mTimeHandler.sendMessage(message);
                        }

                    }
                };

                new Timer(true).schedule(mTimerTask, 1000, 1000);
                mIsStarted = true;

                if (mRemainTimeChangeListener != null) {
                    mRemainTimeChangeListener.onTimerStart(mTimeRemain.getTimeInMillis());
                }
            }
        } /*else if (mModel == Model.StopWatch) {
            if (!isMaxTime() && !mIsStarted) {
                mTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (!isMaxTime()) {
                            Message message = new Message();
                            message.what = 11;
                            mTimeHandler.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = 12;
                            mTimeHandler.sendMessage(message);
                        }
                    }
                };

                timer.schedule(mTimerTask, 1000, 1000);
                mIsStarted = true;

                if (mRemainTimeChangeListener != null) {
                    mRemainTimeChangeListener.onTimerStart(mTimeStart.getTimeInMillis());
                }
            }
        }*/
        return mIsStarted;
    }

    /**
     * 计算计时剩余时间
     *
     * @param isStop 是否为暂停状态
     */
    private void setRemainTime(boolean isStop) {
        long remainTime = mTimeRemain.getTimeInMillis();
        long countdownTime;
        // 暂停状态
        if (isStop) {
            countdownTime = remainTime;
        } else {
            long now = SystemClock.elapsedRealtime();
            countdownTime = now + remainTime;
        }
        saveRemainTime(countdownTime, isStop);
    }

    /**
     * 保存计时时间
     *
     * @param countdownTime 计时时间
     * @param isStop        是否为暂停状态
     */
    private void saveRemainTime(long countdownTime, boolean isStop) {
        SharedPreferences preferences = getContext().getSharedPreferences(
                WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(WeacConstants.COUNTDOWN_TIME, countdownTime);
        editor.putBoolean(WeacConstants.IS_STOP, isStop);
        editor.apply();
    }

    /**
     * 计时时间是否为0
     *
     * @return 计时时间是否为0
     */
    private boolean isTimeEmpty() {
        return !(mTimeRemain.get(Calendar.HOUR_OF_DAY) != 0
                || mTimeRemain.get(Calendar.MINUTE) != 0
                || mTimeRemain.get(Calendar.SECOND) != 0
                || mTimeRemain.get(Calendar.MILLISECOND) != 0);
    }


    /**
     * 停止计时
     */
    public void stop() {
        cancelTimer();
        setRemainTime(true);
    }


    public void setOnTimeChangeListener(OnTimeChangeListener listener) {
        if (listener != null) {
            mRemainTimeChangeListener = listener;
        }
    }

    public void setTimeChangListener(OnMinChangListener timeChangListener) {
        this.mTimeChangListener = timeChangListener;
    }


    /**
     * 重置
     */
    public void reset() {
        cancelTimer();
        mIsStarted = false;
        saveRemainTime(0, false);
        mTimeStart.setTimeInMillis(0);
        mTimeRemain.setTimeInMillis(0);
        mRemainMinute = 0;
        invalidate();
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

    /**
     * 设置计时模式
     *
     * @param model Model.Timer：计时；Model.StopWatcher：秒表
     */
    public void setModel(Model model) {
        this.mModel = model;
    }

    /**
     * 设置默认开始时间
     *
     * @param h      小时
     * @param m      分钟
     * @param s      秒
     * @param isStop 是否为暂停状态
     */
    public void setStartTime(final int h, final int m, final int s, final boolean isStop) {
        mInitialFinishListener = new OnInitialFinishListener() {
            @Override
            public void onInitialFinishListener() {
                mTimeRemain.set(Calendar.HOUR_OF_DAY, h);
                mTimeRemain.set(Calendar.MINUTE, m);
                mTimeRemain.set(Calendar.SECOND, s);
                mTimeStart.set(Calendar.HOUR_OF_DAY, h);
                mTimeStart.set(Calendar.MINUTE, m);
                mTimeStart.set(Calendar.SECOND, s);
                updateDegree();
                invalidate();
                // 计时状态
                if (!isStop) {
                    // 开启计时
                    start();
                }
            }
        };
    }

    /**
     * 取消定时
     */
    public void cancelTimer() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
        }
    }
}
