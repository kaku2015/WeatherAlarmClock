package com.kaku.weac.activities;

import android.support.v4.app.Fragment;

import com.kaku.weac.fragment.CityManageFragment;

/**
 * 城市管理activity
 *
 * @author 咖枯
 * @version 1.0 2015/10/22
 */
public class CityManageActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CityManageFragment();
    }

}