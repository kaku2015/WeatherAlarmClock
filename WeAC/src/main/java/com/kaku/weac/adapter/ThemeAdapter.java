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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kaku.weac.R;
import com.kaku.weac.bean.Theme;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * 主题壁纸适配器类
 *
 * @author 咖枯
 * @version 1.0 2015
 */
public class ThemeAdapter extends ArrayAdapter<Theme> {

    /**
     * activity上下文
     */
    private final Context mContext;

    /**
     * 资源名称，id列表
     */
    private final List<Theme> mList;

    /**
     * 主题壁纸名称
     */
    private String mWallpaperName;

    /**
     * 主题壁纸适配器
     *
     * @param context       activity上下文
     * @param list          壁纸资源名称，id列表
     * @param wallpaperName 选中的主题壁纸名称
     */
    public ThemeAdapter(Context context, List<Theme> list, String wallpaperName) {
        super(context, 0, list);
        this.mContext = context;
        this.mList = list;
        this.mWallpaperName = wallpaperName;
    }

    /**
     * 更新选中的主题壁纸位置
     *
     * @param wallpaperName 选中的壁纸名称
     */
    public void updateSelection(String wallpaperName) {
        mWallpaperName = wallpaperName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.gv_theme, parent, false);
            viewHolder = new ViewHolder();
            // 显示壁纸图片的imageView
            viewHolder.imageView = (RoundedImageView) convertView
                    .findViewById(R.id.bg);
            // 显示标记图标的imageView
            viewHolder.markIcon = (ImageView) convertView
                    .findViewById(R.id.icon);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Theme theme = mList.get(position);
        // 设置壁纸图片

        Glide.with(mContext).load(theme.getResId())
                .into(viewHolder.imageView);

        // 当选中的主题名称与当前相同
        if (mWallpaperName.equals(theme.getResName())) {
            // 设置标记图标
            viewHolder.markIcon.setImageResource(R.drawable.ic_wallpaper_mark);
        } else {
            // 清除标记图标
            viewHolder.markIcon.setImageResource(0);
        }
        return convertView;
    }

    /**
     * 保存控件实例
     */
    private final class ViewHolder {
        // 显示壁纸图片
        RoundedImageView imageView;
        // 显示标记图标
        ImageView markIcon;
    }

}
