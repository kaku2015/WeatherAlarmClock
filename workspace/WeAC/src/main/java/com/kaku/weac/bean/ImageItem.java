/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图片信息实例
 *
 * @author 咖枯
 * @version 1.0 2016/1/13
 */
public class ImageItem implements Parcelable {
    /**
     * 图片id
     */
    private String imageId;

    /**
     * 图片缩略图path
     */
    private String thumbnailPath;

    /**
     * 图片path
     */
    private String imagePath;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageId);
        dest.writeString(this.thumbnailPath);
        dest.writeString(this.imagePath);
    }

    public ImageItem() {
    }

    private ImageItem(Parcel in) {
        this.imageId = in.readString();
        this.thumbnailPath = in.readString();
        this.imagePath = in.readString();
    }

    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
        public ImageItem createFromParcel(Parcel source) {
            return new ImageItem(source);
        }

        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
