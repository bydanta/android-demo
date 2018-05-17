package com.template.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.huanfeng.view.HFImageView;
import com.huanfeng.view.HFTextView;
import com.huanfeng.view.HFViewGroup;
import com.template.base.BaseActivity;

/**
 * 纯代码布局
 */
public class HuanFeng_Demo_Activity extends BaseActivity {

    private HFViewGroup bg;
    private HFImageView img;
    private HFTextView txt;

    @Override
    protected void initContentView(Bundle savedInstanceState) {

        bg = contentView.hfAddView(new HFViewGroup(this));
        bg.hfSetSize(1.0, 1.0);
        bg.hfSetBackgroundColor(Color.BLUE);

        img = bg.hfAddView(new HFImageView(this));
        img.hfSetSize(0.5, 0.5).hfSetCenter(0.5, 0.5);
        img.hfSetImageUrl("http://pic31.nipic.com/20130702/7447430_092254585000_2.jpg");
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        txt = bg.hfAddView(new HFTextView(this));
        txt.hfSetSize(0.2, 0.2).hfSetCenter(img.hfGetCenterX(), img.hfGetCenterY());
        txt.hfSetText("测试内容");
        txt.setTextSize(img.getWidth() * 0.03f);
        txt.setTextColor(Color.WHITE);
    }
}
