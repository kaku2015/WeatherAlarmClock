/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.bean.Event;

/**
 * @author 咖枯
 * @version 1.0 2016/2/3
 */
public class QRcodeLogoEvent {
    // 自定二维码logo存放地址
    private String mLogoPath;

    public QRcodeLogoEvent(String logoPath) {
        mLogoPath = logoPath;
    }

    public String getLogoPath() {
        return mLogoPath;
    }
}
