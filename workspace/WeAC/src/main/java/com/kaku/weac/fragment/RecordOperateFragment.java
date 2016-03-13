/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.common.WeacConstants;

/**
 * 铃声操作fragment
 *
 * @author 咖枯
 * @version 1.0 2015/07
 */
public class RecordOperateFragment extends BaseFragment implements OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_record_operate, container,
                false);
        // 设置Dialog全屏显示
        getActivity().getWindow().setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        TextView recordRenameTv = (TextView) view.findViewById(R.id.record_rename);
        TextView recordDeleteTv = (TextView) view.findViewById(R.id.record_delete);
        TextView recordDeleteBatchTv = (TextView) view
                .findViewById(R.id.record_delete_batch);
        TextView recordDetailTv = (TextView) view.findViewById(R.id.record_detail);

        recordRenameTv.setOnClickListener(this);
        recordDeleteTv.setOnClickListener(this);
        recordDeleteBatchTv.setOnClickListener(this);
        recordDetailTv.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_rename:
                getActivity().getIntent().putExtra(WeacConstants.TYPE, 1);
                finish();
                break;
            case R.id.record_delete:
                getActivity().getIntent().putExtra(WeacConstants.TYPE, 2);
                finish();
                break;
            case R.id.record_delete_batch:
                getActivity().getIntent().putExtra(WeacConstants.TYPE, 3);
                finish();
                break;
            case R.id.record_detail:
                getActivity().getIntent().putExtra(WeacConstants.TYPE, 4);
                finish();
                break;
        }

    }

    private void finish() {
        getActivity().setResult(Activity.RESULT_OK, getActivity().getIntent());
        getActivity().finish();
    }

}