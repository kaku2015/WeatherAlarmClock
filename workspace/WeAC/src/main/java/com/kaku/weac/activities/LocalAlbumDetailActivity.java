/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.adapter.LocalAlbumDetailAdapter;
import com.kaku.weac.bean.ImageItem;
import com.kaku.weac.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地相册详细图片Activity
 *
 * @author 咖枯
 * @version 1.0 2016/1/14
 */
public class LocalAlbumDetailActivity extends BaseActivity implements View.OnClickListener {
    private LocalAlbumDetailAdapter mLocalAlbumDetailAdapter;
    private List<ImageItem> mLocalAlbumDetailList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_album_detail);
        ViewGroup backGround = (ViewGroup) findViewById(R.id.background);
        MyUtil.setBackgroundBlur(backGround, this);
        intiViews();
    }

    private void intiViews() {
        Bundle bundle = getIntent().getExtras();

        ImageView actionBack = (ImageView) findViewById(R.id.action_back);
        actionBack.setOnClickListener(this);

        TextView actionTitle = (TextView) findViewById(R.id.action_title);
        String title = bundle.getString(LocalAlbumActivity.ALBUM_NAME);
        if (!TextUtils.isEmpty(title)) {
            actionTitle.setText(title);
        }

        mLocalAlbumDetailList = bundle.getParcelableArrayList(LocalAlbumActivity.ALBUM_PATH);
        mLocalAlbumDetailAdapter = new LocalAlbumDetailAdapter(this, mLocalAlbumDetailList);
        GridView albumPictureDetailGv = (GridView) findViewById(R.id.album_picture_detail_gv);
        albumPictureDetailGv.setAdapter(mLocalAlbumDetailAdapter);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
