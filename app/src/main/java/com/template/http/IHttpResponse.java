package com.template.http;

/**
 * 回调接口
 */
public interface IHttpResponse<T extends HttpResults> {

	public void onHttpResponse(T result);
}
