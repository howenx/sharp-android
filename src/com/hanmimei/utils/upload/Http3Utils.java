package com.hanmimei.utils.upload;

import java.util.Map;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.hanmimei.activity.BaseActivity;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.upload.MultipartRequest.VolleyResponseListener;

public class Http3Utils {

	public Http3Utils() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static void doPostRequestTask(Context mContext,
			Map<String, String> headers, String url,
			final VolleyJsonCallback callback, MultipartRequestParams params) {
		doRequestTask(mContext, Method.POST, url, callback, params, headers);
	}

	// 实现Volley 异步回调请求的结果
	public static void doRequestTask(Context mContext, int method, String url,
			final VolleyJsonCallback callback, MultipartRequestParams params,
			Map<String, String> headers) {
		final BaseActivity mActivity = (BaseActivity) mContext;
		MultipartRequest request = new MultipartRequest(method, params, url, mContext, new VolleyResponseListener() {
			
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
		},headers);
		request.setTag(url);
		mActivity.getMyApplication().getRequestQueue().add(request);
	}

}
