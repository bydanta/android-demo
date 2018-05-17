package com.huanfeng.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.LinkedList;

import com.huanfeng.tools.Utils;
import com.huanfeng.uitools.UITools;

/**
 * Created by YXL on 2015/12/8.
 */
public class HFActivity extends Activity
{

	public static HFActivity topActivity;
	public HFViewGroup contentView;
	private HFViewGroup maskViewTop;
	private HFViewGroup maskViewCenter;
	private HFViewGroup maskViewBack;
	private boolean destroyed;

	private LinkedList<OnKeyListener> keyListeners = new LinkedList<>();

	public final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			HFActivity.this.handleMessage(msg);
		};
	};

	protected void print(Object... args) {
		Utils.print(args);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		Log.e("finalize", this.toString());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		topActivity = this;
		contentView = new HFViewGroup(this);
		contentView.setRight(UITools.screenWidth);
		contentView.setBottom(UITools.screenHeight);
		contentView.setClickable(true);
		setContentView(contentView);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		addMaskView();
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		addMaskView();
	}

	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		super.setContentView(view, params);
		addMaskView();
	}

	private void addMaskView() {
		addContentView(maskViewBack = new HFViewGroup(this), new ViewGroup.LayoutParams(contentView.getWidth(), contentView.getHeight()));
		addContentView(maskViewCenter = new HFViewGroup(this), new ViewGroup.LayoutParams(contentView.getWidth(), contentView.getHeight()));
		addContentView(maskViewTop = new HFViewGroup(this), new ViewGroup.LayoutParams(contentView.getWidth(), contentView.getHeight()));
		maskViewBack.hfSetSize(contentView.getWidth(), contentView.getHeight());
		maskViewCenter.hfSetSize(contentView.getWidth(), contentView.getHeight());
		maskViewTop.hfSetSize(contentView.getWidth(), contentView.getHeight());
	}

	public void updateContentSize() {
		contentView.setRight(UITools.screenWidth);
		contentView.setBottom(UITools.screenHeight);
		maskViewBack.hfSetSize(contentView.getWidth(), contentView.getHeight());
		maskViewCenter.hfSetSize(contentView.getWidth(), contentView.getHeight());
		maskViewTop.hfSetSize(contentView.getWidth(), contentView.getHeight());
	}

	public void addOnKeyListener(OnKeyListener listener) {
		if (!keyListeners.contains(listener)) {
			keyListeners.addFirst(listener);
		}
	}

	public void removeOnKeyListener(OnKeyListener listener) {
		keyListeners.remove(listener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		topActivity = this;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		destroyed = true;
	}

	public <T extends View> T addMaskViewBack(T view) {
		maskViewBack.addView(view);
		return view;
	}

	public <T extends View> T addMaskViewCenter(T view) {
		maskViewCenter.addView(view);
		return view;
	}

	public <T extends View> T addMaskViewTop(T view) {
		maskViewTop.addView(view);
		return view;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		View v = getWindow().getCurrentFocus();
		if (v != null)
			v.onKeyDown(keyCode, event);
		for (OnKeyListener onKeyListener : keyListeners) {
			if (onKeyListener.onKey(null, keyCode, event)) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		for (OnKeyListener onKeyListener : keyListeners) {
			if (onKeyListener.onKey(null, keyCode, event)) {
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		for (OnKeyListener onKeyListener : keyListeners) {
			if (onKeyListener.onKey(null, keyCode, event)) {
				return true;
			}
		}
		return super.onKeyLongPress(keyCode, event);
	}

	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		for (OnKeyListener onKeyListener : keyListeners) {
			if (onKeyListener.onKey(null, keyCode, event)) {
				return true;
			}
		}
		return super.onKeyMultiple(keyCode, repeatCount, event);
	}

	@Override
	public boolean onKeyShortcut(int keyCode, KeyEvent event) {
		for (OnKeyListener onKeyListener : keyListeners) {
			if (onKeyListener.onKey(null, keyCode, event)) {
				return true;
			}
		}
		return super.onKeyShortcut(keyCode, event);
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void handleMessage(Message msg) {

	}

	public int dp(float dp) {
		return UITools.dip2px(dp);
	}
}
