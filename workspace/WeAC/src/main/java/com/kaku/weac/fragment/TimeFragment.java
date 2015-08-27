package com.kaku.weac.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kaku.weac.R;

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
        return inflater.inflate(R.layout.fm_time, container, false);
    }

}
