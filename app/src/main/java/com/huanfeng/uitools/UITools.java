package com.huanfeng.uitools;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.huanfeng.tools.BitmapTools;
import com.huanfeng.tools.Utils;
import com.huanfeng.view.HFActivity;
import com.huanfeng.view.HFImageView;
import com.huanfeng.view.HFViewGroup;

import java.lang.reflect.Method;

/**
 * Created by YXL on 2015/12/9.
 */
public class UITools
{

	public static interface InitCallback
	{
		void onInitCompleted();
	}

	public static int screenWidth;
	public static int screenHeight;
	public static int screenWidthDP;
	public static int desiginWidth;
	public static int desiginHeight;
	public static float desiginDensity;
	public static int screenHeightDP;
	public static float scaleX;
	public static float scaleY;
	public static float scale;
	public static DisplayMetrics dm;

	private static InitCallback initCallback;
	private static boolean inited;
	private static HFActivity initActivity;

	public static int getWidthPercent(double w) {
		return (int) (w * screenWidth);
	}

	public static int getHeightPercent(double h) {
		return (int) (h * screenHeight);
	}

	public static final View.OnClickListener nullOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

		}
	};

	public static void init(HFActivity mainActivity, int desiginWidth, int desiginHeight, float desiginDensity, InitCallback callback) {
		UITools.initActivity = mainActivity;
		UITools.initCallback = callback;
		UITools.desiginWidth = desiginWidth;
		UITools.desiginHeight = desiginHeight;
		UITools.desiginDensity = desiginDensity;
		UITools.inited = false;
		mainActivity.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new InitAppInfo());

	}

	private static class InitAppInfo implements ViewTreeObserver.OnGlobalLayoutListener
	{
		@Override
		public void onGlobalLayout() {
			if (inited) {
				return;
			}
			inited = true;
			Rect outRect = new Rect();
			initActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
			screenWidth = outRect.width();
			screenHeight = outRect.height();
			dm = initActivity.getResources().getDisplayMetrics();
			screenWidthDP = (int) (screenWidth / dm.density);
			screenHeightDP = (int) (screenHeight / dm.density);
			scaleX = (float) screenWidthDP / (desiginWidth / desiginDensity);
			scaleY = (float) screenHeightDP / (desiginHeight / desiginDensity);
			scale = Math.min(scaleX, scaleY);
			if (initCallback != null) {
				initCallback.onInitCompleted();
			}
			initActivity = null;
			initCallback = null;
			Utils.print(dm, "111111111111111111");
		}
	}

	public static int getHFTag(View view) {
		try {
			Method m = view.getClass().getDeclaredMethod("hfGetTag");
			return (int) m.invoke(view);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public static void showMessage(Context context, String msg) {
		if (context == null)
			context = HFActivity.topActivity;
		new AlertDialog(context).builder().setMsg(msg).setCancelable(true).show();
	}

	public static void showConfirm(Context context, String msg, View.OnClickListener yesClickListener, View.OnClickListener onClickListener) {
		showConfirm(context, msg, "确定", "取消", yesClickListener, onClickListener);
	}

	public static void showConfirm(Context context, String msg, String yes, String no, View.OnClickListener yesClickListener, View.OnClickListener onClickListener) {
		if (yesClickListener == null)
			yesClickListener = nullOnClickListener;
		if (onClickListener == null)
			onClickListener = nullOnClickListener;
		new AlertDialog(context).builder().setMsg(msg).setPositiveButton(yes, yesClickListener).setNegativeButton(no, onClickListener).setCancelable(true).show();
	}

	public static void showToast(String text) {
		Toast.makeText(HFActivity.topActivity, text, Toast.LENGTH_SHORT).show();
	}

	public static boolean isInRangeOfView(View view, float px, float py) {
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		int x = location[0];
		int y = location[1];
		if (px < x || px > (x + view.getWidth()) || py < y || py > (y + view.getHeight())) {
			return false;
		}
		return true;
	}

	public static int px2dip(float pxValue) {
		return (int) (pxValue / dm.density + 0.5f);
	}

	public static int dip2px(float dipValue) {
		return (int) (dipValue * dm.density + 0.5f);
	}

	public static int px2sp(float pxValue) {
		return (int) (pxValue / dm.scaledDensity + 0.5f);
	}

	public static int sp2px(float spValue) {
		return (int) (spValue * dm.scaledDensity + 0.5f);
	}

	public static HFImageView addDemoImageView(HFViewGroup parent, int resid) {
		Bitmap bm = BitmapFactory.decodeResource(parent.getResources(), resid);
		Bitmap nBitmap = Bitmap.createBitmap(bm, 0, 40, bm.getWidth(), bm.getHeight() - 40);
		HFImageView iv = parent.hfAddView(HFImageView.hfCreate(parent.getContext(), nBitmap, ImageView.ScaleType.FIT_XY));
		iv.hfSetSize(1f, 1f);
		iv.setAlpha(0.4f);
		return iv;
	}

	public static void hideSoftKeyBoard() {
		if (HFActivity.topActivity != null && HFActivity.topActivity.getCurrentFocus() != null) {
			InputMethodManager imm = ((InputMethodManager) HFActivity.topActivity.getSystemService(Context.INPUT_METHOD_SERVICE));
			imm.hideSoftInputFromWindow(HFActivity.topActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public static Bitmap convertViewToBitmap(View view) {
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
		view.draw(new Canvas(bitmap));
		return bitmap;
	}

	@SuppressLint("NewApi")
	public static Bitmap blurBitmap(Bitmap bitmap,int radius) {
		if(android.os.Build.VERSION.SDK_INT < 17){
			return BitmapTools.fastblur(HFActivity.topActivity, bitmap, radius);
		}
		Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		RenderScript rs = RenderScript.create(HFActivity.topActivity.getApplicationContext());
		ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
		Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
		Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
		blurScript.setRadius(radius);
		blurScript.setInput(allIn);
		blurScript.forEach(allOut);
		allOut.copyTo(outBitmap);
		rs.destroy();
		return outBitmap;
	}
}
