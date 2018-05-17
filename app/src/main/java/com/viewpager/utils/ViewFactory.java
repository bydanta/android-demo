package com.viewpager.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.huanfeng.tools.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import com.template.app.R;
import com.template.function.APP;

/**
 * ImageView创建工厂
 */
public class ViewFactory {

	private static DisplayImageOptions options;
	/**
	 * 获取ImageView视图的同时加载显示url
	 */
	public static ImageView getImageView(Context context, String url) {

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.mipmap.icon_empty)
				.showImageForEmptyUri(R.mipmap.icon_error)
				.showImageOnFail(R.mipmap.icon_stub)
				.cacheInMemory(false)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(R.layout.view_banner, null);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		Utils.imageLoader.displayImage(url, imageView, options);
		return imageView;
	}
}
