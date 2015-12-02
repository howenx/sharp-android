package com.hanmimei.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.hanmimei.activity.BaseActivity;
/**
 * 网络请求工具类 
 * @author 刘奇
 *
 */
public class HttpUtil {

	public HttpUtil() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}
	/**
	 * 实现用户的登录
	 * @param username  登录用户名
	 * @param userpwd   登录密码
	 * @return
	 */
	public static String doUserLogin(String username, String userpwd) {
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Uname", username));
		params.add(new BasicNameValuePair("Pwd", userpwd));

		String result = null;
		try {
			// 设置参数
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			// 发送请求
			HttpResponse response = client.execute(httpPost);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity());
			} else {
//				EBHandler.getInstance().sendEmptyMessage(
//						HandlerMessger.LOGIN_FAIL_MSG);
			}
		} catch (IOException e) {
			
		}
		return result;
	}
	
	/**
	 * 	实现网络请求数据  
	 * @param mContext  上下文
	 * @param url		请求地址
	 * @param callback	返回数据的回调
	 */
	public static void doPostRequestTask(Context mContext, String url,
			final VolleyJsonCallback callback) {
		doPostRequestTask(mContext, url, callback, null);
	}
	/**
	 * 实现网络请求数据  
	 * @param mContext  上下文
	 * @param url		请求地址
	 * @param callback	返回数据的回调
	 * @param map		传送数据的键值对
	 */

	// 实现Volley 异步回调请求的结果
	public static void doPostRequestTask(Context mContext, String url,
			final VolleyJsonCallback callback, Map<String, String> params) {
		final BaseActivity mActivity = (BaseActivity) mContext;
		PostStringRequest request = null;
		try {
			request = new PostStringRequest(Method.POST, url,
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
