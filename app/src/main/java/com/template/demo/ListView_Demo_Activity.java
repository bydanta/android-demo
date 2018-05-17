package com.template.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.template.adper.ListAdper;
import com.template.app.R;
import com.template.base.BaseActivity;
import com.template.bean.Test;
import com.template.function.MyOnScrollListener;
import com.template.function.SwipeRefreshLayout;
import com.template.http.AHttpClient;
import com.template.http.IHttpResponse;
import com.loopj.android.http.RequestParams;
import com.template.http.XHttpClient;
import com.template.http.result.HttpTestList;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * listview 刷新 加载
 * *
 */
public class ListView_Demo_Activity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnLoadListener {

    private ListView list;//对象
    private SwipeRefreshLayout list_swipe_main;// 刷新
    private ArrayList<Test> listItem = new ArrayList<>();
    private int WHAT_DID_STATUS = 0;
    private int WHAT_DID_PAGE = 0;

    @Override
    protected void initContentView(Bundle savedInstanceState) {

        setContentView(R.layout.activity_listview_demo);

        list = (ListView) findViewById(R.id.list);
        list_swipe_main = (SwipeRefreshLayout) findViewById(R.id.list_swipe_main);

        list_swipe_main.setOnRefreshListener(this);// 刷新初始化
        list_swipe_main.setOnLoadListener(this);// 加载初始化

        list.setOnScrollListener(new MyOnScrollListener(list, listItem));
        onRefresh();
    }

    /**
     * ******************************** listview 刷新代码段 ************************************************
     */

    @Override
    public void onRefresh() {

        listItem.clear();
        WHAT_DID_PAGE = 0;
        WHAT_DID_STATUS = 0;

        JSONObject jsonObject = new JSONObject();
        new XHttpClient("http://idanta.cn/public", jsonObject.toString(), HttpTestList.class, new IHttpResponse<HttpTestList>() {

            @Override
            public void onHttpResponse(HttpTestList result) {

                listItem = result.getList();

                mHandler.sendEmptyMessage(WHAT_DID_STATUS);
            }
        }).showProgerss();
    }

    @Override
    public void onLoad() {

        WHAT_DID_PAGE++;
        WHAT_DID_STATUS = 1;

        JSONObject jsonObject = new JSONObject();
        new XHttpClient("http://idanta.cn/public", jsonObject.toString(), HttpTestList.class, new IHttpResponse<HttpTestList>() {
            @Override
            public void onHttpResponse(HttpTestList result) {

                listItem = result.getList();

                mHandler.sendEmptyMessage(WHAT_DID_STATUS);
            }
        }).showProgerss();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            int what = msg.what;

            switch (what) {

                //刷新
                case 0: {
                    BaseAdapter br = new ListAdper(getApplicationContext(), listItem);
                    list.setAdapter(br);
                    list_swipe_main.setRefreshing(false);
                    break;
                }

                //加载
                case 1: {
                    BaseAdapter br = new ListAdper(getApplicationContext(), listItem);
                    list.setAdapter(br);
                    br.notifyDataSetChanged();
                    list.setSelectionFromTop(MyOnScrollListener.scrollPos, MyOnScrollListener.scrollTop);
                    list_swipe_main.setLoading(false);
                    break;
                }
            }
        }
    };

}
