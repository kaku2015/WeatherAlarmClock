package com.kaku.weac.util;

import java.util.Comparator;

import com.kaku.weac.bean.RecordDeleteItem;

// TODO： 汉字排序

/**
 * 比较铃声名，进行升序排序
 * 
 * @author 咖枯
 * @version 1.0 2015/08
 */
public class RecordItemDeleteComparator implements Comparator<RecordDeleteItem> {
	@Override
	public int compare(RecordDeleteItem o1, RecordDeleteItem o2) {
		String name1 = o1.getRingName();
		String name2 = o2.getRingName();
		return name1.compareToIgnoreCase(name2);
	}
}
