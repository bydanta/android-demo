package com.huanfeng.view;

import android.R.bool;
import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.graphics.Point;
import android.view.ViewParent;

import com.huanfeng.tools.Utils;
import com.huanfeng.uitools.HFAid;
import com.huanfeng.uitools.UITools;

/**
 * Created by YXL on 2015/12/10.
 */
public class HFTextView extends TextView
{

	public static float defaultSize = 14;
	public static int defaultColor = Color.WHITE;

	public HFTextView(Context context) {
		super(context);
	}

	public HFTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HFTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public static HFTextView hfCreate(Context context, CharSequence text) {
		return hfCreate(context, text, defaultSize);
	}

	public static HFTextView hfCreate(Context context, CharSequence text, float size) {
		return hfCreate(context, text, size, defaultColor);
	}

	public static HFTextView hfCreate(Context context, CharSequence text, float size, int color) {
		return hfCreate(context, text, size, color, true);
	}

	public static HFTextView hfCreate(Context context, CharSequence text, float size, int color, boolean singleLine) {
		HFTextView v = new HFTextView(context);
		v.setText(text);
		v.setTextSize(size);
		v.setTextColor(color);
		v.setSingleLine(singleLine);
		v.setGravity(Gravity.LEFT);
		v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		v.setRight(v.getMeasuredWidth());
		v.setBottom(v.getMeasuredHeight());
		return v;
	}

	public static HFTextView hfCreate(Context context, int resId) {
		return hfCreate(context, resId, defaultSize);
	}

	public static HFTextView hfCreate(Context context, int resId, float size) {
		return hfCreate(context, resId, size, defaultColor);
	}

	public static HFTextView hfCreate(Context context, int resId, float size, int color) {
		return hfCreate(context, resId, size, color, true);
	}

	public static HFTextView hfCreate(Context context, int resId, float size, int color, boolean singleLine) {
		HFTextView v = new HFTextView(context);
		v.setText(resId);
		v.setTextSize(size);
		v.setTextColor(color);
		v.setSingleLine(singleLine);
		v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		v.setRight(v.getMeasuredWidth());
		v.setBottom(v.getMeasuredHeight());
		return v;
	}

	public HFTextView hfSetGravity(int gravity) {
		setGravity(gravity);
		return this;
	}

	public HFTextView hfSizeToFit() {
		measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		hfSetSize(getMeasuredWidth(), getMeasuredHeight());
		return this;
	}

	public HFTextView hfSetText(CharSequence text) {
		setText(text);
		hfSizeToFit();
		return this;
	}

	public HFTextView hfSetTextKeepCenter(CharSequence text) {
		setText(text);
		measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		hfSetSizeKeepCenter(getMeasuredWidth(), getMeasuredHeight());
		return this;
	}

	public HFTextView hfSetTextKeepRight(CharSequence text) {
		setText(text);
		measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		int right = getRight();
		hfSetSize(getMeasuredWidth(), getMeasuredHeight());
		hfSetRight(right);
		return this;
	}

	public HFTextView hfSetTextBold(boolean bold) {
		getPaint().setFakeBoldText(bold);
		return this;
	}

	// extension_view


	public Object tagObject1;
	public Object tagObject2;
	public Object tagObject3;
	public Object tagObject4;
	public Object tagObject5;

	public int getParentWidthPercent(double x) {
		int w = UITools.screenWidth;
		if (getParent() != null && getParent() instanceof ViewGroup) {
			int pw = ((ViewGroup) getParent()).getWidth();
			if (pw > 0)
				w = pw;
		}
		return (int) (w * x);
	}

	public int getParentHeightPercent(double y) {
		int h = UITools.screenHeight;
		if (getParent() != null && getParent() instanceof ViewGroup) {
			int ph = ((ViewGroup) getParent()).getHeight();
			if (ph > 0)
				h = ph;
		}
		return (int) (h * y);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public HFTextView hfSetPosition(int x, int y) {
		hfSetX(x);
		hfSetY(y);
		return this;
	}

	public HFTextView hfSetX(int x) {
		int w = getWidth();
		setLeft(x);
		setRight(x + w);
		return this;
	}

	public HFTextView hfSetY(int y) {
		int h = getHeight();
		setTop(y);
		setBottom(y + h);
		return this;
	}

	public HFTextView hfSetPosition(double x, double y) {
		hfSetPosition(getParentWidthPercent(x), getParentHeightPercent(y));
		return this;
	}

	public HFTextView hfSetX(double x) {
		hfSetX(getParentWidthPercent(x));
		return this;
	}

	public HFTextView hfSetY(double y) {
		hfSetY(getParentHeightPercent(y));
		return this;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public HFTextView hfSetCenter(int x, int y) {
		hfSetCenterX(x);
		hfSetCenterY(y);
		return this;
	}

	public HFTextView hfSetCenterX(int x) {
		hfSetX(x - getWidth() / 2);
		return this;
	}

	public HFTextView hfSetCenterY(int y) {
		hfSetY(y - getHeight() / 2);
		return this;
	}

	public HFTextView hfSetCenter(double x, double y) {
		hfSetCenterX(x);
		hfSetCenterY(y);
		return this;
	}

	public HFTextView hfSetCenterX(double x) {
		hfSetCenterX(getParentWidthPercent(x));
		return this;
	}

	public HFTextView hfSetCenterY(double y) {
		hfSetCenterY(getParentHeightPercent(y));
		return this;
	}

	public int hfGetCenterX() {
		return getLeft() + getWidth() / 2;
	}

	public int hfGetCenterY() {
		return getTop() + getHeight() / 2;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public HFTextView hfSetRight(int right) {
		int w = getWidth();
		setRight(right);
		setLeft(right - w);
		return this;
	}

	public HFTextView hfSetBottom(int bottom) {
		int h = getHeight();
		setBottom(bottom);
		setTop(bottom - h);
		return this;
	}

	public HFTextView hfSetRight(double right) {
		return hfSetRight(getParentWidthPercent(right));
	}

	public HFTextView hfSetBottom(double bottom) {
		return hfSetBottom(getParentHeightPercent(bottom));
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public HFTextView hfSetSize(int width, int height) {
		hfSetWidth(width);
		hfSetHeight(height);
		return this;
	}

	public HFTextView hfSetWidth(int width) {
		setRight(getLeft() + width);
		return this;
	}

	public HFTextView hfSetHeight(int height) {
		setBottom(getTop() + height);
		return this;
	}

	public HFTextView hfSetSize(double width, double height) {
		hfSetWidth(width);
		hfSetHeight(height);
		return this;
	}

	public HFTextView hfSetWidth(double width) {
		hfSetWidth(getParentWidthPercent(width));
		return this;
	}

	public HFTextView hfSetHeight(double height) {
		hfSetHeight(getParentHeightPercent(height));
		return this;
	}

	public HFTextView hfSetSizeKeepCenter(int width, int height) {
		hfSetWidthKeepCenter(width);
		hfSetHeightKeepCenter(height);
		return this;
	}

	public HFTextView hfSetWidthKeepCenter(int width) {
		int x = hfGetCenterX();
		setRight(getLeft() + width);
		hfSetCenterX(x);
		return this;
	}

	public HFTextView hfSetHeightKeepCenter(int height) {
		int y = hfGetCenterY();
		setBottom(getTop() + height);
		hfSetCenterY(y);
		return this;
	}

	public HFTextView hfSetSizeKeepCenter(double width, double height) {
		hfSetWidthKeepCenter(width);
		hfSetHeightKeepCenter(height);
		return this;
	}

	public HFTextView hfSetWidthKeepCenter(double width) {
		hfSetWidthKeepCenter(getParentWidthPercent(width));
		return this;
	}

	public HFTextView hfSetHeightKeepCenter(double height) {
		hfSetHeightKeepCenter(getParentHeightPercent(height));
		return this;
	}

	public HFTextView hfScaleSize(double scale) {
		int width = (int) (getWidth() * scale);
		int height = (int) (getHeight() * scale);
		hfSetSize(width, height);
		return this;
	}

	public HFTextView hfSetDesignSizeScale(int width, int height) {
		float scaleX = (float) UITools.screenWidth / UITools.desiginWidth;
		float scaleY = (float) UITools.screenHeight / UITools.desiginHeight;
		float scale = Math.min(scaleX, scaleY);
		hfSetSize((int) (width * scale), (int) (height * scale));
		return this;
	}

	public HFTextView hfSetDesignSizeScaleX(int width, int height) {
		float scale = (float) UITools.screenWidth / UITools.desiginWidth;
		hfSetSize((int) (width * scale), (int) (height * scale));
		return this;
	}

	public HFTextView hfSetDesignSizeScaleY(int width, int height) {
		float scale = (float) UITools.screenHeight / UITools.desiginHeight;
		hfSetSize((int) (width * scale), (int) (height * scale));
		return this;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public HFTextView hfSetBackgroundColor(int color) {
		setBackgroundColor(color);
		return this;
	}

	public HFTextView hfSetTag(int tag) {
		setTag(tag);
		return this;
	}

	public int hfGetTag() {
		return Utils.parseInt(getTag());
	}

	public void hfRemoveFromSuperView() {
		if (getParent() instanceof ViewGroup) {
			((ViewGroup) getParent()).removeView(this);
		}
	}

	public HFViewGroup hfGetParent() {
		return (HFViewGroup) getParent();
	}

	public HFTextView hfSetFillet(int width) {
		float[] outerRadii = new float[8];
		float[] innerRadii = new float[8];
		for (int i = 0; i < 8; i++) {
			outerRadii[i] = width;
			innerRadii[i] = width;
		}
		ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(outerRadii, new RectF(0, 0, 0, 0), innerRadii));
		int color = Color.GRAY;
		if (getBackground() instanceof ColorDrawable) {
			color = ((ColorDrawable) getBackground()).getColor();
		} else if (getBackground() instanceof ShapeDrawable) {
			color = ((ShapeDrawable) getBackground()).getPaint().getColor();
		}
		sd.getPaint().setColor(color);

		setBackgroundDrawable(sd);
		return this;
	}

	public HFTextView hfSetBorder(int cornerRadius, int color) {
		return hfSetBorder(0, cornerRadius, color, color);
	}

	public HFTextView hfSetBorder(int borderWidth, int cornerRadius, int backgroundColor, int borderColor) {
		float[] outerRadii = new float[8];
		float[] innerRadii = new float[8];
		for (int i = 0; i < 8; i++) {
			outerRadii[i] = cornerRadius;
			innerRadii[i] = cornerRadius;
		}
		ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(outerRadii, new RectF(borderWidth, borderWidth, borderWidth, borderWidth), innerRadii));
		setBackgroundColor(backgroundColor);
		sd.getPaint().setColor(borderColor);

		setBackgroundDrawable(sd);
		return this;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public HFTextView aid() {
		new HFAid(this);
		return this;
	}

	public HFTextView hfCopyPostionFrom(View view) {
		hfSetPosition(view.getLeft(), view.getTop());
		return this;
	}

	public HFTextView hfCopyCenterFrom(View view) {
		hfSetCenter(view.getLeft() + view.getWidth() / 2, view.getTop() + view.getHeight() / 2);
		return this;
	}

	public HFTextView hfCopySizeFrom(View view) {
		hfSetSize(view.getWidth(), view.getHeight());
		return this;
	}

	public HFTextView hfCopyFrameFrom(View view) {
		hfCopyPostionFrom(view);
		hfCopySizeFrom(view);
		return this;
	}

	public Point hfGetScreenPosition() {
		Point point = new Point((int) (getLeft() + getWidth() * (1 - getScaleX()) / 2), (int) (getTop() + getHeight() * (1 - getScaleY()) / 2));
		ViewParent parent = getParent();
		while (parent instanceof ViewGroup && parent != HFActivity.topActivity.contentView) {
			ViewGroup g = (ViewGroup) parent;
			point.x += (int) (g.getLeft() + g.getWidth() * (1 - g.getScaleX()) / 2);
			point.y += (int) (g.getTop() + g.getHeight() * (1 - g.getScaleY()) / 2);
			parent = g.getParent();
		}
		return point;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////// anim

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (getParent() instanceof HFViewGroup && widthMeasureSpec > 0 && heightMeasureSpec > 0)
			setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		else
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	
// extension_view_end
}
