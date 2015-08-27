package com.kaku.weac.activities;

import android.support.v4.app.Fragment;

import com.kaku.weac.fragment.RecordOperateFragment;

/**
 * 录音文件操作activity
 * 
 * @author 咖枯
 * @version 1.0 2015/07
 */
public class RecordOperateActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new RecordOperateFragment();
	}

}