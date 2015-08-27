package com.kaku.weac.activities;

import android.support.v4.app.Fragment;

import com.kaku.weac.fragment.NapEditFragment;

/**
 * 小睡修改activity
 * 
 * @author 咖枯
 * @version 1.0 2015/07
 */
public class NapEditActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new NapEditFragment();
	}

}