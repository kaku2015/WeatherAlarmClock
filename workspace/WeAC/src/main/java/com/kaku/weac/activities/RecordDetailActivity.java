package com.kaku.weac.activities;

import android.support.v4.app.Fragment;

import com.kaku.weac.fragment.RecordDetailFragment;

/**
 * 录音文件详情activity
 *
 * @author 咖枯
 * @version 1.0 2015/07
 */
public class RecordDetailActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RecordDetailFragment();
    }

}