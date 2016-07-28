package com.kakao.kakaogift.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

/**
 * 重写 StringRequest Map 传参
 * 
 * @author 刘奇
 * 
 */

public class StringRequestTask extends StringRequest {

	private Map<String, String> map;
	private Map<String, String> headers  ;

	public StringRequestTask(int method, String url, Listener<String> listener,ErrorListener errorListener) {
		super(method, url, listener, errorListener);

	}

	public StringRequestTask(String path, Listener<String> listener,ErrorListener errorListener) {
		super(path, listener, errorListener);
	}

	public StringRequestTask(int method, String path,Listener<String> listener, ErrorListener errorListener,Map<String, String> map) throws IOException {
		super(method, path, listener, errorListener);
		if (map != null) {
			this.map = map;
		}
	}

	public StringRequestTask(int method, String path,Listener<String> listener, ErrorListener errorListener,Map<String, String> params, Map<String, String> headers)
			throws IOException {
		super(method, path, listener, errorListener);
		if (params != null) {
			this.map = params;
		}
		
		this.headers = new HashMap<String, String>();
		this.headers.put("accept", "application/json");
		this.headers.put("Content-Disposition", "form-data");
		if (headers != null) {
			this.headers.putAll(headers);
		}
	}


	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return this.map;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return headers;
	}
	
	
	

}
