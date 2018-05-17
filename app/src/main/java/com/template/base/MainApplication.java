package com.template.base;

import android.app.Application;
import android.content.Context;

import com.template.app.BuildConfig;

import org.xutils.x;

public class MainApplication extends Application {

    /**
     * 全局的上下文.
     */
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }

    /**
     * 获取Context.
     */
    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
