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
package com.kaku.weac.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.kaku.weac.Listener.RecordCheckChangedListener;
import com.kaku.weac.R;
import com.kaku.weac.bean.RecordDeleteItem;

import java.util.List;

/**
 * 录音批量删除适配器类
 *
 * @author 咖枯
 * @version 1.0 2015/08
 */
public class RecordDeleteAdapter extends ArrayAdapter<RecordDeleteItem> {
    private final Context mContext;
    /**
     * 批量录音删除选中回调接口
     */
    private RecordCheckChangedListener mRecordCheckChangedListener;

    public void setRecordCheckChangedListener(RecordCheckChangedListener recordCheckChangedListener) {
        mRecordCheckChangedListener = recordCheckChangedListener;
    }

    /**
     * 录音批量删除适配器
     *
     * @param context context
     * @param list    录音删除对象List
     */
    public RecordDeleteAdapter(Context context, List<RecordDeleteItem> list) {
        super(context, 0, list);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.lv_record_delete_batch, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ringNameTv = (TextView) convertView
                    .findViewById(R.id.ring_list_display_name);
            viewHolder.markIconTgBtn = (ToggleButton) convertView
                    .findViewById(R.id.ring_list_toogle_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 取得录音实例
        final RecordDeleteItem recordItem = getItem(position);
        // 显示录音名
        viewHolder.ringNameTv.setText(recordItem.getRingName());
        viewHolder.markIconTgBtn
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            if (!recordItem.isSelected()) {
                                // 选中处理
                                mRecordCheckChangedListener.onChecked(recordItem);
                            }
                        } else {
                            if (recordItem.isSelected()) {
                                // 解除选中处理
                                mRecordCheckChangedListener.unChecked(recordItem);
                            }
                        }
                    }
                });

        viewHolder.markIconTgBtn.setChecked(recordItem.isSelected());

        return convertView;

    }

    /**
     * 保存控件实例
     */
    private final class ViewHolder {
        // 录音名
        TextView ringNameTv;
        // 标记图标
        ToggleButton markIconTgBtn;
    }
}
