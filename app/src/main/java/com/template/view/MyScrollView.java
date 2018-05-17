package com.template.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 自定义ScrollView，并重写其onTouchEvent和dispatchTouchEvent方法，
 * 以解决viewflipper 与scrollview的手势冲突
 * @author yangjiantong
 *
 */
public class MyScrollView extends ScrollView {

    private float mDX, mDY, mLX, mLY;
    int mLastAct = -1;
    boolean mIntercept = false;

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public MyScrollView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDX = mDY = 0f;
                mLX = ev.getX();
                mLY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                mDX += Math.abs(curX - mLX);
                mDY += Math.abs(curY - mLY);
                mLX = curX;
                mLY = curY;

                if (mIntercept && mLastAct == MotionEvent.ACTION_MOVE) {
                    return false;
                }

                if (mDX > mDY) {

                    mIntercept = true;
                    mLastAct = MotionEvent.ACTION_MOVE;
                    return false;
                }

        }
        mLastAct = ev.getAction();
        mIntercept = false;
        return super.onInterceptTouchEvent(ev);
    }
}