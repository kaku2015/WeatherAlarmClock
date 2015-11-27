/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.Listener;

import com.kaku.weac.bean.RecordDeleteItem;

/**
 * 批量录音删除选中回调接口
 *
 * @author 咖枯
 * @version 1.0 2015/11/27
 */
public interface RecordCheckChangedListener {
    /**
     * 选中
     *
     * @param recordDeleteItem 录音删除信息
     */
    void onChecked(RecordDeleteItem recordDeleteItem);

    /**
     * 取消选中
     *
     * @param recordDeleteItem 录音删除信息
     */
    void unChecked(RecordDeleteItem recordDeleteItem);
}
