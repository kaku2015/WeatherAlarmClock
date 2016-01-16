/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.adapter.LocalAlbumDetailAdapter;
import com.kaku.weac.bean.ImageItem;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.util.MyUtil;

import java.io.File;
import java.util.List;

/**
 * 本地相册详细图片Activity
 *
 * @author 咖枯
 * @version 1.0 2016/1/14
 */
public class LocalAlbumDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST__IMAGE_CROP = 1;

    private LocalAlbumDetailAdapter mLocalAlbumDetailAdapter;
    private ViewGroup backGround;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_album_detail);
        backGround = (ViewGroup) findViewById(R.id.background);
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

        List<ImageItem> localAlbumDetailList = bundle.getParcelableArrayList(LocalAlbumActivity
                .ALBUM_PATH);
        mLocalAlbumDetailAdapter = new LocalAlbumDetailAdapter(this, localAlbumDetailList);
        GridView albumPictureDetailGv = (GridView) findViewById(R.id.album_picture_detail_gv);
        albumPictureDetailGv.setAdapter(mLocalAlbumDetailAdapter);
        albumPictureDetailGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = mLocalAlbumDetailAdapter.getItem(position).getImagePath();

                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;

                Intent intent = new Intent();
                intent.setAction("com.android.camera.action.CROP");
                Uri uri = Uri.fromFile(new File(path));
                intent.setDataAndType(uri, "image/*");
                intent.putExtra("crop", "true");
                // 裁剪框比例
                intent.putExtra("aspectX", width);
                intent.putExtra("aspectY", height);

                String filePath = WeacConstants.DIY_WALLPAPER_PATH + "/1.jpg";
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(MyUtil.getFileDirectory
                        (LocalAlbumDetailActivity.this, filePath)));
                // 是否去除面部检测
                intent.putExtra("noFaceDetection", true);
                // 是否保留比例
                intent.putExtra("scale", true);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                // 裁剪区的宽高
                intent.putExtra("outputX", width);
                intent.putExtra("outputY", height);
                // 是否将数据保留在Bitmap中返回
                intent.putExtra("return-data", false);

                startActivityForResult(intent, REQUEST__IMAGE_CROP);
                overridePendingTransition(R.anim.move_in_bottom, 0);
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
            case REQUEST__IMAGE_CROP:
                String filePath = MyUtil.getFilePath(this, WeacConstants.DIY_WALLPAPER_PATH +
                        "/1.jpg");
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                if (bitmap != null) {
                    backGround.setBackground(Drawable.createFromPath(filePath));
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
