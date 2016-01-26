/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.adapter.LocalAlbumDetailAdapter;
import com.kaku.weac.bean.Event.FinishLocalAlbumActivityEvent;
import com.kaku.weac.bean.Event.WallpaperEvent;
import com.kaku.weac.bean.ImageItem;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.OttoAppConfig;
import com.kaku.weac.zxing.activity.CaptureActivity;

import java.io.File;
import java.util.List;

/**
 * 本地相册详细图片Activity
 *
 * @author 咖枯
 * @version 1.0 2016/1/14
 */
public class LocalAlbumDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_IMAGE_CROP = 1;

    private LocalAlbumDetailAdapter mLocalAlbumDetailAdapter;
    private boolean mIsScanQRcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_album_detail);
        ViewGroup backGround = (ViewGroup) findViewById(R.id.background);
        MyUtil.setBackgroundBlur(backGround, this);
        mIsScanQRcode = getIntent().getBooleanExtra(CaptureActivity.SCAN_CODE, false);
        intiViews();
    }

    private void intiViews() {
        ImageView actionBack = (ImageView) findViewById(R.id.action_back);
        actionBack.setOnClickListener(this);

        TextView actionTitle = (TextView) findViewById(R.id.action_title);
        String title = getIntent().getStringExtra(LocalAlbumActivity.ALBUM_NAME);
        if (!TextUtils.isEmpty(title)) {
            actionTitle.setText(title);
        }

        List<ImageItem> localAlbumDetailList = getIntent().getParcelableArrayListExtra
                (LocalAlbumActivity.ALBUM_PATH);
        mLocalAlbumDetailAdapter = new LocalAlbumDetailAdapter(this, localAlbumDetailList);
        GridView albumPictureDetailGv = (GridView) findViewById(R.id.album_picture_detail_gv);
        albumPictureDetailGv.setAdapter(mLocalAlbumDetailAdapter);
        albumPictureDetailGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = mLocalAlbumDetailAdapter.getItem(position).getImagePath();
                // 主题
                if (!mIsScanQRcode) {
                    Uri uri = Uri.fromFile(new File(path));
                    Intent intent = MyUtil.getCropImageOptions(LocalAlbumDetailActivity.this, uri,
                            WeacConstants.DIY_WALLPAPER_PATH);
                    startActivityForResult(intent, REQUEST_IMAGE_CROP);
                    overridePendingTransition(0, 0);
                } else { // 扫描二维码
                    Intent intent = new Intent();
                    intent.putExtra(WeacConstants.IMAGE_URL, path);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    overridePendingTransition(0, R.anim.zoomout);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            // 截图
            case REQUEST_IMAGE_CROP:
                String filePath = MyUtil.getFilePath(this, WeacConstants.DIY_WALLPAPER_PATH);
                // 保存壁纸信息
                MyUtil.saveWallpaper(this, WeacConstants.WALLPAPER_PATH, filePath);
                // 发送壁纸更新事件
                OttoAppConfig.getInstance().post(new WallpaperEvent());
                // 发送关闭【本地相册activity】事件
                OttoAppConfig.getInstance().post(new FinishLocalAlbumActivityEvent());
                finish();
                overridePendingTransition(0, R.anim.zoomout);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
