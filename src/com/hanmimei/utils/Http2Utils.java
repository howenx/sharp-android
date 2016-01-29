package com.hanmimei.utils;

import java.io.IOException;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.hanmimei.activity.BaseActivity;

/**
 * 网络请求工具类
 * 
 * @author 刘奇
 * 
 */
public class Http2Utils {

	public Http2Utils() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static void doPostRequestTask(Context mContext, String url,
			final VolleyJsonCallback callback) {
		doRequestTask(mContext, Method.POST, url, callback, null, null);
	}

	public static void doPostRequestTask(Context mContext, String url,
			final VolleyJsonCallback callback, Map<String, String> params) {
		doRequestTask(mContext, Method.POST, url, callback, params, null);
	}

	public static void doPostRequestTask(Context mContext,
			Map<String, String> headers, String url,
			final VolleyJsonCallback callback) {
		doRequestTask(mContext, Method.POST, url, callback, null, headers);
	}

	public static void doPostRequestTask(Context mContext,
			Map<String, String> headers, String url,
			final VolleyJsonCallback callback, Map<String, String> params) {
		doRequestTask(mContext, Method.POST, url, callback, params, headers);
	}

	public static void doGetRequestTask(Context mContext, String url,
			final VolleyJsonCallback callback) {
		doRequestTask(mContext, Method.GET, url, callback, null, null);
	}

	public static void doGetRequestTask(Context mContext, String url,
			final VolleyJsonCallback callback, Map<String, String> params) {
		doRequestTask(mContext, Method.GET, url, callback, params, null);
	}

	public static void doGetRequestTask(Context mContext,
			Map<String, String> headers, String url,
			final VolleyJsonCallback callback) {
		doRequestTask(mContext, Method.GET, url, callback, null, headers);
	}

	public static void doGetRequestTask(Context mContext,
			Map<String, String> headers, String url,
			final VolleyJsonCallback callback, Map<String, String> params) {
		doRequestTask(mContext, Method.GET, url, callback, params, headers);
	}

	/**
	 * 实现网络请求数据
	 * 
	 * @param mContext
	 *            上下文
	 * @param method
	 *            请求类型
	 * @param url
	 *            请求地址
	 * @param callback
	 *            返回数据的回调
	 * @param params
	 *            传送数据的键值对
	 * @param headers
	 *            传送头数据的键值对
	 */

	// 实现Volley 异步回调请求的结果
	public static void doRequestTask(Context mContext, int method, String url,
			final VolleyJsonCallback callback, Map<String, String> params,
			Map<String, String> headers) {
		final BaseActivity mActivity = (BaseActivity) mContext;
		PostStringRequest request = null;
		try {
			request = new PostStringRequest(method, url,
					new Listener<String>() {

						@Override
						public void onResponse(String arg0) {
//							Log.i("http-result", arg0);
							callback.onSuccess(arg0);
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							callback.onError();
						}
					}, params, headers);
		} catch (IOException e) {
			callback.onError();
		}
		mActivity.getMyApplication().getRequestQueue().add(request);
	}

	/**
	 * 实现网络请求数据
	 * 
	 * @param mContext
	 *            上下文
	 * @param method
	 *            请求类型
	 * @param url
	 *            请求地址
	 * @param callback
	 *            返回数据的回调
	 * @param params
	 *            传送数据的键值对
	 * @param headers
	 *            传送头数据的键值对
	 */
	
	public static void doPostRequestTask2(Context mContext, Map<String, String> headers,String url,
			final VolleyJsonCallback callback, String params) {
		doRequestTask2(mContext, Method.POST, headers,url, callback, params);
	}

	// 实现Volley 异步回调请求的结果
	public static void doRequestTask2(Context mContext, int method,
			Map<String, String> headers, String url,
			final VolleyJsonCallback callback, String params) {
		final BaseActivity mActivity = (BaseActivity) mContext;
		PostStringRequest2 request = null;
		try {
			request = new PostStringRequest2(method, headers, url,
					new Listener<String>() {

						@Override
						public void onResponse(String arg0) {
//							Log.i("http-result", arg0);
							callback.onSuccess(arg0);
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							callback.onError();
						}
					}, params);
		} catch (IOException e) {
			callback.onError();
		}
		mActivity.getMyApplication().getRequestQueue().add(request);
	}

	// 以下是在同一个类中定义的接口
	public interface VolleyJsonCallback {
		void onSuccess(String result);

		void onError();
	}

}
