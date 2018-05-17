package com.wheel;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class MyPupWindows extends PopupWindow {
    public View popwindow_view;
    private int screen_height;
    private int screen_wide;

    public MyPupWindows(Activity context, int id) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popwindow_view = View.inflate(context, id, null);
        screen_height = context.getWindowManager().getDefaultDisplay().getHeight();
        screen_wide = context.getWindowManager().getDefaultDisplay().getWidth();

        this.setContentView(popwindow_view);

        this.setWidth(screen_wide);

        this.setHeight(LayoutParams.WRAP_CONTENT);

        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setTouchable(true);

        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);

    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 1, 0);
        } else {
            this.dismiss();
        }
    }
}
