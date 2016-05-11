/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.bean.Event;

import com.kaku.weac.bean.AlarmClock;

/**
 * @author 咖枯
 * @version 1.0 2016/2/12
 */
public class AlarmClockDeleteEvent {
    private int mPosition;
    private AlarmClock mAlarmClock;

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public AlarmClockDeleteEvent(int position, AlarmClock alarmClock) {
        this.mPosition = position;
        mAlarmClock = alarmClock;
    }

    public AlarmClock getAlarmClock() {
        return mAlarmClock;
    }
}
