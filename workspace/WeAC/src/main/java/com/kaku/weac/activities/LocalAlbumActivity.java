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
import com.kaku.weac.bean.Event.QRcodeLogoEvent;
import com.kaku.weac.bean.Event.ScanCodeEvent;
import com.kaku.weac.bean.Event.WallpaperEvent;
import com.kaku.weac.bean.ImageBucket;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.db.LocalAlbumImagePickerHelper;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.OttoAppConfig;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


/**
 * 本地相册Activity
 *
 * @author 咖枯
 * @version 1.0 2016/1/13
 */
public class LocalAlbumActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE_THEME = 1;
    private static final int REQUEST_IMAGE_CAPTURE_QRCODE_LOGO = 4;
    private static final int REQUEST_IMAGE_CROP_THEME = 2;
    private static final int REQUEST_IMAGE_CROP_QRCODE_LOGO = 5;
    private static final int REQUEST_ALBUM_DETAIL = 3;

    public static final String ALBUM_PATH = "album_path";
    public static final String ALBUM_NAME = "album_name";
    private LocalAlbumAdapter mLocalAlbumAdapter;
    private List<ImageBucket> mLocalAlbumList;
    private AsyncTask<Void, Void, List<ImageBucket>> mBucketLoadTask;
    private ListView mLocalAlbumListView;

    /**
     * 访问本地相册类型:0，主题；1，扫码；2，造码
     */
    private int mRequestType;

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

        mRequestType = getIntent().getIntExtra(WeacConstants.REQUEST_LOCAL_ALBUM_TYPE, 0);
        switch (mRequestType) {
            // 主题
            case 0:
                // 造码
            case 2:
                captureBtn.setOnClickListener(this);
                break;
            // 扫码
            case 1:
                // 隐藏拍照按钮
                captureBtn.setVisibility(View.GONE);
                break;
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
                intent.putExtra(WeacConstants.REQUEST_LOCAL_ALBUM_TYPE, mRequestType);
                startActivityForResult(intent, REQUEST_ALBUM_DETAIL);
            }
        });

        OverScrollDecoratorHelper.setUpOverScroll(mLocalAlbumListView);
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
                    // 访问相机类型
                    int requestType;
                    // 截取主题壁纸
                    if (mRequestType != 2) {
                        requestType = REQUEST_IMAGE_CAPTURE_THEME;
                    } else { // 截取二维码logo
                        requestType = REQUEST_IMAGE_CAPTURE_QRCODE_LOGO;
                    }

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mImageUri = Uri.fromFile(MyUtil.getFileDirectory(this, "/Android/data/" +
                            getPackageName() + "/capture/temporary.jpg"));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(intent, requestType);
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
            // 截图/相机返回
            overridePendingTransition(0, R.anim.zoomout);
            return;
        }

        // 扫描二维码相册详细取消
        if (data != null) {
            boolean isFinishMe = data.getBooleanExtra(LocalAlbumDetailActivity.FINISH_ACTIVITY, false);
            if (isFinishMe && !isFinishing()) {
                myFinish2();
                return;
            }
        }

        switch (requestCode) {
            // 拍照（截取主题壁纸）
            case REQUEST_IMAGE_CAPTURE_THEME:
                cropImage(0, REQUEST_IMAGE_CROP_THEME, WeacConstants.DIY_WALLPAPER_PATH);
                break;
            // 拍照（截取二维码logo）
            case REQUEST_IMAGE_CAPTURE_QRCODE_LOGO:
                cropImage(1, REQUEST_IMAGE_CROP_QRCODE_LOGO, WeacConstants.DIY_QRCODE_LOGO_PATH);
                break;
            // 截图（截取主题壁纸）
            case REQUEST_IMAGE_CROP_THEME:
                String filePath = MyUtil.getFilePath(this, WeacConstants.DIY_WALLPAPER_PATH);
                // 更新壁纸信息
                MyUtil.saveWallpaper(this, WeacConstants.WALLPAPER_PATH, filePath);
                // 发送壁纸更新事件
                OttoAppConfig.getInstance().post(new WallpaperEvent());
                myFinish();
                break;
            // 截图（截取二维码logo）
            case REQUEST_IMAGE_CROP_QRCODE_LOGO:
                String logoPath = MyUtil.getFilePath(this, WeacConstants.DIY_QRCODE_LOGO_PATH);
                // 保存自定义二维码logo地址
                MyUtil.saveQRcodeLogoPath(this, logoPath);
                // 发送自定义二维码logo截取地址事件
                OttoAppConfig.getInstance().post(new QRcodeLogoEvent(logoPath));
                myFinish();
                break;
            // 相册详细图片
            case REQUEST_ALBUM_DETAIL:
                assert data != null;
                String url = data.getStringExtra(WeacConstants.IMAGE_URL);
                OttoAppConfig.getInstance().post(new ScanCodeEvent(url));
                myFinish2();
                break;
        }
    }

    private void cropImage(int type, int requestType, String path) {
        Intent intent = MyUtil.getCropImageOptions(this, mImageUri, path, type);
        startActivityForResult(intent, requestType);
        overridePendingTransition(0, 0);
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
        if (mRequestType != 2) {
            overridePendingTransition(0, R.anim.zoomout);
        } else {
            overridePendingTransition(0, R.anim.move_out_bottom);
        }
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
