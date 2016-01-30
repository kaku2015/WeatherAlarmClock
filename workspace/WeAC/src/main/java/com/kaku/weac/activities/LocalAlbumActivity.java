/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.adapter.LocalAlbumAdapter;
import com.kaku.weac.bean.Event.FinishLocalAlbumActivityEvent;
import com.kaku.weac.bean.Event.ScanCodeEvent;
import com.kaku.weac.bean.Event.WallpaperEvent;
import com.kaku.weac.bean.ImageBucket;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.db.LocalAlbumImagePickerHelper;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.OttoAppConfig;
import com.kaku.weac.zxing.activity.CaptureActivity;
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
    private static final int REQUEST_ALBUM_DETAIL = 3;

    public static final String ALBUM_PATH = "album_path";
    public static final String ALBUM_NAME = "album_name";
    private LocalAlbumAdapter mLocalAlbumAdapter;
    private List<ImageBucket> mLocalAlbumList;
    private AsyncTask<Void, Void, List<ImageBucket>> mBucketLoadTask;
    ListView mLocalAlbumListView;

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
//                showLoading();
            }

            @Override
            protected List<ImageBucket> doInBackground(Void... params) {
                return LocalAlbumImagePickerHelper.getInstance(LocalAlbumActivity.this)
                        .getImagesBucketList();
            }

            @Override
            protected void onPostExecute(List<ImageBucket> list) {
                dismissLoadingDialog();

                TextView emptyView = (TextView) findViewById(R.id.local_album_lv_empty);
                mLocalAlbumListView.setEmptyView(emptyView);

                mLocalAlbumList.addAll(list);
                mLocalAlbumAdapter.notifyDataSetChanged();
            }
        };

        mBucketLoadTask.execute();
    }

    private void dismissLoadingDialog() {
        ViewGroup progressBarLlyt = (ViewGroup) findViewById(R.id.progress_bar_llyt);
        progressBarLlyt.setVisibility(View.GONE);
    }

    private void assignViews() {
        ImageView backBtn = (ImageView) findViewById(R.id.action_back);
        TextView captureBtn = (TextView) findViewById(R.id.action_capture);

        backBtn.setOnClickListener(this);

        // 是否为扫码
        final boolean isScanQRcode = getIntent().getBooleanExtra(CaptureActivity.SCAN_CODE, false);
        // 主题
        if (!isScanQRcode) {
            captureBtn.setOnClickListener(this);
        } else { // 扫码
            captureBtn.setVisibility(View.GONE);
        }

        mLocalAlbumListView = (ListView) findViewById(R.id.local_album_lv);
        mLocalAlbumListView.setAdapter(mLocalAlbumAdapter);

        mLocalAlbumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LocalAlbumActivity.this, LocalAlbumDetailActivity.class);
                intent.putParcelableArrayListExtra(ALBUM_PATH,
                        mLocalAlbumAdapter.getItem(position).bucketList);
                intent.putExtra(ALBUM_NAME, mLocalAlbumAdapter.getItem(position).bucketName);
                intent.putExtra(CaptureActivity.SCAN_CODE, isScanQRcode);
                startActivityForResult(intent, REQUEST_ALBUM_DETAIL);
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
                PackageManager pm = getPackageManager();
                // FEATURE_CAMERA - 后置相机
                // FEATURE_CAMERA_FRONT - 前置相机
                if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mImageUri = Uri.fromFile(MyUtil.getFileDirectory(this, "/Android/data/" +
                            getPackageName() + "/capture/temporary.jpg"));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    overridePendingTransition(0, R.anim.zoomin);
                } else { // 没有可用相机
                    Intent intent = new Intent(this, MyDialogActivitySingle.class);
                    intent.putExtra(WeacConstants.TITLE, getString(R.string.prompt));
                    intent.putExtra(WeacConstants.DETAIL, getString(R.string.camera_error));
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            overridePendingTransition(0, R.anim.zoomout);
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
            // 相册详细图片
            case REQUEST_ALBUM_DETAIL:
                String url = data.getStringExtra(WeacConstants.IMAGE_URL);
                OttoAppConfig.getInstance().post(new ScanCodeEvent(url));
                myFinish2();
                break;
        }
    }

    @Subscribe
    public void finishMeEvent(FinishLocalAlbumActivityEvent event) {
        myFinish2();
    }

    @Override
    public void onBackPressed() {
        myFinish();
    }

    private void myFinish() {
        finish();
        overridePendingTransition(0, R.anim.zoomout);
    }

    private void myFinish2() {
        finish();
        overridePendingTransition(0, 0);
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
