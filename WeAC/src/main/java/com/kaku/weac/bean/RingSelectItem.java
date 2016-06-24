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
 * 共通铃声选择项目
 *
 * @author 咖枯
 * @version 1.0 2015/06
 */
public class RingSelectItem {
    /**
     * 共通铃声选择项实例
     */
    private static RingSelectItem sRingSelectItem;

    /**
     * 铃声名
     */
    private String mName;

    /**
     * 铃声地址
     */
    private String mUrl;

    /**
     * 铃声界面
     */
    private int mRingPager;

    public int getRingPager() {
        return mRingPager;
    }

    public void setRingPager(int ringPager) {
        mRingPager = ringPager;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    /**
     * 取得共通铃声选择项目实例
     *
     * @return 共通铃声选择项目实例
     */
    public static RingSelectItem getInstance() {
        if (sRingSelectItem == null) {
            sRingSelectItem = new RingSelectItem();
        }
        return sRingSelectItem;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
