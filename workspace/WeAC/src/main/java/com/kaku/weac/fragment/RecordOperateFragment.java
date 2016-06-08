/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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