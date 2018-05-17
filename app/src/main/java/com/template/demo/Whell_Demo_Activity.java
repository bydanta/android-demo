package com.template.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.huanfeng.tools.Utils;
import com.template.app.R;
import com.template.base.BaseActivity;
import com.template.function.APP;
import com.wheel.MyPupWindows;
import com.wheel.WheelDialog;
import com.wheel.WheelTextView;
import com.wheel.WheelView;

import java.util.ArrayList;
import java.util.List;

public class Whell_Demo_Activity extends BaseActivity implements View.OnClickListener {

    private Button start_btn;

    /**
     * 弹窗滚动代码段
     * *
     */
    private String[] ageList = {"18岁以下", "18~24", "25~30", "31-~55", "55岁以上"};
    private MyPupWindows popwindows;
    private WheelDialog wheelDialog;
    private WheelView mAge;
    private TextView one_no_txt;
    private TextView buttom_txt;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.whell_demo);

        buttom_txt = (TextView) findViewById(R.id.buttom_txt);
        start_btn = (Button) findViewById(R.id.start_btn);
        start_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.start_btn://设置年龄
                initPupWindoss();
                popwindows.showPopupWindow(buttom_txt);
                break;

            case R.id.one_ok_txt:
                Integer str = wheelDialog.formatData().get(0);
                Utils.Toast(ageList[str]);
                popwindows.dismiss();
                break;

            case R.id.one_no_txt:
                popwindows.dismiss();
                break;
        }
    }

    /**
     * 选择年龄段
     * *
     */
    private void initPupWindoss() {

        //实例化类
        popwindows = new MyPupWindows(Whell_Demo_Activity.this, R.layout.wheel_one);

        //声明对象
        mAge = (WheelView) popwindows.popwindow_view.findViewById(R.id.wheel1);
        TextView one_ok_txt = (TextView) popwindows.popwindow_view.findViewById(R.id.one_ok_txt);
        one_no_txt = (TextView) popwindows.popwindow_view.findViewById(R.id.one_no_txt);

        //传入对象到控件类里
        List<WheelView> list = new ArrayList<>();
        list.add(mAge);
        wheelDialog = new WheelDialog(list);

        //传入集合对象
        BaseAdapter baseAdapter = wheelDialog.NumberAdapters(this, ageList, getBaseContext().getResources());
        mAge.setAdapter(baseAdapter);
        mAge.setScrollCycle(true);
        mAge.setSelection(0, true);
        mAge.setUnselectedAlpha(0.5f);
        ((WheelTextView) mAge.getSelectedView()).setTextSize(20);

        //设置监听对象
        one_ok_txt.setOnClickListener(this);
        one_no_txt.setOnClickListener(this);
        mAge.setOnItemSelectedListener(wheelDialog.mListener);
    }
}
