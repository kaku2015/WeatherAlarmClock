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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kaku.weac.R;
import com.kaku.weac.bean.ImageItem;

import java.util.List;

/**
 * 本地相册详细图片适配器
 *
 * @author 咖枯
 * @version 1.0 2016/1/15
 */
public class LocalAlbumDetailAdapter extends ArrayAdapter<ImageItem> {

    private Context mContext;

    public LocalAlbumDetailAdapter(Context context, List<ImageItem> localAlbumList) {
        super(context, 0, localAlbumList);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.gv_local_album_detail, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageItem imageItem = getItem(position);
        if (null != imageItem) {
            String imagePath = imageItem.getImagePath();
            if (!TextUtils.isEmpty(imagePath)) {
                Glide.with(mContext).load("file://" + imagePath).
                        placeholder(R.color.default_image_background).into(viewHolder.mPictureIv);
            }
        }
        return convertView;
    }

    public class ViewHolder {
        public final ImageView mPictureIv;

        public ViewHolder(View view) {
            mPictureIv = (ImageView) view.findViewById(R.id.picture_iv);
        }
    }
}
