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
package com.kaku.weac.util;

import com.kaku.weac.common.WeacConstants;

import java.util.Comparator;
import java.util.Map;

// TODO： 汉字排序

/**
 * 比较map中铃声名，进行升序排序
 *
 * @author 咖枯
 * @version 1.0 2015/07
 */
public class RingItemComparator implements Comparator<Map<String, String>> {
    @Override
    public int compare(Map<String, String> o1, Map<String, String> o2) {
        String name1 = o1.get(WeacConstants.RING_NAME);
        String name2 = o2.get(WeacConstants.RING_NAME);
        return name1.compareToIgnoreCase(name2);
    }

}
