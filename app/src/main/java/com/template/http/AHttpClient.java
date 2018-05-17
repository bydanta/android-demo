package com.template.http;

import com.huanfeng.tools.AndroidUtils;
import com.huanfeng.tools.Utils;
import com.orhanobut.logger.Logger;
import com.template.function.ProgersssDialog;
import com.huanfeng.view.HFActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 获取数据的接口实现类
 */
public class AHttpClient {

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static String baseUrl = "";
    private ProgersssDialog dialog;
    private IHttpResponse callback;
    private String url;
    private RequestParams params;
    private Class resultClass;
    private HttpResults result;

    static {
        client.setConnectTimeout(10000);
        client.addHeader("Authorization", "APPCODE 9df083be90ff4306bf88fc696729b24c");
        client.setUserAgent(AndroidUtils.getUserAgent());
    }

    public AHttpClient(String url, RequestParams params, Class resultClass, IHttpResponse callback) {
        if (resultClass == null)
            resultClass = HttpResults.class;

        this.url = baseUrl + url;
        this.params = params;
        this.resultClass = resultClass;
        this.callback = callback;
        this.post();

        Utils.print("发送数据:" + url);
        Utils.print(params.toString());
    }

    private void post() {

        client.get(HFActivity.topActivity, url, params, new TextResponse());
    }

    public void showProgerss() {
        dialog = new ProgersssDialog(HFActivity.topActivity);
    }

    public void showProgerss(String str) {
        dialog = new ProgersssDialog(HFActivity.topActivity, str);
    }

    private HttpResults createResultObject() {
        try {
            return (HttpResults) resultClass.newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void doCallback() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        if (callback != null) {
            if (result == null) {
                result = createResultObject();
                result.setMesg("亲 您的网络不给力哟!");
            }
            callback.onHttpResponse(result);
        }
    }

    private class TextResponse extends TextHttpResponseHandler {
        @Override
        public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
            result = createResultObject();
            result.setCode(-1);
            result.setMesg("亲 您的网络不给力哟!");
            if (throwable instanceof TimeoutException) {
                result.setMesg("亲 您的网络不给力哟!");
            }
            if (throwable != null)
                throwable.printStackTrace();
            doCallback();
        }

        @Override
        public void onSuccess(int i, Header[] headers, String s) {
            Utils.print("接收数据:" + url);
            Logger.json(s);
            try {

                result = (HttpResults) Utils.getGson().fromJson(s, resultClass);
                result.setMap((Map<String, Object>) Utils.getGson().fromJson(s, HashMap.class));
            } catch (Exception ex) {
                result = createResultObject();
                result.setMesg("数据解析异常");
                ex.printStackTrace();
            }
            doCallback();
        }
    }
}
