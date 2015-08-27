package com.kaku.weac.activities;

import android.support.v4.app.Fragment;

import com.kaku.weac.fragment.RecordRenameFragment;

/**
 * 录音重命名activity
 * 
 * @author 咖枯
 * @version 1.0 2015/07
 */
public class RecordRenameActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new RecordRenameFragment();
	}

}