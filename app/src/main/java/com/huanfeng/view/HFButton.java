package com.huanfeng.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TextView;

import com.huanfeng.tools.Utils;
import com.huanfeng.uitools.HFAid;
import com.huanfeng.uitools.UITools;

/**
 * Created by YXL on 2015/12/10.
 */
public class HFButton extends TextView
{
	private boolean useDownState = true;

	public HFButton(Context context) {
		super(context);
	}

	public HFButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HFButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (useDownState && isEnabled()) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				setAlpha(0.6f);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				setAlpha(1f);
			}
		}
		return super.onTouchEvent(event);
	}

	public static HFButton hfCreate(Context context, String text, int tag, OnClickListener onClickListener) {
		return hfCreate(context, text, 14, Color.WHITE, tag, onClickListener);
	}

	public static HFButton hfCreate(Context context, String text, float textSize, int textColor, int tag, OnClickListener onClickListener) {
		HFButton v = new HFButton(context);
		v.setText(text);
		v.setTextSize(textSize);
		v.setTextColor(textColor);
		v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		v.setRight(v.getMeasuredWidth());
		v.setBottom(v.getMeasuredHeight());
		v.setGravity(Gravity.CENTER);
		v.setTag(tag);
		v.setOnClickListener(onClickListener);
		return v;
	}

	public HFButton hfSetUseDownState(boolean use) {
		useDownState = use;
		return this;
	}

	public HFButton hfSetTextColor(int color) {
		setTextColor(color);
		return this;
	}

	public HFButton hfSetTextSize(float size) {
		setTextSize(size);
		return this;
	}

	public HFButton hfSizeToFit() {
		measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		hfSetSize(getMeasuredWidth(), getMeasuredHeight());
		return this;
	}

	public HFButton hfSetText(CharSequence text) {
		setText(text);
		hfSizeToFit();
		return this;
	}

	public HFButton hfSetTextBold(boolean bold) {
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
	public HFButton hfSetPosition(int x, int y) {
		hfSetX(x);
		hfSetY(y);
		return this;
	}

	public HFButton hfSetX(int x) {
		int w = getWidth();
		setLeft(x);
		setRight(x + w);
		return this;
	}

	public HFButton hfSetY(int y) {
		int h = getHeight();
		setTop(y);
		setBottom(y + h);
		return this;
	}

	public HFButton hfSetPosition(double x, double y) {
		hfSetPosition(getParentWidthPercent(x), getParentHeightPercent(y));
		return this;
	}

	public HFButton hfSetX(double x) {
		hfSetX(getParentWidthPercent(x));
		return this;
	}

	public HFButton hfSetY(double y) {
		hfSetY(getParentHeightPercent(y));
		return this;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public HFButton hfSetCenter(int x, int y) {
		hfSetCenterX(x);
		hfSetCenterY(y);
		return this;
	}

	public HFButton hfSetCenterX(int x) {
		hfSetX(x - getWidth() / 2);
		return this;
	}

	public HFButton hfSetCenterY(int y) {
		hfSetY(y - getHeight() / 2);
		return this;
	}

	public HFButton hfSetCenter(double x, double y) {
		hfSetCenterX(x);
		hfSetCenterY(y);
		return this;
	}

	public HFButton hfSetCenterX(double x) {
		hfSetCenterX(getParentWidthPercent(x));
		return this;
	}

	public HFButton hfSetCenterY(double y) {
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

	public HFButton hfSetRight(int right) {
		int w = getWidth();
		setRight(right);
		setLeft(right - w);
		return this;
	}

	public HFButton hfSetBottom(int bottom) {
		int h = getHeight();
		setBottom(bottom);
		setTop(bottom - h);
		return this;
	}

	public HFButton hfSetRight(double right) {
		return hfSetRight(getParentWidthPercent(right));
	}

	public HFButton hfSetBottom(double bottom) {
		return hfSetBottom(getParentHeightPercent(bottom));
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public HFButton hfSetSize(int width, int height) {
		hfSetWidth(width);
		hfSetHeight(height);
		return this;
	}

	public HFButton hfSetWidth(int width) {
		setRight(getLeft() + width);
		return this;
	}

	public HFButton hfSetHeight(int height) {
		setBottom(getTop() + height);
		return this;
	}

	public HFButton hfSetSize(double width, double height) {
		hfSetWidth(width);
		hfSetHeight(height);
		return this;
	}

	public HFButton hfSetWidth(double width) {
		hfSetWidth(getParentWidthPercent(width));
		return this;
	}

	public HFButton hfSetHeight(double height) {
		hfSetHeight(getParentHeightPercent(height));
		return this;
	}

	public HFButton hfSetSizeKeepCenter(int width, int height) {
		hfSetWidthKeepCenter(width);
		hfSetHeightKeepCenter(height);
		return this;
	}

	public HFButton hfSetWidthKeepCenter(int width) {
		int x = hfGetCenterX();
		setRight(getLeft() + width);
		hfSetCenterX(x);
		return this;
	}

	public HFButton hfSetHeightKeepCenter(int height) {
		int y = hfGetCenterY();
		setBottom(getTop() + height);
		hfSetCenterY(y);
		return this;
	}

	public HFButton hfSetSizeKeepCenter(double width, double height) {
		hfSetWidthKeepCenter(width);
		hfSetHeightKeepCenter(height);
		return this;
	}

	public HFButton hfSetWidthKeepCenter(double width) {
		hfSetWidthKeepCenter(getParentWidthPercent(width));
		return this;
	}

	public HFButton hfSetHeightKeepCenter(double height) {
		hfSetHeightKeepCenter(getParentHeightPercent(height));
		return this;
	}

	public HFButton hfScaleSize(double scale) {
		int width = (int) (getWidth() * scale);
		int height = (int) (getHeight() * scale);
		hfSetSize(width, height);
		return this;
	}

	public HFButton hfSetDesignSizeScale(int width, int height) {
		float scaleX = (float) UITools.screenWidth / UITools.desiginWidth;
		float scaleY = (float) UITools.screenHeight / UITools.desiginHeight;
		float scale = Math.min(scaleX, scaleY);
		hfSetSize((int) (width * scale), (int) (height * scale));
		return this;
	}

	public HFButton hfSetDesignSizeScaleX(int width, int height) {
		float scale = (float) UITools.screenWidth / UITools.desiginWidth;
		hfSetSize((int) (width * scale), (int) (height * scale));
		return this;
	}

	public HFButton hfSetDesignSizeScaleY(int width, int height) {
		float scale = (float) UITools.screenHeight / UITools.desiginHeight;
		hfSetSize((int) (width * scale), (int) (height * scale));
		return this;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public HFButton hfSetBackgroundColor(int color) {
		setBackgroundColor(color);
		return this;
	}

	public HFButton hfSetTag(int tag) {
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

	public HFButton hfSetFillet(int width) {
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

	public HFButton hfSetBorder(int cornerRadius, int color) {
		return hfSetBorder(0, cornerRadius, color, color);
	}

	public HFButton hfSetBorder(int borderWidth, int cornerRadius, int backgroundColor, int borderColor) {
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
	public HFButton aid() {
		new HFAid(this);
		return this;
	}

	public HFButton hfCopyPostionFrom(View view) {
		hfSetPosition(view.getLeft(), view.getTop());
		return this;
	}

	public HFButton hfCopyCenterFrom(View view) {
		hfSetCenter(view.getLeft() + view.getWidth() / 2, view.getTop() + view.getHeight() / 2);
		return this;
	}

	public HFButton hfCopySizeFrom(View view) {
		hfSetSize(view.getWidth(), view.getHeight());
		return this;
	}

	public HFButton hfCopyFrameFrom(View view) {
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
