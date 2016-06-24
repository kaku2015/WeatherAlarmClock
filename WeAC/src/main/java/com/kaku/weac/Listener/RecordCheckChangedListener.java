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
