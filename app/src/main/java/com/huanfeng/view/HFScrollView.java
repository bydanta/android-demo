package com.huanfeng.view;

import com.huanfeng.tools.Utils;
import com.huanfeng.uitools.HFAid;
import com.huanfeng.uitools.UITools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.graphics.Point;
import android.view.ViewParent;

public class HFScrollView extends ScrollView
{

	public HFScrollView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getWidth(), getHeight());
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View v = getChildAt(i);
			v.measure(v.getWidth(), v.getHeight());
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View v = getChildAt(i);
			v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
		}
	}

	public <T extends View> T hfAddView(T view) {
		addView(view);
		return view;
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
	public HFScrollView hfSetPosition(int x, int y) {
		hfSetX(x);
		hfSetY(y);
		return this;
	}

	public HFScrollView hfSetX(int x) {
		int w = getWidth();
		setLeft(x);
		setRight(x + w);
		return this;
	}

	public HFScrollView hfSetY(int y) {
		int h = getHeight();
		setTop(y);
		setBottom(y + h);
		return this;
	}

	public HFScrollView hfSetPosition(double x, double y) {
		hfSetPosition(getParentWidthPercent(x), getParentHeightPercent(y));
		return this;
	}

	public HFScrollView hfSetX(double x) {
		hfSetX(getParentWidthPercent(x));
		return this;
	}

	public HFScrollView hfSetY(double y) {
		hfSetY(getParentHeightPercent(y));
		return this;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public HFScrollView hfSetCenter(int x, int y) {
		hfSetCenterX(x);
		hfSetCenterY(y);
		return this;
	}

	public HFScrollView hfSetCenterX(int x) {
		hfSetX(x - getWidth() / 2);
		return this;
	}

	public HFScrollView hfSetCenterY(int y) {
		hfSetY(y - getHeight() / 2);
		return this;
	}

	public HFScrollView hfSetCenter(double x, double y) {
		hfSetCenterX(x);
		hfSetCenterY(y);
		return this;
	}

	public HFScrollView hfSetCenterX(double x) {
		hfSetCenterX(getParentWidthPercent(x));
		return this;
	}

	public HFScrollView hfSetCenterY(double y) {
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

	public HFScrollView hfSetRight(int right) {
		int w = getWidth();
		setRight(right);
		setLeft(right - w);
		return this;
	}

	public HFScrollView hfSetBottom(int bottom) {
		int h = getHeight();
		setBottom(bottom);
		setTop(bottom - h);
		return this;
	}

	public HFScrollView hfSetRight(double right) {
		return hfSetRight(getParentWidthPercent(right));
	}

	public HFScrollView hfSetBottom(double bottom) {
		return hfSetBottom(getParentHeightPercent(bottom));
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public HFScrollView hfSetSize(int width, int height) {
		hfSetWidth(width);
		hfSetHeight(height);
		return this;
	}

	public HFScrollView hfSetWidth(int width) {
		setRight(getLeft() + width);
		return this;
	}

	public HFScrollView hfSetHeight(int height) {
		setBottom(getTop() + height);
		return this;
	}

	public HFScrollView hfSetSize(double width, double height) {
		hfSetWidth(width);
		hfSetHeight(height);
		return this;
	}

	public HFScrollView hfSetWidth(double width) {
		hfSetWidth(getParentWidthPercent(width));
		return this;
	}

	public HFScrollView hfSetHeight(double height) {
		hfSetHeight(getParentHeightPercent(height));
		return this;
	}

	public HFScrollView hfSetSizeKeepCenter(int width, int height) {
		hfSetWidthKeepCenter(width);
		hfSetHeightKeepCenter(height);
		return this;
	}

	public HFScrollView hfSetWidthKeepCenter(int width) {
		int x = hfGetCenterX();
		setRight(getLeft() + width);
		hfSetCenterX(x);
		return this;
	}

	public HFScrollView hfSetHeightKeepCenter(int height) {
		int y = hfGetCenterY();
		setBottom(getTop() + height);
		hfSetCenterY(y);
		return this;
	}

	public HFScrollView hfSetSizeKeepCenter(double width, double height) {
		hfSetWidthKeepCenter(width);
		hfSetHeightKeepCenter(height);
		return this;
	}

	public HFScrollView hfSetWidthKeepCenter(double width) {
		hfSetWidthKeepCenter(getParentWidthPercent(width));
		return this;
	}

	public HFScrollView hfSetHeightKeepCenter(double height) {
		hfSetHeightKeepCenter(getParentHeightPercent(height));
		return this;
	}

	public HFScrollView hfScaleSize(double scale) {
		int width = (int) (getWidth() * scale);
		int height = (int) (getHeight() * scale);
		hfSetSize(width, height);
		return this;
	}

	public HFScrollView hfSetDesignSizeScale(int width, int height) {
		float scaleX = (float) UITools.screenWidth / UITools.desiginWidth;
		float scaleY = (float) UITools.screenHeight / UITools.desiginHeight;
		float scale = Math.min(scaleX, scaleY);
		hfSetSize((int) (width * scale), (int) (height * scale));
		return this;
	}

	public HFScrollView hfSetDesignSizeScaleX(int width, int height) {
		float scale = (float) UITools.screenWidth / UITools.desiginWidth;
		hfSetSize((int) (width * scale), (int) (height * scale));
		return this;
	}

	public HFScrollView hfSetDesignSizeScaleY(int width, int height) {
		float scale = (float) UITools.screenHeight / UITools.desiginHeight;
		hfSetSize((int) (width * scale), (int) (height * scale));
		return this;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public HFScrollView hfSetBackgroundColor(int color) {
		setBackgroundColor(color);
		return this;
	}

	public HFScrollView hfSetTag(int tag) {
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

	public HFScrollView hfSetFillet(int width) {
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

	public HFScrollView hfSetBorder(int cornerRadius, int color) {
		return hfSetBorder(0, cornerRadius, color, color);
	}

	public HFScrollView hfSetBorder(int borderWidth, int cornerRadius, int backgroundColor, int borderColor) {
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
	public HFScrollView aid() {
		new HFAid(this);
		return this;
	}

	public HFScrollView hfCopyPostionFrom(View view) {
		hfSetPosition(view.getLeft(), view.getTop());
		return this;
	}

	public HFScrollView hfCopyCenterFrom(View view) {
		hfSetCenter(view.getLeft() + view.getWidth() / 2, view.getTop() + view.getHeight() / 2);
		return this;
	}

	public HFScrollView hfCopySizeFrom(View view) {
		hfSetSize(view.getWidth(), view.getHeight());
		return this;
	}

	public HFScrollView hfCopyFrameFrom(View view) {
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


	
// extension_view_end

}
