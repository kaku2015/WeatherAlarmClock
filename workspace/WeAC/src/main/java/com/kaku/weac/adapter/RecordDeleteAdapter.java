/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
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

import com.kaku.weac.R;
import com.kaku.weac.bean.RecordDeleteItem;
import com.kaku.weac.fragment.RecordDeleteBatchFragment;

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
                                RecordDeleteBatchFragment.onCheck(recordItem);

                            }
                        } else {
                            if (recordItem.isSelected()) {
                                // 解除选中处理
                                RecordDeleteBatchFragment.unCheck(recordItem);
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
