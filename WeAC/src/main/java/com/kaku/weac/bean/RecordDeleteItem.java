/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.kaku.weac.bean;

/**
 * 录音删除信息实例
 *
 * @author 咖枯
 * @version 1.0 2015/08
 */
public class RecordDeleteItem {

    /**
     * 录音地址
     */
    private final String mRingUrl;

    /**
     * 录音名
     */
    private final String mRingName;

    /**
     * 是否选中
     */
    private boolean mIsSelected;

    public RecordDeleteItem(String ringUrl, String ringName, boolean isSelected) {
        super();
        mRingUrl = ringUrl;
        mRingName = ringName;
        mIsSelected = isSelected;
    }

    public String getRingUrl() {
        return mRingUrl;
    }

    public String getRingName() {
        return mRingName;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean isSelected) {
        this.mIsSelected = isSelected;
    }
}
