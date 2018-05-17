package com.huanfeng.component;

import java.util.ArrayList;

import com.huanfeng.view.HFView;
import com.huanfeng.view.HFViewGroup;

import android.content.Context;

public class HFPointScrollBar extends HFViewGroup
{
	private int length;
	private int index;
	private int normalColor;
	private int selectColor;
	private ArrayList<HFView> pointList = new ArrayList<>();
	
	public HFPointScrollBar(Context context) {
		super(context);
	}

	public void initViews(int normalColor,int selectColor, int pointSize,int space,int pointLength){
		this.length = pointLength;
		this.normalColor = normalColor;
		this.selectColor = selectColor;
		hfSetSize((pointSize + space) * pointLength - space, pointSize);
		for (int i = 0; i < pointLength; i++) {
			HFView point = hfAddView(new HFView(getContext())).hfSetSize(pointSize, pointSize);
			point.hfSetBorder(point.getWidth()/2, i == 0 ? selectColor : normalColor);
			point.setX(i * (pointSize+space));
			pointList.add(point);
		}
	}
	
	public void setSelectIndex(int index){
		if(index < 0 || index >= length)
			return;
		HFView point = pointList.get(this.index);
		point.hfSetBorder(point.getWidth()/2, normalColor);
		point = pointList.get(index);
		point.hfSetBorder(point.getWidth()/2, selectColor);
		
		this.index = index;
	}
	
	public int getSelectIndex(){
		return index;
	}
}
