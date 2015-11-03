package com.kaku.weac.activities;

import android.support.v4.app.Fragment;

import com.kaku.weac.fragment.AddCityFragment;

/**
 * 添加城市activity
 *
 * @author 咖枯
 * @version 1.0 2015/11/02
 */
public class AddCityActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AddCityFragment();
    }

}