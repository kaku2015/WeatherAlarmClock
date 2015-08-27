package com.kaku.weac.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import com.kaku.weac.R;

/**
 * fragment activity的公共抽象类
 * 
 * @author 咖枯
 * @version 1.0 2015
 */
public abstract class SingleFragmentActivity extends FragmentActivity {

	/**
	 * 抽象方法：创建Fragment
	 * 
	 * @return Fragment
	 */
	protected abstract Fragment createFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fm_activity);
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragment_containers);
		if (fragment == null) {
			fragment = createFragment();
			fm.beginTransaction().add(R.id.fragment_containers, fragment)
					.commit();

		}

	}
}