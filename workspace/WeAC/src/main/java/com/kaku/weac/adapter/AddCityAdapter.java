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
public class AddCityAdapter extends ArrayAdapter<String> {

    private final Context mContext;

    /**
     * 城市适配器构造方法
     *
     * @param context context
     * @param list    城市列表
     */
    public AddCityAdapter(Context context, List<String> list) {
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
            @SuppressWarnings("deprecation")
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
