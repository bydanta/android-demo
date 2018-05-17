package com.template.http;

import com.huanfeng.tools.Utils;
import com.huanfeng.view.HFActivity;
import com.orhanobut.logger.Logger;
import com.template.function.ProgersssDialog;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import java.util.Map;

/**
 * XUtils HTTP 框架
 */
public class XHttpClient {

    public String url;
    private ProgersssDialog dialog;
    private IHttpResponse callback;
    private Class resultClass;
    private HttpResults result;
    private String value;

    public XHttpClient(String url, Class resultClass,IHttpResponse callback){

        this.url = url;
        this.callback = callback;
        this.resultClass = resultClass;
        this.get();
    }

    public XHttpClient(String url, String value, Class resultClass, IHttpResponse callback){

        this.url = url;
        this.value = value;
        this.callback = callback;
        this.resultClass = resultClass;
        this.post();
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
                result.setMesg("亲 您的网络不给力哟! 错误doCallback");
            }
            callback.onHttpResponse(result);
        }
    }

    /**
     * 发送get请求
     * **/
    public void get(){

        RequestParams requestParams = new RequestParams(url);
        requestParams.setAsJsonContent(true);
        requestParams.setBodyContent("");
        requestParams.addHeader("Content-Type","application/json");

        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                Logger.d("接受数据GET:");
                Logger.json(s);
                try {
                    result = (HttpResults) Utils.getGson().fromJson(s, resultClass);
                } catch (Exception ex) {
                    result = createResultObject();
                    result.setMesg("数据解析异常");
                    ex.printStackTrace();
                }
                doCallback();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                result = createResultObject();
                result.setCode(-1);
                result.setMesg("亲 您的网络不给力哟! 错误onError");
                doCallback();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 发送post请求
     */
    public void post(){

        Logger.d("发送数据:");
        Logger.json(value);

        RequestParams requestParams = new RequestParams(url);
        requestParams.setAsJsonContent(true);
        requestParams.setBodyContent(value);
        requestParams.addHeader("Content-Type","application/json");

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                Logger.d("接受数据POST:");
                Logger.json(s);
                try {
                    result = (HttpResults) Utils.getGson().fromJson(s, resultClass);
                } catch (Exception ex) {
                    result = createResultObject();
                    result.setMesg("数据解析异常");
                    ex.printStackTrace();
                }
                doCallback();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                result = createResultObject();
                result.setCode(-1);
                result.setMesg("亲 您的网络不给力哟! 错误onFinished");
                doCallback();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 上传文件
     * @param <T>
     */
    public static <T> Callback.Cancelable UpLoadFile(String url, Map<String,Object> map, Callback.CommonCallback<T> callback){
        RequestParams params=new RequestParams(url);
        if(null!=map){
            for(Map.Entry<String, Object> entry : map.entrySet()){
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        params.setMultipart(true);
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

    /**
     * 下载文件
     * @param <T>
     */
    public static <T> Callback.Cancelable DownLoadFile(String url, String filepath, Callback.CommonCallback<T> callback){
        RequestParams params=new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(filepath);
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }
}

