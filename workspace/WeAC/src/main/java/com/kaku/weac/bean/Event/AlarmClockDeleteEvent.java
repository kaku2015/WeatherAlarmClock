/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.bean.Event;

/**
 * @author 咖枯
 * @version 1.0 2016/2/12
 */
public class AlarmClockDeleteEvent {
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public AlarmClockDeleteEvent(int position) {
        this.position = position;
    }
}
