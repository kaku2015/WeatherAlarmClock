package com.kaku.weac.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 管理图片的缓存类
 * 
 * @author 咖枯
 * @version 1.0 2015/05/26
 */
public class LruMemoryCache extends LruCache<String, Bitmap> {

	/**
	 * 管理图片缓存类的构造方法
	 * 
	 * @param maxSize
	 *            可用缓存的最大值
	 */
	public LruMemoryCache(int maxSize) {
		super(maxSize);
	}

	@Override
	protected int sizeOf(String key, Bitmap bitmap) {
		// 重写此方法来衡量每张图片的大小，默认返回图片数量。
		return bitmap.getByteCount() / 1024;
	}

	/**
	 * 添加Bitmap到缓存
	 * 
	 * @param key
	 *            Bitmap的key
	 * @param bitmap
	 *            Bitmap
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		// 缓存中不存在指定的Bitmap时添加Bitmap到缓存
		if (getBitmapFromMemCache(key) == null) {
			put(key, bitmap);
		}
	}

	/**
	 * 从缓存中取得Bitmap
	 * 
	 * @param key
	 *            Bitmap的key
	 * @return Bitmap
	 */
	public Bitmap getBitmapFromMemCache(String key) {
		return get(key);
	}

}
