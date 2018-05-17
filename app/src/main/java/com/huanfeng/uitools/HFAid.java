package com.huanfeng.uitools;

import android.graphics.Point;
import android.graphics.PointF;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.huanfeng.tools.Utils;

import java.text.DecimalFormat;

/**
 * Created by YXL on 2015/12/10.
 */
public class HFAid implements View.OnTouchListener{

    private View view;
    private Point lastPt;
    public  HFAid(View view){
        this.view = view;
        view.setOnTouchListener(this);

    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v == view){
            Point pt = new Point((int)event.getRawX(),(int)event.getRawY());
            if(event.getAction() == MotionEvent.ACTION_MOVE){
                if(lastPt != null){
                    int x = pt.x - lastPt.x + view.getLeft();
                    int y = pt.y - lastPt.y + view.getTop();
                    hfSetPosition(x, y);
                    PointF center = getCenter();
                    Utils.print("center",center.x,center.y,"position",getParentWidthPercent(x),getParentHeightPercent(y),"size",v.getWidth()+"-"+v.getHeight());
                }
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                lastPt = null;
            }
            lastPt = pt;
            return  true;
        }
        return false;
    }

    private PointF getCenter(){
        float x = getParentWidthPercent(view.getLeft() + view.getWidth() / 2);
        float y = getParentHeightPercent(view.getTop() + view.getHeight() / 2);
        return new PointF(x,y);
    }

    public float getParentWidthPercent(int x){
        int w = UITools.screenWidth;
        if (view.getParent() != null && view.getParent() instanceof ViewGroup){
            int pw = ((ViewGroup)view.getParent()).getWidth();
            if(pw > 0)
                w = pw;
        }
        DecimalFormat df = new DecimalFormat("0.000");
        return Float.parseFloat(df.format((float)x / w));
    }

    public float getParentHeightPercent(int y){
        int h = UITools.screenHeight;
        if (view.getParent() != null && view.getParent() instanceof ViewGroup){
            int ph = ((ViewGroup)view.getParent()).getHeight();
            if(ph > 0)
                h = ph;
        }
        DecimalFormat df = new DecimalFormat("0.000");
        return Float.parseFloat(df.format((float)y / h));
    }

    public void hfSetPosition(int x,int y){
        hfSetX(x);
        hfSetY(y);
    }
    public void hfSetX(int x){
        int w = view.getWidth();
        view.setLeft(x);
        view.setRight(x + w);
    }
    public void hfSetY(int y){
        int h = view.getHeight();
        view.setTop(y);
        view.setBottom(y + h);
    }
}
