package com.template.model;

import com.loopj.android.http.RequestParams;
import com.template.bean.Test;
import com.template.http.AHttpClient;
import com.template.http.IHttpResponse;
import com.template.http.XHttpClient;
import com.template.http.result.HttpTestList;

import java.util.List;

/**
 * Model实现
 */
public class Model implements IModel {

    @Override
    public void getData(final ICallback iCallback) {

        new XHttpClient("http://idanta.cn/public", "", HttpTestList.class, new IHttpResponse<HttpTestList>() {
            @Override
            public void onHttpResponse(HttpTestList result) {

                List<Test> list = result.getList();
                for (Test item : list) {
                    iCallback.onResult(item.getTitle() + ":" + item.getContent());//返回数据
                }
            }
        });
    }
}
