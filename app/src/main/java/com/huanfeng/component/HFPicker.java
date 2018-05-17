package com.huanfeng.component;

import java.util.ArrayList;
import java.util.List;

import com.huanfeng.uitools.AnimationAdapter;
import com.huanfeng.uitools.AnimationTools;
import com.huanfeng.uitools.UITools;
import com.huanfeng.view.HFActivity;
import com.huanfeng.view.HFButton;
import com.huanfeng.view.HFTextView;
import com.huanfeng.view.HFView;
import com.huanfeng.view.HFViewGroup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;

public class HFPicker extends HFViewGroup implements View.OnClickListener,View.OnKeyListener
{
	public interface OnSelectListener
	{
		void onPickerSelect(Object object, int position);
	}
	
	public HFViewGroup titleGroup;
	public HFTextView lblTitle;
	public HFView bg;
	public HFViewGroup content;
	private List<Object> data = new ArrayList<>();
	public HFViewGroup bottomGroup;
	public HFButton btnYes;
	public HFButton btnNo;
	public ContentView center;
	private OnSelectListener listener;

	

	public HFPicker(Context context) {
		super(context);
		
		hfSetSize(1f, 1f);
		bg = hfAddView(new HFView(context)).hfSetBackgroundColor(Color.BLACK).hfSetSize(1f, 1f);
		bg.setAlpha(0.8f);
		bg.setClickable(true);
		
		content = hfAddView(new HFViewGroup(context)).hfSetWidth(1f).hfSetHeight(dp(350)).hfSetBottom(1f);
		
		titleGroup = content.hfAddView(new HFViewGroup(context)).hfSetBackgroundColor(Color.WHITE).hfSetWidth(1f).hfSetHeight(dp(50));
		titleGroup.hfAddView(new HFView(context)).hfSetWidth(1f).hfSetHeight(1).hfSetBackgroundColor(0xFFE0E0E0);
		lblTitle = titleGroup.hfAddView(HFTextView.hfCreate(context, "设置",15,Color.BLACK)).hfSetSize(1f, 1f);
		lblTitle.setGravity(Gravity.CENTER);
		
		
		bottomGroup = content.hfAddView(new HFViewGroup(context)).hfSetBackgroundColor(Color.WHITE).hfSetWidth(1f).hfSetHeight(dp(50));
		bottomGroup.hfAddView(new HFView(context)).hfSetWidth(1f).hfSetHeight(1).hfSetBackgroundColor(0xFFE0E0E0);
		bottomGroup.hfAddView(new HFView(context)).hfSetWidth(1).hfSetHeight(0.6f).hfSetBackgroundColor(0xFFE0E0E0).hfSetX(0.5).hfSetCenterY(0.5);
		bottomGroup.hfSetBottom(1f);
		btnNo = bottomGroup.hfAddView(HFButton.hfCreate(context, "取消", 15, 0xFF656565, 1, this)).hfSetSize(0.5, 1f);
		btnYes = bottomGroup.hfAddView(HFButton.hfCreate(context, "确定", 15, 0xFFFF86A6, 2, this)).hfSetSize(0.5, 1f).hfSetX(0.5);
		
		center = (ContentView) content.hfAddView(new ContentView(context)).hfSetWidth(1f).hfSetHeight(content.getHeight()-bottomGroup.getHeight()-titleGroup.getHeight()).hfSetY(titleGroup.getHeight());
		center.setBackgroundColor(0xFFF5F6F7);
		
		setVisibility(View.INVISIBLE);
		
	}
	
	public void setBackgroundEnable(boolean enable){
		bg.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
	}
	
	
	public void setTitle(String title){
		lblTitle.setText(title);
	}
	
	
	public void setData(List<Object> data){
		if(data == null){
			data = new ArrayList<>();
		}else if(data.size() > 0){
			data.add(new NULL());
			data.add(new NULL());
			data.add(0,new NULL());
			data.add(0,new NULL());
		}
		this.data = new ArrayList<>();
		this.data.addAll(data);
	}
	
	public void setSelect(Object select){
		int index = 0;
		for (int i = 0; i < data.size(); i++) {
			if(data.get(i).equals(select)){
				index = i - 2;
				break;
			}
		}
		setSelectIndex(index);
	}
	
	public void setSelectIndex(int index){
		if(index >= 0 && index <= data.size() - 4){
			center.num = -(index) * center.cellHeight;
			center.invalidate();
		}
	}
	
	public void setOnSelectListener(OnSelectListener listener){
		this.listener = listener;
	}
	
	public void show(){
		center.invalidate();
		setVisibility(View.VISIBLE);
		bg.setAlpha(0.8f);
		bg.startAnimation(AnimationTools.alphaAnimation(0, 0.8f, 200));
		content.startAnimation(AnimationTools.translateAnimation(0, 0, getHeight()+content.getHeight(), 0, 300));
		HFActivity.topActivity.addOnKeyListener(this);
	}
	

	public void close(){
		center.num = 0;
		bg.setAlpha(0f);
		bg.startAnimation(AnimationTools.alphaAnimation(0.8f, 0f, 200));
		content.startAnimation(AnimationTools.translateAnimation(0, 0, 0, getHeight()+content.getHeight(), 300));
		content.getAnimation().setAnimationListener(new AnimationAdapter(){
			public void onAnimationEnd(Animation animation) {
				setVisibility(View.INVISIBLE);
			}
		});
		HFActivity.topActivity.removeOnKeyListener(this);
	}

	@Override
	public void onClick(View v) {
		if(UITools.getHFTag(v) == 2){
			if(data.size() == 0)
				return;
			int index = -center.num / center.cellHeight + 2;
			if(listener != null){
				listener.onPickerSelect(data.get(index), index-2);
			}
		}
		close();
	}
	
	private class ContentView extends HFView implements OnTouchListener
	{
		int selectTop,cellHeight,num;
		Paint paint;
		int baseline;
		boolean touchDown;
		int inertia;
		boolean position;
		
		public ContentView(Context context) {
			super(context);
			cellHeight = dp(50);
			selectTop = cellHeight * 2;

			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setTextSize(UITools.sp2px(15));
			paint.setTextAlign(Align.CENTER);
			Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
			baseline = (cellHeight + 0 - fontMetrics.bottom - fontMetrics.top) / 2;
			
			setClickable(true);
			setOnTouchListener(this);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			canvas.clipRect(0, 0, getWidth(), getHeight());
			canvas.drawColor(0xFFF5F6F7);
			
			if(data != null && data.size() > 0){
				paint.setColor(Color.WHITE);
				canvas.drawRect(0, selectTop, getWidth(), selectTop + cellHeight, paint);
				
				paint.setColor(0xFF6A6A6A);
				canvas.save();
				canvas.translate(0, num);
				int offset = (-num + cellHeight / 2) / cellHeight;
				for (int i = 0; i < data.size(); i++) {
					if(i >= offset && i <= offset + 5){
						if(i== offset + 2)
							paint.setColor(0xFFFF86A6);
						else
							paint.setColor(0xFF6A6A6A);
						String str = data.get(i).toString();
						canvas.drawText(str, getWidth()/2, baseline, paint);
					}
					canvas.translate(0, cellHeight);
				}
				canvas.restore();
				paint.setColor(0xFFFF86A6);
				canvas.drawRect(0, selectTop, dp(5), selectTop + cellHeight, paint);
				canvas.drawRect(getWidth()-dp(5), selectTop, getWidth(), selectTop + cellHeight, paint);
			}
		}
		
		private void setNum(int num){
			if(num > 0)
				num = 0;
			else if(num < (data.size() - 5) * -cellHeight)
				num = (data.size() - 5) * -cellHeight;
			this.num = num;
			invalidate();
		}
		
		int vv;
		private Handler handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				if(msg.what == 0){
					if(touchDown){
						inertia = (int) (inertia * 0.8);
						handler.sendEmptyMessageDelayed(0, 20);
					}else{
						int val = (int) (inertia * 0.1);
						inertia -= val;
						if(Math.abs(val) <= 3){
							if(!position){
								position = true;
								vv = -num % cellHeight;
								if(vv != 0 && vv > cellHeight / 2){
									vv = vv - cellHeight;
								}
							}
							val = vv / 5;
							if(val == 0)
								val = vv;
							vv -= val;
						}
						if(Math.abs(val) > 0){
							setNum(num + val);
							handler.sendEmptyMessageDelayed(0, 20);
						}else{
							inertia = 0;
							position = false;
						}
					}
				}
			};
		};

		private int dy;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int y = (int) event.getY();
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				dy = y;
				touchDown = true;
				inertia = 0;
				handler.sendEmptyMessage(0);
			}else if(event.getAction() == MotionEvent.ACTION_MOVE){
				int oy = y - dy;
				inertia += oy * 3;
				setNum(num + oy);
				dy = y;
				invalidate();
			}else if(event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP){
				touchDown = false;
			}
			return true;
		}
	}
	
	private class NULL
	{
		@Override
		public String toString() {
			
			return "";
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			close();
			return true;
		}
		return false;
	}
}

