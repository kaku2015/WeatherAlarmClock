/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.bean.Event;

/**
 * 壁纸更新Event
 *
 * @author 咖枯
 * @version 1.0 2016/1/15
 */
public class WallpaperEvent {
    private boolean mIsAppWallpaper = false;

    public WallpaperEvent() {
    }

    public WallpaperEvent(boolean isAppWallpaper) {
        mIsAppWallpaper = isAppWallpaper;
    }

    /**
     * 变更是否为应用自带壁纸
     */
    public boolean isAppWallpaper() {
        return mIsAppWallpaper;
    }
}
