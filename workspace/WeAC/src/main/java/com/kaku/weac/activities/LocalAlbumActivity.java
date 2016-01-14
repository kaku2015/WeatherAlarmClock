/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.kaku.weac.R;
import com.kaku.weac.adapter.LocalAlbumAdapter;
import com.kaku.weac.bean.ImageBucket;
import com.kaku.weac.db.LocalAlbumImagePickerHelper;
import com.kaku.weac.util.MyUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 本地相册Activity
 *
 * @author 咖枯
 * @version 1.0 2016/1/13
 */
public class LocalAlbumActivity extends BaseActivity implements View.OnClickListener {
    private ListView mLocalAlbumListView;
    private LocalAlbumAdapter mLocalAlbumAdapter;
    private List<ImageBucket> mLocalAlbumList;
    private AsyncTask<Void, Void, List<ImageBucket>> mBucketLoadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                return LocalAlbumImagePickerHelper.getInstance(LocalAlbumActivity.this).getImagesBucketList();
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
        ImageView mBackBtn;
        mBackBtn = (ImageView) findViewById(R.id.back_btn);
        mBackBtn.setOnClickListener(this);

        mLocalAlbumListView = (ListView) findViewById(R.id.local_album_lv);
        mLocalAlbumListView.setAdapter(mLocalAlbumAdapter);
        LinearLayout emptyView = (LinearLayout) findViewById(R.id.local_album_lv_empty);
        mLocalAlbumListView.setEmptyView(emptyView);
    }

    @Override
    public void onClick(View v) {
        myFinish();
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
        if (null != mBucketLoadTask && mBucketLoadTask.getStatus() == AsyncTask.Status.RUNNING) {
            mBucketLoadTask.cancel(true);
        }
    }
}
