package com.kaku.weac.activities;

import android.support.v4.app.Fragment;

import com.kaku.weac.fragment.LifeIndexDetailFragment;

/**
 * 生活指数详情activity
 *
 * @author 咖枯
 * @version 1.0 2015/10/10
 */
public class LifeIndexDetailActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new LifeIndexDetailFragment();
    }

}