/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;

import com.kaku.weac.bean.ImageBucket;
import com.kaku.weac.bean.ImageItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * 本地相册照片读取
 *
 * @author 咖枯
 * @version 1.0 2016/01/13
 */
public class LocalAlbumImagePickerHelper {

    private ContentResolver mContentResolver;

    /**
     * 相册缩略图
     */
    private HashMap<String, String> mThumbnailList = new HashMap<>();

    /**
     * 相册列表信息
     */
    private HashMap<String, ImageBucket> mImageBucketList = new HashMap<>();

    private static LocalAlbumImagePickerHelper sInstance;

    private LocalAlbumImagePickerHelper(Context appContext) {
        mContentResolver = appContext.getContentResolver();
    }

    public static LocalAlbumImagePickerHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new LocalAlbumImagePickerHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * 取得图片缩略图信息
     */
    private void getThumbnail() {
        String[] projection = {Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA};
        Cursor cursor = mContentResolver.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null,
                null, null);
        if (cursor != null) {
            getThumbnailColumnData(cursor);
            cursor.close();
        }
    }

    private void getThumbnailColumnData(Cursor cursor) {
        mThumbnailList.clear();
        if (cursor.moveToFirst()) {
            int image_idColumn = cursor.getColumnIndex(Thumbnails.IMAGE_ID);
            int image_pathColumn = cursor.getColumnIndex(Thumbnails.DATA);

            do {
                // 图片缩略图id
                int image_id = cursor.getInt(image_idColumn);
                // 图片缩略图path
                String image_path = cursor.getString(image_pathColumn);

                mThumbnailList.put("" + image_id, image_path);
            } while (cursor.moveToNext());
        }
    }

    boolean hasBuildImagesBucketList = false;

    /**
     * 取得相册图片列表
     */
    private void buildImagesBucketList() {
        getThumbnail();
        mImageBucketList.clear();

        String columns[] = new String[]{Media._ID, Media.BUCKET_ID, Media.PICASA_ID, Media.DATA,
                Media.DISPLAY_NAME, Media.TITLE, Media.SIZE, Media.BUCKET_DISPLAY_NAME};
        Cursor cursor = mContentResolver.query(Media.EXTERNAL_CONTENT_URI, columns, null, null,
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int photoIDIndex = cursor.getColumnIndexOrThrow(Media._ID);
                int photoPathIndex = cursor.getColumnIndexOrThrow(Media.DATA);
                int bucketDisplayNameIndex = cursor.getColumnIndexOrThrow(Media
                        .BUCKET_DISPLAY_NAME);
                int bucketIdIndex = cursor.getColumnIndexOrThrow(Media.BUCKET_ID);

                do {
                    // 相片id
                    String _id = cursor.getString(photoIDIndex);
                    // 相片path
                    String path = cursor.getString(photoPathIndex);
                    // 相册名
                    String bucketName = cursor.getString(bucketDisplayNameIndex);
                    // 相册id
                    String bucketId = cursor.getString(bucketIdIndex);

                    ImageBucket bucket = mImageBucketList.get(bucketId);
                    // 相同的相册名，不重复添加
                    if (bucket == null) {
                        bucket = new ImageBucket();
                        mImageBucketList.put(bucketId, bucket);
                        bucket.bucketList = new ArrayList<>();
                        bucket.bucketName = bucketName;
                    }
                    bucket.count++;
                    ImageItem imageItem = new ImageItem();
                    imageItem.setImageId(_id);
                    imageItem.setImagePath(path);
                    imageItem.setThumbnailPath(mThumbnailList.get(_id));
                    bucket.bucketList.add(imageItem);

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        hasBuildImagesBucketList = true;
    }

    /**
     * 取得相册图片列表
     *
     * @return 相册图片列表
     */
    public List<ImageBucket> getImagesBucketList() {
        buildImagesBucketList();
        List<ImageBucket> imageBucketList = new ArrayList<>();
        for (Entry<String, ImageBucket> entry : mImageBucketList.entrySet()) {
            imageBucketList.add(entry.getValue());
        }
        return imageBucketList;
    }

}
