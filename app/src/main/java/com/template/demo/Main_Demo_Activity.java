package com.template.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.template.app.R;
import com.template.base.BaseActivity;

public class Main_Demo_Activity extends BaseActivity implements OnClickListener {

    private Button huanfeng_btn;
    private Button view_btn;
    private Button presenter_btn;
    private Button list_btn;
    private Button dialog_btn;
    private Button viewpage_btn;
    private Button whell_btn;
    private Button zbarcode_btn;
    private Button album_btn;

    @Override
    protected void initContentView(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main_demo);
        initView();
    }

    private void initView() {

        huanfeng_btn = (Button)findViewById(R.id.huanfeng_btn);
        view_btn = (Button)findViewById(R.id.view_btn);
        presenter_btn = (Button)findViewById(R.id.presenter_btn);
        list_btn = (Button)findViewById(R.id.list_btn);
        dialog_btn = (Button)findViewById(R.id.dialog_btn);
        viewpage_btn = (Button)findViewById(R.id.viewpage_btn);
        whell_btn = (Button)findViewById(R.id.whell_btn);
        zbarcode_btn = (Button)findViewById(R.id.zbarcode_btn);
        album_btn = (Button)findViewById(R.id.album_btn);

        huanfeng_btn.setOnClickListener(this);
        view_btn.setOnClickListener(this);
        presenter_btn.setOnClickListener(this);
        list_btn.setOnClickListener(this);
        dialog_btn.setOnClickListener(this);
        viewpage_btn.setOnClickListener(this);
        whell_btn.setOnClickListener(this);
        zbarcode_btn.setOnClickListener(this);
        album_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.huanfeng_btn:
                startActivity(new Intent(this, HuanFeng_Demo_Activity.class));
                break;

            case R.id.view_btn:
                startActivity(new Intent(this, View_Demo_Activity.class));
                break;

            case R.id.presenter_btn:
                startActivity(new Intent(this, Presenter_Demo_Activity.class));
                break;

            case R.id.list_btn:
                startActivity(new Intent(this, ListView_Demo_Activity.class));
                break;

            case R.id.dialog_btn:
                startActivity(new Intent(this, Dialog_Demo_Activity.class));
                break;

            case R.id.viewpage_btn:
                startActivity(new Intent(this, ViewPager_Demo_Activity.class));
                break;

            case R.id.whell_btn:
                startActivity(new Intent(this, Whell_Demo_Activity.class));
                break;

            case R.id.zbarcode_btn:
                startActivity(new Intent(this, CameraQrcode_Activity.class));
                break;

            case R.id.album_btn:
                startActivity(new Intent(this, Album_Demo_Activity.class));
                break;

        }
    }
}
