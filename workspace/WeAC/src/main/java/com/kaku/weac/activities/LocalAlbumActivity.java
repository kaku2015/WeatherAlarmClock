/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.adapter.LocalAlbumAdapter;
import com.kaku.weac.bean.Event.FinishLocalAlbumActivityEvent;
import com.kaku.weac.bean.Event.WallpaperEvent;
import com.kaku.weac.bean.ImageBucket;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.db.LocalAlbumImagePickerHelper;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.OttoAppConfig;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;


/**
 * 本地相册Activity
 *
 * @author 咖枯
 * @version 1.0 2016/1/13
 */
public class LocalAlbumActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_CROP = 2;

    public static final String ALBUM_PATH = "album_path";
    public static final String ALBUM_NAME = "album_name";
    private LocalAlbumAdapter mLocalAlbumAdapter;
    private List<ImageBucket> mLocalAlbumList;
    private AsyncTask<Void, Void, List<ImageBucket>> mBucketLoadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OttoAppConfig.getInstance().register(this);
        setContentView(R.layout.activity_local_album);
        ViewGroup backGround = (ViewGroup) findViewById(R.id.background);
        MyUtil.setBackgroundBlur(backGround, this);
        initAdapter();
        assignViews();
    }

    private void initAdapter() {
        mLocalAlbumList = new ArrayList<>();
        mLocalAlbumAdapter = new LocalAlbumAdapter(this, mLocalAlbumList);

        mBucketLoadTask = new AsyncTask<Void, Void, List<ImageBucket>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                toggleShowLoading(true, null);
            }

            @Override
            protected List<ImageBucket> doInBackground(Void... params) {
                return LocalAlbumImagePickerHelper.getInstance(LocalAlbumActivity.this)
                        .getImagesBucketList();
            }

            @Override
            protected void onPostExecute(List<ImageBucket> list) {
//                toggleShowLoading(false, null);

                mLocalAlbumList.addAll(list);
                mLocalAlbumAdapter.notifyDataSetChanged();
            }
        };

        mBucketLoadTask.execute();
    }

    private void assignViews() {
        ImageView backBtn = (ImageView) findViewById(R.id.action_back);
        TextView captureBtn = (TextView) findViewById(R.id.action_capture);

        backBtn.setOnClickListener(this);
        captureBtn.setOnClickListener(this);

        ListView localAlbumListView = (ListView) findViewById(R.id.local_album_lv);
        localAlbumListView.setAdapter(mLocalAlbumAdapter);
        LinearLayout emptyView = (LinearLayout) findViewById(R.id.local_album_lv_empty);
        localAlbumListView.setEmptyView(emptyView);
        localAlbumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LocalAlbumActivity.this, LocalAlbumDetailActivity.class);
                intent.putParcelableArrayListExtra(ALBUM_PATH,
                        mLocalAlbumAdapter.getItem(position).bucketList);
                intent.putExtra(ALBUM_NAME, mLocalAlbumAdapter.getItem(position).bucketName);
                startActivity(intent);
            }
        });
    }

    private Uri mImageUri;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回
            case R.id.action_back:
                myFinish();
                break;
            // 拍照
            case R.id.action_capture:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mImageUri = Uri.fromFile(MyUtil.getFileDirectory(this, WeacConstants
                        .DIY_WALLPAPER_PATH));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                overridePendingTransition(0, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            // 拍照
            case REQUEST_IMAGE_CAPTURE:
                Intent intent = MyUtil.getCropImageOptions(this, mImageUri, WeacConstants
                        .DIY_WALLPAPER_PATH);
                startActivityForResult(intent, REQUEST_IMAGE_CROP);
                overridePendingTransition(0, 0);
                break;
            // 截图
            case REQUEST_IMAGE_CROP:
                String filePath = mImageUri.getPath();
                // 更新壁纸信息
                MyUtil.saveWallpaper(this, WeacConstants.WALLPAPER_PATH, filePath);
                // 发送壁纸更新事件
                OttoAppConfig.getInstance().post(new WallpaperEvent());
                myFinish();
                break;
        }
    }

    @Subscribe
    public void finishMeEvent(FinishLocalAlbumActivityEvent event) {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        myFinish();
    }

    private void myFinish() {
        finish();
        overridePendingTransition(0, R.anim.zoomout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OttoAppConfig.getInstance().unregister(this);
        if (null != mBucketLoadTask && mBucketLoadTask.getStatus() == AsyncTask.Status.RUNNING) {
            mBucketLoadTask.cancel(true);
        }
    }
}
