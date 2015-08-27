package com.kaku.weac.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.kaku.weac.R;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.LruMemoryCache;

/**
 * 主题壁纸适配器类
 * 
 * @author 咖枯
 * @version 1.0 2015
 */
public class ThemeAdapter extends ArrayAdapter<Integer> {

	/**
	 * Log tag ：ThemeAdapter
	 */
	private static final String LOG_TAG = "ThemeAdapter";

	/**
	 * activity上下文
	 */
	private Context mContext;

	/**
	 * 资源id列表
	 */
	private List<Integer> mList;

	/**
	 * 主题壁纸位置
	 */
	private int mWallpaperPosition;

	/**
	 * 管理图片的缓存
	 */
	private LruMemoryCache mMemoryCache;

	/**
	 * 图片显示的宽度
	 */
	private final int reqWidth;

	/**
	 * 图片显示的高度
	 */
	private final int reqHeight;

	/**
	 * 主题壁纸适配器
	 * 
	 * @param context
	 *            activity上下文
	 * @param list
	 *            壁纸资源id列表
	 * @param wallpaperPosition
	 *            选中的主题壁纸位置
	 * @param memoryCache
	 *            管理图片的缓存类
	 * @param reqWidth
	 *            图片显示的宽度
	 * @param reqHeight
	 *            图片显示的高度
	 */
	public ThemeAdapter(Context context, List<Integer> list,
			int wallpaperPosition, LruMemoryCache memoryCache, int reqWidth,
			int reqHeight) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
		this.mWallpaperPosition = wallpaperPosition;
		this.mMemoryCache = memoryCache;
		this.reqWidth = reqWidth;
		this.reqHeight = reqHeight;

	}

	/**
	 * 更新选中的主题壁纸位置
	 * 
	 * @param position
	 *            选中的壁纸位置
	 */
	public void updateSelection(int position) {
		mWallpaperPosition = position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.gv_theme, parent, false);
			viewHolder = new ViewHolder();
			// 显示壁纸图片的imageView
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.bg);
			// 显示标记图标的imageView
			viewHolder.markIcon = (ImageView) convertView
					.findViewById(R.id.icon);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 设置壁纸图片
		LoadBitmap(mList.get(position), viewHolder.imageView);
		// 当选中的主题位置为当前位置时
		if (mWallpaperPosition == position) {
			// 设置标记图标
			viewHolder.markIcon.setImageResource(R.drawable.ic_wallpaper_mark);
		} else {
			// 清除标记图标
			viewHolder.markIcon.setImageResource(0);
		}
		return convertView;
	}

	/**
	 * 保存控件实例
	 * 
	 */
	private final class ViewHolder {
		// 显示壁纸图片
		ImageView imageView;
		// 显示标记图标
		ImageView markIcon;
	}

	/**
	 * 加载Bitmap设置壁纸图片
	 * 
	 * @param resId
	 *            资源图片Id
	 * @param imageView
	 *            ImageView控件
	 */
	private void LoadBitmap(int resId, ImageView imageView) {
		// 定义Bitmap的key
		final String imageKey = String.valueOf(resId);
		// 从缓存中取得Bitmap
		final Bitmap bitmap = mMemoryCache.getBitmapFromMemCache(imageKey);
		if (bitmap != null) {
			// 设置壁纸图片
			imageView.setImageBitmap(bitmap);
		} else {
			// 设置壁纸图片暂为缓存图片
			imageView.setImageResource(R.drawable.ic_pic_loading);
			// 创建异步线程类
			BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			// 开启线程更新图片显示
			task.execute(resId);
		}
	}

	/**
	 * 创建并向内存添加图片的Bitmap异步类
	 * 
	 */
	private class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
		/**
		 * 显示壁纸图片的imageView
		 */
		private ImageView mImageView;

		/**
		 * 创建并向内存添加Bitmap的异步类构造方法
		 * 
		 * @param imageView
		 *            显示图片的ImageView控件
		 */
		public BitmapWorkerTask(ImageView imageView) {
			mImageView = imageView;
		}

		// 在后台加载图片
		@Override
		protected Bitmap doInBackground(Integer... params) {

			// 根据图片创建指定大小的Bitmap
			final Bitmap bitmap = decodeSampledBitmapFromResource(
					mContext.getResources(), params[0], reqWidth, reqHeight);
			mMemoryCache.addBitmapToMemoryCache(String.valueOf(params[0]),
					bitmap);
			LogUtil.i(LOG_TAG, "缓存大小： " + mMemoryCache.size() / 1024);
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			// 设置壁纸图片
			mImageView.setImageBitmap(bitmap);
		}

	}

	/**
	 * 创建控件大小程度的Bitmap
	 * 
	 * @param res
	 *            资源的引用
	 * @param resId
	 *            图片Id
	 * @param reqWidth
	 *            显示图片的宽
	 * @param reqHeight
	 *            显示图片的高
	 * @return
	 */
	private static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 取得源图片的参数options
		BitmapFactory.decodeResource(res, resId, options);
		// 计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);

	}

	/**
	 * 计算inSampleSize值
	 * 
	 * @param options
	 *            源图片的参数信息
	 * @param reqWidth
	 *            显示图片的宽
	 * @param reqHeight
	 *            显示图片的高
	 * @return
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;

	}

}
