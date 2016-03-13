/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.bean;

/**
 * 保存默认主题壁纸名和壁纸ID
 *
 * @author 咖枯
 * @version 1.0 2015/12/18
 */
public class Theme {
    private String mResName;
    private int mResId;

    public int getResId() {
        return mResId;
    }

    public void setResId(int resId) {
        mResId = resId;
    }

    public String getResName() {
        return mResName;
    }

    public void setResName(String resName) {
        mResName = resName;
    }
}
