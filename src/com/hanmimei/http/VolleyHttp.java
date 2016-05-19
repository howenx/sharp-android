package com.hanmimei.http;

import java.io.IOException;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;
import com.hanmimei.http.MultipartRequest.VolleyResponseListener;

/**
 * 网络请求工具类
 * 
 * @author 刘奇
 * 
 */
public class VolleyHttp {

	public VolleyHttp() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	private static RequestQueue requestQueue;
	public static final String TAG = "volley_request";

	/**
	 * <b>注册一个请求队列</b>
	 * 
	 * @param mContext
	 *            - 上下文
	 */
	public static void registRequestQueue(Context mContext) {
		if (requestQueue == null)
			requestQueue = Volley.newRequestQueue(mContext);
	}

	/**
	 * <b>注册一个请求队列</b>
	 * 
	 * @param mContext
	 * @param stack
	 */
	public static void registRequestQueue(Context mContext, HttpStack stack) {
		requestQueue = Volley.newRequestQueue(mContext, stack);
	}

	/**
	 * 获得当前的请求队列
	 * 
	 * @return a requestQueue
	 */
	public static RequestQueue getRequestQueue() {
		return requestQueue;
	}

	// ====================================================
	// 简单的网络请求 传参主要为 字典 及 header
	// ====================================================
	/**
	 * 简单的网络请求 <br>
	 * <b> POST 请求 </b>
	 * 
	 * @param mContext
	 *            - 上下文
	 * @param url
	 *            - 请求接口
	 * @param callback
	 *            - 回调
	 * 
	 */
	public static void doPostRequestTask(String url,
			final VolleyJsonCallback callback) {
		doRequestTask(Method.POST, url, callback, null, null, TAG);
	}

	/**
	 * 简单的网络请求 <br>
	 * <b> POST 请求 </b>
	 * 
	 * @param mContext
	 *            - 上下文
	 * @param url
	 *            - 请求接口
	 * @param callback
	 *            - 回调
	 * @param tag
	 *            请求标签
	 * 
	 */
	public static void doPostRequestTask(String url,
			final VolleyJsonCallback callback, String tag) {
		doRequestTask(Method.POST, url, callback, null, null, tag);
	}

	/**
	 * 简单的网络请求 传参主要为 字典 <br>
	 * <b> POST 请求 </b>
	 * 
	 * @param mContext
	 * @param url
	 * @param callback
	 * @param params
	 */
	public static void doPostRequestTask(String url,
			final VolleyJsonCallback callback, Map<String, String> params) {
		doRequestTask(Method.POST, url, callback, params, null, TAG);
	}

	/**
	 * 简单的网络请求 传参主要为 字典 <br>
	 * <b> POST 请求 </b>
	 * 
	 * @param mContext
	 * @param url
	 * @param callback
	 * @param params
	 * @param tag
	 *            请求标签
	 */
	public static void doPostRequestTask(String url,
			final VolleyJsonCallback callback, Map<String, String> params,
			String tag) {
		doRequestTask(Method.POST, url, callback, params, null, tag);
	}

	/**
	 * 简单的网络请求 传参主要为 header <br>
	 * <b> POST 请求 </b>
	 * 
	 * @param mContext
	 * @param headers
	 * @param url
	 * @param callback
	 */
	public static void doPostRequestTask(Map<String, String> headers,
			String url, final VolleyJsonCallback callback) {
		doRequestTask(Method.POST, url, callback, null, headers, TAG);
	}

	/**
	 * 简单的网络请求 传参主要为 header <br>
	 * <b> POST 请求 </b>
	 * 
	 * @param mContext
	 * @param headers
	 * @param url
	 * @param callback
	 * @param tag
	 *            请求标签
	 */
	public static void doPostRequestTask(Map<String, String> headers,
			String url, final VolleyJsonCallback callback, String tag) {
		doRequestTask(Method.POST, url, callback, null, headers, tag);
	}

	/**
	 * 简单的网络请求 传参主要为 字典 及 header <br>
	 * <b> POST 请求 </b>
	 * 
	 * @param mContext
	 * @param headers
	 * @param url
	 * @param callback
	 * @param params
	 */
	public static void doPostRequestTask(Map<String, String> headers,
			String url, final VolleyJsonCallback callback,
			Map<String, String> params) {
		doRequestTask(Method.POST, url, callback, params, headers, TAG);
	}

	/**
	 * 简单的网络请求 传参主要为 字典 及 header <br>
	 * <b> POST 请求 </b>
	 * 
	 * @param mContext
	 * @param headers
	 * @param url
	 * @param callback
	 * @param params
	 * @param tag
	 *            请求标签
	 */
	public static void doPostRequestTask(Map<String, String> headers,
			String url, final VolleyJsonCallback callback,
			Map<String, String> params, String tag) {
		doRequestTask(Method.POST, url, callback, params, headers, tag);
	}

	/**
	 * 简单的网络请求 <br>
	 * <b> GET 请求 </b>
	 * 
	 * @param mContext
	 * @param url
	 * @param callback
	 */
	public static void doGetRequestTask(String url,
			final VolleyJsonCallback callback) {
		doRequestTask(Method.GET, url, callback, null, null, TAG);
	}

	/**
	 * 简单的网络请求 <br>
	 * <b> GET 请求 </b>
	 * 
	 * @param mContext
	 * @param url
	 * @param callback
	 * @param tag
	 *            请求标签
	 */
	public static void doGetRequestTask(String url,
			final VolleyJsonCallback callback, String tag) {
		doRequestTask(Method.GET, url, callback, null, null, tag);
	}

	/**
	 * 简单的网络请求 传参主要为 字典 <br>
	 * <b> GET 请求 </b>
	 * 
	 * @param mContext
	 * @param url
	 * @param callback
	 * @param params
	 */
	public static void doGetRequestTask(String url,
			final VolleyJsonCallback callback, Map<String, String> params) {
		doRequestTask(Method.GET, url, callback, params, null, TAG);
	}

	/**
	 * 简单的网络请求 传参主要为 字典 <br>
	 * <b> GET 请求 </b>
	 * 
	 * @param mContext
	 * @param url
	 * @param callback
	 * @param params
	 * @param tag
	 *            请求标签
	 */
	public static void doGetRequestTask(String url,
			final VolleyJsonCallback callback, Map<String, String> params,
			String tag) {
		doRequestTask(Method.GET, url, callback, params, null, tag);
	}

	/**
	 * 简单的网络请求 传参主要为 header <br>
	 * <b> GET 请求 </b>
	 * 
	 * @param mContext
	 *            -
	 * @param headers
	 *            -
	 * @param url
	 *            -
	 * @param callback
	 *            -
	 */
	public static void doGetRequestTask(Map<String, String> headers,
			String url, final VolleyJsonCallback callback) {
		doRequestTask(Method.GET, url, callback, null, headers, TAG);
	}

	/**
	 * 简单的网络请求 传参主要为 header <br>
	 * <b> GET 请求 </b>
	 * 
	 * @param mContext
	 *            -
	 * @param headers
	 *            -
	 * @param url
	 *            -
	 * @param callback
	 *            -
	 * @param tag
	 *            请求标签
	 */
	public static void doGetRequestTask(Map<String, String> headers,
			String url, final VolleyJsonCallback callback, String tag) {
		doRequestTask(Method.GET, url, callback, null, headers, tag);
	}

	/**
	 * 简单的网络请求 传参主要为 字典 及 header <br>
	 * <b> GET 请求 </b>
	 * 
	 * @param mContext
	 *            -
	 * @param headers
	 *            -
	 * @param url
	 *            -
	 * @param callback
	 *            -
	 * @param params
	 *            -
	 */
	public static void doGetRequestTask(Map<String, String> headers,
			String url, final VolleyJsonCallback callback,
			Map<String, String> params) {
		doRequestTask(Method.GET, url, callback, params, headers, TAG);
	}

	/**
	 * 简单的网络请求 传参主要为 字典 及 header <br>
	 * <b> GET 请求 </b>
	 * 
	 * @param mContext
	 *            -
	 * @param headers
	 *            -
	 * @param url
	 *            -
	 * @param callback
	 *            -
	 * @param params
	 *            -
	 * @param tag
	 *            请求标签
	 */
	public static void doGetRequestTask(Map<String, String> headers,
			String url, final VolleyJsonCallback callback,
			Map<String, String> params, String tag) {
		doRequestTask(Method.GET, url, callback, params, headers, tag);
	}

	/**
	 * 简单的网络请求 传参主要为 字典 及 header <br>
	 * 
	 * @param mContext
	 *            -上下文
	 * @param method
	 *            - 请求类型
	 * @param url
	 *            - 请求地址
	 * @param callback
	 *            - 返回数据的回调
	 * @param params
	 *            - 传送数据的键值对
	 * @param headers
	 *            - 传送头数据的键值对
	 * @param tag
	 *            请求标签
	 */
	public static void doRequestTask(int method, String url,
			final VolleyJsonCallback callback, Map<String, String> params,
			Map<String, String> headers, String tag) {
		try {
			StringRequestTask request = new StringRequestTask(method, url,
					new Listener<String>() {

						@Override
						public void onResponse(String arg0) {
							// Log.i("http-result", arg0);
							callback.onSuccess(arg0);
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							callback.onError();
						}
					}, params, headers);
			request.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
			request.setTag(TAG);
			getRequestQueue().add(request);
		} catch (IOException e) {
			throw new UnsupportedOperationException(
					"You should first register  the requestQueue");
		}
	}

	// =====================================================================
	// 简单的网络请求 传参主要为 字符串string 及 header
	// =====================================================================
	/**
	 * 简单的网络请求 <br>
	 * <b> POST 请求 </b>
	 * 
	 * @param mContext
	 * @param headers
	 * @param url
	 * @param callback
	 * @param params
	 */
	public static void doPostRequestTask2(Map<String, String> headers,
			String url, final VolleyJsonCallback callback, String params) {
		doRequestTask2(Method.POST, headers, url, callback, params, TAG);
	}

	/**
	 * 简单的网络请求 <br>
	 * <b> POST 请求 </b>
	 * 
	 * @param mContext
	 * @param headers
	 * @param url
	 * @param callback
	 * @param params
	 * @param tag
	 *            请求标签
	 */
	public static void doPostRequestTask2(Map<String, String> headers,
			String url, final VolleyJsonCallback callback, String params,
			String tag) {
		doRequestTask2(Method.POST, headers, url, callback, params, tag);
	}

	/**
	 * 简单的网络请求 传参主要为 字符串string <br>
	 * <b> POST 请求 </b>
	 * 
	 * @param mContext
	 * @param url
	 * @param callback
	 * @param params
	 */
	public static void doPostRequestTask2(String url,
			final VolleyJsonCallback callback, String params) {
		doRequestTask2(Method.POST, null, url, callback, params, TAG);
	}

	/**
	 * 简单的网络请求 传参主要为 字符串string <br>
	 * <b> POST 请求 </b>
	 * 
	 * @param mContext
	 * @param url
	 * @param callback
	 * @param params
	 * @param tag
	 *            请求标签
	 * 
	 */
	public static void doPostRequestTask2(String url,
			final VolleyJsonCallback callback, String params, String tag) {
		doRequestTask2(Method.POST, null, url, callback, params, tag);
	}

	/**
	 * 简单的网络请求 传参主要为 字符串string 及 header <br>
	 * <b> POST 请求 </b>
	 * 
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
	 *            传送数据的字符串
	 * @param headers
	 *            传送头数据的键值对
	 * @param tag
	 *            请求标签
	 */
	public static void doRequestTask2(int method, Map<String, String> headers,
			String url, final VolleyJsonCallback callback, String params,
			String tag) {
		StringRequestTask2 request = null;
		try {
			request = new StringRequestTask2(method, headers, url,
					new Listener<String>() {
						@Override
						public void onResponse(String arg0) {
							callback.onSuccess(arg0);
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError arg0) {
							callback.onError();
						}
					}, params);
			request.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
			request.setTag(TAG);
			getRequestQueue().add(request);
		} catch (IOException e) {
			throw new UnsupportedOperationException(
					"You should first register  the requestQueue");
		}
	}

	// =====================================================================
	// 简单的网络请求 支持小文件上传 以及 普通传参
	// =====================================================================

	/**
	 * 简单的网络请求 支持小文件上传 以及 普通传参
	 * 
	 * <br>
	 * <b> POST 请求 </b>
	 * 
	 * @param url
	 *            - 请求接口地址
	 * @param callback
	 *            - 普通回调 {@link VolleyJsonCallback}
	 * @param params
	 *            {@link MultipartRequestParams}
	 */
	public static void doPostRequestTask3(String url,
			final VolleyJsonCallback callback, MultipartRequestParams params) {
		doRequestTask3(Method.POST, null, url, callback, params, TAG);
	}

	/**
	 * 简单的网络请求 支持小文件上传 以及 普通传参
	 * 
	 * <br>
	 * <b> POST 请求 </b>
	 * 
	 * @param url
	 *            - 请求接口地址
	 * @param callback
	 *            - 普通回调 {@link VolleyJsonCallback}
	 * @param params
	 *            {@link MultipartRequestParams}
	 * @param tag
	 *            请求标签
	 */
	public static void doPostRequestTask3(String url,
			final VolleyJsonCallback callback, MultipartRequestParams params,
			String tag) {
		doRequestTask3(Method.POST, null, url, callback, params, tag);
	}

	/**
	 * 简单的网络请求 支持小文件上传 以及 普通传参
	 * 
	 * <br>
	 * <b> POST 请求 </b>
	 * 
	 * @param headers
	 *            - 请求header
	 * @param url
	 *            - 请求接口地址
	 * @param callback
	 *            - 普通回调 {@link VolleyJsonCallback}
	 * @param params
	 *            {@link MultipartRequestParams}
	 * 
	 */
	public static void doPostRequestTask3(Map<String, String> headers,
			String url, final VolleyJsonCallback callback,
			MultipartRequestParams params) {
		doRequestTask3(Method.POST, headers, url, callback, params, TAG);
	}

	/**
	 * 简单的网络请求 支持小文件上传 以及 普通传参
	 * 
	 * <br>
	 * <b> POST 请求 </b>
	 * 
	 * @param headers
	 *            - 请求header
	 * @param url
	 *            - 请求接口地址
	 * @param callback
	 *            - 普通回调 {@link VolleyJsonCallback}
	 * @param params
	 *            {@link MultipartRequestParams}
	 * @param tag
	 *            请求标签
	 * 
	 */
	public static void doPostRequestTask3(Map<String, String> headers,
			String url, final VolleyJsonCallback callback,
			MultipartRequestParams params, String tag) {
		doRequestTask3(Method.POST, headers, url, callback, params, tag);
	}

	/**
	 * @Description - 实现多图片上传 以及文字字典上传
	 * @param method
	 *            -
	 * @param url
	 *            -
	 * @param callback
	 *            -
	 * @param params
	 *            -
	 * @param headers
	 *            -
	 * @param tag
	 *            请求标签
	 */
	public static void doRequestTask3(int method, Map<String, String> headers,
			String url, final VolleyJsonCallback callback,
			MultipartRequestParams params, String tag) {
		try {

			MultipartRequest request = new MultipartRequest(method, params,
					url, new VolleyResponseListener() {

						@Override
						public void setFlag(boolean flag) {
						}

						@Override
						public void onResponse(JSONObject response) {
							callback.onSuccess(response.toString());
						}

						@Override
						public void onErrorResponse(VolleyError error) {
							callback.onError();
						}
					}, headers);
			request.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
			request.setTag(TAG);
			getRequestQueue().add(request);
		} catch (Exception e) {
			throw new UnsupportedOperationException(
					"You should first register  the requestQueue");
		}
	}

	public static void parseRequestTask(String tag) {
		if (requestQueue != null && requestQueue.getSequenceNumber() > 0)
			requestQueue.cancelAll(tag);
	}

	// 以下是在同一个类中定义的接口
	public interface VolleyJsonCallback {
		void onSuccess(String result);

		void onError();
	}

}
