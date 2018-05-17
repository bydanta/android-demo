package com.huanfeng.component;

import java.util.ArrayList;

import com.huanfeng.view.HFImageView;
import com.huanfeng.view.HFView;
import com.huanfeng.view.HFViewGroup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HFImageScrollBar extends HFViewGroup
{
	private int length;
	private int index;
	private Bitmap normarBm;
	private Bitmap selectBm;
	private ArrayList<HFImageView> pointList = new ArrayList<>();
	
	public HFImageScrollBar(Context context) {
		super(context);
	}

	public void initViews(int normalRes,int selectRes,int space,int pointLength){
		this.length = pointLength;
		this.normarBm = BitmapFactory.decodeResource(getResources(), normalRes);
		this.selectBm = BitmapFactory.decodeResource(getResources(), selectRes);
		int pointWidth = Math.max(normarBm.getWidth(), selectBm.getWidth());
		int width =  (pointWidth + space) * pointLength - space;
		int height = Math.max(normarBm.getHeight(), selectBm.getHeight());
		hfSetSize(width, height);
		for (int i = 0; i < pointLength; i++) {
			HFImageView point = hfAddView(HFImageView.hfCreate(getContext(), normarBm));
			point.hfSetWidth(pointWidth).hfSetHeight(1f);
			point.hfSetX(i * (pointWidth+space));
			pointList.add(point);
		}
	}
	
	public void setSelectIndex(int index){
		if(index < 0 || index >= length)
			return;
		HFImageView point = pointList.get(this.index);
		point.setImageBitmap(normarBm);
		point = pointList.get(index);
		point.setImageBitmap(selectBm);
		
		this.index = index;
	}
	
	public int getSelectIndex(){
		return index;
	}
}
