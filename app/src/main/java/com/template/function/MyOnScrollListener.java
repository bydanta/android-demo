package com.template.function;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 设置listview加载新数据后的位置
 * **/
public class MyOnScrollListener implements AbsListView.OnScrollListener {

    public static int scrollPos;
    public static int scrollTop;

    ListView myList;
    Object arrayList;

    public MyOnScrollListener(ListView myList, Object arrayList) {
        super();
        this.myList = myList;
        this.arrayList = arrayList;
    }

    @Override
    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            scrollPos = myList.getFirstVisiblePosition();
        }
        if (arrayList != null) {
            View v = myList.getChildAt(0);
            scrollTop = (v == null) ? 0 : v.getTop();
        }
    }
}
