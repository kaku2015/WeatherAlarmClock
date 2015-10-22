package com.kaku.weac.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.kaku.weac.R;

/**
 * 折线温度曲线
 *
 * @author 咖枯
 * @version 1.0 2015/09/08
 */
public class LineChartView extends View {

    /**
     * x轴集合
     */
    private float mXAxis[] = new float[6];

    /**
     * y轴集合
     */
    private float mYAxis[] = new float[6];
    /**
     * x,y轴集合数
     */
    private int mLength = 6;

    /**
     * 温度集合
     */
    private int mTemp[] = new int[6];

    /**
     * 控件高
     */
    private int mHeight;

    /**
     * 字体大小
     */
    private float mTextSize;

    /**
     * 圓半径
     */
    private float mRadius;

    /**
     * 文字移动空间大小
     */
    private float mTextSpace;

    /**
     * 线的大小
     */
    private float mStokeWidth;

    /**
     * 折线颜色
     */
    private int mLineColor;

    /**
     * 点颜色
     */
    private int mPointColor;
    /**
     * 字体颜色
     */
    private int mTextColor;

    /**
     * 屏幕密度
     */
    private float mDensity;

    /**
     * 控件边的空白空间
     */
    private float mSpace;

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDensity = getResources().getDisplayMetrics().density;
        mTextSize = 14 * mDensity;
        mRadius = 3 * mDensity;
        mSpace = 3 * mDensity;
        mTextSpace = 8 * mDensity;
        mStokeWidth = 2 * mDensity;
        mLineColor = Color.WHITE;
        mTextColor = getResources().getColor(R.color.white_trans90);
        mPointColor = Color.WHITE;

    }

    public LineChartView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setWidthHeight();
        computeYAxisValues();
        drawChart(canvas);
    }

    /**
     * 计算y轴集合数值
     */
    private void computeYAxisValues() {
        // 存放最低温度
        int minTemp = 0;
        // 存放最高温度
        int maxTemp = 0;
        for (int item : mTemp) {
            if (minTemp == 0) {
                minTemp = item;
            }
            if (maxTemp == 0) {
                maxTemp = item;
            }
            if (item < minTemp) {
                minTemp = item;
            }
            if (item > maxTemp) {
                maxTemp = item;
            }
        }
        // 份数
        float parts = maxTemp - minTemp;
        // 白天气温
        if (mTextSpace >= 0) {
            // y轴高度
            float yAxisHeight = mHeight - mTextSize - mTextSpace - mRadius * 2 - mSpace * 2;
            // 当温度都相同时
            if (parts == 0) {
                for (int i = 0; i < mLength; i++) {
                    mYAxis[i] = mHeight - (yAxisHeight / 2) - mRadius - mSpace;
                }
            } else {
                // 当温度相差小于等于3
                if (parts <= 3) {
                    yAxisHeight /= 2;
                } else if (parts <= 5) {
                    yAxisHeight /= 1.5;
                }
                // 份数值
                float partValue = yAxisHeight / parts;
                for (int i = 0; i < mLength; i++) {
                    mYAxis[i] = mHeight - partValue * (mTemp[i] - minTemp) - mRadius - mSpace;
                }
            }
            // 夜间气温
        } else {
            // y轴高度
            float yAxisHeight = mHeight - mTextSize + mTextSpace - mRadius * 2 - mSpace * 2;
            if (parts == 0) {
                for (int i = 0; i < mLength; i++) {
                    mYAxis[i] = mHeight - (yAxisHeight / 2) - mSpace + mTextSpace - mTextSize;
                }
            } else {
                if (parts <= 3) {
                    yAxisHeight /= 2;
                } else if (parts <= 5) {
                    yAxisHeight /= 1.5;
                }
                // 份数值
                float partValue = yAxisHeight / parts;
                for (int i = 0; i < mLength; i++) {
                    mYAxis[i] = mHeight - partValue * (mTemp[i] - minTemp) - mSpace + mTextSpace - mTextSize;
                }
            }

        }


    }

    /**
     * 画折线图
     *
     * @param canvas canvas
     */
    private void drawChart(Canvas canvas) {
        // 线画笔
        Paint linePaint = new Paint();
        // 抗锯齿
        linePaint.setAntiAlias(true);
        // 线宽
        linePaint.setStrokeWidth(mStokeWidth);
        linePaint.setColor(mLineColor);
        // 空心
        linePaint.setStyle(Paint.Style.STROKE);

        // 点画笔
        Paint pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setColor(mPointColor);

        // 字体画笔
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(mTextColor);
        textPaint.setTextSize(mTextSize);
        // 文字居中
        textPaint.setTextAlign(Paint.Align.CENTER);

        int alpha1 = 102;
        int alpha2 = 255;
        for (int i = 0; i < mLength; i++) {
            // 画线
            if (i < mLength - 1) {
                // 昨天
                if (i == 0) {
                    linePaint.setAlpha(alpha1);
                    // 设置虚线效果
                    linePaint.setPathEffect(new DashPathEffect(new float[]{2 * mDensity, 2 * mDensity}, 0));
                    // 路径
                    Path path = new Path();
                    // 路径起点
                    path.moveTo(mXAxis[i], mYAxis[i]);
                    // 路径连接到
                    path.lineTo(mXAxis[i + 1], mYAxis[i + 1]);
                    canvas.drawPath(path, linePaint);
                } else {
                    linePaint.setAlpha(alpha2);
                    linePaint.setPathEffect(null);
                    canvas.drawLine(mXAxis[i], mYAxis[i], mXAxis[i + 1], mYAxis[i + 1], linePaint);
                }
            }

            // 画点
            if (i != 1) {
                // 昨天
                if (i == 0) {
                    pointPaint.setAlpha(alpha1);
                    canvas.drawCircle(mXAxis[i], mYAxis[i], mRadius, pointPaint);
                } else {
                    pointPaint.setAlpha(alpha2);
                    canvas.drawCircle(mXAxis[i], mYAxis[i], mRadius, pointPaint);
                }
                // 今天
            } else {
                pointPaint.setAlpha(alpha2);
                float radius = 5 * mDensity;
                canvas.drawCircle(mXAxis[i], mYAxis[i], radius, pointPaint);
            }

            // 画字
            // 昨天
            if (i == 0) {
                textPaint.setAlpha(alpha1);
                drawText(canvas, textPaint, i);
            } else {
                textPaint.setAlpha(alpha2);
                drawText(canvas, textPaint, i);
            }
        }
    }

    private void drawText(Canvas canvas, Paint textPaint, int i) {
        if (mTextSpace >= 0) {
            // 显示白天气温
            canvas.drawText(mTemp[i] + "°", mXAxis[i], mYAxis[i] - mRadius - mTextSpace, textPaint);
        } else {
            // 显示夜间气温
            canvas.drawText(mTemp[i] + "°", mXAxis[i], mYAxis[i] - mTextSpace + mTextSize, textPaint);
        }
    }

    /**
     * 设置宽高，x轴集合
     */
    private void setWidthHeight() {
        mHeight = getHeight();
        // 控件宽
        int width = getWidth();
        // 每一份宽
        float w = width / 12;
        mXAxis[0] = w;
        mXAxis[1] = w * 3;
        mXAxis[2] = w * 5;
        mXAxis[3] = w * 7;
        mXAxis[4] = w * 9;
        mXAxis[5] = w * 11;
    }

    /**
     * 设置温度
     *
     * @param temp 温度数组集合
     */
    public void setTemp(int[] temp) {
        mTemp = temp;
    }

    /**
     * 设置文字距离坐标位移
     *
     * @param textSpace 位移：dp
     */
    public void setTextSpace(float textSpace) {
        mTextSpace = textSpace * mDensity;
    }

    /**
     * 设置折线的颜色
     *
     * @param lineColor 折线色
     */
    public void setLineColor(int lineColor) {
        mLineColor = lineColor;
    }

    /**
     * 设置点的颜色
     *
     * @param pointColor 点色
     */
    public void setPointColor(int pointColor) {
        mPointColor = pointColor;
    }
}
