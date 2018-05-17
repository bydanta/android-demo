package com.template.demo;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.huanfeng.tools.Utils;
import com.template.app.R;
import com.template.base.BaseActivity;
import com.template.function.APP;
import com.viewpager.bean.ADInfo;
import com.viewpager.lib.CycleViewPager;
import com.viewpager.lib.CycleViewPager.ImageCycleViewListener;
import com.viewpager.utils.ViewFactory;

import java.util.ArrayList;
import java.util.List;

public class ViewPager_Demo_Activity extends BaseActivity {

    private CycleViewPager cycleViewPager;
    private List<String> url_maps;

    @Override
    protected void initContentView(Bundle savedInstanceState) {

        setContentView(R.layout.viewpager_demo);

        //图片滚动
        url_maps = new ArrayList<>();
        url_maps.add("http://img5.imgtn.bdimg.com/it/u=3997761965,345345136&fm=21&gp=0.jpg");
        url_maps.add("http://img5.imgtn.bdimg.com/it/u=3838945757,2092713406&fm=21&gp=0.jpg");
        url_maps.add("http://img1.imgtn.bdimg.com/it/u=3590032268,267736769&fm=21&gp=0.jpg");
        testCircleIndicator();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void testCircleIndicator() {

        List<ImageView> views = new ArrayList<>();
        List<ADInfo> infos = new ArrayList<>();

        cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.play_img);

        for(int i = 0; i < url_maps.size(); i ++){
            ADInfo info = new ADInfo();
            info.setUrl(url_maps.get(i));
            info.setContent("编号:" + i);
            infos.add(info);
        }

        // 将最后一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(infos.size() - 1).getUrl()));
        for (int i = 0; i < infos.size(); i++) {
            views.add(ViewFactory.getImageView(this, infos.get(i).getUrl()));
        }
        // 将第一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(0).getUrl()));
        // 设置循环，在调用setData方法前调用
        cycleViewPager.setCycle(true);
        // 在加载数据前设置是否循环
        cycleViewPager.setData(views, infos, mAdCycleViewListener);
        //设置轮播
        cycleViewPager.setWheel(true);
        // 设置轮播时间，默认5000ms
        cycleViewPager.setTime(2000);
        //设置圆点指示图标组居中显示，默认靠右
        cycleViewPager.setIndicatorCenter();
    }

    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                position = position - 1;
                Utils.Toast(info.getContent());
            }
        }
    };
}
