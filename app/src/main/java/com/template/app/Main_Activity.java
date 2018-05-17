package com.template.app;

import android.content.Intent;
import android.os.Bundle;

import com.template.base.BaseActivity;
import com.huanfeng.tools.Utils;
import com.huanfeng.uitools.UITools;
import com.template.demo.Main_Demo_Activity;

/**
 * 纯代码工具初始化类
 */
public class Main_Activity extends BaseActivity implements UITools.InitCallback {

    @Override
    protected void initContentView(Bundle savedInstanceState) {

        UITools.init(this, 1080, 1920, 3.0f, this);
        Utils.getImageLoader(this);
    }

    @Override
    public void onInitCompleted() {

        startActivity(new Intent(Main_Activity.this, Main_Demo_Activity.class));
        finish();
    }

}
