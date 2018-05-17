package com.template.demo;

import android.os.Bundle;
import com.template.app.R;
import com.template.base.BaseActivity;

public class View_Demo_Activity extends BaseActivity {

    @Override
    protected void initContentView(Bundle savedInstanceState) {

        setContentView(R.layout.activity_view);
        initView();
    }

    private void initView() {

    }
}
