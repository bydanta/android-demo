package com.template.http;

import org.xutils.common.Callback;

/**
 * 回调
 */
public class XIHttpResponse <ResultType> implements Callback.CommonCallback<ResultType>{

    @Override
    public void onSuccess(ResultType result) {
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onCancelled(Callback.CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
