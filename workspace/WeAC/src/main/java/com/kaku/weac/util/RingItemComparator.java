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
