package com.kaku.weac.bean;

/**
 * 录音删除信息实例
 * 
 * @author 咖枯
 * @version 1.0 2015/08
 * 
 */
public class RecordDeleteItem {

	/**
	 * 录音地址
	 */
	private String mRingUrl;

	/**
	 * 录音名
	 */
	private String mRingName;

	/**
	 * 是否选中
	 */
	private boolean isSelected;

	public RecordDeleteItem(String ringUrl, String ringName, boolean isSelected) {
		super();
		mRingUrl = ringUrl;
		mRingName = ringName;
		this.isSelected = isSelected;
	}

	public String getRingUrl() {
		return mRingUrl;
	}

	public void setRingUrl(String ringUrl) {
		mRingUrl = ringUrl;
	}

	public String getRingName() {
		return mRingName;
	}

	public void setRingName(String ringName) {
		mRingName = ringName;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
