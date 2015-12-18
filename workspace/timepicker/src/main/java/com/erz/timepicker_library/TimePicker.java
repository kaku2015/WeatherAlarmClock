/*
    Copyright 2015 erz05

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package com.erz.timepicker_library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by edgarramirez on 2/26/15.
 */
public class TimePicker extends View {

    private static final String STATE_PARENT = "parent";
    private static final String STATE_ANGLE = "angle";
    private static final String STATE_TEXT_COLOR = "textColor";
    private static final String STATE_CLOCK_COLOR = "clockColor";
    private static final String STATE_DIAL_COLOR = "dialColor";
    private static final String STATE_DISABLE_TOUCH = "disableTouch";

    Paint paint;
    RectF rectF;

    float width;
    float height;
    float min;
    float padding;
    float radius;
    float dialRadius;
    float offset;
    float slopX;
    float slopY;
    float posX;
    float posY;
    float dialX;
    float dialY;
    final static float secAngle = 360/12;

    int startAngle;
    int hour;
    int minutes;
    int tmp;
    int previousHour;
    int textColor = Color.BLACK;
    int clockColor = Color.BLACK;
    int dialColor = Color.BLACK;

    double angle;
    double degrees;

    boolean isMoving;
    boolean amPm;
    boolean twentyFour;
    boolean disableTouch;

    String hStr;
    String mStr;
    String amPmStr;

    OnTimeChangedListener timeChangedListener;
    Calendar calendar = Calendar.getInstance();

    public interface OnTimeChangedListener {
        void timeChanged(Date date);
    }

    public TimePicker(Context context) {
        super(context);
        init(context, null);
    }

    public TimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        angle = (-Math.PI / 2)+.001;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setTextAlign(Paint.Align.CENTER);

        rectF = new RectF();

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimePicker);
            if (typedArray != null) {
                textColor = typedArray.getColor(R.styleable.TimePicker_text_color, Color.BLACK);
                clockColor = typedArray.getColor(R.styleable.TimePicker_clock_color, Color.BLACK);
                dialColor = typedArray.getColor(R.styleable.TimePicker_dial_color, Color.BLACK);
                disableTouch = typedArray.getBoolean(R.styleable.TimePicker_disable_touch, false);
                typedArray.recycle();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        min = Math.min(width, height);
        setMeasuredDimension((int)min, (int)min);

        offset = min * 0.5f;
        padding = min/20;
        radius = min/2-(padding*2);
        dialRadius = radius/5;
        rectF.set(-radius,-radius,radius,radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(offset, offset);

        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(255);

        //Rad to Deg
        degrees = (Math.toDegrees(angle) + 90) % 360;
        degrees = (degrees + 360)%360;

        //get Hour
        hour = ((int)degrees/30)%12;
        if (hour == 0) hour = 12;

        //get Minutes
        minutes = ((int)(degrees * 2))%60;
        mStr = (minutes < 10) ? "0"+minutes : minutes+"";

        //get AM/PM
        if ((hour == 12 && previousHour == 11) || (hour == 11 && previousHour == 12)) amPm = !amPm;
        amPmStr = amPm ? "AM" : "PM";

        previousHour = hour;

        paint.setColor(textColor);
        paint.setTextSize(min/5);
        if (twentyFour) {
            tmp = hour;
            if (!amPm) {
                if (tmp < 12) tmp += 12;
            } else {
                if (tmp == 12) tmp = 0;
            }
            hStr = (tmp < 10) ? "0"+tmp : tmp+"";
            canvas.drawText(hStr + ":" + mStr, 0, paint.getTextSize()/3, paint);
        } else {
            hStr = (hour < 10) ? "0"+hour : hour+"";
            canvas.drawText(hStr + ":" + mStr, 0, paint.getTextSize() / 4, paint);
            paint.setTextSize(min / 10);
            canvas.drawText(amPmStr, 0, paint.getTextSize() * 2, paint);
        }

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(min/30);
        paint.setColor(clockColor);
        canvas.drawOval(rectF, paint);

        startAngle = 0;
        for (tmp=0; tmp<12; tmp++) {
            canvas.save();
            canvas.rotate(startAngle, 0, 0);
            canvas.drawLine(0, radius, 0, radius - padding, paint);
            canvas.restore();
            startAngle += secAngle;
        }

        if (!disableTouch) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(dialColor);
            paint.setAlpha(100);

            calculatePointerPosition(angle);
            canvas.drawCircle(dialX, dialY, dialRadius, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null || disableTouch) return false;
        getParent().requestDisallowInterceptTouchEvent(true);

        posX = event.getX() - offset;
        posY = event.getY() - offset;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                calculatePointerPosition(angle);
                if (posX >= (dialX - dialRadius) && posX <= (dialX + dialRadius)
                        && posY >= (dialY - dialRadius) && posY <= (dialY + dialRadius)) {

                    slopX = posX - dialX;
                    slopY = posY - dialY;
                    isMoving = true;
                    invalidate();
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMoving) {
                    angle = (float) Math.atan2(posY - slopY, posX - slopX);
                    if (timeChangedListener != null) {
                        timeChangedListener.timeChanged(getTime());
                    }
                    invalidate();
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isMoving = false;
                invalidate();
                break;
        }
        return true;
    }

    private void calculatePointerPosition(double angle) {
        dialX = (float) (radius * Math.cos(angle));
        dialY = (float) (radius * Math.sin(angle));
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Bundle state = new Bundle();

        state.putParcelable(STATE_PARENT, superState);
        state.putDouble(STATE_ANGLE, angle);
        state.putInt(STATE_CLOCK_COLOR, clockColor);
        state.putInt(STATE_DIAL_COLOR, dialColor);
        state.putInt(STATE_TEXT_COLOR, textColor);
        state.putBoolean(STATE_DISABLE_TOUCH, disableTouch);

        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle savedState = (Bundle) state;
        Parcelable superState = savedState.getParcelable(STATE_PARENT);
        super.onRestoreInstanceState(superState);

        angle = savedState.getDouble(STATE_ANGLE);
        clockColor = savedState.getInt(STATE_CLOCK_COLOR);
        dialColor = savedState.getInt(STATE_DIAL_COLOR);
        textColor = savedState.getInt(STATE_TEXT_COLOR);
        disableTouch = savedState.getBoolean(STATE_DISABLE_TOUCH);
    }

    public void setColor(int color) {
        this.textColor = color;
        this.clockColor = color;
        this.dialColor = color;
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public void setClockColor(int clockColor) {
        this.clockColor = clockColor;
        invalidate();
    }

    public void setDialColor(int dialColor) {
        this.dialColor = dialColor;
        invalidate();
    }

    public void enableTwentyFourHour(boolean twentyFour) {
        this.twentyFour = twentyFour;
        invalidate();
    }

    public void disableTouch(boolean disableTouch){
        this.disableTouch = disableTouch;
    }

    public Date getTime() {
        tmp = hour;
        if (!amPm) {
            if (tmp < 12) tmp += 12;
        } else {
            if (tmp == 12) tmp = 0;
        }

        calendar.set(Calendar.HOUR_OF_DAY, tmp);
        calendar.set(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public void setTime(Date date) {
        calendar.setTime(date);
        hour = calendar.get(Calendar.HOUR);
        minutes = calendar.get(Calendar.MINUTE);
        amPm = (calendar.get(Calendar.AM_PM) == Calendar.AM);
        degrees = ((hour * 30)+270)%360;
        angle = Math.toRadians(degrees);
        degrees = ((double)minutes/2);
        angle += Math.toRadians(degrees)+.001;
        invalidate();
    }

    public void setTimeChangedListener(OnTimeChangedListener timeChangedListener) {
        this.timeChangedListener = timeChangedListener;
    }
}
