/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kaku.weac.R;

import java.util.List;

/**
 * 城市适配器类
 *
 * @author 咖枯
 * @version 1.0 2015/11/08
 */
public class CityAdapter extends ArrayAdapter<String> {

    private final Context mContext;

    /**
     * 城市适配器构造方法
     *
     * @param context context
     * @param list    城市列表
     */
    public CityAdapter(Context context, List<String> list) {
        super(context, 0, list);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String cityName = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.gv_add_city, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.cityName = (TextView) convertView
                    .findViewById(R.id.city_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (cityName.equals("定位")) {
            Drawable drawable = getContext().getResources().getDrawable(R.drawable.ic_gps);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            }
            viewHolder.cityName.setCompoundDrawables(drawable, null, null, null);
        } else {
            viewHolder.cityName.setCompoundDrawables(null, null, null, null);
        }
        viewHolder.cityName.setText(cityName);
        return convertView;
    }

    /**
     * 保存控件实例
     */
    private final class ViewHolder {
        // 城市
        TextView cityName;
    }
}
