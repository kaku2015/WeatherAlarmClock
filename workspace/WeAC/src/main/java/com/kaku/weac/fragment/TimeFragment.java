package com.kaku.weac.fragment;

import com.kaku.weac.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 计时fragment
 * 
 * @author 咖枯
 * @version 1.0 2015/05
 */
public class TimeFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fm_time, container, false);
		return view;
	}

}
