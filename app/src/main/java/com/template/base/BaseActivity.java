package com.template.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;

import com.huanfeng.tools.Utils;
import com.huanfeng.view.HFActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

/**
 * 父类Activity
 * *
 */
public abstract class BaseActivity extends HFActivity {

    // 可以把常量单独放到一个Class中
    public static final String ACTION_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    //这个地方有点“模板方法“的设计模式样子
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setBase();
        super.onCreate(savedInstanceState);
        initContentView(savedInstanceState);

        com.orhanobut.logger.Logger.init("json");
        Utils.getImageLoader(this);
    }

    //初始化UI，setContentView等
    protected abstract void initContentView(Bundle savedInstanceState);

    //可能全屏或者没有ActionBar等
    private void setBase() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    // 横竖屏切换，键盘等
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {

            super.onRestoreInstanceState(savedInstanceState);
        }
        catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //把每个Activity添加进内存里方便批量操作
        SysApplication.getInstance().addActivity(this);

        // 你可以添加多个Action捕获
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NETWORK_CHANGE);
        registerReceiver(receiver, filter);

        //还可能发送统计数据，比如第三方的SDK 做统计需求
        MobclickAgent.onPageStart(this.getLocalClassName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);

        //还可能发送统计数据，比如第三方的SDK 做统计需求
        MobclickAgent.onPageEnd(this.getLocalClassName());
        MobclickAgent.onPause(this);
    }

    /**
     * 广播消息接收
     * *
     */
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 处理各种情况
            String action = intent.getAction();
            if (ACTION_NETWORK_CHANGE.equals(action)) { //网络发生变化
                // 处理网络问题
            }
        }
    };
}