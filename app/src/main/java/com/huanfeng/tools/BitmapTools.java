package com.huanfeng.tools;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapTools {
	/**
	 * 模糊
	 * **/
	public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {

		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

		if (radius < 1) {
			return (null);
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		// Log.e("pix", w + " " + h + " " + pix.length);
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int temp = 256 * divsum;
		int dv[] = new int[temp];
		for (i = 0; i < temp; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		// Log.e("pix", w + " " + h + " " + pix.length);
		bitmap.setPixels(pix, 0, w, 0, 0, w, h);
		return (bitmap);
	}

	/**
	 * 通过图片uri进行缩放
	 *
	 * @param uri
	 * @param context
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Bitmap getScaleBitmapByUri(Context context, Uri uri, int width) throws FileNotFoundException {
		Bitmap bitmap;
		int w;
		InputStream is = context.getContentResolver().openInputStream(uri);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, options);
		w = options.outWidth;
		if (w > width) {
			options.inSampleSize = w / width;
			options.inJustDecodeBounds = false;
			String path = getPicPathByUri(context, uri);
			bitmap = BitmapFactory.decodeFile(path, options);
		}
		else{
			options.inSampleSize = w / width;
			options.inJustDecodeBounds = false;
			String path = getPicPathByUri(context, uri);
			bitmap = BitmapFactory.decodeFile(path, options);
		}
		return bitmap;
	}

	public static Bitmap getBitmapByUri(Uri uri, Context context) throws FileNotFoundException {
		Bitmap bitmap;
		int w;
		InputStream is = context.getContentResolver().openInputStream(uri);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeStream(is, null, options);
		return bitmap;
	}


	/**
	 * 通过图片路径进行图片缩放
	 *
	 * @param path
	 * @param context
	 * @return
	 */
	public static Bitmap getScaleBitmapByPath(String path, Context context, int width) {
		Bitmap bitmap;
		int w;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(path, options);
		w = options.outWidth;
		if (w > width) {
			options.inSampleSize = w / width;
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(path, options);
		} else {
			bitmap = BitmapFactory.decodeFile(path);
		}
		return bitmap;
	}

	//使用Bitmap加Matrix来缩放
	public static Bitmap resizeImage(Bitmap bitmap, float w, float h) {
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		float newWidth = w;
		float newHeight = h;
		float scaleWidth = ((float) newWidth) / (float) width;
		float scaleHeight = ((float) newHeight) / (float) height;
		Log.i("INFO", "scaleWidth" + scaleWidth);
		Log.i("INFO", "scaleHeight" + scaleHeight);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleWidth);
		// if you want to rotate the Bitmap
		// matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);
		Log.i("INFO", "resizedBitmapWidth" + resizedBitmap.getWidth());
		Log.i("INFO", "resizedBitmapHeight" + resizedBitmap.getHeight());
		return resizedBitmap;

	}

	/**
	 * 通过uri获取图片的路径
	 *
	 * @param uri
	 * @param context
	 * @return
	 */
	public static String getPicPathByUri(Context context, Uri uri) {

		String path;
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
		if(cursor!=null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			path = picturePath;
		}
		else {
			path = uri.getPath();
		}

		return path;
	}

	/**
	 * 根据uri获取图片路径
	 * **/
	public static String getPathFromUri(Context mContext, Uri contentUri) {
		String[] proj = {MediaStore.Images.Media.DATA};
		CursorLoader loader = new CursorLoader(mContext, contentUri, proj, null, null, null);
		Cursor cursor = loader.loadInBackground();
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	//获取图片的旋转角度
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 将图片纠正到正确方向
	 *
	 * @param degree ： 图片被系统旋转的角度
	 * @param bitmap ： 需纠正方向的图片
	 * @return 纠向后的图片
	 */
	public static Bitmap rotateBitmap(int degree, Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);

		Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return bm;
	}

	/**
	 * 通过URL获取图片
	 *
	 * @param Url
	 * @return
	 */
	public static Bitmap getImageByUrl(String Url) throws Exception {

		try {
			Log.e("src", Url);
			URL url = new URL(Url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			Log.e("Bitmap", "returned");
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("Exception", e.getMessage());
			return null;
		}
	}

	/**
	 * 保存图片到SD卡并返回File格式
	 *
	 * @param bmp : 图片
	 * @param filename 新文件名称
	 * **/
	public static File saveBitmaptoFile(Bitmap bmp, String filename) {
		File file = new File("/sdcard/bee");
		if (!file.exists()) {
			file.mkdir();
		}
		Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
		int quality = 100;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream("/sdcard/bee/" + filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (bmp.compress(format, quality, stream)) {
			return new File("/sdcard/bee/" + filename);
		} else {
			return null;
		}
	}
}
