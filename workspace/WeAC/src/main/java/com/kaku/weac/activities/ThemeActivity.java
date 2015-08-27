package com.kaku.weac.activities;

import android.support.v4.app.Fragment;

import com.kaku.weac.fragment.ThemeFragment;

/**
 * 主题activity
 * 
 * @author 咖枯
 * @version 1.0 2015
 */
public class ThemeActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new ThemeFragment();
	}
}